package com.phatlt.suportmedicalacrgisapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FeedBack extends Activity {
    EditText medicalName, medicalAdress, feedBackContent;
    Button btnSend;
    String medicalNameText, medicalAdressText, feedBackContentText;
    boolean emptyText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        addControls();
        controlsButton();
    }
    private void controlsButton(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Check();
                if (emptyText == false) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    builder.setTitle("Bạn có chắc chắn muốn gửi hay không");
                    builder.setMessage("Hãy lựa chọn bên dưới để xác nhận");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Do sent data

                        }
                    });
                    builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //

                        }
                    });
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(FeedBack.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    builder.setTitle("THÔNG BÁO");
                    builder.setMessage("Vui lòng điền đầy đủ thông tin, thông tin không được bỏ trống");
                    builder.setIcon(android.R.drawable.ic_dialog_alert);
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //

                        }
                    });
                    builder.show();
                }
            }
        });
    }
   private void addControls(){
        medicalName = (EditText) findViewById(R.id.medicalName);
        medicalAdress = (EditText) findViewById(R.id.medicalAdress);
        feedBackContent = (EditText) findViewById(R.id.feedBackContent);
        btnSend = (Button) findViewById(R.id.btnSent);

   }

   private void Check(){
       medicalNameText = medicalName.getText().toString().trim();
       medicalAdressText = medicalAdress.getText().toString().trim();
       feedBackContentText = feedBackContent.getText().toString().trim();
        if(medicalNameText.equals("") || medicalAdressText.equals("") || feedBackContent.equals("")){
            emptyText = true;
        }
        else
            emptyText = false;
   }
}
