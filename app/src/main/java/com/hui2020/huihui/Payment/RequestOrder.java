package com.hui2020.huihui.Payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.hui2020.huihui.Login.HttpUtils;
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
import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2017/1/12.
 */

public class RequestOrder extends AsyncTask<Void, Void, Boolean> {
    private URL url = null;
    private MyApplication app;
    private Activity activity;
    private ArrayList<Contact> contacts;
    private String newpassword, ticketId;
    private String order;
    private String title,sum;

    public RequestOrder(MyApplication myApplication, Activity activity, String ticketId,
                        ArrayList<Contact> arrayList, String title,String sum) {
        this.app=myApplication;
        this.activity = activity;
        this.ticketId = ticketId;
        this.contacts=arrayList;
        this.title=title;
        this.sum=sum;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=addOrderN&");
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
        try {
            Log.e("GetChoiceTicket,post", app.getPassword());
            newpassword = new Mdfive().Mdfive(app.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //post的参数 xx=xx&yy=yy
        order = "[";
        for (int i =0; i<contacts.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("tid",ticketId);
                jsonObject.put("type","4");
                jsonObject.put("ticketName",contacts.get(i).getName());
                jsonObject.put("ticketPhone",contacts.get(i).getPhone());
                jsonObject.put("ticketJob",contacts.get(i).getJob());
                jsonObject.put("ticketCompany",contacts.get(i).getCompany());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if ((i+1)==contacts.size()) {
                order += jsonObject.toString();
            }else {
                order += jsonObject.toString()+",";
            }
        }
        order+="]";


        String post = "userName=" + app.getUserName() + "&pwd=" + newpassword +"&userId="+ app.getUserId()+"&orders=" + order;
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
            order = jsonobj.optString("mdata");
            try {
                jsonobj = new JSONObject(order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            order = jsonobj.optString("orderCode");
            return true;
        }
    }
    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            Log.e("RequestOrder","success"+order);
            Intent intent = new Intent(activity.getApplicationContext(), PaymentActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("sum",sum);
            intent.putExtra("orderNum",order);
            activity.startActivity(intent);
        } else {
            Log.e("RequestOrder","false"+order);
        }
    }
}
