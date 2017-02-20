package com.hui2020.huihui.Personal.warehouse;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.FeetButtonInitialize;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;
import com.hui2020.huihui.Scence.ScencedetailActivity;
import com.hui2020.huihui.VideoActivity;

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

public class WarehouseActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication app;
    private RelativeLayout back;
    private TextView delcom;
    private LinearLayout linearLayout;
    private boolean flag;
    private float y1 = 0,y2 = 0;
    private int from, size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        app = (MyApplication) getApplication();
        initComponent();
        new GetWarehouse().execute();
    }

    private void initComponent() {
        from =0;
        size =10;
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        delcom = (TextView)findViewById(R.id.warehouse_delcom);
        delcom.setOnClickListener(this);
        linearLayout = (LinearLayout)findViewById(R.id.warehouse_lin);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


    private class GetWarehouse extends AsyncTask<Void, Void, Boolean> {
        private URL url;
        private String newpassword;
        private JSONArray itemarray;
        private JSONObject item;

        public GetWarehouse() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=getMyThink&");
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
            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId()+"&pageIdx="+from+"&pageSize="+size;
            Log.e("GetWarehouse,post", post);
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
            Log.e("GetWarehouse.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("GetWarehouse", "Service error");
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
                Log.e("GetWarehouse.size", itemarray.length() + "");
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
                            Log.e("GetWarehouse.singleitem", item.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LayoutInflater inflater;
                        inflater = LayoutInflater.from(getApplicationContext());
                        View view = inflater.inflate(R.layout.home_item_video, null);
                        final String title = item.optString("title");
                        final String viewtime = item.optString("viewTimes");
                        final String sharetime = item.optString("shareTimes");
                        final String image = item.optString("img");
                        final String videourl = item.optString("videoUrl");
                        TextView tvtitle = (TextView) view.findViewById(R.id.home_item_title);
                        tvtitle.setText("  " + title);
                        TextView tvviewtime = (TextView) view.findViewById(R.id.home_item_numplay_text);
                        tvviewtime.setText("播放次数" + viewtime);
                        TextView tvsharetime = (TextView) view.findViewById(R.id.home_item_numshare_text);
                        tvsharetime.setText("  " + sharetime + "次");
                        SimpleDraweeView sdvimage = (SimpleDraweeView) view.findViewById(R.id.home_item_videopic);
                        Uri uri = Uri.parse(image);
                        sdvimage.setImageURI(uri);
                        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.home_content_item);
                        relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                                intent.putExtra("videourl", videourl);
                                startActivity(intent);
                            }
                        });
                        linearLayout.addView(view);
                    }
                }

                Toast.makeText(getApplicationContext(), "加载成功 ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "加载失败 ", Toast.LENGTH_SHORT).show();
            }

        }
    }



}
