package com.example.hys.checkinapp;

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

public class HandleAppealActivity extends AppCompatActivity {

    private DatePicker datePicker = null;
    private Button btn_coursenum = null;
    private Button btn_search = null;
    private TextView tv = null;
    private ImageView iv = null;
    private FinalBitmap fb = null;
    private EditText mCoursenumView = null;
    private EditText mStudentidView = null;

    private String CourseTime = "";
    private String AppealList = "";
    private String CourseNum = "";
    private String StudentID = "";

    private final String DownloadUrl = "http://18k22437d2.iask.in:20267/HttpClientDemo/proof/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle_appeal);

        fb=FinalBitmap.create(this);
        mCoursenumView = (EditText) findViewById(R.id.courseId);
        mStudentidView = (EditText) findViewById(R.id.studentId);

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
            CourseTime = dff.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] names = CourseTime.split("-");
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
                CourseTime = format.format(calendar.getTime());
                //Toast.makeText(StudentInquiryActivity.this, "要查询的时间是："+time, Toast.LENGTH_SHORT).show();
            }
        });


        btn_coursenum = (Button)findViewById(R.id.search_replace_classes);
        btn_coursenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                SearchForList();
            }
        });

        btn_search = (Button)findViewById(R.id.btn);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CourseNum = mCoursenumView.getText().toString();
                StudentID = mStudentidView.getText().toString();
                if(validStudentID() && validCourseNum())
                    DownloadProof();
            }
        });

        tv = (TextView)findViewById(R.id.allCorseId);
        iv = (ImageView)findViewById(R.id.iv);

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

    private void SearchForList()
    {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/QueryImage");
            HttpPost httpPost = new HttpPost("http://18k22437d2.iask.in:20267/HttpClientDemo/DownloadAppealList");
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseTime", CourseTime));//要查询的课程时间

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            //int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");
                String[] name = response.split("\\.");
                if (name[0].equals("true"))
                {
                    String[] i = name[1].split("-");//i里每一个元素是一条记录
                    for(int j=0; j<i.length; j++)
                    {
                        String[] cur = i[j].split("@");
                        AppealList = AppealList + "学生："+cur[0]+"    课程号："+cur[1]+"\n";
                    }
                    tv.setText(AppealList);
                    //fb.display(iv, Const.DOWNLOAD_URL+path);
                }
                else
                    Toast.makeText(HandleAppealActivity.this, "无相应记录，请检查查询条件！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DownloadProof()
    {
        /****************** 连接服务器和DB *******************/
        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httpPost = new HttpPost("http://192.168.191.1:8080/HttpClientDemo/QueryImage");
            HttpPost httpPost = new HttpPost("http://18k22437d2.iask.in:20267/HttpClientDemo/DownloadProof");
            List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("CourseTime", CourseTime));//要查询的课程时间
            params1.add(new BasicNameValuePair("CourseNum", CourseNum));//要查询的课程号
            params1.add(new BasicNameValuePair("StudentID", StudentID));//要查询的学生账号
            params1.add(new BasicNameValuePair("AdminID", LoginActivity.email));//当前辅导员账号

            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            httpPost.setEntity(entity);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            int i = httpResponse.getStatusLine().getStatusCode();
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity1 = httpResponse.getEntity();
                String response = EntityUtils.toString(entity1, "utf-8");
                String[] name = response.split(" ");
                if (name[0].equals("true"))
                {
                    fb.display(iv, DownloadUrl + name[1]);
                }
                else
                    Toast.makeText(HandleAppealActivity.this, "无相应记录，请检查查询条件！", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




}
