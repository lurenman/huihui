package com.hui2020.huihui.Login;

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

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView login, verify, confirme;
    private EditText numberIn, verifyIn, passwordIna, passwordInb;
    private String number,verifycode, passworda, passwordb;
    private SendVerifyTask verifyTask;
    private CountdownTask countdownTask;
    private ForgetTask forgetTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initComponent();
    }

    private void initComponent(){
        login = (TextView) findViewById(R.id.forget_login);
        login.setOnClickListener(this);
        login.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG ); //下划线
        login.getPaint().setAntiAlias(true);//抗锯齿
        verify = (Button) findViewById(R.id.forget_verify_send);
        verify.setOnClickListener(this);
        confirme = (TextView) findViewById(R.id.forget_confirme);
        confirme.setOnClickListener(this);
        numberIn = (EditText) findViewById(R.id.forget_number_text);
        numberIn.setOnClickListener(this);
        verifyIn = (EditText) findViewById(R.id.forget_verify_text);
        verifyIn.setOnClickListener(this);
        passwordIna = (EditText) findViewById(R.id.forget_password_text);
        passwordIna.setOnClickListener(this);
        passwordInb = (EditText) findViewById(R.id.forget_passwordconfirm_text);
        passwordInb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.forget_login:
                finish();
                break;
            case R.id.forget_verify_send:
              /////////request forget verify code
                number = numberIn.getText().toString();

                if (number.length() != 11) {
                    Toast.makeText(ForgetActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
                } else {
                    verifyTask = new SendVerifyTask();
                    verifyTask.execute();
                    countdownTask = new CountdownTask(60000,1000);
                    countdownTask.start();
                }
                break;
            case R.id.forget_confirme:
              ///////Send the update require
                number = numberIn.getText().toString();
                passworda = passwordIna.getText().toString();
                passwordb = passwordInb.getText().toString();
                verifycode = verifyIn.getText().toString();
                if (number.length() != 11) {
                    Toast.makeText(ForgetActivity.this, "手机号输入错误", Toast.LENGTH_SHORT).show();
                } else if (passworda.length() < 6 || passwordb.length() < 6) {
                    Toast.makeText(ForgetActivity.this, "密码输入错误", Toast.LENGTH_SHORT).show();
                } else if(!passworda.equals(passwordb)) {
                    Toast.makeText(ForgetActivity.this, "两次密码不匹配", Toast.LENGTH_SHORT).show();
                } else if (verifycode.length() != 4) {
                    Toast.makeText(ForgetActivity.this, "验证码输入错误", Toast.LENGTH_SHORT).show();
                } else {
                    forgetTask = new ForgetTask();
                    forgetTask.execute();
                }
                break;
        }
    }

    private class SendVerifyTask extends AsyncTask<Void, Void, Boolean> {
        URL url = null;
        String newpassword;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://192.168.1.48/m.php?act=getSms&");
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
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
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
            }
            // 发送请求参数
            String post = "userName=" + number;
            Log.e("INFO", post);
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            InputStream is = null;
            try {
                is = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = HttpUtils.readMyInputStream(is);
            System.out.println(result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
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
                Toast.makeText(ForgetActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ForgetActivity.this, "获取验证码失败", Toast.LENGTH_SHORT).show();
                verifyTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            verifyTask = null;
        }
    }

    private class ForgetTask extends AsyncTask<Void, Void, Boolean> {
        URL url = null;
        String newpassword;

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://192.168.1.48/m.php?act=resetPwd&");
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
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
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
            }
            // 发送请求参数
            try {

                newpassword = new Mdfive().Mdfive(passworda);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String post = "userName=" + number + "&pwd=" + newpassword + "&verify=" + verifycode;
            Log.e("INFO", post);
            printWriter.write(post);//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
            printWriter.flush();
            InputStream is = null;
            try {
                is = httpURLConnection.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String result = HttpUtils.readMyInputStream(is);
            System.out.println(result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.w("Result", result);
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
                Toast.makeText(ForgetActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ForgetActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                verifyTask = null;
            }
        }

        @Override
        protected void onCancelled() {
            verifyTask = null;
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
}

