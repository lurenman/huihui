package com.hui2020.huihui.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.util.ArrayList;

public class Pay_addexistActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication app;
    private RelativeLayout back;
    private TextView confirm;
    private LinearLayout cardList;
    private ArrayList<Contact> contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_addexist);
        app=(MyApplication)getApplication();
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        confirm = (TextView)findViewById(R.id.pay_addexist_confirm);
        confirm.setOnClickListener(this);
        cardList = (LinearLayout)findViewById(R.id.pay_addexist_lin);
        contacts = new ArrayList<Contact>();
        new PrepareCardList(app,cardList,contacts).execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back:
                finish();
                break;
            case R.id.pay_addexist_confirm:
                Intent intent = getIntent();
                intent.putExtra("contacts", contacts);
                setResult(2,intent);
                finish();
                break;
        }
    }
}
