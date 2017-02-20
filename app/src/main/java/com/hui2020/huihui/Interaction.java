package com.hui2020.huihui;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hui2020.huihui.Login.HttpUtils;

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
 * Created by FD-GHOST on 2017/1/9.
 * This class is a index of add/delete friend, collection, attention
 * You can find the switch below
 */

public class Interaction extends AsyncTask<Void, Void, Boolean> {
    private int type;
    private String urlString;
    private URL url;
    private String userName, password, userId, realName, actId, otherId;
    private TextView textView;
    private ImageView imageView;
    private MyApplication app;


    public Interaction(MyApplication myApplication, int type, String userName, String password, String userId, String realName,
                       String actId, String otherId, TextView textView, ImageView imageView) {
        this.app = myApplication;
        this.type = type;
        this.userName = userName;
        this.password = password;
        this.userId = userId;
        this.realName = realName;
        this.actId = actId;
        this.otherId = otherId;
        this.textView = textView;
        this.imageView = imageView;
        /**
         * 0: add friend
         * 1: delete friend
         * 2: add collect
         * 3: delete collect
         * 4: add attention
         * 5: delete attention
         */
        switch (type) {
            case 0:
                urlString = "http://m.hui2020.com/server/m.php?act=addCard&";
                break;
            case 1:
                urlString = "http://m.hui2020.com/server/m.php?act=delCard&";
                break;
            case 2:
                urlString = "http://m.hui2020.com/server/m.php?act=addCollect&";
                break;
            case 3:
                urlString = "http://m.hui2020.com/server/m.php?act=delCollect&";
                break;
            case 4:
                urlString = "http://m.hui2020.com/server/m.php?act=addRemark&";
                break;
            case 5:
                urlString = "http://m.hui2020.com/server/m.php?act=delRemark&";
                break;
        }

    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL(urlString);
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
            httpURLConnection.setRequestMethod("POST");//
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        httpURLConnection.setConnectTimeout(30000);//
        // conn.setReadTimeout(2000);

        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);//


        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        String newpassword = "";
        try {
            newpassword = new Mdfive().Mdfive(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String post = "userName=" + userName + "&pwd=" + newpassword + "&userId=" + userId +
                "&realName=" + realName + "&actId=" + actId + "&otherId=" + otherId;
        Log.e("Interaction,post", post);
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
        Log.e("Interaction.result", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Interaction", "Service error");
            return false;
        }

        String state = jsonobj.optString("res");
        if (state.equals("0")) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            switch (type) {
                case 0:
                    app.getFriend().add(otherId);
                    imageView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    app.getUserInfoDB().removeUserFriend(otherId);
                    app.getFriend().remove(otherId);
                    break;
                case 2:
                    app.getUserInfoDB().addUserCollection(actId);
                    app.getCollection().add(actId);
                    imageView.setImageResource(R.drawable.collect_b);
                    break;
                case 3:
                    app.getUserInfoDB().removeUserCollection(actId);
                    app.getCollection().remove(actId);
                    imageView.setImageResource(R.drawable.collect_a);
                    break;
                case 4:
                    app.getUserInfoDB().addUserAttention(otherId);
                    app.getAttention().add(otherId);
                    imageView.setImageResource(R.drawable.scencedetail_host_attentiona);
                    break;
                case 5:
                    app.getUserInfoDB().removeUserAttention(otherId);
                    app.getAttention().remove(otherId);
                    imageView.setImageResource(R.drawable.scencedetail_host_attentionb);
                    break;
            }
        }

    }
}
