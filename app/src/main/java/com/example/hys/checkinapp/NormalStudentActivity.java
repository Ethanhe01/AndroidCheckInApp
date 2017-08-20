package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NormalStudentActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.hys.checkinapp.MESSAGE1";

    private String account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_student);
/*
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        this.account = message;
        TextView layout = (TextView) findViewById((R.id.password));
        layout.setText(message);*/
    }

    public void checkIn(View view){
        Intent intent = new Intent(this,CheckInActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void courseTable(View view){
        Intent intent = new Intent(this,CourseTableActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }
}
