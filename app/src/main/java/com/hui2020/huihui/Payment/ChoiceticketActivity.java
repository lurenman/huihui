package com.hui2020.huihui.Payment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Interaction;
import com.hui2020.huihui.MeetDetail.MeetingdetailActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.util.ArrayList;

public class ChoiceticketActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout back;
    private String actId;
    private LinearLayout linearLayout;
    private ArrayList<String> choice;
    private RelativeLayout rcollect;
    private TextView pay;
    private ImageView ivcollect;
    private SimpleDraweeView image;
    private MyApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choiceticket);
        initComponent();
        app = (MyApplication) getApplication();
        image = (SimpleDraweeView) findViewById(R.id.choiceticket_image);
        Uri uri = Uri.parse(getIntent().getStringExtra("image"));
        image.setImageURI(uri);
        ivcollect = (ImageView) findViewById(R.id.choiceticket_feet_collect_image);
        if (app.getCollection().contains(actId)) {
            ivcollect.setImageResource(R.drawable.collect_b);
        } else {
            ivcollect.setImageResource(R.drawable.collect_a);
        }
        new GetChoiceTicket(this, getApplicationContext(), linearLayout, choice, app.getUserName(),
                app.getPassword(), actId, getIntent().getStringExtra("title")).execute();
    }

    private void initComponent() {
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        linearLayout = (LinearLayout) findViewById(R.id.choiceticket_lin);
        pay = (TextView) findViewById(R.id.choiceticket_feet_pay);
        pay.setOnClickListener(this);
        actId = getIntent().getStringExtra("actId");
        rcollect = (RelativeLayout) findViewById(R.id.choiceticket_feet_collect);
        rcollect.setOnClickListener(this);
        choice = new ArrayList<String>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;

            case R.id.choiceticket_feet_pay:
                if (choice.size() > 0) {
                    Toast.makeText(getApplicationContext(), choice.get(0), Toast.LENGTH_SHORT).show();//Ticket Id
                    Toast.makeText(getApplicationContext(), getIntent().getStringExtra("title"), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), choice.get(1), Toast.LENGTH_SHORT).show();//Date
                    Toast.makeText(getApplicationContext(), choice.get(2), Toast.LENGTH_SHORT).show();//Address
                    Toast.makeText(getApplicationContext(), choice.get(3), Toast.LENGTH_SHORT).show();//type
                    Toast.makeText(getApplicationContext(), choice.get(4), Toast.LENGTH_SHORT).show();//price
                    Toast.makeText(getApplicationContext(), choice.get(5), Toast.LENGTH_SHORT).show();//remain
                    Intent intent = new Intent(getApplicationContext(),PayActivity.class);
                    intent.putExtra("ticketId",choice.get(0));
                    intent.putExtra("title",getIntent().getStringExtra("title"));
                    intent.putExtra("date",choice.get(1));
                    intent.putExtra("address",choice.get(2));
                    intent.putExtra("type",choice.get(3));
                    intent.putExtra("price",choice.get(4));
                    intent.putExtra("remain",choice.get(5));
                    startActivity(intent);
                }
                break;
            case R.id.choiceticket_feet_collect:
                if (app.getCollection().contains(actId)) {
                    new Interaction(app, 3, app.getUserName(), app.getPassword(), app.getUserId(),
                            app.getRealName(), actId, null, null, ivcollect).execute();
                } else {
                    new Interaction(app, 2, app.getUserName(), app.getPassword(), app.getUserId(),
                            app.getRealName(), actId, null, null, ivcollect).execute();
                }

                break;
        }
    }
}
