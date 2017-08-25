package com.example.hys.checkinapp;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class ServerUtils {

	/**
	 * 文件上传
	 * 
	 * @param urlStr  接口路径
	 * @param filePath  本地图片路径
	 * @return
	 */

	// Storage Permissions
	/*private static final int REQUEST_EXTERNAL_STORAGE = 1;
	private static String[] PERMISSIONS_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};*/

	/**
	 * Checks if the app has permission to write to device storage
	 *
	 * If the app does not has permission then the user will be prompted to grant permissions
	 *
	 * @param //activity
	 */
	/*public static void verifyStoragePermissions(Activity activity) {
		// Check if we have write permission
		int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

		if (permission != PackageManager.PERMISSION_GRANTED) {
			// We don't have permission so prompt the user
			ActivityCompat.requestPermissions(
					activity,
					PERMISSIONS_STORAGE,
					REQUEST_EXTERNAL_STORAGE
			);
		}
	}*/

	public static String formUpload(String urlStr, String filePath) {
		String rsp = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "|"; // request头和上传文件内容分隔符
		try {

            /*List<NameValuePair> params1 = new ArrayList<NameValuePair>();
            params1.add(new BasicNameValuePair("ID", LoginActivity.email));
            final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params1, "utf-8");
            conn.setEntity(entity);*/


			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(30000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			File file = new File(filePath);
			String filename = file.getName();
			String contentType = "";
			if (filename.endsWith(".png")) {
				contentType = "image/png";
			}
			if (filename.endsWith(".jpg")) {
				contentType = "image/jpg";
			}
			if (filename.endsWith(".gif")) {
				contentType = "image/gif";
			}
			if (filename.endsWith(".bmp")) {
				contentType = "image/bmp";
			}
			if (contentType == null || contentType.equals("")) {
				contentType = "application/octet-stream";
			}
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"" + filePath + "\"; filename=\"" + filename + " " + LoginActivity.email + "\"\r\n");
			strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
			out.write(strBuf.toString().getBytes());
			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			rsp = buffer.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return rsp;
	}

}
