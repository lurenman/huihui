package com.hui2020.huihui.Personal.Setting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.hui2020.huihui.Home.HomeActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.Personal.MarketActivity;
import com.hui2020.huihui.Personal.PersonaleditActivity;
import com.hui2020.huihui.R;
import com.hui2020.huihui.myiosdialog.ActionSheetDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private Intent intent;
    private RelativeLayout suggestion, aboutus, cleancache, version, exist,back,setting_onlinehelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        app=(MyApplication)getApplication();
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        suggestion = (RelativeLayout) findViewById(R.id.setting_suggestion);
        suggestion.setOnClickListener(this);
        aboutus = (RelativeLayout) findViewById(R.id.setting_aboutus);
        aboutus.setOnClickListener(this);
        cleancache = (RelativeLayout) findViewById(R.id.setting_cleancache);
        cleancache.setOnClickListener(this);
        version = (RelativeLayout) findViewById(R.id.setting_version);
        version.setOnClickListener(this);
        exist = (RelativeLayout) findViewById(R.id.setting_exit);
        exist.setOnClickListener(this);
        setting_onlinehelp=(RelativeLayout)findViewById(R.id.setting_onlinehelp);
        setting_onlinehelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setting_suggestion:
                intent = new Intent(SettingActivity.this, SuggestionActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_aboutus:
                intent = new Intent(SettingActivity.this, AboutusActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_cleancache:
                long cacheSize = Fresco.getImagePipelineFactory().getMainDiskStorageCache().getSize();
                int cacheSizeInt =Math.round((cacheSize/1024)/1024);
                ImagePipeline imagePipeline = Fresco.getImagePipeline();
                imagePipeline.clearCaches();
                Toast.makeText(getApplicationContext(),"已清理"+cacheSizeInt+"",Toast.LENGTH_SHORT);
                break;
            case R.id.setting_version:
                ////////
                break;
            case R.id.setting_onlinehelp:
                intent = new Intent(SettingActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_exit:
                new ActionSheetDialog(SettingActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("退出",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        app.setSign(false);
                                        //TODO It should also clean the database if necessary
                                        intent = new Intent(SettingActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(),"退出登录成功",Toast.LENGTH_SHORT);
                                        finish();
                                    }
                                }).show();

                break;
        }
    }
}
