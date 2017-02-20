package com.hui2020.huihui.Card;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Home.HomeActivity;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.PersonalinfoActivity;
import com.hui2020.huihui.Personal.Setting.SettingActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;
import com.hui2020.huihui.myiosdialog.ActionSheetDialog;

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

public class CarddetailActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private JSONObject jsonobj;
    private SimpleDraweeView sdvhead, sdvcompanyinfoimagea, sdvcompanyinfoimageb, sdvcompanyinfoimagec;
    private TextView tvname, tvjob, tvcompany, tvphone, tvemail, tvwebsite, tvqq, tvwechat, tvaddress, change;
    private ImageView ivgender;
    private URL url;
    private String newpassword, cardId;
    private Intent intent;
    private RelativeLayout back;
    private String id,name;
    private boolean isopen=false;
    ImageButton ib_isopen;
    LinearLayout lr_isopen;
    TextView tv_is_open;
    RelativeLayout personalinfo_phone;
    String phone="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Fresco.initialize(this);
        setContentView(R.layout.activity_personalinfo); //Note, this activity will share layout file,remember to change the button action
        app = (MyApplication) getApplication();
        cardId = getIntent().getStringExtra("userId");
        initComponent();
        new GetCarddetail().execute();
    }


    private void initComponent() {
        personalinfo_phone=(RelativeLayout)findViewById(R.id.personalinfo_phone) ;
        personalinfo_phone.setOnClickListener(this);
        tv_is_open=(TextView)findViewById(R.id.tv_is_open);
        lr_isopen=(LinearLayout)findViewById(R.id.lr_isopen);
        lr_isopen.setOnClickListener(this);
        ib_isopen=(ImageButton)findViewById(R.id.ib_isopen);

        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);

        sdvhead = (SimpleDraweeView) findViewById(R.id.personalinfo_head);
        sdvcompanyinfoimagea = (SimpleDraweeView) findViewById(R.id.personalinfo_companyinfo_image_a);
        sdvcompanyinfoimageb = (SimpleDraweeView) findViewById(R.id.personalinfo_companyinfo_image_b);
        sdvcompanyinfoimageb.setVisibility(View.GONE);
        sdvcompanyinfoimagec = (SimpleDraweeView) findViewById(R.id.personalinfo_companyinfo_image_c);
        sdvcompanyinfoimagec.setVisibility(View.GONE);

        tvname = (TextView) findViewById(R.id.personalinfo_name);
        tvjob = (TextView) findViewById(R.id.personalinfo_job);
        tvcompany = (TextView) findViewById(R.id.personalinfo_company);
        tvphone = (TextView) findViewById(R.id.personalinfo_phone_text);
        tvemail = (TextView) findViewById(R.id.personalinfo_email);
        tvwebsite = (TextView) findViewById(R.id.personalinfo_website);
        tvqq = (TextView) findViewById(R.id.personalinfo_qq);
        tvwechat = (TextView) findViewById(R.id.personalinfo_wechat);
        tvaddress = (TextView) findViewById(R.id.personalinfo_address_text);

        ivgender = (ImageView) findViewById(R.id.personalinfo_gender);

        change = (TextView) findViewById(R.id.personalinfo_change);
        change.setText("递名片");
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.personalinfo_change:
               /* intent = new Intent(CarddetailActivity.this, CardActActivity.class);
                intent.putExtra("userId", id);
                intent.putExtra("realName", name);
                CarddetailActivity.this.startActivity(intent);*/
                new ActionSheetDialog(CarddetailActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("是否向"+name+"递交名片？",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {

                                    }
                                })
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("确定",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                          new giveCard().execute();
                                    }
                                }) .show();
                break;
            case R.id.  lr_isopen:
                if(isopen==false){
                    isopen=true;
                    tv_is_open.setText("展开");
                    ib_isopen.setBackgroundResource(R.drawable.down_arrow);
                    sdvcompanyinfoimageb.setVisibility(View.GONE);
                    sdvcompanyinfoimagec.setVisibility(View.GONE);
                }else {
                    isopen=false;
                    tv_is_open.setText("收起");
                    ib_isopen.setBackgroundResource(R.drawable.up_arrow);

                    sdvcompanyinfoimageb.setVisibility(View.VISIBLE);
                    sdvcompanyinfoimagec.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.personalinfo_phone:
                if(StringUtil.isEmpty(phone)){
                    ToastUtils.showToast(CarddetailActivity.this,"电话号码不能为空");
                }else {
                    showcallphonedialog(phone);
                }
                break;
        }
    }

    private class GetCarddetail extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=getCard&");
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
            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&cardId=" + cardId;
            Log.e("GetCard,post", post);
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
            Log.e("GetCard.result", result);
            jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("GetCard", "Service error");
                return false;
            }

            String state = jsonobj.optString("res");
            if (state.equals("0")) {
                return false;
            } else {
                try {
                    jsonobj = jsonobj.getJSONObject("mdata");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                id = jsonobj.optString("id");

                String pic = jsonobj.optString("pic");
                Uri headuri = Uri.parse(pic);
                sdvhead.setImageURI(headuri);

                name = jsonobj.optString("realName");
                tvname.setText(name);
                String gender = jsonobj.optString("sex");
                if (gender.equals("0")) {
                    ivgender.setImageResource(R.drawable.personalinfo_gender_man);
                }
                if (gender.equals("1")) {
                    ivgender.setImageResource(R.drawable.personalinfo_gender_woman);
                }
                if (gender.equals("2")) {
                    ivgender.setVisibility(View.GONE);
                }

                String job = jsonobj.optString("job");
                tvjob.setText(job);
                String company = jsonobj.optString("company");
                tvcompany.setText(company);
                phone = jsonobj.optString("mobile");
                tvphone.setText("电话：" + phone);
                String email = jsonobj.optString("mail");
                tvemail.setText("邮箱：" + email);
                String website = jsonobj.optString("website");
                tvwebsite.setText("网站：" + website);
                String qq = jsonobj.optString("qq");
                tvqq.setText("QQ：" + qq);
                String wechat = jsonobj.optString("weixin");
                tvwechat.setText("微信：" + wechat);
                String address = jsonobj.optString("post");
                tvaddress.setText("地址：" + address);

                String imagea = jsonobj.optString("infoPic1");
                Uri imageauri = Uri.parse(imagea);
                sdvcompanyinfoimagea.setImageURI(imageauri);
                sdvcompanyinfoimagea.setEnabled(false);
                String imageb = jsonobj.optString("infoPic2");
                Uri imageburi = Uri.parse(imageb);
                sdvcompanyinfoimageb.setImageURI(imageburi);
                sdvcompanyinfoimageb.setEnabled(false);
                String imagec = jsonobj.optString("infoPic3");
                Uri imagecuri = Uri.parse(imagec);
                sdvcompanyinfoimagec.setImageURI(imagecuri);
                sdvcompanyinfoimagec.setEnabled(false);

                Toast.makeText(CarddetailActivity.this, "加载成功 ", Toast.LENGTH_SHORT).show();

            } else

            {
                Toast.makeText(CarddetailActivity.this, "加载失败 ", Toast.LENGTH_SHORT).show();
            }


        }

    }
    public void showcallphonedialog(final String phone){
        //对话框
        final Dialog dialog = new Dialog(CarddetailActivity.this, R.style.MyDialogstyle);
        View contentView = LayoutInflater.from(CarddetailActivity.this).inflate(R.layout.dialog_call_phone, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        //	dialog.setCancelable(false);
        Button mydialog_tv_cancel = (Button) contentView.findViewById(R.id.mydialog_tv_cancel);
        Button mydialog_tv_confirm = (Button) contentView.findViewById(R.id.mydialog_tv_confirm);

        TextView tv_phonenumber = (TextView) contentView.findViewById(R.id.tv_phonenumber);
        tv_phonenumber.setText(phone);


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
    private class giveCard extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=giveCard&");
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


            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId()+ "&otherId=" + id + "&realName=" + app.getRealName();
            Log.e("giveCard,post", post);
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
            Log.e("giveCard.result", result);
            jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("giveCard", "Service error");
                return false;
            }

            String state = jsonobj.optString("res");
            if (state.equals("0")) {
                return false;
            } else {
                try {
                    jsonobj = jsonobj.getJSONObject("mdata");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {


                Toast.makeText(CarddetailActivity.this, "提交成功 ", Toast.LENGTH_SHORT).show();

            } else

            {
                Toast.makeText(CarddetailActivity.this, "提交失败 ", Toast.LENGTH_SHORT).show();
            }


        }

    }
}

