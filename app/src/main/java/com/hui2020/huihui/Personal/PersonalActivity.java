package com.hui2020.huihui.Personal;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.FeetButtonInitialize;
import com.hui2020.huihui.Login.LoginActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.Order.OrderActivity;
import com.hui2020.huihui.Personal.warehouse.WarehouseActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.Personal.Setting.SettingActivity;

/**
 * Created by FD-GHOST on 2016/12/24.
 * This class is used to initialize each component in the Personal Activity
 */


public class PersonalActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private RelativeLayout card, assistance, note, warehouse, order, market, collection, setting;
    private MyApplication app;
    private SimpleDraweeView icon;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        app = (MyApplication) getApplication();
        if(app.getSign()!=true){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            initComponent();
            new FeetButtonInitialize(this);
            ImageView iv = (ImageView) findViewById(R.id.feet_my_a);
            iv.setImageResource(R.drawable.feet_my_b);
        }
    }

    private void initComponent() {
        icon = (SimpleDraweeView)findViewById(R.id.personal_icon);
        icon.setImageURI(Uri.parse(app.getAvatar()));
        name = (TextView) findViewById(R.id.personal_username);
        name.setText(app.getRealName());
        card = (RelativeLayout) findViewById(R.id.personal_card);
        card.setOnClickListener(this);
        assistance = (RelativeLayout) findViewById(R.id.personal_warehouse);
        assistance.setOnClickListener(this);
//        market = (RelativeLayout) findViewById(R.id.personal_market);
//        market.setOnClickListener(this);
        order = (RelativeLayout) findViewById(R.id.personal_order);
        order.setOnClickListener(this);
        setting = (RelativeLayout) findViewById(R.id.personal_setting);
        setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personal_card:
                intent = new Intent(PersonalActivity.this, PersonalinfoActivity.class);
                PersonalActivity.this.startActivity(intent);
                break;
            case R.id.personal_warehouse:
                intent = new Intent(PersonalActivity.this, WarehouseActivity.class);
                PersonalActivity.this.startActivity(intent);
                break;
//            case R.id.personal_market:
//                intent = new Intent(PersonalActivity.this, MarketActivity.class);
//                PersonalActivity.this.startActivity(intent);
//                break;
            case R.id.personal_order:
                intent = new Intent(PersonalActivity.this, OrderActivity.class);
                PersonalActivity.this.startActivity(intent);
                break;

            case R.id.personal_setting:
                intent = new Intent(PersonalActivity.this, SettingActivity.class);
                PersonalActivity.this.startActivity(intent);
                break;
        }
    }
}
