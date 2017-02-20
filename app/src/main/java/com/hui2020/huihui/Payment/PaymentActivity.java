package com.hui2020.huihui.Payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title, sum, confirm;
    private RadioButton alipay, wechat;
    private RelativeLayout back;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.payment_title);
        title.setText(getIntent().getStringExtra("title"));
        sum = (TextView) findViewById(R.id.payment_sum);
        String ssum = "需支付总额:  ￥" + getIntent().getStringExtra("sum");
        sum.setText(ssum);
        confirm = (TextView) findViewById(R.id.payment_confirm);
        confirm.setOnClickListener(this);
        alipay = (RadioButton) findViewById(R.id.payment_alipay_radio);
        alipay.setOnClickListener(this);
        wechat = (RadioButton) findViewById(R.id.payment_wechat_radio);
        wechat.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.payment_alipay_radio:
                wechat.setChecked(false);
                break;

            case R.id.payment_wechat_radio:
                alipay.setChecked(false);
                break;

            case R.id.payment_confirm:
                if (alipay.isChecked()) {

                    new RequestAlipay(getIntent().getStringExtra("orderNum")).execute();
                } else if (wechat.isChecked()) {
                    new RequestWechat(getIntent().getStringExtra("orderNum")).execute();
                } else {
                    Toast.makeText(getApplicationContext(),"请选择付款方式",Toast.LENGTH_SHORT);
                }

                break;
        }
    }


    public class RequestAlipay extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONObject last;

        private String orderNumber;
        private String orderInfo;

        public RequestAlipay(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/alipaywap/signatures_url.php");
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
            //post的参数 xx=xx&yy=yy
            String post = "orderCode=" + orderNumber;
            Log.e("Payment,post", post);
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
            Log.e("Payment.result", result);
            JSONObject jsonobj = null;
            try {
                last = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Payment", "Service error");
                return false;
            }

            orderInfo="";
            orderInfo = last.optString("mdata");


            if (orderInfo == null) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.e("Payment", "success");
                final String orderinfo = orderInfo;   // 订单信息
                Log.e("orderinfo", orderinfo);
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(PaymentActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo,true);
                        Log.e("msg", String.valueOf(result));
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            } else {
                Log.e("RequestOrder", "false" + orderInfo);
            }
        }


    }

    public class RequestWechat extends AsyncTask<Void, Void, Boolean> {
        private URL url = null;
        private JSONObject jsonOject;
        private String orderNumber;
        private String appId,partnerId,prepayId,packageValue,nonceStr,timeStamp,sign;

        public RequestWechat(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/weixinpayandroid/index.php ");
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
            //post的参数 xx=xx&yy=yy
            String post = "orderCode=" + orderNumber;
            Log.e("Payment,post", post);
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
            Log.e("Payment.result", result);
            try {
                jsonOject = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("Payment", "Service error");
                return false;
            }

            String errorCode = jsonOject.optString("errorCode");
            if(errorCode.equals("0")){
                String  responseData= jsonOject.optString("responseData");
                try {
                    jsonOject= new JSONObject(responseData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String appResponse = jsonOject.optString("app_response");
                try {
                    jsonOject = new JSONObject(appResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                appId=jsonOject.optString("appid");
                partnerId=jsonOject.optString("partnerid");
                prepayId=jsonOject.optString("prepayid");
                packageValue=jsonOject.optString("package");
                nonceStr=jsonOject.optString("noncestr");
                timeStamp=jsonOject.optString("timestamp");
                sign=jsonOject.optString("sign");
            }else {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Log.e("Payment", "success");
                final IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), null);
                msgApi.registerApp("wx535721802507c541");
                PayReq req = new PayReq();
                Log.e("PayReq",appId+"  "+partnerId+"  "+prepayId+"  "+nonceStr+"  "+timeStamp+"  "+packageValue+"  "+sign);
                req.appId			= appId;
                req.partnerId		= partnerId;
                req.prepayId		= prepayId;
                req.nonceStr		= nonceStr;
                req.timeStamp		= timeStamp;
                req.packageValue	= packageValue;
                req.sign			= sign;
                req.extData			= "app data"; // optional
                Toast.makeText(getApplicationContext(), "正常调起支付", Toast.LENGTH_SHORT).show();
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                msgApi.sendReq(req);

            } else {
                Log.e("RequestOrder", "false");
            }
        }


    }


}
