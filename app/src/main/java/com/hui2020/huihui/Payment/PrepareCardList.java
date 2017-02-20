package com.hui2020.huihui.Payment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Card.CarddetailActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2017/1/14.
 * This is for read and show the cards list
 */

public class PrepareCardList extends AsyncTask<Void, Void, Boolean> {
    private MyApplication app;
    private LinearLayout linearLayout;
    private ArrayList<String> card;
    private ArrayList<Contact> contacts;

    public PrepareCardList(MyApplication myApplication, LinearLayout linearLayout, ArrayList<Contact> contacts){
        this.app=myApplication;
        this.linearLayout=linearLayout;
        this.contacts=contacts;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        Log.e("PrepareCard","doing");
        card = app.getFriend();
        return true;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        Log.e("PrepareCard","onpost");
        LayoutInflater inflater = LayoutInflater.from(app.getApplicationContext());
        for (int i =0;i<card.size();) {
            final View view = inflater.inflate(R.layout.pay_addexist_item, null);
            final int idpostion = i;
            i++;
            SimpleDraweeView head = (SimpleDraweeView) view.findViewById(R.id.pay_addexist_item_image);
            Uri uri = Uri.parse(card.get(i));
            Log.e("IdPhoto",card.get(i));
            head.setImageURI(uri);
            i++;
            TextView tvname = (TextView) view.findViewById(R.id.pay_addexist_item_name);
            final String name = card.get(i);
            tvname.setText(name);
            i++;
            TextView tvjob = (TextView) view.findViewById(R.id.pay_addexist_item_job);
            final String job = card.get(i);
            tvjob.setText(job);
            i++;
            TextView tvcompany = (TextView) view.findViewById(R.id.pay_addexist_item_company);
            final String company = card.get(i);
            tvcompany.setText(company);
            i++;
            TextView tvphone = (TextView) view.findViewById(R.id.pay_addexist_item_phone);
            final String phone = card.get(i);
            tvphone.setText(phone);
            i++;
            final Contact contact = new Contact(name,phone,company,job);
            final CheckBox checkBox = (CheckBox) view.findViewById(R.id.pay_addexist_item_check);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()){
                        contacts.add(contact);
                    }else {
                        contacts.remove(contact);
                    }
                }
            });
            linearLayout.addView(view);
        }
    }

}
