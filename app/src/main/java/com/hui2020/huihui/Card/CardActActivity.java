package com.hui2020.huihui.Card;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.R;

/**
 * Created by FD-GHOST on 2016/12/19.
 * To control Popup dialogs that ask user to confirm add friend
 */

public class CardActActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView comfirme, cancle, title;
    private SurfaceView surfaceView;
    private String id, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_act);
        id=getIntent().getStringExtra("userId");
        name = getIntent().getStringExtra("realName");
        title = (TextView) findViewById(R.id.cardact_title);
        title.setText("是否要向"+name+"递交名片");
        title.setOnClickListener(this);
        comfirme = (TextView) findViewById(R.id.cardact_comfirme);
        comfirme.setOnClickListener(this);
        cancle = (TextView) findViewById(R.id.cardact_cancle);
        cancle.setOnClickListener(this);
        surfaceView = (SurfaceView)findViewById(R.id.cardact_null);
        surfaceView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cardact_title:
                Toast.makeText(CardActActivity.this,"别乱点标题！！！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardact_comfirme:
                Toast.makeText(CardActActivity.this,"别乱点确认！！！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.cardact_cancle:
                finish();
                break;
            case R.id.cardact_null: //Here is for click outside the layout will canceled this action
                finish();
                break;
        }
    }
}
