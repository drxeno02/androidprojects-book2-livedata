package com.example.livedatademo.utils;

import android.content.Context;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;

public class Utils {

    // click control threshold
    private static final int CLICK_THRESHOLD = 250;
    private static final long ANIM_DURATION_SHORT_200 = 200; // milliseconds
    private static long mLastClickTime;
    private static InputMethodManager imm;

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return True if clicks have not occurred within 300ms window
     */
    public static boolean isViewClickable() {
        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 250 ms) --LT
         */
        long mCurrClickTimestamp = SystemClock.uptimeMillis();
        long mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime;
        mLastClickTime = mCurrClickTimestamp;
        return !(mElapsedTimestamp <= CLICK_THRESHOLD);
    }

    /**
     * Method is used to confirm that string parameter is in valid email format
     *
     * @param email Email of the user
     * @return True if email is valid format, otherwise false
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidEmail(@Nullable String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.substring(email.lastIndexOf(".") + 1).length() > 1;
    }

    /**
     * Method is used to show virtual keyboard
     *
     * @param context Interface to global information about an application environment
     */
    public static void showKeyboard(@NonNull Context context) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        // toggleSoftInput is not consistent when called multiple times back to back
        // The work around is to add a slight delay for consistently displaying soft keyboard
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        }, ANIM_DURATION_SHORT_200);
    }

    /**
     * Method is used to hide virtual keyboard
     *
     * @param context Interface to global information about an application environment
     * @param binder  Base interface for a remotable object, the core part of a lightweight remote
     *                procedure call mechanism designed for high performance when performing
     *                in-process and cross-process calls
     */
    public static void hideKeyboard(@NonNull Context context, @NonNull IBinder binder) {
        if (imm == null) {
            imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        imm.hideSoftInputFromWindow(binder, 0);
    }
}
