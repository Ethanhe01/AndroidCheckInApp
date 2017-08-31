package com.example.hys.checkinapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SetPunishmentActivity extends AppCompatActivity {

    private DatePicker datePicker = null;
    private EditText mCoursenumView = null;
    private EditText mStudentidView = null;
    private EditText mPunishmentView = null;
    private Button btn = null;

    private String time = "";//默认时间
    private String CourseNum = "";
    private String StudentID = "";
    private String PunishmentContent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_punishment);

        /************** 获取北京时间，作为默认的查询时间 ****************/
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd");
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = null;//生成连接对象
            uc = url.openConnection();
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            Date date = new Date(ld); //转换为标准时间对象
            time = dff.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] names = time.split("-");
        String year = names[0];
        String month = names[1];
        String day = names[2];

        int m = Integer.parseInt(month);
        if(m==1)
            m=12;
        else
            m--;

        /* 选择要查询的记录产生的日期*/
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(year), m, Integer.parseInt(day), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 获取一个日历对象，并初始化为当前选中的时间
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                time = format.format(calendar.getTime());
                //Toast.makeText(StudentInquiryActivity.this, "要查询的时间是："+time, Toast.LENGTH_SHORT).show();
            }
        });

        btn = (Button)findViewById(R.id.viewResult);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean start = true;

                mCoursenumView = (EditText) findViewById(R.id.courseId);
                CourseNum = mCoursenumView.getText().toString();//获取输入的课程号
                if(!validCourseNum())
                {
                    start = false;
                    mCoursenumView.setError("课程号有误，请检查！");
                    mCoursenumView.requestFocus();
                }

                mStudentidView = (EditText) findViewById(R.id.studentId);
                StudentID = mStudentidView.getText().toString();//获取输入的学生学号
                if(!validStudentID())
                {
                    start = false;
                    mStudentidView.setError("学生学号有误，请检查！");
                    mStudentidView.requestFocus();
                }

                mPunishmentView = (EditText) findViewById(R.id.content);
                PunishmentContent = mPunishmentView.getText().toString();//获取输入的处分内容
                if(!validContent())
                {
                    start = false;
                    mPunishmentView.setError("处分内容有误，请检查！");
                    mPunishmentView.requestFocus();
                }
                if(start)
                {
                    Toast.makeText(SetPunishmentActivity.this, "课程时间是：" + time, Toast.LENGTH_SHORT).show();
                    UploadPunishment();
                }
            }
        });
    }

    private boolean validStudentID()
    {
        if(StudentID.length()!=13)
            return false;
        else
        {
            String sub = StudentID.substring(0,4);//取出前4位
            int year = Integer.parseInt(sub);
            if((year<2014)||(year>2067))
                return false;
            return true;
        }
    }

    private boolean validCourseNum()
    {
        if(CourseNum.length()!=11)
            return false;
        else
        {
            String sub = CourseNum.substring(0,4);//取出前4位
            int year = Integer.parseInt(sub);
            if((year<2014)||(year>2067))
                return false;
            return true;
        }
    }

    private  boolean validContent()
    {
        if(PunishmentContent.length()==0)
            return false;
        if(PunishmentContent.length()>140)
            return false;
        return true;
    }

    private void UploadPunishment()
    {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/QueryImage");
            HttpPost httpPost = new HttpPost("http://18k22437d2.iask.in:20267/HttpClientDemo/UploadPunishment");
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseNum", CourseNum));//课程号
            params1.add(new BasicNameValuePair("StudentID", StudentID));//学生学号
            params1.add(new BasicNameValuePair("Time", time));//课程时间
            params1.add(new BasicNameValuePair("Content", PunishmentContent));//处分内容
            params1.add(new BasicNameValuePair("AdminID", LoginActivity.email));//辅导员账号

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");

                if (response.equals("true"))
                    Toast.makeText(SetPunishmentActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SetPunishmentActivity.this, "上传失败，请重试！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


}