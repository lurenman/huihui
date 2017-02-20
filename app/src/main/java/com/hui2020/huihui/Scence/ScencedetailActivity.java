package com.hui2020.huihui.Scence;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.hui2020.huihui.Interaction;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FD-GHOST on 2017/01/07.
 * This class is used to initialize each component in the scene information page
 * Note: The action sign and distance will be controlled here
 */

public class ScencedetailActivity extends AppCompatActivity{

    private RelativeLayout back;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//TabView title
    private View detail, card;//TabView content
    private List<View> mViewList = new ArrayList<>();//TabView content collection
    private BDLocationListener bdLocationListener;
    private LocationClient mLocationClient;
    private String actId;
    private TextView distance;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scencedetail);

        app = (MyApplication) getApplication();
        actId=getIntent().getStringExtra("actId");
        initComponent();


    }

    private void initComponent() {
        back = (RelativeLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.scencedetail_view);
        mTabLayout = (TabLayout) findViewById(R.id.scencedetail_tab);

        mInflater = LayoutInflater.from(ScencedetailActivity.this);
        detail = mInflater.inflate(R.layout.scencedetail_item_detail, null);
        card = mInflater.inflate(R.layout.scencedetail_item_card,null);

        //添加页卡视图
        mViewList.add(detail);
        mViewList.add(new View(this));
        mViewList.add(card);
        //添加页卡标题
        mTitleList.add("活动详情");
        mTitleList.add("会议室");
        mTitleList.add("参会嘉宾");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

        new GetScencedetailDetail(app,this,getApplicationContext(),detail,app.getUserName(),app.getPassword(), actId).execute();
        TextView tvlatitude = (TextView) detail.findViewById(R.id.scencedetail_targetLatitude);
        TextView tvLongitude = (TextView) detail.findViewById(R.id.scencedetail_targetLongitude);
        distance = (TextView) detail.findViewById(R.id.scencedetail_distance);
                bdLocationListener = new ScenceLocationListener(tvlatitude, tvLongitude, distance);
                mLocationClient = new LocationClient(getApplicationContext());
                mLocationClient.registerLocationListener(bdLocationListener);
                initLocation();
                mLocationClient.start();
                mLocationClient.requestLocation();
        new GetScencedetailCard(app,ScencedetailActivity.this, getApplicationContext(), card,actId).execute();

    }




    //ViewPager适配器
    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);//页卡标题
        }

    }

    @Override
    public void finish() {
        bdLocationListener=null;
        mLocationClient.stop();
        mLocationClient=null;
        super.finish();
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 10000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(false);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(false);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

}
