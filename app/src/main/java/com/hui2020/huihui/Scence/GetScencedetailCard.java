package com.hui2020.huihui.Scence;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.Card.CarddetailActivity;
import com.hui2020.huihui.Interaction;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
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
 * Created by FD-GHOST on 2016/12/30.
 * This class is used to prepare the data for the in action member's card in each detailed scene
 */

public class GetScencedetailCard extends AsyncTask<Void, Void, Boolean> {
    private MyApplication app;
    private Activity activity;
    private Context context;
    private LinearLayout linearLayout;
    private String actId, newpassword;
    private URL url = null;
    private JSONArray itemarray;
    private JSONObject item;
    private int size;
    private Intent intent;

    public GetScencedetailCard(MyApplication app, Activity activity, Context context, View view, String actId) {
        this.app = app;
        this.activity = activity;
        this.context = context;
        this.linearLayout = (LinearLayout) view.findViewById(R.id.scencedetail_card_lin);
        this.actId = actId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getNowCardLst&");
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

            newpassword = new Mdfive().Mdfive(app.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //post的参数 xx=xx&yy=yy
        String post = "userId=" + app.getUserId() + "&userName=" + app.getUserName() + "&pwd=" + newpassword + "&actId=" + actId;
        Log.e("GetScdetailCard,post", post);
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
        Log.e("GetScdetailCard.result", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetScdetailCard", "Service error");
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
            Log.e("GetScdetailCard.size", itemarray.length() + "");
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
                        Log.e("GetScdetailC.singleitem", item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LayoutInflater inflater;
                    inflater = LayoutInflater.from(context);

                    View view = inflater.inflate(R.layout.scencedetailcard_item, null);

                    final String image = item.optString("pic");
                    final String id = item.optString("id");
                    final String name = item.optString("realName");
                    final String job = item.optString("job");
                    final String phone = item.optString("phone");
                    final String company = item.optString("company");
                    ImageView ivimage = (ImageView) view.findViewById(R.id.scencedetailcard_image);
                    Uri uri = Uri.parse(image);
                    ivimage.setImageURI(uri);
                    TextView tvname = (TextView) view.findViewById(R.id.scencedetailcard_name);
                    tvname.setText(name);
                    TextView tvjob = (TextView) view.findViewById(R.id.scencedetailcard_job);
                    tvjob.setText(job);
                    TextView tvphone = (TextView) view.findViewById(R.id.scencedetailcard_company);
                    tvphone.setText("电话：" + phone);
                    TextView tvcompany = (TextView) view.findViewById(R.id.scencedetailcard_phone);
                    tvcompany.setText("公司：" + company);
                    final TextView tvisfriend = (TextView) view.findViewById(R.id.scencedetailcard_isfriend);
                    if (app.getFriend().contains(id)) {
                        tvisfriend.setVisibility(View.GONE);
                    } else {
                        tvisfriend.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                new Interaction(app, 4, app.getUserName(), app.getPassword(), app.getUserId(),
                                        app.getRealName(), null, id, tvisfriend, null).execute();
                            }
                        });
                    }
                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.scencedetailcard_item);
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                            intent = new Intent(context, CarddetailActivity.class);
                            intent.putExtra("userId", id);
                            activity.startActivity(intent);
                        }
                    });
                    linearLayout.addView(view);
                }
            }
            Toast.makeText(context, "加载成功 ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "加载失败 ", Toast.LENGTH_SHORT).show();
        }
    }

}
