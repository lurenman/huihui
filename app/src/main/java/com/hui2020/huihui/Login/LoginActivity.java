package com.hui2020.huihui.Login;

import android.app.Application;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hui2020.huihui.Home.HomeActivity;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;
import com.tencent.tauth.Tencent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private UserLoginTask login;
    private TextView registe, confirme, forget;
    private EditText numberIn, passwordIn;
    private String number, password, newpassword;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (MyApplication) getApplication();
        initComponent();
    }

    private void initComponent(){
        registe = (TextView) findViewById(R.id.login_registe);
        registe.setOnClickListener(this);
        //registe.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        //registe.getPaint().setAntiAlias(true);//抗锯齿
        confirme = (TextView) findViewById(R.id.login_confirme);
        confirme.setOnClickListener(this);
        forget = (TextView) findViewById(R.id.login_forget);
        forget.setOnClickListener(this);
        numberIn = (EditText) findViewById(R.id.login_number_text);
        numberIn.setOnClickListener(this);
        passwordIn = (EditText) findViewById(R.id.login_password_text);
        passwordIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_registe:
                intent = new Intent(LoginActivity.this, RegisteActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
            case R.id.login_confirme:
                number = numberIn.getText().toString();
                password = passwordIn.getText().toString();
                if(number.length() != 11){
                    Toast.makeText(LoginActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
                }else if(password.length() < 6){
                    Toast.makeText(LoginActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                }else {
                    login = new UserLoginTask(app,this,number,password);
                    login.execute();
                }
              /*  number = "18611112222";
                password ="123456";*/
            /*    login = new UserLoginTask(app,this,number,password);
                login.execute();*/
                break;
            case R.id.login_forget:
                intent = new Intent(LoginActivity.this, ForgetActivity.class);
                LoginActivity.this.startActivity(intent);
                break;
        }
    }



}
