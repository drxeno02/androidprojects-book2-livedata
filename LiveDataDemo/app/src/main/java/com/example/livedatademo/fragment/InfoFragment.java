package com.example.livedatademo.fragment;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livedatademo.R;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.viewmodel.UserSessionViewModel;

import java.util.List;

public class InfoFragment extends BaseFragment implements View.OnClickListener {


    private Context mContext;
    private Activity mActivity;
    private View mRootView;
    private TextView tvHeader;
    private ImageView ivBack;

    // Room database
    private UserSessionViewModel mUserSessionViewModel;
    private UserSessionEntity mUserSessionEntity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.activity_main, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();
        initializeRoomDb();
        return mRootView;
    }

    /**
     * Method is used to initialize views
     */
    private void initializeViews() {
        // instantiate context and activity instance
        mContext = getActivity();
        mActivity = getActivity();

        // initialize view models
        mUserSessionViewModel = ViewModelProviders.of(this).get(UserSessionViewModel.class);
        mUserSessionEntity = new UserSessionEntity();

        // initialize views
        tvHeader = mRootView.findViewById(R.id.tv_title);
        tvHeader.setText(getResources().getString(R.string.view_data));

        ivBack = mRootView.findViewById(R.id.iv_back);



    }

    /**
     * Method is used to initialize click listeners
     */
    private void initializeHandlers() {
        ivBack.setOnClickListener(this);
    }

    /**
     * Method is used to add the given observer to the observers list
     * within the lifespan for Room database
     */
    private void initializeRoomDb() {
        // set observer
        mUserSessionViewModel.getInfo().observe(this, new Observer<List<UserSessionEntity>>() {
            @Override
            public void onChanged(@Nullable List<UserSessionEntity> userSessionEntities) {
                if (userSessionEntities != null && !userSessionEntities.isEmpty()) {
                    mUserSessionEntity = userSessionEntities.get(0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
