package cz.johnyapps.cheers.async;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncWorker {
    @Nullable
    private static AsyncWorker instance = new AsyncWorker();
    private static final int NUMBER_OF_THREADS = 4;

    @Nullable
    private ExecutorService executorService;
    @Nullable
    private Handler mainThreadHandler;

    private AsyncWorker() {

    }

    @NonNull
    public static AsyncWorker getInstance() {
        if (instance == null) {
            instance = new AsyncWorker();
        }

        return instance;
    }

    @NonNull
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        }

        return executorService;
    }

    @NonNull
    public Handler getMainThreadHandler() {
        if (mainThreadHandler == null) {
            mainThreadHandler = new Handler(Looper.getMainLooper());
        }

        return mainThreadHandler;
    }
}
