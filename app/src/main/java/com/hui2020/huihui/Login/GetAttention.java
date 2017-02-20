package com.hui2020.huihui.Login;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

/**
 * Created by FD-GHOST on 2017/1/7.
 */

public class GetAttention extends AsyncTask<Void, Void, Boolean> {
    private URL url = null;
    private String newpassword;
    private MyApplication application;

    public GetAttention(MyApplication application) {
        this.application=application;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getRemarkLst&");
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
        // conn.setConnectTimeout(10000);//连接超时 单位毫秒
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
        try {
            newpassword = new Mdfive().Mdfive(application.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String post = "userId="+application.getUserId()+"&userName=" + application.getUserName() + "&pwd=" + newpassword;
        Log.e("GetAttention.post", post);
        printWriter.write(post);//post的参数 xx=xx&yy=yy
        // flush输出流的缓冲
        printWriter.flush();
        InputStream is = null;
        try {
            is = httpURLConnection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = HttpUtils.readMyInputStream(is);
        Log.e("GetAttention.post", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            return false;
        }

        String state = jsonobj.optString("res");
        if (state.equals("0")) {
            return false;
        } else {
            JSONObject ObjectInfo = jsonobj.optJSONObject("mdata");
            String meetingIds = ObjectInfo.optString("rUserIds");
            String ids[]=meetingIds.split(",");
            application.getUserInfoDB().cleanUserAttention();
            for (int i = 0; i < ids.length; i++) {
                application.getAttention().add(ids[i]);
                application.getUserInfoDB().addUserAttention(ids[i]);
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            Toast.makeText(application.getApplicationContext(), "更新关注成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(application.getApplicationContext(), "更新关注失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {

    }
}

