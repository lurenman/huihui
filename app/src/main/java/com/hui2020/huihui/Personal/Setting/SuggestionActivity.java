package com.hui2020.huihui.Personal.Setting;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.Order.TicketDetialsActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;

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

public class SuggestionActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout back;
    private MyApplication app;
    EditText et_content;
    EditText ed_qq_number;
    Button btn_commit;
String content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initComponent();
    }

    private void initComponent() {
        app = (MyApplication) getApplication();
        back = (RelativeLayout) findViewById(R.id.back);
        et_content = (EditText) findViewById(R.id.et_content);
        ed_qq_number = (EditText) findViewById(R.id.ed_qq_number);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_commit.setOnClickListener(this);
        back.setOnClickListener(this);
        content=et_content.getText().toString();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_commit:
    if(StringUtil.editIsNull(et_content)){
        ToastUtils.showToast(SuggestionActivity.this,"请输入你要提交的内容");
        return;
    }else if(StringUtil.editIsNull(ed_qq_number)){
        ToastUtils.showToast(SuggestionActivity.this,"请输入你的qq号码");
        return;
    }else {
        new addFeed().execute();
    }
    break;
        }
    }

    public class addFeed extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONObject item;
        private String message;
        private String newpassword;

        public addFeed() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=addFeed&");
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
                Log.e("addFeed,post", app.getPassword());
                newpassword = new Mdfive().Mdfive(app.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //post的参数 xx=xx&yy=yy


            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId()
                    + "&detail=" + content;
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
            Log.e("addFeed.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("addFeed", "Service error");
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
                ToastUtils.showToast(SuggestionActivity.this, "提交成功");
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
