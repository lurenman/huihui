package com.hui2020.huihui.Card;

import android.content.Intent;
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

import com.facebook.drawee.backends.pipeline.Fresco;
import com.hui2020.huihui.FeetButtonInitialize;
import com.hui2020.huihui.Login.LoginActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.MarketActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.Scence.ScencedetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CardActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication app;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LayoutInflater mInflater;
    private List<String> mTitleList = new ArrayList<>();//TabView title
    private View card, warehouse, attention;//TabView content
    private List<View> mViewList = new ArrayList<>();//TabView content collection
    RelativeLayout scencedetail_card_newfriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        app = (MyApplication) getApplication();
        if(app.getSign()!=true){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            initComponent();
        }
    }

    private void initComponent() {

        new FeetButtonInitialize(this);
        ImageView iv = (ImageView) findViewById(R.id.feet_card_a);
        iv.setImageResource(R.drawable.feet_card_b);
        mTabLayout = (TabLayout)findViewById(R.id.card_tab);
        mViewPager = (ViewPager)findViewById(R.id.card_view);
        mInflater = LayoutInflater.from(getApplicationContext());
        card = mInflater.inflate(R.layout.card_item_card, null);
        warehouse = new View(getApplicationContext());
        attention = new View(getApplicationContext());
        mViewList.add(card);
        mViewList.add(warehouse);
        mViewList.add(attention);
        mTitleList.add("名片");
        mTitleList.add("智库");
        mTitleList.add("关注");
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        MyPagerAdapter mAdapter = new MyPagerAdapter(mViewList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        new PrepareCards(app,this,(LinearLayout) card.findViewById(R.id.card_item_card__lin)).execute();

        scencedetail_card_newfriend=(RelativeLayout)card.findViewById(R.id.scencedetail_card_newfriend);
        scencedetail_card_newfriend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scencedetail_card_newfriend:
                Intent intent=new Intent(CardActivity.this,NewFriendsActivity.class);
                startActivity(intent);
                break;
        }
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
}
