package com.hui2020.huihui.Personal;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;

import java.net.URI;
import java.net.URL;



/**
 * Created by FD-GHOST on 2016/12/24.
 * This class is used to initialize each component in PersonalInfo Activity
 */

public class PersonalinfoActivity extends AppCompatActivity implements View.OnClickListener{

    private MyApplication app;
    private SimpleDraweeView sdvhead,sdvcompanyinfoimagea,sdvcompanyinfoimageb,sdvcompanyinfoimagec;
    private TextView tvname,tvjob,tvcompany,tvphone,tvemail,tvwebsite,tvqq,tvwechat,tvaddress,change;
    private ImageView ivgender;
    private RelativeLayout back;
    private ImageView personalinfo_share;
    private  boolean isopen=false;
    private LinearLayout lr_isopen;
    private ImageButton ib_isopen;
    private TextView tv_is_open;
    RelativeLayout personalinfo_phone,personalinfo_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personalinfo);

        initComponent();
    }

    private void initComponent(){
        app = (MyApplication)getApplication();
        personalinfo_address=(RelativeLayout)findViewById(R.id.personalinfo_address);
        personalinfo_address.setOnClickListener(this);
        personalinfo_phone=(RelativeLayout)findViewById(R.id.personalinfo_phone);
        personalinfo_phone.setOnClickListener(this);
        lr_isopen=(LinearLayout)findViewById(R.id.lr_isopen);
        lr_isopen.setOnClickListener(this);
        ib_isopen=(ImageButton)findViewById(R.id.ib_isopen);
        tv_is_open=(TextView)findViewById(R.id.tv_is_open);
        personalinfo_share=(ImageView)findViewById(R.id.personalinfo_share) ;
        personalinfo_share.setOnClickListener(this);
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        sdvhead = (SimpleDraweeView)findViewById(R.id.personalinfo_head);
        Uri headuri = Uri.parse(app.getAvatar());
        sdvhead.setImageURI(headuri);
        sdvcompanyinfoimagea = (SimpleDraweeView)findViewById(R.id.personalinfo_companyinfo_image_a);
        Uri a = Uri.parse(app.getCompanyinfo_a());
        sdvcompanyinfoimagea.setImageURI(a);
        sdvcompanyinfoimageb = (SimpleDraweeView)findViewById(R.id.personalinfo_companyinfo_image_b);
        Uri b = Uri.parse(app.getCompanyinfo_b());
        sdvcompanyinfoimageb.setImageURI(b);
        sdvcompanyinfoimageb.setVisibility(View.GONE);
        sdvcompanyinfoimagec = (SimpleDraweeView)findViewById(R.id.personalinfo_companyinfo_image_c);
        Uri c = Uri.parse(app.getCompanyinfo_c());
        sdvcompanyinfoimagec.setImageURI(c);
        sdvcompanyinfoimagec.setVisibility(View.GONE);


        tvname = (TextView) findViewById(R.id.personalinfo_name);
        tvname.setText(app.getRealName());
        tvjob = (TextView) findViewById(R.id.personalinfo_job);
        tvjob.setText(app.getJob());
        tvcompany = (TextView) findViewById(R.id.personalinfo_company);
        tvcompany.setText(app.getCompany());
        tvphone = (TextView) findViewById(R.id.personalinfo_phone_text);
        tvphone.setText("电话："+app.getPhone());
        tvemail = (TextView) findViewById(R.id.personalinfo_email);
        tvemail.setText("邮箱："+app.getEmail());
        tvwebsite = (TextView) findViewById(R.id.personalinfo_website);
        tvwebsite.setText("网站："+app.getWebsite());
        tvqq = (TextView) findViewById(R.id.personalinfo_qq);
        tvqq.setText("QQ："+app.getQq());
        tvwechat = (TextView) findViewById(R.id.personalinfo_wechat);
        tvwechat.setText("微信："+app.getWechat());
        tvaddress = (TextView) findViewById(R.id.personalinfo_address_text);
        tvaddress.setText("地址："+app.getAddress());

        ivgender= (ImageView)findViewById(R.id.personalinfo_gender);
        if (app.getGender().equals("0")){
            ivgender.setImageResource(R.drawable.personalinfo_gender_man);
        }
        if (app.getGender().equals("1")){
            ivgender.setImageResource(R.drawable.personalinfo_gender_woman);
        }
        if (app.getGender().equals("2")){
            ivgender.setVisibility(View.GONE);
        }
        change = (TextView)findViewById(R.id.personalinfo_change);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonaleditActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.personalinfo_phone:
                if(StringUtil.isEmpty(app.getPhone())){
                    ToastUtils.showToast(PersonalinfoActivity.this,"你的电话号码为空");
                    return;
                }else {
                    showcallphonedialog(app.getPhone());
                }
                break;

            case R.id.personalinfo_share:
                showsharedialog();
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
            case R.id.personalinfo_address:
                Intent intent=new Intent(PersonalinfoActivity.this,BaidumapActivity.class);
                startActivity(intent);
                break;

        }
    }
    public void showsharedialog(){
        //对话框
        final Dialog dialog = new Dialog(PersonalinfoActivity.this, R.style.MyDialogstyle);
        View contentView = LayoutInflater.from(PersonalinfoActivity.this).inflate(R.layout.dialog_sharetomyfriends, null);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        //	dialog.setCancelable(false);
        ImageButton iv_wx = (ImageButton) contentView.findViewById(R.id.iv_wx);
        ImageButton iv_wx_friends = (ImageButton) contentView.findViewById(R.id.iv_wx_friends);
        ImageButton iv_qq = (ImageButton) contentView.findViewById(R.id.iv_qq);
        TextView tv_cancle = (TextView) contentView.findViewById(R.id.tv_cancle);



        iv_wx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                dialog.dismiss();
            }
        });
        iv_wx_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                dialog.dismiss();
            }
        });
        iv_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //	shenApplication.finishAllActivities();
                dialog.dismiss();
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showcallphonedialog(final String phone){
        //对话框
        final Dialog dialog = new Dialog(PersonalinfoActivity.this, R.style.MyDialogstyle);
        View contentView = LayoutInflater.from(PersonalinfoActivity.this).inflate(R.layout.dialog_call_phone, null);
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
}
