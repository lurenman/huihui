package com.hui2020.huihui.Personal.Order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hui2020.huihui.R;
import com.hui2020.huihui.zxing.encode.EncodingUtils;

public class TicketActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout back;
    private Intent intent;
    private TextView id,title,date,address,hostPhone,type,price,name,phone,company,job;
    private ImageView qrcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        intent=getIntent();
        initComponent();
    }

    private void initComponent(){
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        id = (TextView) findViewById(R.id.ticket_id);
        id.setText(intent.getStringExtra("ticketId"));
        title = (TextView) findViewById(R.id.ticket_title);
        title.setText(intent.getStringExtra("title"));
        date = (TextView) findViewById(R.id.ticket_date);
        date.setText(intent.getStringExtra("dateBegin"));
        address = (TextView) findViewById(R.id.ticket_address);
        address.setText(intent.getStringExtra("address"));
        hostPhone = (TextView) findViewById(R.id.ticket_hostphone);
        hostPhone.setText(intent.getStringExtra("hostPhone"));
        type = (TextView) findViewById(R.id.ticket_type);
        type.setText(intent.getStringExtra("consumeType"));
        price = (TextView) findViewById(R.id.ticket_price);
        price.setText(intent.getStringExtra("price"));
        name = (TextView) findViewById(R.id.ticket_ticketname);
        name.setText(intent.getStringExtra("ticketName"));
        phone = (TextView) findViewById(R.id.ticket_ticketphone);
        phone.setText(intent.getStringExtra("ticketPhone"));
        company = (TextView) findViewById(R.id.ticket_ticketcompany);
        company.setText(intent.getStringExtra("ticketCompany"));
        job = (TextView) findViewById(R.id.ticket_ticketjob);
        job.setText(intent.getStringExtra("ticketJob"));
        qrcode = (ImageView)findViewById(R.id.ticket_qrcode);
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(intent.getStringExtra("ticketId"), 100, 100, null);
        qrcode.setImageBitmap(qrCodeBitmap);
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
