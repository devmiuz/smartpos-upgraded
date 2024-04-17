package uz.uzkassa.apay.data.repository.qr.card.engine;

import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
public abstract class AsyncRunnable<T> {
    /**
     * Starts executing the asynchronous code.
     *
     * Important: the method MUST call finish(AtomicReference<T>, T) once it's done executing
     * or the thread will be blocked forever.
     *
     * @param notifier use this parameter to call finish(AtomicReference<T>, T).
     */
    protected abstract void run(AtomicReference<T> notifier);

    /**
     * Call this method from the run method once it's done executing.
     *
     * @param notifier the same notifier variable passed into run(AtomicReference<T>).
     * @param result the method's return value. MUSTN'T be null or the thread won't unblock.
     */
    protected final void finish(AtomicReference<T> notifier, T result) {
        synchronized (notifier) {
            notifier.set(result);
            notifier.notify();
        }
    }

    /**
     * Use this method to block a synchronous call when running asynchronous code.
     *
     * @param runnable The AsyncRunnable<T> to run.
     * @param <T> The type parameter that defines the method's return type.
     */
    public static <T> T wait(AsyncRunnable<T> runnable) {
        final AtomicReference<T> notifier = new AtomicReference<>();

        // run the asynchronous code
        runnable.run(notifier);

        // wait for the asynchronous code to finish
        synchronized (notifier) {
            while (notifier.get() == null) {
                try {
                    notifier.wait();
                } catch (InterruptedException ignore) {}
            }
        }

        // return the result of the asynchronous code
        return notifier.get();
    }

}