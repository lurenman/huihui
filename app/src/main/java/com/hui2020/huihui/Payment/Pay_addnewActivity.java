package com.hui2020.huihui.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.R;

public class Pay_addnewActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout back;
    private EditText nameIn,phoneIn,companyIn,jobIn;
    private TextView confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_addnew);
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        nameIn = (EditText)findViewById(R.id.pay_addnew_name_textin);
        phoneIn = (EditText)findViewById(R.id.pay_addnew_phone_textin);
        companyIn = (EditText)findViewById(R.id.pay_addnew_company_textin);
        jobIn = (EditText)findViewById(R.id.pay_addnew_job_textin);
        confirm = (TextView)findViewById(R.id.pay_addnew_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,phone,company,job;
                name = nameIn.getText().toString();
                phone = phoneIn.getText().toString();
                company = ""+companyIn.getText().toString();
                job = ""+jobIn.getText().toString();
                if(name.length()<=0){
                    Toast.makeText(getApplicationContext(),"您输入的姓名有误",Toast.LENGTH_SHORT).show();
                }else if(phone.length()!=11){
                    Toast.makeText(getApplicationContext(),"您输入的手机有误",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = getIntent();
                    Contact contact = new Contact(name,phone,company,job);
                    intent.putExtra("contact", contact);
                    setResult(1,intent);
                    finish();
                }
            }
        });
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
