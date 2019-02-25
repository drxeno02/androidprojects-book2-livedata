package com.example.livedatademo.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.livedatademo.R;
import com.example.livedatademo.adapter.InfoAdapter;
import com.example.livedatademo.constants.Constants;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.viewmodel.UserSessionViewModel;
import com.example.livedatademo.utils.Utils;

import java.util.List;

public class InfoFragment extends BaseFragment implements View.OnClickListener {


    private Context mContext;
    private View mRootView;
    private TextView tvHeader, tvNoDataAvailable;
    private ImageView ivBack;

    // Room database
    private UserSessionViewModel mUserSessionViewModel;

    // adapter
    private LinearLayoutManager mLayoutManager;
    private RecyclerView rvInfo;
    private InfoAdapter mInfoAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_info, container, false);

        // instantiate views
        initializeViews();
        initializeHandlers();
        initializeListeners();
        initializeRoomDb();
        return mRootView;
    }

    /**
     * Method is used to initialize views
     */
    private void initializeViews() {
        // instantiate context and activity instance
        mContext = getActivity();

        // initialize view models
        mUserSessionViewModel = ViewModelProviders.of(this).get(UserSessionViewModel.class);

        // initialize views
        tvHeader = mRootView.findViewById(R.id.tv_title);
        tvHeader.setText(getResources().getString(R.string.view_information));

        tvNoDataAvailable = mRootView.findViewById(R.id.tv_no_data_available);
        ivBack = mRootView.findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);

        // initialize adapter
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvInfo = mRootView.findViewById(R.id.rv_info);
        rvInfo.setLayoutManager(mLayoutManager);
    }

    /**
     * Method is used to initialize click listeners
     */
    private void initializeHandlers() {
        ivBack.setOnClickListener(this);
    }

    /**
     * Initialize custom listeners
     */
    private void initializeListeners() {
        // onEdit listener
        InfoAdapter.onEditListener(new InfoAdapter.OnEditListener() {
            @Override
            public void onEdit(int selectedUid) {
                // add fragment
                Fragment fragment = new EditFragment();
                Bundle args = new Bundle();
                args.putInt(Constants.KEY_UID, selectedUid);
                fragment.setArguments(args);
                addFragment(R.id.rl_container, fragment);
            }
        });
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
                    // set visibility
                    rvInfo.setVisibility(View.VISIBLE);
                    tvNoDataAvailable.setVisibility(View.GONE);

                    // instantiate/update adapter
                    if (mInfoAdapter == null) {
                        mInfoAdapter = new InfoAdapter(mContext, userSessionEntities);
                        rvInfo.setAdapter(mInfoAdapter);
                    } else {
                        mInfoAdapter.updateData(userSessionEntities);
                    }
                } else {
                    // set visibility
                    tvNoDataAvailable.setVisibility(View.VISIBLE);
                    rvInfo.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!Utils.isViewClickable()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_back:
                remove();
                popBackStack();
                break;
            default:
                break;
        }
    }
}
