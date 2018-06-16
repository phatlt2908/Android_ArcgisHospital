package com.phatlt.suportmedicalacrgisapi;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedBack extends AppCompatActivity {

    EditText txtFdbkName, txtFdbkAddress, txtFdbkCoor, txtFdbkNote, txtFdbkEmail, txtFdbkPhone;
    Button btnFdbkSend;

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

        addControls();
        addEvents();
    }

    private void addEvents() {

    }

    private void addControls() {

    }
}
