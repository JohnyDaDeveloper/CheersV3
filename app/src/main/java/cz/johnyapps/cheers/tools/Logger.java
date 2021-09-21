package cz.johnyapps.cheers.tools;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cz.johnyapps.cheers.BuildConfig;

public class Logger {
    @NonNull
    private static final String LOGGER_TAG = "Logger";

    public static void v(@NonNull String tag, @Nullable String message) {
        Log.v(formatTag(tag), message);
    }

    public static void v(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            v(tag, message);
        } else {
            Log.v(formatTag(tag), message, throwable);
        }
    }

    public static void v(@NonNull String tag, @Nullable String message, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            v(tag, message);
        } else {
            v(tag, String.format(message, args));
        }
    }

    public static void v(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            v(tag, message, throwable);
        } else {
            v(tag, String.format(message, args), throwable);
        }
    }

    public static void d(@NonNull String tag, @Nullable String message) {
        if (BuildConfig.DEBUG) {
            Log.d(formatTag(tag), message);
        }
    }

    public static void d(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (BuildConfig.DEBUG) {
            if (throwable == null) {
                d(tag, message);
            } else {
                Log.d(formatTag(tag), message, throwable);
            }
        }
    }

    public static void d(@NonNull String tag, @Nullable String message, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            d(tag, message);
        } else {
            d(tag, String.format(message, args));
        }
    }

    public static void d(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            d(tag, message, throwable);
        } else {
            d(tag, String.format(message, args), throwable);
        }
    }

    public static void i(@NonNull String tag, @Nullable String message) {
        Log.i(tag, message);
    }

    public static void i(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            i(tag, message);
        } else {
            Log.i(formatTag(tag), message, throwable);
        }
    }

    public static void i(@NonNull String tag, @Nullable String message, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            i(tag, message);
        } else {
            i(tag, String.format(message, args));
        }
    }

    public static void i(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            i(tag, message, throwable);
        } else {
            i(tag, String.format(message, args), throwable);
        }
    }

    public static void w(@NonNull String tag, @Nullable String message) {
        Log.w(tag, message);
    }

    public static void w(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            w(tag, message);
        } else {
            Log.w(formatTag(tag), message, throwable);
        }
    }

    public static void w(@NonNull String tag, @Nullable String message, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            w(tag, message);
        } else {
            w(tag, String.format(message, args));
        }
    }

    public static void w(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            w(tag, message, throwable);
        } else {
            w(tag, String.format(message, args), throwable);
        }
    }

    public static void e(@NonNull String tag, @Nullable String message) {
        Log.e(tag, message);
    }

    public static void e(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable) {
        if (throwable == null) {
            e(tag, message);
        } else {
            Log.e(formatTag(tag), message, throwable);
        }
    }

    public static void e(@NonNull String tag, @Nullable String message, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            e(tag, message);
        } else {
            e(tag, String.format(message, args));
        }
    }

    public static void e(@NonNull String tag, @Nullable String message, @Nullable Throwable throwable, @Nullable Object... args) {
        if (args == null || args.length == 0) {
            e(tag, message, throwable);
        } else {
            e(tag, String.format(message, args), throwable);
        }
    }

    public static String formatTag(@NonNull String tag) {
        return String.format("%s: %s", LOGGER_TAG, tag);
    }
}
