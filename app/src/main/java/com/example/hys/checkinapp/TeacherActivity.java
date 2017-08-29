package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TeacherActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.hys.checkinapp.MESSAGE1";

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);
    }

    public void teacherInquiry(View view){
        Intent intent = new Intent(this,TeacherInquiryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void setRatio(View view){
        Intent intent = new Intent(this,SetRatioActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void checkIn(View view){
        Intent intent = new Intent(this,CheckInActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void checkOut(View view){
        Intent intent = new Intent(this,CheckOutActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }
}
