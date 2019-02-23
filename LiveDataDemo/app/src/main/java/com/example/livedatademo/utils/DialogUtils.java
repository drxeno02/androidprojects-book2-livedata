package com.example.livedatademo.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.example.livedatademo.R;

public class DialogUtils {

    private static Dialog mDialog;
    private static ProgressDialog mProgressDialog; // display during processing requests/responses

    // click listener for default dialog
    private static final DialogInterface.OnClickListener mDefaultListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismissDialog();
        }
    };

    /**
     * Method is used to display progress dialog. Call when processing requests/responses
     *
     * @param context Interface to global information about an application environment
     */
    public static void showProgressDialog(@NonNull Context context) {
        if (((Activity) context).isFinishing() || (mProgressDialog != null && mProgressDialog.isShowing())) {
            return;
        }

        try {
            mProgressDialog = ProgressDialog.show(context, null, null, true, false);
            if (mProgressDialog.getWindow() != null) {
                mProgressDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.color.transparent));
                mProgressDialog.setContentView(R.layout.dialog_progress);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to dismiss progress dialog
     */
    public static void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dialog constructor
     *
     * @param context Interface to global information about an application environment
     * @param title   The displayed title
     * @param msg     The displayed message
     */
    public static void showDefaultOKAlert(@NonNull Context context, String title, String msg) {
        showDefaultOKAlert(context, title, msg, null);
    }

    /**
     * Dialog constructor
     *
     * @param context  Interface to global information about an application environment
     * @param title    The displayed title
     * @param msg      The displayed message
     * @param listener Interface used to allow the creator of a dialog to run some code
     *                 when an item on the dialog is clicked
     */
    public static void showDefaultOKAlert(@NonNull Context context, String title, String msg, DialogInterface.OnClickListener listener) {
        if (((Activity) context).isFinishing() || (mDialog != null && mDialog.isShowing())) {
            return;
        }
        if (listener == null) {
            listener = mDefaultListener;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), listener);
        // create builder and set equal to dialog
        mDialog = builder.create();
        if (!((Activity) context).isFinishing() && mDialog != null) {
            // show dialog
            mDialog.show();
        }
    }

    /**
     * Method is used to construct dialog with a message, and both negative and positive buttons
     *
     * @param context     Interface to global information about an application environment
     * @param msg         The displayed message
     * @param yesListener Interface used to allow the creator of a dialog to run some code
     *                    when an item on the dialog is clicked
     * @param noListener  Interface used to allow the creator of a dialog to run some code
     *                    when an item on the dialog is clicked
     */
    public static void showYesNoAlert(@NonNull Context context, String title, String msg, String yesText, String noText,
                                      DialogInterface.OnClickListener yesListener, DialogInterface.OnClickListener noListener) {
        if (((Activity) context).isFinishing() || (mDialog != null && mDialog.isShowing())) {
            return;
        }

        String yes = !TextUtils.isEmpty(yesText) ? yesText :
                context.getResources().getString(R.string.yes);
        String no = !TextUtils.isEmpty(noText) ? noText :
                context.getResources().getString(R.string.no);

        if (yesListener == null) {
            yesListener = mDefaultListener;
        }
        if (noListener == null) {
            noListener = mDefaultListener;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(yes, yesListener)
                .setNegativeButton(no, noListener);
        // create builder and set equal to dialog
        mDialog = builder.create();
        if (!((Activity) context).isFinishing() && mDialog != null) {
            // show dialog
            mDialog.show();
        }
    }

    /**
     * Method is used to dismiss dialog
     */
    public static void dismissDialog() {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (@NonNull IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
