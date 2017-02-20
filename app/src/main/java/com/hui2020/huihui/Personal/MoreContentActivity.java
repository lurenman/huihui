package com.hui2020.huihui.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.platform.comapi.map.E;
import com.hui2020.huihui.Location.utils.ToastUtils;
import com.hui2020.huihui.R;
import com.hui2020.huihui.StringUtil;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/2/17.
 */

public class MoreContentActivity extends AppCompatActivity implements View.OnClickListener{

    EditText et_content;
    RelativeLayout back;
    TextView tv_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morecontent);
        initui();

    }
 public void initui(){
     back=(RelativeLayout)findViewById(R.id.back);
     back.setOnClickListener(this);
     tv_confirm=(TextView)findViewById(R.id.tv_confirm);
     tv_confirm.setOnClickListener(this);
     et_content=(EditText)findViewById(R.id.et_content);

 }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.tv_confirm:
                if(StringUtil.editIsNull(et_content)){
                    ToastUtils.showToast(MoreContentActivity.this,"输入内容不能为空");
                }else {
                    setResult(RESULT_OK, new Intent().putExtra("content", et_content.getText().toString()));
                    finish();
                }
                break;

        }
    }
}
