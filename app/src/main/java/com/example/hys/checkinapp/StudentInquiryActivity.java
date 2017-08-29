package com.example.hys.checkinapp;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

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

public class StudentInquiryActivity extends AppCompatActivity {

    private DatePicker datePicker;
    private EditText mCoursenumView=null;
    private Button btn_image=null;
    private Button btn_result=null;
    private TextView tv=null;
    private ImageView iv=null;
    private FinalBitmap fb=null;

    private String time="";//默认时间
    private String CourseNum="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_inquiry);

        mCoursenumView = (EditText) findViewById(R.id.courseId);
        fb=FinalBitmap.create(this);

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

        /* 选择要查询的记录产生的日期*/
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.init(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), new DatePicker.OnDateChangedListener() {
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

        /* 查询照片按钮 */
        btn_image=(Button)findViewById(R.id.btn);
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(StudentInquiryActivity.this, "要查询的时间是："+time, Toast.LENGTH_SHORT).show();
                CourseNum = mCoursenumView.getText().toString();
                if(CourseNum=="")
                {
                    mCoursenumView.setError("请填写课程号");
                    mCoursenumView.requestFocus();
                }
                else
                    getPic();
            }
        });

        /* 查询考勤记录 */
        btn_result=(Button)findViewById(R.id.viewResult);
        btn_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(StudentInquiryActivity.this, "要查询的时间是："+time, Toast.LENGTH_SHORT).show();
                CourseNum = mCoursenumView.getText().toString();
                if(CourseNum=="")
                {
                    mCoursenumView.setError("请填写课程号");
                    mCoursenumView.requestFocus();
                }
                else
                    showAttendanceInfo();
            }
        });

        tv=(TextView)findViewById(R.id.tv);
        iv=(ImageView)findViewById(R.id.iv);

    }


    private void getPic() {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/QueryImage");

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseNum", CourseNum));//要查询的课程号
            params1.add(new BasicNameValuePair("Time", time));//要查询的时间
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");
                String[] name = response.split("@");
                if (name[1].equals("true")) {
                    String path = name[0];
                    fb.display(iv, Const.DOWNLOAD_URL+path);
                } else
                    Toast.makeText(StudentInquiryActivity.this, "无相应记录，请检查查询条件！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showAttendanceInfo() {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/QueryResult");

            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseNum", CourseNum));//要查询的课程号
            params1.add(new BasicNameValuePair("ID", LoginActivity.email));//当前用户账号
            params1.add(new BasicNameValuePair("Time", time));//要查询的时间
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");
                String[] name = response.split("\\.");
                String inresult = name[0];//签到判定结果
                String outresult = name[1];//签退判定结果
                if(name[2].equals("true"))
                {
                    if(inresult.equals("Y"))
                        inresult = "成功";
                    else
                        inresult = "失败";
                    if(outresult.equals("Y"))
                        outresult = "成功";
                    else
                        outresult = "失败";
                    String result = "签到："+inresult + "       签退："+outresult;
                    tv.setText(result);
                }
                else
                    Toast.makeText(StudentInquiryActivity.this, "无相应记录，请检查查询条件！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}