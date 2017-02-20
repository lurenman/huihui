package com.hui2020.huihui.Payment;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.Home.HomeActivity;
import com.hui2020.huihui.Location.LocationActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

public class PayActivity extends AppCompatActivity implements View.OnClickListener{
    private int ticketId;
    private float price,sum;
    private TextView tvtitle,tvdate,tvaddress,tvtype,tvremain,tvaddfromcards,tvaddnew,tvsum,tvconfirm;
    private RelativeLayout back;
    private LinearLayout contacts;
    private ArrayList<Contact> contact;
    private Intent intent;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        app = (MyApplication)getApplication();
        initComponent();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        contact = new ArrayList<Contact>();
        ticketId=Integer.parseInt(getIntent().getStringExtra("ticketId"));
        price=Float.parseFloat(getIntent().getStringExtra("price"));
        tvtitle=(TextView)findViewById(R.id.pay_meetinginfo_title);
        tvtitle.setText(getIntent().getStringExtra("title"));
        tvdate=(TextView)findViewById(R.id.pay_meetinginfo_time);
        tvdate.setText(getIntent().getStringExtra("date"));
        tvaddress=(TextView)findViewById(R.id.pay_meetinginfo_address);
        tvaddress.setText(getIntent().getStringExtra("address"));
        tvtype=(TextView)findViewById(R.id.pay_ticketinfo_typeprice);
        String typeandprice = getIntent().getStringExtra("type")+" "+getIntent().getStringExtra("price"+"元");
        tvtype.setText(typeandprice);
        tvremain=(TextView)findViewById(R.id.pay_ticketinfo_remain);
        String remain ="剩余"+getIntent().getStringExtra("remain")+"张";
        tvremain.setText(remain);
        tvaddfromcards=(TextView)findViewById(R.id.pay_addmore_fromcards);
        tvaddfromcards.setOnClickListener(this);
        tvaddnew=(TextView)findViewById(R.id.pay_addmore_addnew);
        tvaddnew.setOnClickListener(this);
        tvsum=(TextView)findViewById(R.id.pay_sum);
        tvsum.setText("订单总额: ￥0.00");
        tvconfirm=(TextView)findViewById(R.id.pay_confirme);
        tvconfirm.setOnClickListener(this);
        contacts = (LinearLayout)findViewById(R.id.pay_contacts);
        final Contact self = new Contact(app.getRealName(),app.getPhone(),app.getJob(),app.getCompany());
        contact.add(self);
        LayoutInflater inflater;
        inflater = LayoutInflater.from(getApplicationContext());
        final View item = inflater.inflate(R.layout.pay_item, null);
        final TextView name = (TextView) item.findViewById(R.id.pay_item_name);
        name.setText(app.getRealName());
        TextView phone = (TextView) item.findViewById(R.id.pay_item_phone);
        phone.setText(app.getPhone());
        TextView delete = (TextView) item.findViewById(R.id.pay_item_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("PayClick","点击"+name);
                contact.remove(self);
                contacts.removeView(item);
                sum = price*contact.size();
                tvsum.setText("订单总额: ￥"+sum+"   "+price+"*"+contact.size());
            }
        });
        sum = price*contact.size();
        tvsum.setText("订单总额: ￥"+sum+"   "+price+"*"+contact.size());
        contacts.addView(item);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.back:
                finish();
                break;

            case R.id.pay_addmore_fromcards:
                intent = new Intent(getApplicationContext(), Pay_addexistActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.pay_addmore_addnew:
                intent = new Intent(getApplicationContext(), Pay_addnewActivity.class);
                startActivityForResult(intent, 0);
                break;

            case R.id.pay_confirme:
                if(contact.size()>0) {
                    String title = getIntent().getStringExtra("title");
                    String sum = this.sum+"";
                    new RequestOrder(app,this,ticketId+"",contact,title,sum).execute();
                }else{
                    Toast.makeText(getApplicationContext(),"您还没有添加任何信息",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        switch(resultCode){
            case 1:
                final Contact newcontact = (Contact) data.getSerializableExtra("contact");
                contact.add(newcontact);
                final View item = inflater.inflate(R.layout.pay_item, null);
                final TextView name = (TextView) item.findViewById(R.id.pay_item_name);
                name.setText(newcontact.getName());
                TextView phone = (TextView) item.findViewById(R.id.pay_item_phone);
                phone.setText(newcontact.getPhone());
                TextView delete = (TextView) item.findViewById(R.id.pay_item_delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("PayClick","点击"+name);
                        contact.remove(newcontact);
                        contacts.removeView(item);
                        sum = price*contact.size();
                        tvsum.setText("订单总额: ￥"+sum+"   "+price+"*"+contact.size());
                    }
                });
                sum = price*contact.size();
                tvsum.setText("订单总额: ￥"+sum+"   "+price+"*"+contact.size());
                contacts.addView(item);
                break;


            case 2:
                final ArrayList<Contact> newcontacts = (ArrayList<Contact>) data.getSerializableExtra("contacts");
                for(int i=0; i<newcontacts.size();i++) {
                    final Contact existcontact = newcontacts.get(i);
                    contact.add(existcontact);
                    final View existitem = inflater.inflate(R.layout.pay_item, null);
                    final TextView existname = (TextView) existitem.findViewById(R.id.pay_item_name);
                    existname.setText(existcontact.getName());
                    TextView existphone = (TextView) existitem.findViewById(R.id.pay_item_phone);
                    existphone.setText(existcontact.getPhone());
                    TextView existdelete = (TextView) existitem.findViewById(R.id.pay_item_delete);
                    existdelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e("PayClick", "点击" + existname);
                            contact.remove(existcontact);
                            contacts.removeView(existitem);
                            sum = price * contact.size();
                            tvsum.setText("订单总额: ￥" + sum + "   " + price + "*" + contact.size());
                        }
                    });
                    sum = price * contact.size();
                    tvsum.setText("订单总额: ￥" + sum + "   " + price + "*" + contact.size());
                    contacts.addView(existitem);
                }
                break;

            default:
                Toast.makeText(getApplicationContext(),resultCode+"你取消了操作",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
