package com.phatlt.suportmedicalacrgisapi;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.phatlt.suportmedicalacrgisapi.retrofit.APIUtils;
import com.phatlt.suportmedicalacrgisapi.retrofit.DataClient;

import models.Feedback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedBackActivity extends AppCompatActivity {

    EditText txtFdbkInfo, txtFdbkNote, txtFdbkEmail, txtFdbkPhone;
    Button btnFdbkSend;
    RadioGroup radioGroupType;

    DataClient dataClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        Toolbar toolbar = findViewById(R.id.feedBackToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Gửi phản hồi");
        actionbar.setDisplayHomeAsUpEnabled(true);
        // set appbar back button onclick
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dataClient = APIUtils.getData();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        addControls();
        addEvents();
    }

    private void addEvents() {
        btnFdbkSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Feedback feedback = new Feedback();
                feedback.setHos_id("1");
                feedback.setFdbk_info(txtFdbkInfo.getText().toString());
                feedback.setFdbk_note(txtFdbkNote.getText().toString());
                feedback.setEmail(txtFdbkEmail.getText().toString());
                feedback.setPhone(txtFdbkPhone.getText().toString());
                feedback.setType(radioGroupType.getCheckedRadioButtonId());

                Call<Object> callback = dataClient.sendFeedback(feedback);
                callback.enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response != null) {
                            if (response.isSuccessful()){
                                Snackbar.make(findViewById(R.id.feedBackLayout), "Phản hồi của bạn đã được gửi!\nXin cảm ơn!", Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(findViewById(R.id.feedBackLayout), "Đã xảy ra lỗi!", Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Snackbar.make(findViewById(R.id.feedBackLayout), "Đã xảy ra lỗi!", Snackbar.LENGTH_SHORT).show();
                    }
                });

                // Close keyboard
                View view = FeedBackActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private void addControls() {
        txtFdbkInfo = findViewById(R.id.txtFdbkInfo);
        txtFdbkNote = findViewById(R.id.txtFdbkNote);
        txtFdbkEmail = findViewById(R.id.txtFdbkEmail);
        txtFdbkPhone = findViewById(R.id.txtFdbkPhone);
        btnFdbkSend = findViewById(R.id.btnFdbkSend);
        radioGroupType = findViewById(R.id.radioGroup);
    }
}
