package com.hui2020.huihui.Login;

import android.app.Activity;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
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

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegisteActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private SendVerifyTask verifyTask;
    private RegisteTask registeTask;
    private TextView login, confirme;
    private Button verify;
    private EditText numberIn, verifyIn, passwordIn, pawordconfirmIn;
    private String number, verifycode, password, passwordconfirm;
    private CountdownTask countdownTask;
    private Response response;
    private String result;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        app = (MyApplication) getApplication();
        activity=this;
        initComponent();
    }

    private void initComponent() {
        login = (TextView) findViewById(R.id.registe_login);
        login.setOnClickListener(this);
        login.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        login.getPaint().setAntiAlias(true);//抗锯齿
        verify = (Button) findViewById(R.id.registe_verify_send);
        verify.setOnClickListener(this);
        confirme = (TextView) findViewById(R.id.registe_confirme);
        confirme.setOnClickListener(this);
        numberIn = (EditText) findViewById(R.id.registe_number_text);
        numberIn.setOnClickListener(this);
        verifyIn = (EditText) findViewById(R.id.registe_verify_text);
        verifyIn.setOnClickListener(this);
        passwordIn = (EditText) findViewById(R.id.registe_password_text);
        passwordIn.setOnClickListener(this);
        pawordconfirmIn = (EditText) findViewById(R.id.registe_passwordconfirm_text);
        pawordconfirmIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.registe_login:
                finish();
                break;
            case R.id.registe_verify_send:
                number = numberIn.getText().toString();

                if (number.length() != 11) {
                    Toast.makeText(RegisteActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
                } else {
                    verifyTask = new SendVerifyTask();
                    verifyTask.execute();
                    countdownTask = new CountdownTask(60000,1000);
                    countdownTask.start();
                }
                break;
            case R.id.registe_confirme:
                ///////Send the registe require
                number = numberIn.getText().toString();
                password = passwordIn.getText().toString();
                passwordconfirm = pawordconfirmIn.getText().toString();
                verifycode = verifyIn.getText().toString();
                if (number.length() != 11) {
                    Toast.makeText(RegisteActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisteActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordconfirm)) {
                    Toast.makeText(RegisteActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                } else if (verifycode.length() != 4) {
                    Toast.makeText(RegisteActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                } else {
                    registeTask = new RegisteTask();
                    registeTask.execute();
                }
                break;
        }
    }

    private class SendVerifyTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                SendVerifyPost();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisteActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String state = jsonobj.optString("res");
            if (state.equals("0")) {
                Toast.makeText(RegisteActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisteActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private class RegisteTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                RegistePost();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(RegisteActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String state = jsonobj.optString("res");
            if (state.equals("0")) {
                Toast.makeText(RegisteActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisteActivity.this, "注册成功，正在登录", Toast.LENGTH_SHORT).show();
                UserLoginTask login = new UserLoginTask(app, activity, number, password);
                login.execute();
            }
        }
    }

    private class CountdownTask extends CountDownTimer {

        public CountdownTask(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onTick(long millisUntilFinished) {
            verify.setClickable(false);
            verify.setText(millisUntilFinished /1000+"秒后重发");
        }

        @Override
        public void onFinish() {
            verify.setText("重新验证");
            verify.setClickable(true);
        }
    }


    public void SendVerifyPost() throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("userName",number)
                .build();

        Request request = new Request.Builder()
                .url("http://m.hui2020.com/server/m.php?act=getSms")
                .post(body)
                .build();
        response=null;
        response  = client.newCall(request).execute();

        if (response.isSuccessful()) {
            result = response.body().string();
            Log.e("SendVF",result);
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    public void RegistePost() throws IOException{
        String newpassword=null;
        try {
            newpassword = new Mdfive().Mdfive(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("userName",number)
                .add("pwd",newpassword)
                .add("phone",number)
                .add("verify",verifycode)
                .add("ipaddr","0.0.0.0")
                .add("ip_location","杭州")
                .add("client","client")
                .build();

        Request request = new Request.Builder()
                .url("http://m.hui2020.com/server/m.php?act=userReg")
                .post(body)
                .build();
        response=null;
        response  = client.newCall(request).execute();

        if (response.isSuccessful()) {
            result = response.body().string();
            Log.e("RegistPOST",result);
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
}
