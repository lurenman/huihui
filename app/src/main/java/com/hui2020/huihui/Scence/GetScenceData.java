package com.hui2020.huihui.Scence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.R;

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
 * Created by FD-GHOST on 2016/12/24.
 * This class is used to prepare the data for Main Scence Activity
 */

public class GetScenceData extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private LinearLayout linearLayout;
    private String from, number, username, password, newpassword;
    private Flag flag;
    private URL url = null;
    private JSONArray itemarray;
    private JSONObject item;
    private Bundle bundle;
    private int size;
    private Intent intent;
    private Activity activity;

    public GetScenceData(Activity activity, Context context, LinearLayout linearLayout, String from, String number, String userName, String password, Flag flag) {
        this.activity=activity;
        this.context = context;
        this.linearLayout = linearLayout;
        this.from = from;
        this.number = number;
        this.username = userName;
        this.password = password;
        this.flag = flag;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getInActLst&");
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

            newpassword = new Mdfive().Mdfive(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //post的参数 xx=xx&yy=yy
        String post = "userName=" + username + "&pwd=" + newpassword + "&userId=1001639543"+"&pageIdx=" + from + "&pageSize=" + number;
        Log.e("GetScenceData,post", post);
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
        Log.e("GetScenceData.result",result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetScenceData","Service error");
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
            }
            Log.e("GetHomeData.size",itemarray.length()+"");
            size = itemarray.length();
            return true;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            if (size > 0) {
                for (int i = 0; i < itemarray.length(); i++) {
                    try {
                        item = itemarray.getJSONObject(i);
                        Log.e("GetSceData.singleitem",item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LayoutInflater inflater;
                    inflater = LayoutInflater.from(context);
                    View view = inflater.inflate(R.layout.scence_item, null);
                    final String title = item.optString("title");
                    final String datetime = item.optString("dateBegin");
                    final String year = datetime.substring(0,5);
                    final String date = datetime.substring(5,10);
                    final String time = datetime.substring(11,16);
                    final String pos = item.optString("address");
                    final String id = item.optString("id");
                    final String image = item.optString("picture1");
                    ImageView ivimage = (ImageView) view.findViewById(R.id.scence_item_image);
                    Uri uri = Uri.parse(image);
                    ivimage.setImageURI(uri);

                    TextView yvtitle = (TextView) view.findViewById(R.id.scence_item_title);
                    yvtitle.setText(title);
                    TextView tvyear = (TextView) view.findViewById(R.id.scence_item_year);
                    tvyear.setText(date);
                    TextView tvdate = (TextView) view.findViewById(R.id.scence_item_date);
                    tvdate.setText(date);
                    TextView tvtime = (TextView) view.findViewById(R.id.scence_item_time);
                    tvtime.setText(time);
                    TextView tvaddress = (TextView) view.findViewById(R.id.scence_item_address);
                    tvaddress.setText(pos);

                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.scence_item_relativelayout);
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                            intent = new Intent(context,ScencedetailActivity.class);
                            intent.putExtra("actId", id);
                            activity.startActivity(intent);
                        }
                    });
                    linearLayout.addView(view);

                    flag.setFlag(true);

                }
            }
            flag.setFlag(true);
            Toast.makeText(context, "加载成功 ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "加载失败 ", Toast.LENGTH_SHORT).show();
        }
    }

    public int getSize() {
        Log.e("GetHomeData.getSize",size+"");
        return this.size;
    }
}
