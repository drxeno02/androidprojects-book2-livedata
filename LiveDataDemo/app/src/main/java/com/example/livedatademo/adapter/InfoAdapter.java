package com.example.livedatademo.adapter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private List<UserSessionEntity> alUserSessionEntity;

    public InfoAdapter(@NonNull Context context, @Nullable List<UserSessionEntity> userSessionEntities) {
        mContext = context;
        mUserSessionViewModel = ViewModelProviders.of((FragmentActivity) mContext).get(UserSessionViewModel.class);
        alUserSessionEntity = userSessionEntities;

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
                    // update list
                    alUserSessionEntity = userSessionEntities;
                    notifyDataSetChanged();
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

        // info
        holder.tvName.setText(alUserSessionEntity.get(position).getName());
        holder.tvEmail.setText(alUserSessionEntity.get(position).getEmailAddress());

        // set favorite color
        holder.ivFavoriteColor.setColorFilter(ContextCompat.getColor(mContext,
                getFavoriteColor(alUserSessionEntity.get(position).getFavoriteColor())));

        // set click listener
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // set visibility

            }
        });
    }

    @Override
    public int getItemCount() {
        return alUserSessionEntity.size();
    }

    /**
     * @param userSessionEntities
     */
    public void updateData(@NonNull List<UserSessionEntity> userSessionEntities) {
        if (!userSessionEntities.isEmpty()) {
            alUserSessionEntity = userSessionEntities;
            notifyDataSetChanged();
        }
    }

    /**
     * Method is used to retrieve favorite colors
     *
     * @param color Favorite color (String value)
     * @return Favorite color
     */
    private int getFavoriteColor(@NonNull String color) {
        if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.red))) {
            return R.color.md_red_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.pink))) {
            return R.color.md_pink_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.purple))) {
            return R.color.md_purple_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.indigo))) {
            return R.color.md_indigo_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.blue))) {
            return R.color.md_blue_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.cyan))) {
            return R.color.md_cyan_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.teal))) {
            return R.color.md_teal_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.green))) {
            return R.color.md_green_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.yellow))) {
            return R.color.md_yellow_500;
        } else if (color.equalsIgnoreCase(mContext.getResources().getString(R.string.orange))) {
            return R.color.md_orange_500;
        }
        return R.color.md_grey_500;
    }

    /**
     * View holder class
     * <p>A ViewHolder describes an item view and metadata about its place within the RecyclerView</p>
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout llWrapper;
        private final TextView tvName, tvEmail;
        private final ImageView ivFavoriteColor, ivEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            llWrapper = itemView.findViewById(R.id.ll_wrapper);
            tvName = itemView.findViewById(R.id.tv_name);
            tvEmail = itemView.findViewById(R.id.tv_email);
            ivFavoriteColor = itemView.findViewById(R.id.iv_favorite_color);
            ivEdit = itemView.findViewById(R.id.iv_edit);

        }
    }
}
