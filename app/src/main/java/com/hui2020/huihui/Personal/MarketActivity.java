package com.hui2020.huihui.Personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hui2020.huihui.R;

/**
 * Created by FD-GHOST on 2016/12/24.
 * Incomplete class
 */

public class MarketActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

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
