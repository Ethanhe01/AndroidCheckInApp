package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RepresentativeStudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_student);
/*
        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        this.account = message;
        TextView layout = (TextView) findViewById((R.id.password));
        layout.setText(message);*/
    }

    public void checkIn(View view){
        Intent intent = new Intent();
        intent.setClass(RepresentativeStudentActivity.this, CheckInActivity.class);
        startActivity(intent);
        //intent.putExtra(EXTRA_MESSAGE, this.account);
    }
}
