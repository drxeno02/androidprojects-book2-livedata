package com.example.livedatademo.fragment;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.livedatademo.R;
import com.example.livedatademo.constants.Constants;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.viewmodel.UserSessionViewModel;
import com.example.livedatademo.utils.DialogUtils;
import com.example.livedatademo.utils.Utils;

import java.util.List;

public class EditFragment extends BaseFragment implements View.OnClickListener {

    private Context mContext;
    private Activity mActivity;
    private View mRootView;
    private TextView tvEdit;
    private ImageView ivClearName, ivClearEmail, ivCheckMarkName, ivCheckMarkEmail, ivBack;
    private EditText edtName, edtEmail;
    private Spinner mSpnrFavoriteColor;
    private int mSelectedUid;

    // Room database
    private UserSessionViewModel mUserSessionViewModel;
    private UserSessionEntity mUserSessionEntity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_edit, container, false);

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
        mActivity = getActivity();

        // initialize view models
        mUserSessionViewModel = ViewModelProviders.of(this).get(UserSessionViewModel.class);
        mUserSessionEntity = new UserSessionEntity();

        // initialize views
        TextView tvHeader = mRootView.findViewById(R.id.tv_title);
        tvHeader.setText(getResources().getString(R.string.edit_information));

        ivBack = mRootView.findViewById(R.id.iv_back);
        ivBack.setVisibility(View.VISIBLE);

        FrameLayout rlName = mRootView.findViewById(R.id.item_name);
        FrameLayout rlEmail = mRootView.findViewById(R.id.item_email);
        tvEdit = mRootView.findViewById(R.id.tv_edit);
        // name
        TextView tvNameLabel = rlName.findViewById(R.id.tv_label);
        edtName = rlName.findViewById(R.id.edt_input);
        ivClearName = rlName.findViewById(R.id.iv_clear);
        ivCheckMarkName = rlName.findViewById(R.id.iv_check_mark);
        // email
        TextView tvEmailLabel = rlEmail.findViewById(R.id.tv_label);
        edtEmail = rlEmail.findViewById(R.id.edt_input);
        ivClearEmail = rlEmail.findViewById(R.id.iv_clear);
        ivCheckMarkEmail = rlEmail.findViewById(R.id.iv_check_mark);

        // save button
        tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));

        // spinner
        mSpnrFavoriteColor = mRootView.findViewById(R.id.spnr_favorite_color);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.array_menu_options));
        mSpnrFavoriteColor.setAdapter(adapter);
        mSpnrFavoriteColor.setSelection(0); // default pos

        // set text
        tvNameLabel.setText(getResources().getString(R.string.name));
        tvEmailLabel.setText(getResources().getString(R.string.email));

        // clear focus (default)
        edtName.clearFocus();
        edtEmail.clearFocus();
    }

    /**
     * Method is used to initialize click listeners
     */
    private void initializeHandlers() {
        tvEdit.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivClearName.setOnClickListener(this);
        ivClearEmail.setOnClickListener(this);
    }

    /**
     * Method is used to add the given observer to the observers list
     * within the lifespan for Room database
     */
    private void initializeRoomDb() {
        Bundle args = getArguments();
        if (args != null) {
            mSelectedUid = args.getInt(Constants.KEY_UID, 0);
        }

        // set observer
        mUserSessionViewModel.getInfo().observe(this, new Observer<List<UserSessionEntity>>() {
            @Override
            public void onChanged(@Nullable List<UserSessionEntity> userSessionEntities) {
                if (userSessionEntities != null && !userSessionEntities.isEmpty()) {
                    for (int i = 0; i < userSessionEntities.size(); i++) {
                        if (mSelectedUid == userSessionEntities.get(i).getUid()) {
                            // set session entity
                            mUserSessionEntity = userSessionEntities.get(i);
                            // set values
                            edtName.setText(mUserSessionEntity.getName());
                            edtEmail.setText(mUserSessionEntity.getEmailAddress());
                            mSpnrFavoriteColor.setSelection(mUserSessionEntity.getFavoriteColorPos());
                            // set visibility
                            ivClearName.setVisibility(View.VISIBLE);
                            ivCheckMarkName.setVisibility(View.VISIBLE);
                            ivCheckMarkEmail.setVisibility(View.VISIBLE);
                            // set focus
                            edtName.requestFocus();
                            edtName.setSelectAllOnFocus(true);
                            edtEmail.setSelectAllOnFocus(true);
                            // show keyboard
                            Utils.showKeyboard(mContext);
                        }
                    }
                }
            }
        });
    }

    /**
     * Initialize custom listeners
     */
    private void initializeListeners() {
        // set OnItemSelected listener
        mSpnrFavoriteColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInformationValid()) {
                    // set background
                    tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_light_blue));
                    // clear focus
                    edtName.clearFocus();
                    edtEmail.clearFocus();
                    // hide keyboard
                    Utils.hideKeyboard(mContext, mActivity.getWindow().getDecorView().getWindowToken());
                } else {
                    // set background
                    tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // do nothing
            }
        });

        // addTextChangedListener listener
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtName.hasFocus()) {
                    if (s.length() > 0) {
                        // set visibility
                        ivClearName.setVisibility(View.VISIBLE);
                        ivCheckMarkName.setVisibility(View.VISIBLE);

                        if (isInformationValid()) {
                            // set background
                            tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_light_blue));
                        } else {
                            // set background
                            tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                        }
                    } else {
                        // set visibility
                        ivClearName.setVisibility(View.GONE);
                        ivCheckMarkName.setVisibility(View.INVISIBLE);
                        // set background
                        tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                    }
                }
            }
        });

        // onFocusChange listener
        edtName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // set visibility
                    if (edtName.getText().length() > 0) {
                        ivClearName.setVisibility(View.VISIBLE);
                    }
                } else {
                    // set visibility
                    ivClearName.setVisibility(View.GONE);
                }
            }
        });

        // addTextChangedListener listener
        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtEmail.hasFocus()) {
                    if (s.length() > 0) {
                        // set visibility
                        ivClearEmail.setVisibility(View.VISIBLE);
                        if (Utils.isValidEmail(edtEmail.getText().toString())) {
                            ivCheckMarkEmail.setVisibility(View.VISIBLE);
                        }

                        if (isInformationValid()) {
                            // set background
                            tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_light_blue));
                        } else {
                            // set background
                            tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                        }
                    } else {
                        // set visibility
                        ivClearEmail.setVisibility(View.GONE);
                        ivCheckMarkEmail.setVisibility(View.INVISIBLE);
                        // set background
                        tvEdit.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                    }
                }
            }
        });

        // onFocusChange listener
        edtEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // set visibility
                    if (edtEmail.getText().length() > 0) {
                        ivClearEmail.setVisibility(View.VISIBLE);
                        if (Utils.isValidEmail(edtEmail.getText().toString())) {
                            ivCheckMarkEmail.setVisibility(View.VISIBLE);
                        } else {
                            ivCheckMarkEmail.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    // set visibility
                    ivClearEmail.setVisibility(View.GONE);
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
            case R.id.iv_clear:
                if (edtName.hasFocus()) {
                    edtName.setText("");
                } else {
                    edtEmail.setText("");
                }
                break;
            case R.id.tv_edit:
                if (isInformationValid()) {
                    // update user SQLite database
                    mUserSessionEntity.setName(edtName.getText().toString());
                    mUserSessionEntity.setEmailAddress(edtEmail.getText().toString());
                    mUserSessionEntity.setFavoriteColorPos(mSpnrFavoriteColor.getSelectedItemPosition());
                    mUserSessionViewModel.update(mUserSessionEntity);

                    // show dialog
                    DialogUtils.showDefaultOKAlert(mContext, getResources().getString(R.string.success),
                            mContext.getResources().getString(R.string.information_successfully_edited),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dismiss dialog
                                    DialogUtils.dismissDialog();
                                    popBackStack();
                                    remove();
                                }
                            });
                }
                break;
            default:
                break;
        }
    }

    /**
     * Method is used to check that the form information e.g. name, email and favorite color
     * is filled out
     *
     * @return True if form information is correctly filled out, otherwise false
     */
    private boolean isInformationValid() {
        if (mSpnrFavoriteColor.getSelectedItemPosition() > 0 &&
                !TextUtils.isEmpty(edtName.getText().toString()) &&
                (!TextUtils.isEmpty(edtEmail.getText().toString()) &&
                        Utils.isValidEmail(edtEmail.getText().toString()))) {
            return true;
        }
        return false;
    }

    @Override
    public void onPause() {
        // hide keyboard
        Utils.hideKeyboard(mContext, mActivity.getWindow().getDecorView().getWindowToken());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (edtName.hasFocus() || edtEmail.hasFocus()) {
            // show keyboard
            Utils.showKeyboard(mContext);
        }
    }
}
