package com.hui2020.huihui.Login;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;

import org.json.JSONArray;
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
 * Created by FD-GHOST on 2017/1/14.
 */

public class GetFriend extends AsyncTask<Void, Void, Boolean> {
    private URL url = null;
    private String newpassword;
    private MyApplication application;
    private JSONArray itemarray;
    private JSONObject item;

    public GetFriend(MyApplication application) {
        this.application = application;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getCardLst&");
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
        String post = "userId=" + application.getUserId() + "&userName=" + application.getUserName() + "&pwd=" + newpassword+"&pageIdx=0&pageSize=10000";
        Log.e("GetFriend.post", post);
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
        Log.e("GetFriend.post", result);
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
            try {
                itemarray = jsonobj.getJSONArray("mdata");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("GetFriend.size", "服务器暂无数据");
                return false;
            }
            return true;

        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            application.getUserInfoDB().cleanUserFriend();
            if (itemarray.length() > 0) {
                for (int i = 0; i < itemarray.length(); i++) {
                    try {
                        item = itemarray.getJSONObject(i);
                        Log.e("GetFriend.singleitem", item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                        String userId = item.optString("id"+"");
                        application.getFriend().add(userId+"");
                        String avatar = item.optString("avatar"+"");
                        application.getFriend().add(avatar+"");
                        String realName = item.optString("realName"+"");
                        application.getFriend().add(realName+"");
                        String job = item.optString("job"+"");
                        application.getFriend().add(job+"");
                    String company = item.optString("company"+"");
                    application.getFriend().add(company+"");
                        String phone = item.optString("phone"+"");
                        application.getFriend().add(phone+"");
                        application.getUserInfoDB().addUserFriend(userId, avatar, realName, job, company, phone);

                }
                Toast.makeText(application.getApplicationContext(), "更新好友成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(application.getApplicationContext(), "更新好友失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}