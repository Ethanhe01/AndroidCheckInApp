package com.example.hys.checkinapp;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class SetRatioActivity extends AppCompatActivity {
    private EditText mCoursenumView=null;
    private EditText mRatioView=null;
    private Button btn=null;

    private String CourseNum="";
    private String Ratio="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ratio);

        mCoursenumView = (EditText) findViewById(R.id.courseId);
        mRatioView = (EditText)findViewById(R.id.ratio);
        btn=(Button)findViewById(R.id.viewResult);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CourseNum = mCoursenumView.getText().toString();//取出课程号
                Ratio = mRatioView.getText().toString();//取出占比值
                if(CourseNum=="")
                {
                    mCoursenumView.setError("请填写课程号");
                    mCoursenumView.requestFocus();
                }
                else if(Ratio=="")
                {
                    mRatioView.setError("请填写考勤成绩占比");
                    mRatioView.requestFocus();
                }
                else
                {
                    Float ratio = Float.parseFloat(Ratio);
                    if((ratio<0.01)||(ratio>0.50))
                    {
                        mRatioView.setError("范围错误！");
                        mRatioView.requestFocus();
                    }
                    else
                        uploadRatio();//向server上传info
                }
            }
        });
    }

    private void uploadRatio()
    {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/Uploadratio");
            HttpPost httpPost = new HttpPost("http://008271b.nat123.cc/HttpClientDemo/Uploadratio");

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseNum", CourseNum));
            params1.add(new BasicNameValuePair("Percentage", Ratio));
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");
                if(response.equals("true"))
                    Toast.makeText(SetRatioActivity.this, "提交成功", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SetRatioActivity.this, "提交失败，请检查是否已经提交过！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
