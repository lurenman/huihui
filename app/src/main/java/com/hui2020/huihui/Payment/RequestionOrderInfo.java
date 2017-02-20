package com.hui2020.huihui.Payment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2017/1/12.
 */

public class RequestionOrderInfo extends AsyncTask<Void, Void, Boolean> {
    private URL url = null;
    private JSONObject item;
    private Intent intent;

    private Activity activity;
    private String orderNumber, newpassword;
    private String orderInfo;

    public RequestionOrderInfo(Activity activity, String orderNumber) {
        this.activity = activity;
        this.orderNumber = orderNumber;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/alipaywap/signatures_url.php");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            httpURLConnection.setRequestMethod("POST");// 提交模式
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpURLConnection.setConnectTimeout(30000);//连接超时 单位毫秒
        // conn.setReadTimeout(2000);//读取超时 单位毫秒
        // 发送POST请求必须设置如下两行
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);//忽略缓存

        // 获取URLConnection对象对应的输出流
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // 发送请求参数
        //post的参数 xx=xx&yy=yy
        String post = "orderCode=" + orderNumber;
        Log.e("GetChoiceTicket,post", post);
        printWriter.write(post);
        printWriter.flush();
        InputStream is = null;
        try {
            is = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        String result = HttpUtils.readMyInputStream(is);
        Log.e("GetChoiceTicket.result", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetChoiceTicket", "Service error");
            return false;
        }

        String state = jsonobj.optString("res");
        if (state.equals("0")) {
            return false;
        } else {
        }

        orderInfo = jsonobj.optString("orderInfo");
        if (state.equals("0")) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.e("RequestOrder","success"+orderInfo);
        } else {
            Log.e("RequestOrder","false"+orderInfo);
        }
    }



}
