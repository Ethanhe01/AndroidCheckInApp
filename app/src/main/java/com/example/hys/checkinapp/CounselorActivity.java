package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.example.hys.checkinapp.TeacherActivity.EXTRA_MESSAGE;

public class CounselorActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.hys.checkinapp.MESSAGE1";

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counselor);
    }

    public void counselorSearch(View view){
        Intent intent = new Intent(this,TeacherInquiryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void recheck(View view){
        Intent intent = new Intent(this,HandleAppealActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void setPunishment(View view){
        Intent intent = new Intent(this,SetPunishmentActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

}
