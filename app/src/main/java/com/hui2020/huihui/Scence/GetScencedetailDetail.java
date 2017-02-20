package com.hui2020.huihui.Scence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Interaction;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

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
 * Created by FD-GHOST on 2016/12/29.
 * This class is used to prepare the action information data for each detailed scene
 */

public class GetScencedetailDetail extends AsyncTask<Void, Void, Boolean> {
    String intro;
    private Context context;
    private View view;
    private String username, password, newpassword, actId;
    private URL url = null;
    private JSONObject item;
    private MyApplication app;
    private ImageView attention;
    private ImageView collection;
    private Activity activity;

    public GetScencedetailDetail(MyApplication myApplication, Activity activity, Context context, View view, String username, String password, String actId) {
        this.app = myApplication;
        this.activity=activity;
        this.context = context;
        this.view = view;
        this.username = username;
        this.password = password;
        this.actId = actId;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getAct&");
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
        String post = "userName=" + username + "&pwd=" + newpassword + "&actId=" + actId;
        Log.e("GetScenceDetail,post", post);
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
        // Log.e("GetScenceDetail.result", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetScenceDetail", "Service error");
            return false;
        }
        Log.e("GetScenceDetail.re", result);
        String state = jsonobj.optString("res");
        if (state.equals("0")) {
            return false;
        } else {
            try {
                item = jsonobj.getJSONObject("mdata");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("GetScenceDetail.result", item.toString());
            return true;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {

            final String hostId = item.optString("userID");
            String title = item.optString("title");
            String datetime = item.optString("dateBegin");
            String date = datetime.substring(0, 10);
            String address = item.optString("address");
            String position = item.optString("address_pos");
            String applied = item.optString("countSold");
            String amount = item.optString("countSum");
            String hostname = item.optString("hostContact");
            String hostcompany = item.optString("hostName");
            final String hostphone = item.optString("hostPhone");
            intro = item.optString("note1") + item.optString("note2") + item.optString("note3") + item.optString("note4");
            for (int i = 0; i < 100; i++) {
                String matter1 = "<a";//目标字符串1
                String matter2 = "</a>";//目标字符串2
                String matter3 = "";
                int a = intro.indexOf(matter1);//第一个字符串的起始位置
                int b = intro.indexOf(matter2);//第二个字符串的起始位置
                if (a != -1 && b != -1) {
                    String st = intro.substring(a, b);
                    for (int n = 0; n < st.length(); n++) {
                        if ((int) (st.charAt(n)) > 1000) {
                            matter3 += st.charAt(n);
                        }
                    }
                    //Log.e("LOG", matter3);
                    intro = intro.substring(0, a) + matter3 + intro.substring(b + matter2.length());//利用substring进行字符串截取
                }
            }


            final String id = item.optString("id");

            final String image = item.optString("img1");
            final String hostimage = item.optString("hostImage");
            SimpleDraweeView ivimage = (SimpleDraweeView) view.findViewById(R.id.scencedetail_image);
            final Uri uri = Uri.parse(image);
            ivimage.setImageURI(uri);
            SimpleDraweeView ivhostimage = (SimpleDraweeView) view.findViewById(R.id.scencedetail_host_image);
            final Uri hosturi = Uri.parse(hostimage);
            ivhostimage.setImageURI(hosturi);

            if (position.length() > 15) {
                TextView location = (TextView) view.findViewById(R.id.scencedetail_distance);
                int comma = position.indexOf(",");
                String targetLatitude = position.substring(0, comma);
                String targetLongitude = position.substring(comma + 1, position.length() - 1);
                TextView tvlatitude = (TextView) view.findViewById(R.id.scencedetail_targetLatitude);
                tvlatitude.setText(targetLatitude);
                TextView tvLongitude = (TextView) view.findViewById(R.id.scencedetail_targetLongitude);
                tvLongitude.setText(targetLongitude);
            }
            TextView tvtitle = (TextView) view.findViewById(R.id.scencedetail_title);
            tvtitle.setText(title);
            TextView tvdate = (TextView) view.findViewById(R.id.scencedetail_date);
            tvdate.setText(datetime);
            TextView tvaddress = (TextView) view.findViewById(R.id.scencedetail_address);
            tvaddress.setText(address);
            TextView tvapplied = (TextView) view.findViewById(R.id.scencedetail_amount);
            tvapplied.setText("已有" + applied + "人报名/限" + amount + "人报名");
            TextView tvhostname = (TextView) view.findViewById(R.id.scencedetail_host_name);
            tvhostname.setText(hostname);
            TextView tvhostcompany = (TextView) view.findViewById(R.id.scencedetail_host_company);
            tvhostcompany.setText("公司：" + hostcompany);
            TextView tvhostphone = (TextView) view.findViewById(R.id.scencedetail_host_phone);
            tvhostphone.setText("电话：" + hostphone);
            attention = (ImageView) view.findViewById(R.id.scencedetail_host_attention);
            if (app.getAttention().contains(hostId)) {
                attention.setImageResource(R.drawable.scencedetail_host_attentionb);
            } else {
                attention.setImageResource(R.drawable.scencedetail_host_attentiona);
            }
            attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (app.getAttention().contains(hostId)) {
                        Log.e("GetScenAtt", "has");
                        new Interaction(app, 5, app.getUserName(), app.getPassword(), app.getUserId(),
                                app.getRealName(), null, hostId, null, attention).execute();
                    } else {
                        Log.e("GetScenAtt", "no");
                        new Interaction(app, 4, app.getUserName(), app.getPassword(), app.getUserId(),
                                app.getRealName(), null, hostId, null, attention).execute();
                    }
                }
            });
            LinearLayout llphone = (LinearLayout) view.findViewById(R.id.scencedetail_feet_contect);
            llphone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("确认拨打："+hostphone);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String phonecall = hostphone.replaceAll("-","");
                            Intent intent  = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phonecall));
                            activity.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(activity.getApplicationContext(), "被你取消了！！！", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
            });
            LinearLayout llcollect = (LinearLayout) view.findViewById(R.id.scencedetail_feet_collect);
            collection = (ImageView) view.findViewById(R.id.scencedetail_feet_collect_image);
            llcollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (app.getCollection().contains(actId)) {
                        new Interaction(app, 3, app.getUserName(), app.getPassword(), app.getUserId(),
                                app.getRealName(), actId, null, null, collection).execute();
                    } else {
                        new Interaction(app, 2, app.getUserName(), app.getPassword(), app.getUserId(),
                                app.getRealName(), actId, null, null, collection).execute();
                    }
                }
            });
            if (app.getCollection().contains(actId)) {
                collection.setImageResource(R.drawable.scencedetail_collectb);
            } else {
                collection.setImageResource(R.drawable.scencedetail_collecta);
            }
            TextView tvsign = (TextView) view.findViewById(R.id.scencedetail_feet_sign);
            tvsign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            WebView webview = (WebView) view.findViewById(R.id.scencedetail_webview);
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                    view.loadUrl(url);
                    return true;
                }
            });
            webview.loadDataWithBaseURL(null, intro, "text/html", "utf-8", null);
            WebSettings settings = webview.getSettings();
            settings.setJavaScriptEnabled(true);
        }
        Toast.makeText(context, "加载成功 ", Toast.LENGTH_SHORT).show();
    }
}

