package com.hui2020.huihui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.Login.RegisteActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.attr.bitmap;

public class ChooseimageActivity extends AppCompatActivity implements View.OnClickListener{
    private MyApplication app;
    private Activity activity;

    private RelativeLayout back;
    private TextView fromlocal, fromcamera, confirm;
    private ImageView image;

    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String CAMERA_FILE_NAME = "temp_camera.jpg";
    private File tempFile;
    private Bitmap bitmap;

    private String type;

    private Response response;
    private String result="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseimage);
        app = (MyApplication)getApplication();
        activity=this;
        type = getIntent().getStringExtra("type");
        tempFile = new File(Environment.getExternalStorageDirectory(),
                PHOTO_FILE_NAME);
        initcomponent();
    }

    private void initcomponent() {

        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        fromlocal = (TextView)findViewById(R.id.chooseimage_fromlocal);
        fromlocal.setOnClickListener(this);
        fromcamera = (TextView)findViewById(R.id.chooseimage_fromcamera);
        fromcamera.setOnClickListener(this);
        confirm = (TextView)findViewById(R.id.chooseimage_confirme);
        confirm.setOnClickListener(this);
        image = (ImageView)findViewById(R.id.chooseimage_preview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.chooseimage_fromlocal:
                Fromlocal();
                break;
            case R.id.chooseimage_fromcamera:
                Fromcamera();
                break;
            case R.id.chooseimage_confirme:
                new UploadFile().execute();
                //finish();
                break;
        }
    }

    public void Fromlocal(){
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
    public void Fromcamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Environment
                            .getExternalStorageDirectory(), CAMERA_FILE_NAME)));
        }
        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            if (hasSdcard()) {
                crop(Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), CAMERA_FILE_NAME)));
            } else {
                Toast.makeText(getApplicationContext(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
//                bitmap = data.getParcelableExtra("data");
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile)));
                image.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 图片比例和大小
        if(type.equals("icon")) {
            intent.putExtra("aspectX", 160);
            intent.putExtra("aspectY", 160);
            intent.putExtra("outputX", 160);
            intent.putExtra("outputY", 160);
        }else{
            intent.putExtra("aspectX", 360);
            intent.putExtra("aspectY", 211);
            intent.putExtra("outputX", 360);
            intent.putExtra("outputY", 211);
        }
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
//        startActivityForResult(intent, PHOTO_REQUEST_CUT);

        Uri uritempFile = Uri.fromFile(tempFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    private class UploadFile extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                UploadfilePost();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            if(result.equals("error")) {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                Intent intent = getIntent();
                intent.putExtra("path", "http://img.hui2020.com"+result);
                activity.setResult(RESULT_OK,intent);
                boolean delete = tempFile.delete();
                System.out.println("delete = " + delete);
                activity.finish();
            }
        }
    }

    public void UploadfilePost() throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        out.flush();
        out.close();
        byte[] buffer = out.toByteArray();
        byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
        String photo = new String(encode);

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(tempFile != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), tempFile);
            String filename = tempFile.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("myFile", tempFile.getName(), body);
        }
        requestBody.addFormDataPart("imgType","1");
        Request request = new Request.Builder()
                .url("http://ht.hui2020.com/api/image/upload.action")
                .post(requestBody.build())
                .build();
        response=null;
        response  = client.newCall(request).execute();

        if (response.isSuccessful()) {
            result = response.body().string();
            Log.e("Upload",result);
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }


}
