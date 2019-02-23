package com.example.livedatademo.adapter;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.livedatademo.R;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.viewmodel.UserSessionViewModel;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private Context mContext;

    // Room database
    private UserSessionViewModel mUserSessionViewModel;
    private UserSessionEntity mUserSessionEntity;

    public InfoAdapter(@NonNull Context context, @NonNull UserSessionViewModel userSessionViewModel) {
        mContext = context;
        mUserSessionViewModel = userSessionViewModel;

        // initialize Room db
        initializeRoomDb();
    }

    /**
     * Method is used to add the given observer to the observers list
     * within the lifespan for Room database
     */
    private void initializeRoomDb() {
        // set observer
        mUserSessionViewModel.getInfo().observe((FragmentActivity) mContext, new Observer<List<UserSessionEntity>>() {
            @Override
            public void onChanged(@Nullable List<UserSessionEntity> userSessionEntities) {
                if (userSessionEntities != null && !userSessionEntities.isEmpty()) {
                    mUserSessionEntity = userSessionEntities.get(0);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_info, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int index = holder.getAdapterPosition();

    }

    @Override
    public int getItemCount() {
        return mUserSessionViewModel.getInfo().getValue().size();
    }

    /**
     * View holder class
     * <p>A ViewHolder describes an item view and metadata about its place within the RecyclerView</p>
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llWrapper;
        private final TextView tv

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llWrapper = itemView.findViewById(R.id.ll_wrapper);

        }
    }
}
