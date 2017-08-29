package com.example.hys.checkinapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RepresentativeStudentActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.example.hys.checkinapp.MESSAGE2";
    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_student);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        this.account = message;
    }

    public void checkIn(View view){
        Intent intent = new Intent();
        intent.setClass(RepresentativeStudentActivity.this, CheckInActivity.class);
        startActivity(intent);
        intent.putExtra(EXTRA_MESSAGE, this.account);
    }

    public void checkOut(View view){
        Intent intent = new Intent();
        intent.setClass(RepresentativeStudentActivity.this, CheckOutActivity.class);
        startActivity(intent);
        intent.putExtra(EXTRA_MESSAGE, this.account);
    }

    public void uploadImage(View view){
        Intent intent = new Intent();
        intent.setClass(RepresentativeStudentActivity.this, UploadImagesActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void courseTable(View view){
        Intent intent = new Intent(this,CourseTableActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void appeal(View view){
        Intent intent = new Intent(this,AppealActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

    public void inquiry(View view){
        Intent intent = new Intent(this,StudentInquiryActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.account);
        startActivity(intent);
    }

}
