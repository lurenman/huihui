package com.hui2020.huihui.Card;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2017/1/14.
 * This is for read and show the cards list
 */

public class PrepareCards extends AsyncTask<Void, Void, Boolean> {
    private MyApplication app;
    private Activity activity;
    private LinearLayout linearLayout;
    private ArrayList<String> card;

    public PrepareCards(MyApplication myApplication, Activity activity, LinearLayout linearLayout){
        this.app=myApplication;
        this.activity=activity;
        this.linearLayout=linearLayout;
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
            final View view = inflater.inflate(R.layout.card_item_card_card, null);
            final int idpostion = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(app.getApplicationContext(), CarddetailActivity.class);
                    intent.putExtra("userId", card.get(idpostion));
                    activity.startActivity(intent);
                    //Toast.makeText(context,card.get(idpostion),Toast.LENGTH_SHORT).show();
                }
            });
            i++;

            SimpleDraweeView head = (SimpleDraweeView) view.findViewById(R.id.card_item_card_card_image);
            Uri uri = Uri.parse(card.get(i));
            Log.e("IdPhoto",card.get(i));
            head.setImageURI(uri);
            i++;
            TextView name = (TextView) view.findViewById(R.id.card_item_card_card_name);
            name.setText(card.get(i));
            i++;
            TextView job = (TextView) view.findViewById(R.id.card_item_card_card_job);
            job.setText(card.get(i));
            i++;
            TextView company = (TextView) view.findViewById(R.id.card_item_card_card_company);
            company.setText(card.get(i));
            i++;
            TextView phone = (TextView) view.findViewById(R.id.card_item_card_card_phone);
            phone.setText(card.get(i));
            i++;

            linearLayout.addView(view);
        }
    }

}
