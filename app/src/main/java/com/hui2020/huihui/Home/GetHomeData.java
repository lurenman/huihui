package com.hui2020.huihui.Home;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.hui2020.huihui.Home.RefreshableLayout.PullToRefreshLayout;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MeetDetail.MeetingdetailActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.VideoActivity;

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
 * Created by FD-GHOST on 2016/12/24.
 */

public class GetHomeData extends AsyncTask<Void, Void, Boolean> {
    private PullToRefreshLayout pullToRefreshLayout;
    private Context context;
    private LinearLayout linearLayout;
    private String type, city, from, number;
    private Boolean refresh;
    private URL url = null;
    private JSONArray itemarray;
    private JSONObject item;
    private int size;
    private Activity activity;
    private Intent intent;

    public GetHomeData(Activity activity, Context context, PullToRefreshLayout pullToRefreshLayout, LinearLayout linearLayout, String type, String city, String from, String number, Boolean refresh) {
        this.activity=activity;
        this.context = context;
        this.pullToRefreshLayout = pullToRefreshLayout;
        this.linearLayout = linearLayout;
        this.type = type;
        this.city = city;
        this.from = from;
        this.number = number;
        this.refresh = refresh;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            url = new URL("http://m.hui2020.com/server/m.php?act=getActLst&");
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
        String post = "type=" + type + "&city=" + city + "&pageIdx=" + from + "&pageSize=" + number;
        Log.e("GetHomeData,post", post);
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
        Log.e("GetHomeData.result",result);
        JSONObject jsonobj = null;
        try {
            jsonobj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("GetHomeData","Service error");
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
                Log.e("GetHomeData.size","服务器暂无数据");
                return false;
            }
            Log.e("GetHomeData.size",itemarray.length()+"");
            size = itemarray.length();
            return true;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if (success) {
            if (refresh == true) {
                //Can add new logic to relish memory
                linearLayout.removeAllViews();
            }
            if (size > 0) {
                for (int i = 0; i < itemarray.length(); i++) {
                    try {
                        item = itemarray.getJSONObject(i);
                        Log.e("GetHomeData.singleitem",item.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LayoutInflater inflater;
                    inflater = LayoutInflater.from(context);
                    int style = Integer.parseInt(item.optString("listStyle"));
                    View view = null;
                    switch (style) {
                        case 0:
                            view = inflater.inflate(R.layout.home_item_tupone, null);
                            break;
                        case 1:
                            view = inflater.inflate(R.layout.home_item_nt, null);
                            break;
                        case 2:
                            view = inflater.inflate(R.layout.home_item_tl, null);
                            break;
                        case 3:
                            view = inflater.inflate(R.layout.home_item_tr, null);
                            break;
                        case 4:
                            view = inflater.inflate(R.layout.home_item_tupthree, null);
                            break;
                        case 5:
                            view = inflater.inflate(R.layout.home_item_video, null);
                            break;
                    }
                    if(style==5){

                        final String title = item.optString("title");
                        final String viewtime = item.optString("viewTimes");
                        final String sharetime = item.optString("shareTimes");
                        final String image = item.optString("img1");
                        final String videourl = item.optString("videoUrl");
                        TextView tvtitle = (TextView) view.findViewById(R.id.home_item_title);
                        tvtitle.setText("  "+title);
                        TextView tvviewtime = (TextView) view.findViewById(R.id.home_item_numplay_text);
                        tvviewtime.setText("播放次数"+viewtime);
                        TextView tvsharetime = (TextView) view.findViewById(R.id.home_item_numshare_text);
                        tvsharetime.setText("  "+sharetime+"次");
                        SimpleDraweeView sdvimage = (SimpleDraweeView ) view.findViewById(R.id.home_item_videopic);
                        Uri uri = Uri.parse(image);
                        sdvimage.setImageURI(uri);
                        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.home_content_item);
                        relativeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, VideoActivity.class);
                                intent.putExtra("videourl","http://us.sinaimg.cn/002SnPDSjx076NnfONY3010401005jR60k01.mp4?label=mp4_hd&Expires=1486637686&ssig=c%2ByQMyMIkG&KID=unistore,video");
                                activity.startActivity(intent);
                            }
                        });
                        linearLayout.addView(view);
                        continue;
                    }
                    final String price=item.optString("price");
                    final String title = item.optString("title");
                    String date = item.optString("date");
                    String pos = item.optString("pos");
                    final String id = item.optString("id");
                    final String xiuUrl = item.optString("xiuUrl");
                    final String phone = item.optString("hostPhone");
                    final String image1 = item.optString("img1");
                    String image2 = item.optString("img2");
                    String image3 = item.optString("img3");


                    TextView home_item_view_number = (TextView) view.findViewById(R.id.home_item_view_number);
                    home_item_view_number.setText("￥"+price+"起");
                    TextView tvtitle = (TextView) view.findViewById(R.id.home_item_title);
                    tvtitle.setText(title);
                    TextView tvtime = (TextView) view.findViewById(R.id.home_item_date);
                    tvtime.setText(date);
                    TextView tvaddress = (TextView) view.findViewById(R.id.home_item_address);
                    tvaddress.setText(pos);
                    if (style!=1) {
                        if (image1.length() > 30) {
                            SimpleDraweeView sdvimage = (SimpleDraweeView) view.findViewById(R.id.home_item_image_a);
                            Uri uri = Uri.parse(image1);
                            sdvimage.setImageURI(uri);
                        }
                    }
                    if (style==4) {
                        Log.e("Style","4");
                        if (image2.length() > 30) {
                            SimpleDraweeView sdvimage = (SimpleDraweeView) view.findViewById(R.id.home_item_image_b);
                            Uri uri = Uri.parse(image2);
                            sdvimage.setImageURI(uri);
                        }
                        if (image3.length() > 30) {
                            SimpleDraweeView sdvimage = (SimpleDraweeView) view.findViewById(R.id.home_item_image_c);
                            Uri uri = Uri.parse(image3);
                            sdvimage.setImageURI(uri);
                        }
                    }

                    RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.home_content_item);
                    relativeLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent = new Intent(context,MeetingdetailActivity.class);
                            intent.putExtra("title",title);
                            intent.putExtra("xiuUrl",xiuUrl);
                            intent.putExtra("actId",id);
                            intent.putExtra("phone",phone);
                            intent.putExtra("image",image1);
                            activity.startActivity(intent);
                        }
                    });
                    linearLayout.addView(view);
                    //pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                    //pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        } else {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.FAIL);
        }
    }

    public int getSize() {
        Log.e("GetHomeData.getSize",size+"");
        return this.size;
    }
}
