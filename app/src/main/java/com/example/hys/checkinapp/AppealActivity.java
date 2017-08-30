package com.example.hys.checkinapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AppealActivity extends AppCompatActivity {

    private EditText mCoursenumView=null;
    private Button btn=null;
    private ProgressDialog dialog=null;
    private FinalBitmap fb=null;
    private TextView tv=null;
    private ImageView iv=null;

    public static String CourseNum="";
    public static String CurTime="";

    private String result="";
    //private final String UploadUrl = "http://192.168.191.1:8080/HttpClientDemo/UploadProof";
    //private final String DownloadUrl = "http://192.168.191.1:8080/HttpClientDemo/proof/";
    private final String UploadUrl = "http://18131q29d3.51mypc.cn:28420/HttpClientDemo/UploadProof";
    private final String DownloadUrl = "http://18131q29d3.51mypc.cn:28420/HttpClientDemo/proof/";

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dialog.dismiss();
            switch (msg.what) {
                case 1:
                    Toast.makeText(getApplicationContext(), "处理后的图片上传成功", Toast.LENGTH_SHORT).show();
                    String[] res = result.split(" ");
                    String path = res[1];
                    tv.setText("上传成功,处理后的图片路径："+DownloadUrl+path);//result);
                    fb.display(iv, DownloadUrl+path);//result);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "处理后的图片上传失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appeal);

        mCoursenumView = (EditText) findViewById(R.id.courseId);

        dialog=new ProgressDialog(this);
        dialog.setTitle("请稍后...");

        fb=FinalBitmap.create(this);

        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                CourseNum = mCoursenumView.getText().toString();
                if(CourseNum==null)
                {
                    mCoursenumView.setError("请填写课程号");
                    mCoursenumView.requestFocus();
                }
                else
                {
                    if(CourseNum.length()!=11)
                    {
                        mCoursenumView.setError("课程号有误，请检查！");
                        mCoursenumView.requestFocus();
                    }
                    else
                    {
                        int iyear = Integer.parseInt(CourseNum.substring(0,4));//前4位
                        if((iyear<2014)||(iyear>2067)){
                            mCoursenumView.setError("课程号有误，请检查！");
                            mCoursenumView.requestFocus();
                        }
                        else{
                            Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(openAlbumIntent, 1);
                        }
                    }
                }
            }
        });

        tv=(TextView)findViewById(R.id.tv);
        iv=(ImageView)findViewById(R.id.iv);
        //CourseNum = mCoursenumView.getText().toString();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        /************** 获取北京时间，非系统时间 ****************/
        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        URL url = null;//取得资源对象
        try {
            url = new URL("http://www.baidu.com");
            URLConnection uc = null;//生成连接对象
            uc = url.openConnection();
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            Date date = new Date(ld); //转换为标准时间对象
            CurTime = dff.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        Uri uri = null;
        switch (requestCode) {
            case 1:// 相册
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String[] proj = { MediaStore.Images.Media.DATA };
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                final String path = cursor.getString(column_index);// 图片在的路径
                dialog.show();

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //ServerUtils.verifyStoragePermissions(thisActivity);
                        result = ServerUtils_Proof.formUpload(UploadUrl, path);
                        Log.e("jj", "result:"+result);
                        int index = result.indexOf("true");
                        if(index!=-1){//!TextUtils.isEmpty(result)){
                            handler.sendEmptyMessage(1);
                        }else{
                            handler.sendEmptyMessage(2);
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
