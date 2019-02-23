package com.example.livedatademo.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.example.livedatademo.R;

public class BaseFragment extends Fragment {

    @Nullable
    private OnRemoveFragment mOnRemoveFragment;

    /**
     * Set onRemoveListener used for inheritance
     *
     * @param fragment The Fragment to be removed
     */
    public void setOnRemoveListener(OnRemoveFragment fragment) {
        mOnRemoveFragment = fragment;
    }

    /**
     * Method is used to add fragment to the current stack
     *
     * @param containerId The id of the container that will be replaced
     * @param fragment    The new Fragment that is going to replace the container
     */
    public void addFragment(int containerId, @NonNull Fragment fragment) {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            // check if the fragment has been added already
            Fragment temp = getActivity().getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
            if (temp != null && temp.isAdded()) {
                return;
            }

            // add fragment and transition with animation
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom, R.anim.slide_in_from_bottom,
                    R.anim.slide_out_to_bottom).add(containerId, fragment,
                    fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commit();
        }
    }

    /**
     * Method is used to remove a fragment
     *
     * @param fragment The fragment to be removed
     */
    public void removeFragment(@NonNull Fragment fragment) {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            try {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                ft.remove(fragment).commitAllowingStateLoss();
                popBackStack();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for removing the Fragment view
     */
    public void remove() {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            try {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
                ft.remove(this).commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method is used to pop the top state off the back stack. Returns true if there
     * was one to pop, else false. This function is asynchronous -- it enqueues the
     * request to pop, but the action will not be performed until the application
     * returns to its event loop.
     */
    public void popBackStack() {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            try {
                getActivity().getSupportFragmentManager().popBackStack();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mOnRemoveFragment != null) {
            mOnRemoveFragment.onRemove();
            mOnRemoveFragment = null;
        }
    }

    /**
     * Method for removing a fragment
     */
    public interface OnRemoveFragment {
        void onRemove();
    }
}
