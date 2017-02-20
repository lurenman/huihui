package com.hui2020.huihui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.hui2020.huihui.Card.CardActivity;
import com.hui2020.huihui.Home.HomeActivity;
import com.hui2020.huihui.Personal.PersonalActivity;
import com.hui2020.huihui.Scence.ScenceActivity;


/**
 * Created by FD-GHOST on 2016/11/30.
 * This class is to initialize the feet button
 */

public class FeetButtonInitialize {
    private Activity mainActivity;
    private LinearLayout hy;
    private LinearLayout hd;
    private LinearLayout gz;
    private LinearLayout wd;
    private Intent intent;

    public FeetButtonInitialize(Activity mainActivity){

        this.mainActivity = mainActivity;
        hy = (LinearLayout) mainActivity.findViewById(R.id.app_feet_discover);
        hd = (LinearLayout) mainActivity.findViewById(R.id.app_feet_activity);
        gz = (LinearLayout) mainActivity.findViewById(R.id.app_feet_card);
        wd = (LinearLayout) mainActivity.findViewById(R.id.app_feet_my);
        setbehaviour();
    }

    private void setbehaviour(){
        hy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int start = 0;
                String a = mainActivity.getLocalClassName();
                String b = "";
                for(int i=0;i<a.length();i++){
                    if(a.charAt(i)=='.'){
                        start = i;
                    }
                }
                for(int i=start+1;i<a.length();i++){
                    b += a.charAt(i);
                }
                if(b.equals("HomeActivity")){

                }else {
                    intent = new Intent(mainActivity, HomeActivity.class);
                    mainActivity.startActivity(intent);
                    mainActivity.finish();
                }
            }
        });
        hd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int start = 0;
                String a = mainActivity.getLocalClassName();
                String b = "";
                for(int i=0;i<a.length();i++){
                    if(a.charAt(i)=='.'){
                        start = i;
                    }
                }
                for(int i=start+1;i<a.length();i++){
                    b += a.charAt(i);
                }
                if(b.equals("ScenceActivity")){

                }else {
                    intent = new Intent(mainActivity, ScenceActivity.class);
                    mainActivity.startActivity(intent);
                    mainActivity.finish();
                }
            }
        });
        gz.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int start = 0;
                String a = mainActivity.getLocalClassName();
                String b = "";
                for(int i=0;i<a.length();i++){
                    if(a.charAt(i)=='.'){
                        start = i;
                    }
                }
                for(int i=start+1;i<a.length();i++){
                    b += a.charAt(i);
                }
                if(b.equals("CardActivity")){

                }else {
                    intent = new Intent(mainActivity, CardActivity.class);
                    mainActivity.startActivity(intent);
                    mainActivity.finish();
                }
            }
        });



        wd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int start = 0;
                String a = mainActivity.getLocalClassName();
                String b = "";
                for(int i=0;i<a.length();i++){
                    if(a.charAt(i)=='.'){
                        start = i;
                    }
                }
                for(int i=start+1;i<a.length();i++){
                    b += a.charAt(i);
                }
                if(b.equals("PersonalActivity")){

                }else {
                    intent = new Intent(mainActivity, PersonalActivity.class);
                    mainActivity.startActivity(intent);
                    mainActivity.finish();
                }
            }
        });
    }

}
