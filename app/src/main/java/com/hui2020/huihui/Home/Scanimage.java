package com.hui2020.huihui.Home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hui2020.huihui.R;
import com.hui2020.huihui.zxing.android.CaptureActivity;


public class Scanimage extends AppCompatActivity implements View.OnClickListener {

    private ImageButton back;
    Intent intent;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";


    private static final int REQUEST_CODE_SCAN = 0x0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanimage);
        initView();

    }

    private void initView() {
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(this);
        intent = new Intent(Scanimage.this, CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SCAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(DECODED_CONTENT_KEY);
                Bitmap bitmap = data.getParcelableExtra(DECODED_BITMAP_KEY);

//                //resultTv.setText("解码结果： \n" + content);
                Intent intent = getIntent();
                intent.putExtra("codedContent", content);
//                setResult(RESULT_OK, intent);
//                finish();
////                intent = new Intent(Scanimage.this, NewfriendActivity.class);
//                Scanimage.this.startActivity(intent);
                this.setResult(2,intent);
                this.finish();

            }
        } else {
            finish();
        }
    }
}
