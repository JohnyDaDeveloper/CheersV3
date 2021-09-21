package cz.johnyapps.cheers.async;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public abstract class AsyncTask<INPUT, PROGRESS, OUTPUT> {
    private boolean cancelled = false;
    private Future<OUTPUT> outputFuture;

    public AsyncTask() {

    }

    /**
     * @see #execute(Object)
     */
    public AsyncTask<INPUT, PROGRESS, OUTPUT> execute() {
        return execute(null);
    }

    /**
     * Starts is all
     * @param input Data you want to work with in the background
     */
    @NonNull
    public AsyncTask<INPUT, PROGRESS, OUTPUT> execute(@Nullable final INPUT input) {
        onPreExecute();

        ExecutorService executorService = AsyncWorker.getInstance().getExecutorService();
        outputFuture = executorService.submit(() -> {
            try {
                final OUTPUT output = doInBackground(input);
                AsyncWorker.getInstance().getMainThreadHandler().post(() -> onPostExecute(output));
                return output;
            } catch (Exception e) {
                AsyncWorker.getInstance().getMainThreadHandler().post(() -> onBackgroundError(e));
                throw e;
            }
        });

        return this;
    }

    @NonNull
    public OUTPUT get() throws Exception {
        if (outputFuture == null) {
            throw new TaskNotExecutedException();
        } else {
            return outputFuture.get();
        }
    }

    @NonNull
    public OUTPUT get(long timeout, @NonNull TimeUnit timeUnit) throws Exception {
        if (outputFuture == null) {
            throw new TaskNotExecutedException();
        } else {
            return outputFuture.get(timeout, timeUnit);
        }
    }
    /**
     * Call to publish progress from background
     * @param progress  Progress made
     */
    protected void publishProgress(@Nullable final PROGRESS progress) {
        AsyncWorker.getInstance().getMainThreadHandler().post(() -> {
            onProgress(progress);

            if (onProgressListener != null) {
                onProgressListener.onProgress(progress);
            }
        });
    }

    protected void onProgress(@Nullable final PROGRESS progress) {

    }

    /**
     * Call to cancel background work
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     *
     * @return Returns true if the background work should be cancelled
     */
    protected boolean isCancelled() {
        return cancelled;
    }

    /**
     * Call this method after cancelling background work
     */
    protected void onCancelled() {
        AsyncWorker.getInstance().getMainThreadHandler().post(() -> {
            if (onCancelledListener != null) {
                onCancelledListener.onCancelled();
            }
        });
    }

    /**
     * Work which you want to be done on UI thread before {@link #doInBackground(Object)}
     */
    protected void onPreExecute() {

    }

    /**
     * Work on background
     * @param input Input data
     * @return      Output data
     * @throws Exception    Any uncought exception which occurred while working in background. If
     * any occurs, {@link #onBackgroundError(Exception)} will be executed (on the UI thread)
     */
    @Nullable
    protected abstract OUTPUT doInBackground(@Nullable INPUT input) throws Exception;

    /**
     * Work which you want to be done on UI thread after {@link #doInBackground(Object)}
     * @param output    Output data from {@link #doInBackground(Object)}
     */
    protected void onPostExecute(@Nullable OUTPUT output) {

    }

    /**
     * Triggered on UI thread if any uncought exception occurred while working in background
     * @param e Exception
     * @see #doInBackground(Object)
     */
    protected abstract void onBackgroundError(@Nullable Exception e);

    @Nullable
    private OnProgressListener<PROGRESS> onProgressListener;
    public interface OnProgressListener<PROGRESS> {
        void onProgress(@Nullable PROGRESS progress);
    }

    public void setOnProgressListener(@Nullable OnProgressListener<PROGRESS> onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    @Nullable
    private OnCancelledListener onCancelledListener;
    public interface OnCancelledListener {
        void onCancelled();
    }

    public void setOnCancelledListener(@Nullable OnCancelledListener onCancelledListener) {
        this.onCancelledListener = onCancelledListener;
    }
}
