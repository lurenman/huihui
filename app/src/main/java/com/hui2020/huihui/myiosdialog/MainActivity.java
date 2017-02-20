package com.hui2020.huihui.myiosdialog;/*
package views.myiosdialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.baikeng.myclient.R;


*/
/**
 * package:example.com.showdialog
 * name:MainActivity.java
 * Write by Jimmy.li
 * Date:2016/4/24 16:39
 *//*

public class MainActivity extends Activity implements View.OnClickListener {
    private Button btn1, btn2, btn3, btn4, btn5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                new ActionSheetDialog(MainActivity.this)
                        .builder()
                        .setTitle("娓呯┖娑堟伅鍒楄〃鍚庯紝鑱婂ぉ璁板綍渚濈劧淇濈暀锛岀‘瀹氳娓呯┖娑堟伅鍒楄〃锛�")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("娓呯┖娑堟伅鍒楄〃", ActionSheetDialog.SheetItemColor.Red
                                , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //濉啓浜嬩欢
                            }
                        }).show();
                break;
            case R.id.btn2:
                new ActionSheetDialog(MainActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("鍙戦�佺粰濂藉弸",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //濉啓浜嬩欢
                                    }
                                })
                        .addSheetItem("杞浇鍒扮┖闂寸浉鍐�",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //濉啓浜嬩欢
                                    }
                                })
                        .addSheetItem("涓婁紶鍒扮兢鐩稿唽",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //濉啓浜嬩欢
                                    }
                                })
                        .addSheetItem("淇濆瓨鍒版墜鏈�",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        //濉啓浜嬩欢
                                    }
                                }).show();
                break;
            case R.id.btn3:
                new ActionSheetDialog(MainActivity.this)
                        .builder()
                        .setTitle("濂藉弸鍒楄〃")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("鍒犻櫎濂藉弸", ActionSheetDialog.SheetItemColor.Red
                                , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //濉啓浜嬩欢
                            }
                        })
                        .addSheetItem("澧炲姞濂藉弸", ActionSheetDialog.SheetItemColor.Blue
                                , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //濉啓浜嬩欢
                            }
                        })
                        .addSheetItem("澶囨敞", ActionSheetDialog.SheetItemColor.Blue
                                , new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                //濉啓浜嬩欢
                            }
                        }).show();
                break;
            case R.id.btn4:

            case R.id.btn5:

                break;
        }
    }
}
*/
