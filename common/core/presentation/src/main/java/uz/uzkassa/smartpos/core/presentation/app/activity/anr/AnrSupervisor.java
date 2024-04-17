package uz.uzkassa.smartpos.core.presentation.app.activity.anr;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A class supervising the UI thread for ANR errors. Use 
 * {@link #start()} and {@link #stop()} to control
 * when the UI thread is supervised
 */
final class AnrSupervisor {
    /**
     * The {@link ExecutorService} checking the UI thread
     */
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    /**
     * The {@link AnrSupervisorRunnable} running on a separate
     * thread
     */
    private final AnrSupervisorRunnable mSupervisor = new AnrSupervisorRunnable();

    /**
     * Starts the supervision
     */
    public synchronized void start() {
        synchronized (this.mSupervisor) {
            if (mSupervisor.isStopped()) {
                mExecutor.execute(mSupervisor);
            } else {
                mSupervisor.unstop();
            }
        }
    }

    /**
     * Stops the supervision. The stop is delayed, so if 
     * start() is called right after stop(),
     * both methods will have no effect. There will be at least one 
     * more ANR check before the supervision is stopped.
     */
    public synchronized void stop() {
        mSupervisor.stop();
    }
}