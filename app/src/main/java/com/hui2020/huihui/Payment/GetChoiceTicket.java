package com.hui2020.huihui.Payment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.R;

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

/**
 * Created by FD-GHOST on 2017/1/3.
 */

public class GetChoiceTicket extends AsyncTask<Void, Void, Boolean> {
    private URL url = null;
    private JSONArray itemarray;
    private JSONObject item;
    private Bundle bundle;
    private Intent intent;
    private Activity activity;
    private Context context;
    private String username, password, newpassword, actId, title;
    private LinearLayout linearLayout;
    private ArrayList<RadioButton> radioButtons;
    private ArrayList<String> choice;

    public GetChoiceTicket(Activity activity, Context context, LinearLayout linearLayout, ArrayList<String> choice, String userName, String password, String actId, String title) {
        this.activity = activity;
        this.context = context;
        this.linearLayout = linearLayout;
        this.username = userName;
        this.password = password;
        this.actId = actId;
        this.title = title;
        this.choice = choice;
        radioButtons = new ArrayList<RadioButton>();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getTicketLst&");
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
            Log.e("GetChoiceTicket,post", password);
            newpassword = new Mdfive().Mdfive(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //post的参数 xx=xx&yy=yy
        String post = "userName=" + username + "&pwd=" + newpassword + "&actId=" + actId;
        Log.e("GetChoiceTicket,post", post);
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
        Log.e("GetChoiceTicket.result", result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetChoiceTicket", "Service error");
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
            Log.e("GetChoiceTicket.size", itemarray.length() + "");
            return true;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {

            for (int i = 0; i < itemarray.length(); i++) {
                try {
                    item = itemarray.getJSONObject(i);
                    Log.e("GetSceData.singleitem", item.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LayoutInflater inflater;
                inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.choiceticket_item, null);

                ViewHolder viewHolder = null;
                viewHolder = new ViewHolder();

                final String id = item.optString("id");
                final String datestart = item.optString("dateBegin");
                final String dateend = item.optString("dateEnd");
                final String pos = item.optString("address");
                final String type = item.optString("name");
                final String total = item.optString("count");
                final String remain = item.optString("countNow");
                final String price = item.optString("price");

                viewHolder.title = (TextView) view.findViewById(R.id.payment_item_title);
                viewHolder.title.setText(title);
                viewHolder.date = (TextView) view.findViewById(R.id.payment_item_date);
                viewHolder.date.setText(datestart + " 至 " + dateend);
                viewHolder.address = (TextView) view.findViewById(R.id.payment_item_address);
                viewHolder.address.setText(pos);
                viewHolder.type = (TextView) view.findViewById(R.id.payment_item_type);
                viewHolder.type.setText(type);
                viewHolder.price = (TextView)view.findViewById(R.id.payment_item_price) ;
                viewHolder.price.setText("￥"+price);
                viewHolder.radioButton = (RadioButton) view.findViewById(R.id.payment_item_radiobutton);
                radioButtons.add(viewHolder.radioButton);
                if (Integer.parseInt(remain) <= 0) {
                    viewHolder.isnull = (ImageView) view.findViewById(R.id.payment_item_null);
                    viewHolder.isnull.setVisibility(View.VISIBLE);
                    viewHolder.radioButton.setEnabled(false);
                }
                viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 0; i < radioButtons.size(); i++) {
                            ((RadioButton) radioButtons.get(i)).setChecked(false);
                        }
                        ((RadioButton) view).setChecked(true);
                        choice.clear();
                        choice.add(id+"");
                        choice.add(datestart + " 至 " + dateend);
                        choice.add(pos+"");
                        choice.add(type+"");
                        choice.add(price+"");
                        choice.add(remain+"");
                    }
                });
                linearLayout.setTag(viewHolder);
                linearLayout.addView(view);
            }

            Toast.makeText(context, "加载成功 ", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "加载失败 ", Toast.LENGTH_SHORT).show();
        }
    }

    public class ViewHolder {
        TextView title, date, address, type, price;
        ImageView isnull;
        RadioButton radioButton;
    }
}
