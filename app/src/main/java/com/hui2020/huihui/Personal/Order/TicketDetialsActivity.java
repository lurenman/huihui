package com.hui2020.huihui.Personal.Order;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.MoreContentActivity;
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
 * Created by Administrator on 2017/2/15.
 */

public class TicketDetialsActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;
    final static int code = 1;   // 请求码
    LinearLayout lr_more_content;
    TextView tv_confirm;
    RelativeLayout back;
    private MyApplication app;
    EditText ed_compay_title;
    EditText ed_compay_number;
    EditText ed_ticket_type;
    TextView tv_money;
    EditText ed_morecontent;
    EditText ed_getpeople;
    EditText ed_phone_;
    EditText ed_address;
    String iCompany;
    String iCode;
    String iMoney;
    String iAddr;
    String iPhone;
    String iInfo;
    String iContact;
    String getiAddr;
    String orderCode;
    String price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticketdetials);
        initview();
    }

    public void initview() {
        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);
        app = (MyApplication) getApplication();
        lr_more_content = (LinearLayout) findViewById(R.id.lr_more_content);
        lr_more_content.setOnClickListener(this);
        ed_morecontent = (EditText) findViewById(R.id.ed_morecontent);
        ed_morecontent.setOnClickListener(this);
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(this);

        ed_compay_title = (EditText) findViewById(R.id.ed_compay_title);
        ed_compay_number = (EditText) findViewById(R.id.ed_compay_number);
        ed_ticket_type = (EditText) findViewById(R.id.ed_ticket_type);
        tv_money = (TextView) findViewById(R.id.tv_money);
        ed_morecontent = (EditText) findViewById(R.id.ed_morecontent);
        ed_getpeople = (EditText) findViewById(R.id.ed_getpeople);
        ed_phone_ = (EditText) findViewById(R.id.ed_phone_);
        ed_address = (EditText) findViewById(R.id.ed_address);

        iCompany = ed_compay_title.getText().toString();
        iCode = ed_compay_number.getText().toString();
        iMoney = tv_money.getText().toString();
        iInfo = ed_morecontent.getText().toString();
        iPhone = ed_phone_.getText().toString();
        iContact = ed_getpeople.getText().toString();
        getiAddr = ed_address.getText().toString();

        intent = getIntent();
        orderCode = intent.getStringExtra("orderCode");
        price=intent.getStringExtra("price");
        tv_money.setText(price);
    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.lr_more_content:
                intent = new Intent(TicketDetialsActivity.this, MoreContentActivity.class);
                startActivityForResult(intent, code); //跳转到下一个activity（Second）
                break;
            case R.id.ed_morecontent:
                intent = new Intent(TicketDetialsActivity.this, MoreContentActivity.class);
                startActivityForResult(intent, code); //跳转到下一个activity（Second）
                break;
            case R.id.back:
                finish();
                break;
            case R.id.tv_confirm:
                if(StringUtil.editIsNull(ed_compay_title)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入公司抬头");
                    return;
                }else if(StringUtil.editIsNull(ed_compay_number)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入税号");
                    return;
                }else if(StringUtil.editIsNull(ed_ticket_type)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入发票类型");
                    return;
                } else if(StringUtil.editIsNull(ed_morecontent)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入更多内容");
                    return;
                } else if(StringUtil.editIsNull(ed_getpeople)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入收件人");
                    return;
                }else if(StringUtil.editIsNull(ed_phone_)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入联系方式");
                    return;
                }else if(StringUtil.editIsNull(ed_getpeople)){
                    ToastUtils.showToast(TicketDetialsActivity.this,"请输入详细地址");
                    return;
                }else {
                    new addInvoice().execute();
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == code) {
            if (resultCode == RESULT_OK) {
                String s = data.getStringExtra("content");
                ed_morecontent.setText(s);        //设置text的值为second返回的字符串
            } else {
                System.out.println("没有返回值");
            }
        }

    }


    public class addInvoice extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONObject item;
        private String message;
        private String newpassword;

        public addInvoice() {

        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=addInvoice&");
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
                Log.e("addInvoice,post", app.getPassword());
                newpassword = new Mdfive().Mdfive(app.getPassword());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            //post的参数 xx=xx&yy=yy

            String post = "userName=" + app.getUserName() + "&pwd=" + newpassword + "&userId=" + app.getUserId()
                    + "&iCompany=" + iCompany + "&iCode=" + iCode + "&iMoney=" + iMoney + "&iAddr=" + iAddr + "&iPhone="
                    + iPhone + "&iContact=" + iContact + "&orderCode=" + orderCode + "&iInfo=" + iInfo;
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
            Log.e("addInvoice.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("addInvoice", "Service error");
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
                ToastUtils.showToast(TicketDetialsActivity.this,"提交成功");
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "拉取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
