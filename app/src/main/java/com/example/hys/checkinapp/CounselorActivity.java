package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.example.hys.checkinapp.TeacherActivity.EXTRA_MESSAGE;

public class CounselorActivity extends AppCompatActivity {

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



}
