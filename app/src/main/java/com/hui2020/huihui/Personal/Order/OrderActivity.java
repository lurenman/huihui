package com.hui2020.huihui.Personal.Order;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Payment.GetChoiceTicket;
import com.hui2020.huihui.Personal.PersonalinfoActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;
import com.nostra13.universalimageloader.utils.L;

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
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    private MyApplication app;
    private RelativeLayout back;
    private LinearLayout orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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
                url = new URL("http://m.hui2020.com/server/m.php?act=getOrderLst&");
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
                Log.e("GetOrder,post", app.getPassword());
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
            Log.e("GetOrder.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("GetOrder", "Service error");
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
                Log.e("GetOrder.size", itemarray.length() + "");
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                for (int i = 0; i < itemarray.length(); i++) {
                    try {
                        item = itemarray.getJSONObject(i);
                        Log.e("GetOrder.singleitem", item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String ticketId = item.optString("ticketId");
                    final String orderCode=item.optString("orderCode");
                    final String picture1 = item.optString("picture1");
                    final String dateBegin = item.optString("dateBegin");
                    final String dateEnd = item.optString("dateEnd");
                    final String price = item.optString("money");
                    final String dateCreate = item.optString("dateCreate");
                    final String datePay = item.optString("datePay");
                    final String consumeType = item.optString("consumeType");
                    final String userPayType = item.optString("userPayType");
                    final String payStatus = item.optString("payStatus");

                    final String invoiceFlag = item.optString("invoiceFlag");
                    final String invoiceCode = item.optString("invoiceCode");
                    final String invoiceCompany = item.optString("invoiceCompany");
                    final String invoiceMoney = item.optString("invoiceMoney");
                    final String invoiceAddress = item.optString("invoiceAddress");
                    final String invoicePhone = item.optString("invoicePhone");
                    final String invoiceContact = item.optString("invoiceContact");
                    final String invoiceStatu = item.optString("invoiceStatu");
                    final String invoiceInfo = item.optString("invoiceInfo");


                    final String title = item.optString("title");
                    final String address = item.optString("address");
                    final String hostPhone = item.optString("hostPhone");
                    final String tickettype = item.optString("name");
                    final String ticketStatu = item.optString("ticketStatu");
                    final String ticketName = item.optString("ticketName");
                    final String ticketPhone = item.optString("ticketPhone");
                    final String ticketCompany = item.optString("ticketCompany");
                    final String ticketJob = item.optString("ticketJob");


                    final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                    View view = inflater.inflate(R.layout.order_item, null);

                    SimpleDraweeView image = (SimpleDraweeView) view.findViewById(R.id.order_item_image);
                    Uri uri = Uri.parse(picture1);
                    image.setImageURI(uri);
                    TextView tvtitle = (TextView) view.findViewById(R.id.order_item_title);
                    tvtitle.setText(title);
                    TextView tvdate = (TextView) view.findViewById(R.id.order_item_date);
                    tvdate.setText(dateBegin);
                    TextView tvaddress = (TextView) view.findViewById(R.id.order_item_address);
                    tvaddress.setText(address);
                    TextView tvtype = (TextView) view.findViewById(R.id.order_item_type);
                    tvtype.setText(tickettype);
                    TextView tvprice = (TextView) view.findViewById(R.id.order_item_price);
                    tvprice.setText("￥"+price);
                    TextView tvstatus = (TextView) view.findViewById(R.id.order_item_ticketstatus);
                    TextView tveticket = (TextView) view.findViewById(R.id.order_item_eticket);
                    TextView tvinvoice = (TextView) view.findViewById(R.id.order_item_invoice);
                    TextView tvsupport = (TextView) view.findViewById(R.id.order_item_support);
                    switch (Integer.parseInt(payStatus)){
                        case 0:
                            tvstatus.setText("正在出票");
                            tveticket.setVisibility(View.GONE);
                            tvinvoice.setVisibility(View.GONE);
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                        case 1:
                            tvstatus.setText("有效 未使用");
                            tveticket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(),TicketActivity.class);
                                    intent.putExtra("ticketId",ticketId);
                                    intent.putExtra("title",title);
                                    intent.putExtra("dateBegin",dateBegin);
                                    intent.putExtra("address",address);
                                    intent.putExtra("hostPhone",hostPhone);
                                    intent.putExtra("consumeType","类型："+consumeType);
                                    intent.putExtra("price","价格："+price);
                                    intent.putExtra("ticketName","姓名："+ticketName);
                                    intent.putExtra("ticketPhone","联系："+ticketPhone);
                                    intent.putExtra("ticketCompany","公司："+ticketCompany);
                                    intent.putExtra("ticketJob","职位："+ticketJob);
                                    startActivity(intent);
                                }
                            });
                            tvinvoice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                  //  Toast.makeText(getApplicationContext(),"发票",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),TicketDetialsActivity.class);
                                    intent.putExtra("price",price);
                                    intent.putExtra("ticketId",ticketId);
                                    intent.putExtra("orderCode",orderCode);
                                    intent.putExtra("title",title);
                                    intent.putExtra("dateBegin",dateBegin);
                                    intent.putExtra("address",address);
                                    intent.putExtra("hostPhone",hostPhone);
                                    intent.putExtra("consumeType","类型："+consumeType);
                                    intent.putExtra("ticketName","姓名："+ticketName);
                                    intent.putExtra("ticketPhone","联系："+ticketPhone);
                                    intent.putExtra("ticketCompany","公司："+ticketCompany);
                                    intent.putExtra("ticketJob","职位："+ticketJob);
                                    startActivity(intent);
                                }
                            });
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   // Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                        case 2:
                            tvstatus.setText("有效 已使用");
                            tveticket.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(),TicketActivity.class);
                                    intent.putExtra("ticketId",ticketId);
                                    intent.putExtra("title",title);
                                    intent.putExtra("dateBegin",dateBegin);
                                    intent.putExtra("address",address);
                                    intent.putExtra("hostPhone",hostPhone);
                                    intent.putExtra("consumeType","类型："+consumeType);
                                    intent.putExtra("ticketName","姓名："+ticketName);
                                    intent.putExtra("ticketPhone","联系："+ticketPhone);
                                    intent.putExtra("ticketCompany","公司："+ticketCompany);
                                    intent.putExtra("ticketJob","职位："+ticketJob);
                                    startActivity(intent);
                                }
                            });
                            tvinvoice.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                  //  Toast.makeText(getApplicationContext(),"发票",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(),TicketDetialsActivity.class);
                                    intent.putExtra("price",price);
                                    intent.putExtra("ticketId",ticketId);
                                    intent.putExtra("title",title);
                                    intent.putExtra("dateBegin",dateBegin);
                                    intent.putExtra("address",address);
                                    intent.putExtra("hostPhone",hostPhone);
                                    intent.putExtra("consumeType","类型："+consumeType);
                                    intent.putExtra("ticketName","姓名："+ticketName);
                                    intent.putExtra("ticketPhone","联系："+ticketPhone);
                                    intent.putExtra("ticketCompany","公司："+ticketCompany);
                                    intent.putExtra("ticketJob","职位："+ticketJob);
                                    startActivity(intent);
                                }
                            });
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                 //   Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                        case 3:
                            tvstatus.setText("无效 未支付");
                            tveticket.setVisibility(View.GONE);
                            tvinvoice.setVisibility(View.GONE);
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                  //  Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                        case 4:
                            tvstatus.setText("无效 已支付 出票失败");
                            tveticket.setVisibility(View.GONE);
                            tvinvoice.setVisibility(View.GONE);
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                        case 5:
                            tvstatus.setText("已退款");
                            tveticket.setVisibility(View.GONE);
                            tvinvoice.setVisibility(View.GONE);
                            tvsupport.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                   // Toast.makeText(getApplicationContext(),"联系客服",Toast.LENGTH_SHORT).show();
                                    if(StringUtil.isEmpty(hostPhone)){
                                        ToastUtils.showToast(OrderActivity.this,"电话号码不能为空");
                                    }else {
                                        showcallphonedialog(hostPhone);
                                    }
                                }
                            });
                            break;
                    }
                    orders.addView(view);
                }
            } else {
                        Toast.makeText(getApplicationContext(),"拉取失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showcallphonedialog(final String phone){
        //对话框
        final Dialog dialog = new Dialog(OrderActivity.this, R.style.MyDialogstyle);
        View contentView = LayoutInflater.from(OrderActivity.this).inflate(R.layout.dialog_call_phone, null);
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

}
