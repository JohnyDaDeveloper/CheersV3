package cz.johnyapps.cheers.database.tasks;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.async.AsyncTask;
import cz.johnyapps.cheers.database.CheersRoomDatabase;

public abstract class BaseDatabaseTask<INPUT, PROGRESS, OUTPUT> extends AsyncTask<INPUT, PROGRESS, OUTPUT> {
    @Nullable
    private OnCompleteListener<OUTPUT> onCompleteListener;
    @NonNull
    private final CheersRoomDatabase cheersRoomDatabase;

    public BaseDatabaseTask(@NonNull Context context) {
        this.cheersRoomDatabase = CheersRoomDatabase.getDatabase(context);
    }

    @Override
    protected void onBackgroundError(@Nullable Exception e) {
        if (onCompleteListener != null) {
            onCompleteListener.onFailure(e);
            onCompleteListener.onComplete();
        }
    }

    @Override
    protected void onPostExecute(@Nullable OUTPUT output) {
        super.onPostExecute(output);

        if (onCompleteListener != null) {
            onCompleteListener.onSuccess(output);
            onCompleteListener.onComplete();
        }
    }

    @NonNull
    public CheersRoomDatabase getDatabase() {
        return cheersRoomDatabase;
    }

    public void setOnCompleteListener(@Nullable OnCompleteListener<OUTPUT> onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
        setOnCancelledListener(new OnCancelledListener() {
            @Override
            public void onCancelled() {
                if (BaseDatabaseTask.this.onCompleteListener != null) {
                    BaseDatabaseTask.this.onCompleteListener.onComplete();
                }
            }
        });
    }

    public interface OnCompleteListener<OUTPUT> {
        void onSuccess(@Nullable OUTPUT output);
        void onFailure(@Nullable Exception e);
        void onComplete();
    }
}
