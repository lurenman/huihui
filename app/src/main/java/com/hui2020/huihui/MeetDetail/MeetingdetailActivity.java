package com.hui2020.huihui.MeetDetail;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.Interaction;
import com.hui2020.huihui.Login.LoginActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Payment.ChoiceticketActivity;
import com.hui2020.huihui.R;

public class MeetingdetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private WebView wv;
    private RelativeLayout rphone, rcollect, back;
    private ImageView ivcollect;
    private TextView tvjoin;
    private String title, actId, xiuUrl, image, phone;
    private MyApplication app;

    //@SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetingdetail);
        app = (MyApplication)getApplication();
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        title = getIntent().getStringExtra("title");
        actId = getIntent().getStringExtra("actId");
        phone = getIntent().getStringExtra("phone");
        xiuUrl = getIntent().getStringExtra("xiuUrl");
        image = getIntent().getStringExtra("image");
//        rphone = (RelativeLayout) findViewById(R.id.meetingdetail_feet_phone);
//        rphone.setOnClickListener(this);
        ivcollect=(ImageView)findViewById(R.id.meetingdetail_feet_collect_image);
        if(app.getCollection().contains(actId)){
            ivcollect.setImageResource(R.drawable.collect_b);
        }else {
            ivcollect.setImageResource(R.drawable.collect_a);
        }
        rcollect = (RelativeLayout) findViewById(R.id.meetingdetail_feet_collect);
        rcollect.setOnClickListener(this);
        tvjoin = (TextView) findViewById(R.id.meetingdetail_feet_join);
        tvjoin.setOnClickListener(this);
        wv = (WebView) findViewById(R.id.webtest);
        //xiuUrl = "http://xiu.hui2020.com/v/U80105388E38";

        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(xiuUrl);
        wv.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        wv.destroy();
        finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.meetingdetail_feet_collect:
                if(app.getCollection().contains(actId)){
                    new Interaction(app,3,app.getUserName(),app.getPassword(),app.getUserId(),
                            app.getRealName(),actId,null,null,ivcollect).execute();
                }else {
                    if(app.getSign()!=true){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        new Interaction(app, 2, app.getUserName(), app.getPassword(), app.getUserId(),
                                app.getRealName(), actId, null, null, ivcollect).execute();
                    }
                }
                break;
            case R.id.meetingdetail_feet_join:
                if(app.getSign()!=true){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    wv.onPause();
                    intent = new Intent(getApplicationContext(), ChoiceticketActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("xiuUrl", xiuUrl);
                    intent.putExtra("actId", actId);
                    intent.putExtra("phone", phone);
                    intent.putExtra("image", image);
                    MeetingdetailActivity.this.startActivity(intent);
                }
                break;
        }
    }


    @Override
    public void onPause(){
        super.onPause();
        //wv.destroy();
    }

    @Override
    public void onResume(){
        super.onResume();
        wv = (WebView) findViewById(R.id.webtest);

        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl(xiuUrl);
        wv.getSettings().setJavaScriptEnabled(true);
        if(app.getCollection().contains(actId)){
            ivcollect.setImageResource(R.drawable.collect_b);
        }else {
            ivcollect.setImageResource(R.drawable.collect_a);
        }
    }
}
