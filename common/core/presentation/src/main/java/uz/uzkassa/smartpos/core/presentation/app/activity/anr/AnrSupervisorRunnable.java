package uz.uzkassa.smartpos.core.presentation.app.activity.anr;

import android.os.Handler;
import android.os.Looper;

import uz.uzkassa.smartpos.core.manager.logger.Logger;

/**
 * A {@link Runnable} testing the UI thread every 10s until {@link
 * #stop()} is called
 */
final class AnrSupervisorRunnable implements Runnable {

    /**
     * The {@link Handler} to access the UI threads message queue
     */
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * The stop flag
     */
    private boolean mStopped;

    /**
     * Flag indicating the stop was performed
     */
    private boolean mStopCompleted = true;

    @Override
    public void run() {
        this.mStopCompleted = false;

        // Loop until stop() was called or thread is interrupted
        while (!Thread.interrupted()) {
            try {
                // Create new callback
                AnrSupervisorCallback callback = new AnrSupervisorCallback();

                // Perform test, Handler should run 
                // the callback within 1s
                synchronized (callback) {
                    this.mHandler.post(callback);
                    callback.wait(1000);

                    // Check if called
                    if (!callback.isCalled()) {
                        // Log
                        AnrException e = new AnrException(this.mHandler.getLooper().getThread());
                        Logger.Companion.wtf("ANR EXCEPTION", e);
                        e.logProcessMap();
                        // Wait until the thread responds again
                        callback.wait();
                    }
                }

                // Check if stopped
                this.checkStopped();

                // Sleep for next test
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                break;
            }
        }

        // Set stop completed flag
        this.mStopCompleted = true;

        // Log
        Logger.Companion.d(AnrSupervisor.class.getSimpleName(), "ANR supervision stopped");

    }

    private synchronized void checkStopped() throws InterruptedException {
        if (this.mStopped) {
            // Wait 1000ms
            Thread.sleep(1000);

            // Break if still stopped
            if (this.mStopped) {
                throw new InterruptedException();
            }
        }
    }

    /**
     * Stops the check
     */
    synchronized void stop() {
        Logger.Companion.d(AnrSupervisor.class.getSimpleName(), "Stopping...");
        this.mStopped = true;
    }

    /**
     * Stops the check
     */
    synchronized void unstop() {
        Logger.Companion.d(AnrSupervisor.class.getSimpleName(), "Revert stopping...");
        this.mStopped = false;
    }

    /**
     * Returns whether the stop is completed
     *
     * @return true if stop is completed, false if not
     */
    synchronized boolean isStopped() {
        return this.mStopCompleted;
    }
}