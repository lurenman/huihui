package com.hui2020.huihui.Home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hui2020.huihui.FeetButtonInitialize;
import com.hui2020.huihui.Home.RefreshableLayout.MyListener;
import com.hui2020.huihui.Home.RefreshableLayout.PullToRefreshLayout;
import com.hui2020.huihui.Location.LocationActivity;
import com.hui2020.huihui.Login.UserLoginTask;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Payment.Contact;
import com.hui2020.huihui.R;
import com.hui2020.huihui.Home.SlideShowView.SlideShowView;
import com.hui2020.huihui.StringUtil;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private LinearLayout homecontent;
    private SlideShowView homead;
    private String type, position;
    private ImageView location, scan;
    private int current, size;
    private GetHomeData getHomeData;
    private boolean refresh;
    private LayoutInflater inflater;
    private TextView tvlocation, tj,it,jr,jy,yl,cy,mr,sh,hg,ny,gy,wl,dc,wt,qt;
    private Intent intent;
    private Activity activity;


    private PullToRefreshLayout pull;

    public LocationClient mLocationClient = null;
    public MyLocationListenner myListener = new MyLocationListenner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        app = (MyApplication) getApplication();
        if(app.getSign()){
            UserLoginTask login = new UserLoginTask(app,this,app.getUserName(),app.getPassword());
            login.execute();
        }
        activity=this;
        ImageView iv = (ImageView) findViewById(R.id.feet_discover_a);
        iv.setImageResource(R.drawable.feet_discover_b);
        type = "推荐";
       // position = app.getLocation();
        current = 0;
        size = 10;
        refresh = true;
        initComponent();
        setNavColortoDef();
        tj.setTextColor(Color.parseColor("#0099FF"));
    }

    private void  initComponent() {


        mLocationClient = new LocationClient( this );
        mLocationClient.registerLocationListener( myListener );

        setLocationOption();
        mLocationClient.start();

        new FeetButtonInitialize(this);
        inflater = LayoutInflater.from(this);
        homecontent = (LinearLayout) findViewById(R.id.home_content_lin);
        pull = (PullToRefreshLayout) findViewById(R.id.home_content);
        pull.setOnRefreshListener(new addviewListener());
        pull.autoRefresh();
        location = (ImageView) findViewById(R.id.home_head_location);
        location.setOnClickListener(this);
        tvlocation = (TextView) findViewById(R.id.home_head_location_text);
        tvlocation.setOnClickListener(this);
       // tvlocation.setText(app.getLocation());
        scan = (ImageView) findViewById(R.id.home_head_scan);
        scan.setOnClickListener(this);
        tj=(TextView) findViewById(R.id.home_nav_lin_tj);
        tj.setOnClickListener(this);
        it=(TextView) findViewById(R.id.home_nav_lin_it);
        it.setOnClickListener(this);
        jr=(TextView) findViewById(R.id.home_nav_lin_jr);
        jr.setOnClickListener(this);
        jy=(TextView) findViewById(R.id.home_nav_lin_jy);
        jy.setOnClickListener(this);
        yl=(TextView) findViewById(R.id.home_nav_lin_yl);
        yl.setOnClickListener(this);
        cy=(TextView) findViewById(R.id.home_nav_lin_cy);
        cy.setOnClickListener(this);
        mr=(TextView) findViewById(R.id.home_nav_lin_mr);
        mr.setOnClickListener(this);
        sh=(TextView) findViewById(R.id.home_nav_lin_sh);
        sh.setOnClickListener(this);
        hg=(TextView) findViewById(R.id.home_nav_lin_hg);
        hg.setOnClickListener(this);
        ny=(TextView) findViewById(R.id.home_nav_lin_ny);
        ny.setOnClickListener(this);
        gy=(TextView) findViewById(R.id.home_nav_lin_gy);
        gy.setOnClickListener(this);
        wl=(TextView) findViewById(R.id.home_nav_lin_wl);
        wl.setOnClickListener(this);
        dc=(TextView) findViewById(R.id.home_nav_lin_dc);
        dc.setOnClickListener(this);
        wt=(TextView) findViewById(R.id.home_nav_lin_wt);
        wt.setOnClickListener(this);
        qt=(TextView) findViewById(R.id.home_nav_lin_qt);
        qt.setOnClickListener(this);
        homead = (SlideShowView) findViewById(R.id.home_content_side);
        homead.updateHomeAd(position);



    }

    //////////////////////////////下拉上拉的刷新控件
    public class addviewListener extends MyListener {

        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            // 下拉刷新操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件刷新完毕了哦！
                    if(getHomeData!=null) {
                        Toast.makeText(getApplicationContext(), "上一次获取的数据大小: "+getHomeData.getSize(), Toast.LENGTH_SHORT).show();
                    }
                    if(refresh = true){
                        current=0;
                    }
                    getHomeData = new GetHomeData(activity,getApplicationContext(),pullToRefreshLayout,homecontent,type, position, Integer.toString(current), Integer.toString(size),refresh);
                    getHomeData.execute();
                    refresh = false;
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            // 加载操作
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    // 千万别忘了告诉控件加载完毕了哦！
                    current += getHomeData.getSize();
                    getHomeData = new GetHomeData(activity,getApplicationContext(),pullToRefreshLayout,homecontent,type, position, Integer.toString(current), Integer.toString(size),refresh);
                    getHomeData.execute();
                    Toast.makeText(getApplicationContext(), "刷新控件生效", Toast.LENGTH_SHORT).show();
                    //pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }.sendEmptyMessageDelayed(0, 1000);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_head_location:
                intent = new Intent(HomeActivity.this, LocationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.home_head_location_text:
                intent = new Intent(HomeActivity.this, LocationActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.home_head_scan:
                intent = new Intent(HomeActivity.this, Scanimage.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.home_nav_lin_tj:
                homead.setVisibility(View.VISIBLE);
                type = "推荐";
                current = 0;
                refresh=true;
                setNavColortoDef();
                tj.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_it:
                homead.setVisibility(View.GONE);
                type = "IT";
                current = 0;
                refresh=true;
                setNavColortoDef();
                it.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_jr:
                homead.setVisibility(View.GONE);
                type = "金融";
                current = 0;
                refresh=true;
                setNavColortoDef();
                jr.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_jy:
                homead.setVisibility(View.GONE);
                type = "教育";
                current = 0;
                refresh=true;
                setNavColortoDef();
                jy.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_yl:
                homead.setVisibility(View.GONE);
                type = "医疗";
                current = 0;
                refresh=true;
                setNavColortoDef();
                yl.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_cy:
                homead.setVisibility(View.GONE);
                type = "创业";
                current = 0;
                refresh=true;
                setNavColortoDef();
                cy.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_mr:
                homead.setVisibility(View.GONE);
                type = "美容";
                current = 0;
                refresh=true;
                setNavColortoDef();
                mr.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_sh:
                homead.setVisibility(View.GONE);
                type = "商会";
                current = 0;
                refresh=true;
                setNavColortoDef();
                sh.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_hg:
                homead.setVisibility(View.GONE);
                type = "化工";
                current = 0;
                refresh=true;
                setNavColortoDef();
                hg.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_ny:
                homead.setVisibility(View.GONE);
                type = "农业";
                current = 0;
                refresh=true;
                setNavColortoDef();
                ny.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
            case R.id.home_nav_lin_gy:
                homead.setVisibility(View.GONE);
                type = "工业";
                current = 0;
                refresh=true;
                setNavColortoDef();
                gy.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;case R.id.home_nav_lin_wl:
                homead.setVisibility(View.GONE);
                type = "物流";
                current = 0;
                refresh=true;
                setNavColortoDef();
                wl.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;case R.id.home_nav_lin_dc:
                homead.setVisibility(View.GONE);
                type = "地产";
                current = 0;
                refresh=true;
                setNavColortoDef();
                dc.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;case R.id.home_nav_lin_wt:
                homead.setVisibility(View.GONE);
                type = "文体";
                current = 0;
                refresh=true;
                setNavColortoDef();
                wt.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;case R.id.home_nav_lin_qt:
                homead.setVisibility(View.GONE);
                type = "其他";
                current = 0;
                refresh=true;
                setNavColortoDef();
                qt.setTextColor(Color.parseColor("#0099FF"));
                pull.autoRefresh();
                break;
        }
    }
    private void setNavColortoDef(){
        tj.setTextColor(Color.parseColor("#333333"));
        it.setTextColor(Color.parseColor("#333333"));
        jr.setTextColor(Color.parseColor("#333333"));
        jy.setTextColor(Color.parseColor("#333333"));
        yl.setTextColor(Color.parseColor("#333333"));
        cy.setTextColor(Color.parseColor("#333333"));
        mr.setTextColor(Color.parseColor("#333333"));
        sh.setTextColor(Color.parseColor("#333333"));
        hg.setTextColor(Color.parseColor("#333333"));
        ny.setTextColor(Color.parseColor("#333333"));
        gy.setTextColor(Color.parseColor("#333333"));
        wl.setTextColor(Color.parseColor("#333333"));
        dc.setTextColor(Color.parseColor("#333333"));
        wt.setTextColor(Color.parseColor("#333333"));
        qt.setTextColor(Color.parseColor("#333333"));
    }

    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode){
            case 1:
                //来自按钮1的请求，作相应业务处理
                position=data.getStringExtra("city");
                homead.updateHomeAd(position);
                pull.autoRefresh();
                tvlocation.setText(position);
                break;
            case 2:
                Toast.makeText(getApplicationContext(),resultCode+""+data.getExtras().get("codedContent").toString(),Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getApplicationContext(),resultCode+"",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            tvlocation.setText(location.getCity());
            if(StringUtil.isEmpty(location.getCity())){
                position="";
            }else {
                position=(location.getCity()).substring(0,(location.getCity()).indexOf("市"));
                Log.e("ct",position);
                homead.updateHomeAd(position);
            }

        }


    }
    @Override
    public void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    //设置相关参数
    private void setLocationOption(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); //打开gps
        option.setServiceName("com.baidu.location.service_v2.9");
      //  option.setPoiExtraInfo(true);
        option.setAddrType("all");
        option.setPriority(LocationClientOption.NetWorkFirst);
        option.setPriority(LocationClientOption.GpsFirst);       //gps
       // option.setPoiNumber(10);
        option.disableCache(true);
        mLocationClient.setLocOption(option);
    }


}
