package com.example.livedatademo.activity;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.livedatademo.R;
import com.example.livedatademo.room.entity.UserSessionEntity;
import com.example.livedatademo.room.viewmodel.UserSessionViewModel;
import com.example.livedatademo.utils.DialogUtils;
import com.example.livedatademo.utils.Utils;

import java.util.List;

public class MainActivity extends BaseFragmentActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;
    private Activity mActivity;
    private FrameLayout rlName, rlEmail;
    private TextView tvHeader, tvNameLabel, tvEmailLabel, tvSave, tvViewData;
    private ImageView ivBack, ivClearName, ivClearEmail, ivCheckMarkName, ivCheckMarkEmail;
    private EditText edtName, edtEmail;
    private Spinner mSpnrFavoriteColor;

    // Room database
    private UserSessionViewModel mUserSessionViewModel;
    private UserSessionEntity mUserSessionEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiate views
        initializeViews();
        initializeHandlers();
        initializeListeners();
        initializeRoomDb();
    }

    /**
     * Method is used to initialize views
     */
    private void initializeViews() {
        // instantiate context and activity instance
        mContext = MainActivity.this;
        mActivity = MainActivity.this;

        // initialize view models
        mUserSessionViewModel = ViewModelProviders.of(this).get(UserSessionViewModel.class);
        mUserSessionEntity = new UserSessionEntity();

        // initialize views
        tvHeader = findViewById(R.id.tv_title);
        tvHeader.setText(getResources().getString(R.string.save_information));

        rlName = findViewById(R.id.item_name);
        rlEmail = findViewById(R.id.item_email);
        tvViewData = findViewById(R.id.tv_view_data);
        tvSave = findViewById(R.id.tv_save);
        tvNameLabel = rlName.findViewById(R.id.tv_label);
        edtName = rlName.findViewById(R.id.edt_input);
        ivClearName = rlName.findViewById(R.id.iv_clear);
        ivCheckMarkName = rlName.findViewById(R.id.iv_check_mark);
        tvEmailLabel = rlEmail.findViewById(R.id.tv_label);
        edtEmail = rlEmail.findViewById(R.id.edt_input);
        ivClearEmail = rlEmail.findViewById(R.id.iv_clear);
        ivCheckMarkEmail = rlEmail.findViewById(R.id.iv_check_mark);

        // save button
        tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));

        // spinner
        mSpnrFavoriteColor = findViewById(R.id.spnr_favorite_color);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.array_menu_options));
        mSpnrFavoriteColor.setAdapter(adapter);
        mSpnrFavoriteColor.setSelection(0); // default pos

        // set text
        tvNameLabel.setText(getResources().getString(R.string.name));
        tvEmailLabel.setText(getResources().getString(R.string.email));
    }

    /**
     * Method is used to initialize click listeners
     */
    private void initializeHandlers() {
        tvViewData.setOnClickListener(this);
        tvSave.setOnClickListener(this);
        ivClearName.setOnClickListener(this);
        ivClearEmail.setOnClickListener(this);
    }

    /**
     * Initialize custom listeners
     */
    private void initializeListeners() {
        // set OnItemSelected listener
        mSpnrFavoriteColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "<onItemSelected> pos= " + position + " //id= " + id);
                if (isInformationValid()) {
                    // set background
                    tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_teal));
                } else {
                    // set background
                    tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
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
                            tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_teal));
                        } else {
                            // set background
                            tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                        }
                    } else {
                        // set visibility
                        ivClearName.setVisibility(View.GONE);
                        ivCheckMarkName.setVisibility(View.INVISIBLE);
                        // set background
                        tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
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
                            tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_teal));
                        } else {
                            // set background
                            tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
                        }
                    } else {
                        // set visibility
                        ivClearEmail.setVisibility(View.GONE);
                        ivCheckMarkEmail.setVisibility(View.INVISIBLE);
                        // set background
                        tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
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
                        }
                    }
                } else {
                    // set visibility
                    ivClearEmail.setVisibility(View.GONE);
                    ivCheckMarkEmail.setVisibility(View.INVISIBLE);
                }
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
                    mUserSessionEntity = userSessionEntities.get(0);
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
            case R.id.iv_clear:
                if (edtName.hasFocus()) {
                    edtName.setText("");
                } else {
                    edtEmail.setText("");
                }
                break;
            case R.id.tv_view_data:

                break;
            case R.id.tv_save:
                if (isInformationValid()) {
                    // update user SQLite database
                    mUserSessionEntity.setName(edtName.getText().toString());
                    mUserSessionEntity.setEmailAddress(edtEmail.getText().toString());
                    mUserSessionEntity.setFavoriteColor(mSpnrFavoriteColor.getSelectedItem().toString());
                    if (mUserSessionViewModel.getInfo() == null) {
                        mUserSessionViewModel.insert(mUserSessionEntity);
                    } else {
                        mUserSessionViewModel.update(mUserSessionEntity);
                    }

                    // show dialog
                    DialogUtils.showDefaultOKAlert(mContext, getResources().getString(R.string.success),
                            mContext.getResources().getString(R.string.successfully_saved_information),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DialogUtils.dismissDialog();

                                    // clear form
                                    clearForm();
                                }
                            });
                } else {
                    // show dialog
                    DialogUtils.showDefaultOKAlert(mContext, getResources().getString(R.string.missing_information),
                            mContext.getResources().getString(R.string.must_fill_out_information),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DialogUtils.dismissDialog();
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

    /**
     * Method is used to clear form
     */
    private void clearForm() {
        // reset values
        edtName.setText("");
        edtEmail.setText("");
        // set focus
        edtName.requestFocus();
        // set selection
        mSpnrFavoriteColor.setSelection(0);
        // set visibility
        ivClearName.setVisibility(View.GONE);
        ivCheckMarkName.setVisibility(View.INVISIBLE);
        ivClearEmail.setVisibility(View.GONE);
        ivCheckMarkEmail.setVisibility(View.INVISIBLE);
        // set background
        tvSave.setBackground(ContextCompat.getDrawable(mContext, R.drawable.pill_grey));
    }
}
