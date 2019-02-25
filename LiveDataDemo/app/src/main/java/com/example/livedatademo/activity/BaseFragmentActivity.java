package com.example.livedatademo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.livedatademo.R;

public class BaseFragmentActivity extends AppCompatActivity {

    private static final String BUG_19917_KEY = "WORKAROUND_FOR_BUG_19917_KEY";
    private static final String BUG_19917_VALUE = "WORKAROUND_FOR_BUG_19917_VALUE";

    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(BUG_19917_KEY, BUG_19917_VALUE);
        super.onSaveInstanceState(outState);
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param containerId The id of the container that will be replaced
     * @param fragment    The fragment to be added. This fragment must not already be added to the activity
     */
    public void addFragment(int containerId, @NonNull Fragment fragment) {
        // check if the fragment has been added already
        Fragment temp = mFragmentManager.findFragmentByTag(fragment.getClass().getSimpleName());
        if (temp != null && temp.isAdded()) {
            return;
        }

        // add fragment and transition with animation
        try {
            mFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom, R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom).add(containerId, fragment,
                    fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            mFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom, R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom).add(containerId, fragment,
                    fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
        }
    }

    /**
     * Method is used to remove a fragment
     *
     * @param fragment The fragment to be removed. This fragment must already be added to the activity
     * @see BaseFragmentActivity#popBackStack()
     */
    public void removeFragment(@NonNull Fragment fragment) {
        try {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.remove(fragment).commitAllowingStateLoss();
            popBackStack(fragment.getTag());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method is used to pop the top state off the back stack
     */
    public void popBackStack() {
        mFragmentManager.popBackStack();
    }

    /**
     * Method is used to pop the top state off the back stack by name
     */
    public void popBackStack(String name) {
        mFragmentManager.popBackStack(name, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
