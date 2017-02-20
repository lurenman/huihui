package com.hui2020.huihui.Scence;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.hui2020.huihui.FeetButtonInitialize;
import com.hui2020.huihui.Login.LoginActivity;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by FD-GHOST on 2016/12/24.
 * This class is used to initialize each component in the Main Scence Activity
 */

public class ScenceActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private TextView textView;
    private Flag flag;
    private float y1 = 0,y2 = 0;
    private int from, size;
    private GetScenceData getScenceData;
    private MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scence);
        app =(MyApplication) getApplication();
        if(app.getSign()!=true){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            new FeetButtonInitialize(this);
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            textView = (TextView) findViewById(R.id.scence_currentdate);
            textView.setText(str);
            flag = new Flag();

            ImageView iv = (ImageView) findViewById(R.id.feet_activity_a);
            iv.setImageResource(R.drawable.feet_activity_b);
            linearLayout = (LinearLayout) findViewById(R.id.scence_linear);
            from = 0;
            size = 5;
            getScenceData = new GetScenceData(this, getApplicationContext(), linearLayout, Integer.toString(from), Integer.toString(size), app.getUserName(), app.getPassword(), flag);
            getScenceData.execute();
            scrollView = (ScrollView) findViewById(R.id.scence_scroll);
            scrollView.setOnTouchListener(new ScrollViewListner());
        }
    }

    private class ScrollViewListner implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    y1 = motionEvent.getY();
                    Log.e("Y",view.getScrollY()+"");
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = scrollView.getChildAt(0).getMeasuredHeight();
                    Log.e("Position", (y2-y1)+"");
                    if ((scrollY + height-view.getPaddingTop()-view.getPaddingBottom()) == scrollViewMeasuredHeight) {
                        Toast.makeText(ScenceActivity.this, "bot", Toast.LENGTH_SHORT).show();
                            Log.e("Flag", flag.getFlag()+"");
                            if(flag.getFlag()==true){
                                from += getScenceData.getSize();
                                getScenceData = new GetScenceData(ScenceActivity.this, getApplicationContext(),linearLayout, Integer.toString(from),Integer.toString(size),"18611112222", "123456", flag);
                                getScenceData.execute();

                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    y2 = motionEvent.getY();
                    Log.e("Y2", "Y1"+y1+"Y2:"+y2);
                    break;

                default:
                    break;
            }
            return false;
        }

    }
}
