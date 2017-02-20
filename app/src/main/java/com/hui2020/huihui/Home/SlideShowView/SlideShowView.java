package com.hui2020.huihui.Home.SlideShowView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Login.HttpUtils;
import com.hui2020.huihui.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 */

public class SlideShowView extends FrameLayout {

    //轮播图图片数量
    private final static int IMAGE_COUNT = 3;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 3;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;
    //自定义轮播图的资源
    private List<String> imageUrls;
    //放轮播图片的ImageView 的list
    private List<SimpleDraweeView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    static final private String filename = "homeAdUrl";
    private String city;

    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideShowView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //initImageLoader(context);

        initData();
        if (isAutoPlay) {
            startPlay();
        }

    }


    /**
     * 开始轮播图切换
     */
    private void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    private void stopPlay() {
        scheduledExecutorService.shutdown();
    }

    /**
     * Initialized Data
     * First read from local memory
     * Then sync with server and update local memory
     */
    private void initData() {
        Log.e("SlideShow", "initData");
        imageViewsList = new ArrayList<SimpleDraweeView>();
        dotViewsList = new ArrayList<View>();
        imageUrls = new ArrayList<String>();
        File newfile = new File(getContext().getFilesDir() + filename);
        try {
            InputStream instream = new FileInputStream(newfile);
            InputStreamReader inputreader = new InputStreamReader(instream);
            BufferedReader buffreader = new BufferedReader(inputreader);
            String line;
            while ((line = buffreader.readLine()) != null) {
                imageUrls.add(line);
            }
        } catch (Exception ex) {
        }
        new GetHomeAdData().execute();
        initUI(getContext());
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
//        if(imageUrls == null || imageUrls.length == 0)
//            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slidedot, this, true);

        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        dotViewsList.clear();
        imageViewsList.clear();
        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.size(); i++) {

            SimpleDraweeView view = new SimpleDraweeView(context);
            //view.setTag(imageUrls.get(i));
            final Context contextl = context;
            if (i == 1) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(contextl.getFilesDir(), filename);
                        String output = "";
//                        Log.e("Path:",contextl.getFilesDir() + "/"+filename);
                        try {
                            InputStream instream = new FileInputStream(file);//读取输入流
                            InputStreamReader inputreader = new InputStreamReader(instream);//设置流读取方式
                            BufferedReader buffreader = new BufferedReader(inputreader);
                            String line;
                            while ((line = buffreader.readLine()) != null) {
                                output += line + "\n";//读取的文件内容
                                Toast.makeText(contextl, output, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                        }
                        Toast.makeText(contextl, output, Toast.LENGTH_SHORT).show();
                    }
                });
            }
//            }
            Uri uri = Uri.parse(imageUrls.get(i));
            view.setImageURI(uri);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 16;
            params.rightMargin = 16;
            params.height = 20;
            params.width = 20;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 销毁ImageView资源，回收内存
     */

    private void destoryBitmaps() {


        for (int i = 0; i < IMAGE_COUNT; i++) {
            SimpleDraweeView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }


    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager) container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            SimpleDraweeView imageView = imageViewsList.get(position);

            //imageLoader.displayImage(imageView.getTag() + "", imageView);

            ((ViewPager) container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements OnPageChangeListener {

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == pos) {
                    ((View) dotViewsList.get(pos)).setBackgroundResource(R.drawable.dot1);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.dot2);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 异步任务,获取数据
     */
    public class GetHomeAdData extends AsyncTask<Void, Void, Boolean> {
        private String newpassword;
        private URL url = null;
        private JSONArray itemarray;
        private JSONObject item;
        private int size;


        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                url = new URL("http://m.hui2020.com/server/m.php?act=getLunbo&");
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
            String post = "type=1" + "&city=" + city;
            Log.e("SlideShow,post", post);
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
            Log.e("SlideShow.result", result);
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("SlideShow", "Service error");
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
                Log.e("SlideShow.size", itemarray.length() + "");
                size = itemarray.length();
                return true;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                String string = "";
                imageUrls.clear();
                if (size > 0) {
                    for (int i = 0; i < itemarray.length(); i++) {
                        try {
                            item = itemarray.getJSONObject(i);
                            Log.e("SlideShow.singleitem", item.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String img = item.optString("proImg");
                        imageUrls.add(img);
                        string += (img + "\n");
                    }
                }

                Toast.makeText(getContext(), "加载成功 ", Toast.LENGTH_SHORT).show();
                initUI(getContext());
                File file = new File(getContext().getCacheDir(), filename);
                FileOutputStream outputStream;
                try {
                    outputStream = new FileOutputStream(file);
                    outputStream.write(string.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "加载失败 ", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateHomeAd(String city) {
        this.city = city;
        new GetHomeAdData().execute();
    }

}