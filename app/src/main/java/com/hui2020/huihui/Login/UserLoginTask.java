package com.hui2020.huihui.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hui2020.huihui.Home.HomeActivity;
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
 * Created by FD-GHOST on 2017/2/8.
 */

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
    private MyApplication app;
    private Activity activity;
    private URL url = null;
    private String userId, nickName, realName, avatar, sex, birthday, location, address, phone, qq, wechat, email,
            job, company, companyinfo, website, companyimagea, companyimageb, companyimagec, ishide, ispush;
    private String inNumber, inPassword, newpassword;

    public UserLoginTask(MyApplication myApplication, Activity activity, String number, String password) {
        this.app = myApplication;
        this.activity = activity;
        this.inNumber = number;
        this.inPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=userLogin&");
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

            newpassword = new Mdfive().Mdfive(inPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String post = "userName=" + inNumber + "&pwd=" + newpassword;
        Log.e("INFO", post);
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
        Log.e("Login", result);
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
            userId = ObjectInfo.optString("userId") + "";
            System.out.println("UserID: " + userId);
            nickName = ObjectInfo.optString("nickName") + "";
            System.out.println("nickName: " + nickName);
            realName = ObjectInfo.optString("realName") + "";
            System.out.println("realName: " + realName);
            avatar = ObjectInfo.optString("avatar") + "";
            System.out.println("avatar: " + avatar);
            sex = ObjectInfo.optString("sex") + "";
            System.out.println("sex: " + sex);//0:男1:女
            birthday = ObjectInfo.optString("birthday") + "";
            System.out.println("birthday: " + birthday);
            location = ObjectInfo.optString("location") + "";
            System.out.println("location: " + location);
            address = ObjectInfo.optString("address") + "";
            System.out.println("address: " + address);
            phone = ObjectInfo.optString("mobilePhone") + "";
            System.out.println("phone: " + phone);
            qq = ObjectInfo.optString("qq") + "";
            System.out.println("qq: " + qq);
            wechat = ObjectInfo.optString("weixin") + "";
            System.out.println("wechat: " + wechat);
            email = ObjectInfo.optString("email") + "";
            System.out.println("email: " + email);
            job = ObjectInfo.optString("job") + "";
            System.out.println("job: " + job);
            company = ObjectInfo.optString("company") + "";
            System.out.println("company: " + company);
            companyinfo = ObjectInfo.optString("compinfo") + "";
            System.out.println("compinfo: " + companyinfo);
            website = ObjectInfo.optString("website") + "";
            System.out.println("website: " + website);
            companyimagea = ObjectInfo.optString("infoPic1") + "";
            System.out.println("companyimagea: " + companyimagea);
            companyimageb = ObjectInfo.optString("infoPic2") + "";
            System.out.println("companyimageb: " + companyimagea);
            companyimagec = ObjectInfo.optString("infoPic3") + "";
            System.out.println("companyimagec: " + companyimagea);
            ispush = ObjectInfo.optString("ispush") + "";
            System.out.println("ispush: " + ispush);
            ishide = ObjectInfo.optString("ishide") + "";
            System.out.println("ishide: " + ishide);
        }

        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            app.setSign(true);
            app.iniApp(userId, inNumber, inPassword, nickName, realName, avatar, sex, birthday,
                    location, address, phone, qq, wechat, email, job, company, companyinfo, website,
                    companyimagea, companyimageb, companyimagec, ishide, ispush);
            new GetCollect(app).execute();
            new GetAttention(app).execute();
            new GetFriend(app).execute();
            /**
             * This is use to detect which activity called this method
             * If called by home, then do not need to create another homeactivity
             * But if called by login, then redirect to home
             */
            int start = 0;
            String a = activity.getLocalClassName();
            String b = "";
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) == '.') {
                    start = i;
                }
            }
            for (int i = start + 1; i < a.length(); i++) {
                b += a.charAt(i);
            }
            if (b.equals("HomeActivity")) {

            } else {
                Intent intent = new Intent(app.getApplicationContext(), HomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }


        } else {
            /**
             * This is use to detect which activity called this method
             * If called by home, when password wrong, user should be redirect to relogin
             * But if called by login, then just show password is wrong
             */
            app.setSign(false);
            int start = 0;
            String a = activity.getLocalClassName();
            String b = "";
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) == '.') {
                    start = i;
                }
            }
            for (int i = start + 1; i < a.length(); i++) {
                b += a.charAt(i);
            }
            if (b.equals("HomeActivity")) {
                Toast.makeText(app.getApplicationContext(), "您的登录失效", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(app.getApplicationContext(), HomeActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                Toast.makeText(app.getApplicationContext(), "手机号或密码输入错误", Toast.LENGTH_SHORT).show();
            }


        }
    }

    @Override
    protected void onCancelled() {

    }
}
