package com.hui2020.huihui.Card;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.Order.OrderActivity;
import com.hui2020.huihui.Personal.Order.TicketActivity;
import com.hui2020.huihui.Personal.Order.TicketDetialsActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;

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
 * Created by Administrator on 2017/2/18.
 */

public class NewFriendsActivity extends AppCompatActivity {
    private MyApplication app;
    private RelativeLayout back;
    private LinearLayout orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfriends);
        app=(MyApplication)getApplication();
        initComponent();
        new GetOrder().execute();
    }

    private void initComponent() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        orders = (LinearLayout)findViewById(R.id.order_lin);
    }




    public class GetOrder extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONArray itemarray;
        private JSONObject item;
        private String newpassword;

        public GetOrder(){

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=getGiveCardLst&");
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
                Log.e("getGiveCardLst,post", app.getPassword());
                newpassword = new Mdfive().Mdfive(app.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //post的参数 xx=xx&yy=yy
            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId();
            Log.e("GetOrder,post", post);
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
            Log.e("getGiveCardLst.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("getGiveCardLst", "Service error");
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
                Log.e("getGiveCardLst.size", itemarray.length() + "");
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                for (int i = 0; i < itemarray.length(); i++) {
                    try {
                        item = itemarray.getJSONObject(i);
                        Log.e("GiveCardLst.singleitem", item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                   // final String otherId = item.optString("otherId");
                    final String ufromImg = item.optString("ufromImg");
                    final String realName = item.optString("realName");
                    final String job = item.optString("job");
                    final    String gender = item.optString("sex");

                    final String company=item.optString("company");
                    final String mobile = item.optString("mobile");
                    final String mail = item.optString("mail");
                    final String website = item.optString("website");
                    final String qq = item.optString("qq");
                    final String weixin = item.optString("weixin");
                    final String post = item.optString("post");



                    final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view = inflater.inflate(R.layout.newfriends_item, null);

                    SimpleDraweeView personalinfo_head=(SimpleDraweeView)view.findViewById(R.id.personalinfo_head) ;
                    Uri headuri = Uri.parse(ufromImg);
                    personalinfo_head.setImageURI(headuri);
                    TextView personalinfo_name=(TextView)view.findViewById(R.id.personalinfo_name);
                    personalinfo_name.setText(realName);
                    ImageView personalinfo_gender=(ImageView)view.findViewById(R.id.personalinfo_gender) ;
                    if (gender.equals("0")) {
                        personalinfo_gender.setImageResource(R.drawable.personalinfo_gender_man);
                    }
                    if (gender.equals("1")) {
                        personalinfo_gender.setImageResource(R.drawable.personalinfo_gender_woman);
                    }
                    if (gender.equals("2")) {
                        personalinfo_gender.setVisibility(View.GONE);
                    }

                    TextView personalinfo_job=(TextView)view.findViewById(R.id.personalinfo_job);
                    personalinfo_job.setText(job);
                    TextView personalinfo_company=(TextView)view.findViewById(R.id.personalinfo_company);
                    personalinfo_company.setText("公司："+company);
                    TextView personalinfo_phone_text=(TextView)view.findViewById(R.id.personalinfo_phone_text);
                    personalinfo_phone_text.setText("手机："+mobile);
                    TextView personalinfo_email=(TextView)view.findViewById(R.id.personalinfo_email);
                    personalinfo_email.setText("邮箱："+mail);
                    TextView personalinfo_website=(TextView)view.findViewById(R.id.personalinfo_website);
                    personalinfo_website.setText("网址："+website);
                    TextView personalinfo_qq=(TextView)view.findViewById(R.id.personalinfo_qq);
                    personalinfo_qq.setText("qq:"+qq);
                    TextView personalinfo_wechat=(TextView)view.findViewById(R.id.personalinfo_wechat);
                    personalinfo_wechat.setText("微信："+weixin);
                    TextView personalinfo_address_text=(TextView)view.findViewById(R.id.personalinfo_address_text) ;
                    personalinfo_address_text.setText("地址："+post);
                    Button btn_pass=(Button)view.findViewById(R.id.btn_pass);
                    Button btn_nopass=(Button)view.findViewById(R.id.btn_nopass) ;




                    orders.addView(view);
                }
            } else {
                Toast.makeText(getApplicationContext(),"拉取失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showcallphonedialog(final String phone){
        //对话框
        final Dialog dialog = new Dialog(NewFriendsActivity.this, R.style.MyDialogstyle);
        View contentView = LayoutInflater.from(NewFriendsActivity.this).inflate(R.layout.dialog_call_phone, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        //	dialog.setCancelable(false);
        Button mydialog_tv_cancel = (Button) contentView.findViewById(R.id.mydialog_tv_cancel);
        Button mydialog_tv_confirm = (Button) contentView.findViewById(R.id.mydialog_tv_confirm);

        TextView tv_phonenumber = (TextView) contentView.findViewById(R.id.tv_phonenumber);
        tv_phonenumber.setText(phone+"?");


        mydialog_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                dialog.dismiss();
            }
        });
        mydialog_tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
                dialog.dismiss();
            }
        });
        tv_phonenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                dialog.dismiss();
            }
        });


        dialog.show();
    }






    public class addCard extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONArray itemarray;
        private JSONObject item;
        private String newpassword;

        public addCard(){

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=addCard&");
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
                Log.e("addCard,post", app.getPassword());
                newpassword = new Mdfive().Mdfive(app.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //post的参数 xx=xx&yy=yyuserName用户名,pwd密码,userId本用户id,otherId对方用户id

            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId()+ "&otherId=" + app.getUserId();
            Log.e("addCard,post", post);
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
            Log.e("addCard.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("addCard", "Service error");
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

                Toast.makeText(getApplicationContext(),"添加成功",Toast.LENGTH_SHORT).show();



            } else {
                Toast.makeText(getApplicationContext(),"拉取失败",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
