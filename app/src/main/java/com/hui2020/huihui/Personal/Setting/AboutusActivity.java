package com.hui2020.huihui.Personal.Setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.hui2020.huihui.R;

public class AboutusActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;

        }
    }
}
