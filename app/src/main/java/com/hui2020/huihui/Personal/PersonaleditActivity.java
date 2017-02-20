package com.hui2020.huihui.Personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hui2020.huihui.ChooseimageActivity;
import com.hui2020.huihui.Login.RegisteActivity;
import com.hui2020.huihui.Mdfive;
import com.hui2020.huihui.MyApplication;
import com.hui2020.huihui.R;
import com.hui2020.huihui.myiosdialog.ActionSheetDialog;

import org.feezu.liuli.timeselector.TimeSelector;
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

/**
 * Created by FD-GHOST on 2016/12/24.
 * This class is used to initialize each component in Personal information edit Activity
 * Incomplete, need to udate to new version and finish the user icon and information update with server
 */

public class PersonaleditActivity extends AppCompatActivity implements View.OnClickListener {
    private MyApplication app;
    private Activity activity;
    private RadioGroup genderIn;
    private ScrollView scrollView;
    private SimpleDraweeView icon, personaledit_companyinfo_image_a,personaledit_companyinfo_image_b,personaledit_companyinfo_image_c,intro_d;
    private EditText nickname, realname, company, job, website, mobile,qq, address, email;
    private TextView birthday,save;
    private InputMethodManager keybord;
    private RelativeLayout back;
    private String gender;
    private String imagea,imageb,imagec,imaged;
    private Intent intent;
    private Response response;
    private String result;
    private RelativeLayout personaledit_icon;




    private TextView fromlocal, fromcamera, confirm;
    private ImageView image;

    private static final int PHOTO_REQUEST_CAMERA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String CAMERA_FILE_NAME = "temp_camera.jpg";
    private File tempFile,tempFile1,tempFile2,tempFile3;
    private Bitmap bitmap,bitmap1,bitmap2,bitmap3;
    private String type="";
    private Response response1;
    private String result1="";
    String pth="";
    String pth1="";
    String pth2="";
    String pth3="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personaledit);
        app=(MyApplication)getApplication();
        activity=this;
        initcomponent();

    }

    private void initcomponent() {

        personaledit_icon=(RelativeLayout)findViewById(R.id.personaledit_icon);
        personaledit_icon.setOnClickListener(this);
        back = (RelativeLayout)findViewById(R.id.back);
        back.setOnClickListener(this);
        save = (TextView)findViewById(R.id.personaledit_save) ;
        save.setOnClickListener(this);
        icon = (SimpleDraweeView)findViewById(R.id.personaledit_icon_image);
        imagea=app.getAvatar();
        icon.setImageURI(Uri.parse(imagea));
        icon.setOnClickListener(this);
        keybord = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        realname = (EditText) findViewById(R.id.personaledit_realname_textb);
        realname.setOnClickListener(this);
        realname.setText(app.getRealName());
        realname.setSelection(app.getRealName().length());//将光标移至文字末尾
        nickname = (EditText) findViewById(R.id.personaledit_nickname_textb);
        nickname.setOnClickListener(this);
        nickname.setText(app.getNickName());
        nickname.setSelection(app.getNickName().length());//将光标移至文字末尾
        birthday = (TextView) findViewById(R.id.personaledit_birthday_textb);
        birthday.setOnClickListener(this);
        birthday.setText("                   "+app.getBirthday());
        company = (EditText) findViewById(R.id.personaledit_company_textb);
        company.setOnClickListener(this);
        company.setText(app.getCompany());
        company.setSelection(app.getCompany().length());//将光标移至文字末尾
        job = (EditText) findViewById(R.id.personaledit_job_textb);
        job.setOnClickListener(this);
        job.setText(app.getJob());
        job.setSelection(app.getJob().length());//将光标移至文字末尾
        website = (EditText) findViewById(R.id.personaledit_website_textb);
        website.setOnClickListener(this);
        website.setText(app.getWebsite());
        website.setSelection(app.getWebsite().length());//将光标移至文字末尾
        mobile = (EditText) findViewById(R.id.personaledit_mobile_textb);
        mobile.setOnClickListener(this);
        mobile.setText(app.getPhone());
        mobile.setSelection(app.getPhone().length());//将光标移至文字末尾
        qq = (EditText) findViewById(R.id.personaledit_qq_textb);
        qq.setOnClickListener(this);
        qq.setText(app.getQq());
        qq.setSelection(app.getQq().length());//将光标移至文字末尾
        address = (EditText) findViewById(R.id.personaledit_address_textb);
        address.setOnClickListener(this);
        address.setText(app.getAddress());
        address.setSelection(app.getAddress().length());//将光标移至文字末尾
        email = (EditText) findViewById(R.id.personaledit_email_textb);
        email.setOnClickListener(this);
        email.setText(app.getEmail());
        email.setSelection(app.getEmail().length());//将光标移至文字末尾
        personaledit_companyinfo_image_a = (SimpleDraweeView)findViewById(R.id.personaledit_companyinfo_image_a);
        imageb=app.getCompanyinfo_a();
        personaledit_companyinfo_image_a.setImageURI(Uri.parse(imageb));
        personaledit_companyinfo_image_a.setOnClickListener(this);
        personaledit_companyinfo_image_b = (SimpleDraweeView)findViewById(R.id.personaledit_companyinfo_image_b);
        imagec=app.getCompanyinfo_b();
        personaledit_companyinfo_image_b.setImageURI(Uri.parse(imagec));
        personaledit_companyinfo_image_b.setOnClickListener(this);
        personaledit_companyinfo_image_c = (SimpleDraweeView)findViewById(R.id.personaledit_companyinfo_image_c);
        imaged=app.getCompanyinfo_c();
        personaledit_companyinfo_image_c.setImageURI(Uri.parse(imaged));
        personaledit_companyinfo_image_c.setOnClickListener(this);

        scrollView = (ScrollView) findViewById(R.id.personaledit_scroll);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_MOVE == motionEvent.getAction()) {
                    nickname.setFocusable(false);
                    realname.setFocusable(false);
                    //gender.setFocusable(false);
                    //birthday.setFocusable(false);
                    company.setFocusable(false);
                    job.setFocusable(false);
                    website.setFocusable(false);
                    mobile.setFocusable(false);
                    qq.setFocusable(false);
                    address.setFocusable(false);
                    email.setFocusable(false);
                    keybord.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                }
                return false;
            }
        });
        genderIn = (RadioGroup) findViewById(R.id.personaledit_gender_selecter);
        gender=app.getGender();
        if(gender.equals("0")){
            RadioButton male = (RadioButton)findViewById(R.id.personaledit_gender_male);
            male.setChecked(true);
        }else if(gender.equals("1")){
            RadioButton female = (RadioButton)findViewById(R.id.personaledit_gender_female);
            female.setChecked(true);
        }
        genderIn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                nickname.setFocusable(false);
                realname.setFocusable(false);
                company.setFocusable(false);
                job.setFocusable(false);
                website.setFocusable(false);
                mobile.setFocusable(false);
                qq.setFocusable(false);
                address.setFocusable(false);
                email.setFocusable(false);
                if (checkedId == R.id.personaledit_gender_male) {
                    gender = "0";

                } else if (checkedId == R.id.personaledit_gender_female) {
                    gender = "1";

                }
            }
        });

        tempFile = new File(Environment.getExternalStorageDirectory(),
                PHOTO_FILE_NAME);
        tempFile1 = new File(Environment.getExternalStorageDirectory(),
                PHOTO_FILE_NAME);
        tempFile2 = new File(Environment.getExternalStorageDirectory(),
                PHOTO_FILE_NAME);
        tempFile3 = new File(Environment.getExternalStorageDirectory(),
                PHOTO_FILE_NAME);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.personaledit_save:
                new Update().execute();
                break;
            case R.id.personaledit_icon:
                type="0";
               new ActionSheetDialog(PersonaleditActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("本地图库",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromlocal();
                                    }
                                })
                        .addSheetItem("拍照选取",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromcamera();
                                    }
                                })

                        .show();
                break;


            case R.id.personaledit_icon_image:
              /*  intent = new Intent(getApplicationContext(), ChooseimageActivity.class);
                intent.putExtra("type","icon");
                startActivityForResult(intent,0);*/

                break;
            case R.id.personaledit_nickname_textb:
                nickname.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_realname_textb:
                realname.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_birthday_textb:
                TimeSelector timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
                        birthday.setText(time.substring(0,10));
                    }
                }, "1900-01-01 00:00", "2099-12-31 23:59");
                timeSelector.setMode(TimeSelector.MODE.YMD);
                timeSelector.show();
                break;
            case R.id.personaledit_company_textb:
                company.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_job_textb:
                job.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_website_textb:
                website.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_mobile_textb:
                mobile.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_qq_textb:
                qq.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_address_textb:
                address.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_email_textb:
                email.setFocusableInTouchMode(true);
                break;
            case R.id.personaledit_companyinfo_image_a:
                /*intent = new Intent(getApplicationContext(), ChooseimageActivity.class);
                intent.putExtra("type","intro");
                startActivityForResult(intent,1);*/
                type="1";
                new ActionSheetDialog(PersonaleditActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("本地图库",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromlocal();
                                    }
                                })
                        .addSheetItem("拍照选取",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromcamera();
                                    }
                                })

                        .show();
                break;
            case R.id.personaledit_companyinfo_image_b:
               /* intent = new Intent(getApplicationContext(), ChooseimageActivity.class);
                intent.putExtra("type","intro");
                startActivityForResult(intent,2);*/
                type="2";
                new ActionSheetDialog(PersonaleditActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("本地图库",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromlocal();
                                    }
                                })
                        .addSheetItem("拍照选取",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromcamera();
                                    }
                                })

                        .show();
                break;
            case R.id.personaledit_companyinfo_image_c:
             /*   intent = new Intent(getApplicationContext(), ChooseimageActivity.class);
                intent.putExtra("type","intro");
                startActivityForResult(intent,3);*/
                type="3";
                new ActionSheetDialog(PersonaleditActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("本地图库",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromlocal();
                                    }
                                })
                        .addSheetItem("拍照选取",
                                ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Fromcamera();
                                    }
                                })

                        .show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 0 && resultCode == RESULT_OK) {
            imagea=data.getStringExtra("path");
            icon.setImageURI(Uri.parse(imagea));
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageb=data.getStringExtra("path");
            intro_a.setImageURI(Uri.parse(imageb));
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            imagec=data.getStringExtra("path");
            intro_b.setImageURI(Uri.parse(imagec));
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            imaged=data.getStringExtra("path");
            intro_c.setImageURI(Uri.parse(imaged));
        }


*/


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
                if(type.equals("0")) {
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile)));
                    icon.setImageBitmap(bitmap);
                    new UploadFile().execute();
                }else if(type.equals("1")){
                    bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile1)));
                    personaledit_companyinfo_image_a.setImageBitmap(bitmap1);
                    new UploadFile().execute();
                }else if(type.equals("2")){
                    bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile2)));
                    personaledit_companyinfo_image_b.setImageBitmap(bitmap2);
                    new UploadFile().execute();
                }
                else if(type.equals("3")){
                    bitmap3 = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.fromFile(tempFile3)));
                    personaledit_companyinfo_image_c.setImageBitmap(bitmap3);
                    new UploadFile().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);




    }


    private class Update extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                UpdatePOST();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            JSONObject jsonobj = null;
            try {
                jsonobj = new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "更新失败1", Toast.LENGTH_SHORT).show();
                return;
            }
            String state = jsonobj.optString("res");
            if (state.equals("0")) {
                Toast.makeText(getApplicationContext(), "更新失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "更新成功", Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        }

    }

    public void UpdatePOST() throws IOException {
        String newpassword = null;
        try {
           newpassword = new Mdfive().Mdfive(app.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        OkHttpClient client = new OkHttpClient();
        FormBody body = new FormBody.Builder()
                .add("userId",app.getUserId())
                .add("userName",app.getUserName())
                .add("pwd",newpassword)
                .add("realName",realname.getText().toString())
                .add("nickName",nickname.getText().toString())
              /*  .add("avatar",imagea)*/
                .add("avatar",pth)
                .add("sex",gender)
                .add("birthday",birthday.getText().toString().replaceAll("-",""))
                .add("location",app.getLocation())
                .add("mobilePhone",mobile.getText().toString())
                .add("qq",qq.getText().toString())
                .add("email",email.getText().toString())
                .add("website",website.getText().toString())
                .add("address",address.getText().toString())
                .add("job",job.getText().toString())
                .add("compinfo","")
                .add("weixin","")
                .add("company",company.getText().toString())
                .add("ipaddr","0.0.0.0")
                .add("ip_location","NULL")
                .add("aid","0000000")
                .add("ispush","0")
                .add("ishide","0")
            /*    .add("infoPic1",imageb)
                .add("infoPic2",imagec)
                .add("infoPic3",imaged)*/
                .add("infoPic1",pth1)
                .add("infoPic2",pth2)
                .add("infoPic3",pth3)
                .build();

        Request request = new Request.Builder()
                .url("http://m.hui2020.com/server/m.php?act=updUserInfo")
                .post(body)
                .build();
        response=null;
        response  = client.newCall(request).execute();

        if (response.isSuccessful()) {
            result = response.body().string();
            Log.e("UpdatePOST",result);
        } else {
            throw new IOException("Unexpected code " + response);
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


    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 图片比例和大小
        if(type.equals("0")) {
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
                if(type.equals("0")){
                    UploadfilePost(tempFile);
                }else if(type.equals("1")){
                    UploadfilePost(tempFile1);
                }
                else if(type.equals("2")){
                    UploadfilePost(tempFile2);
                }
                else if(type.equals("3")){
                    UploadfilePost(tempFile3);
                }

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
            if(result1.equals("error")) {
                Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                /*Intent intent = getIntent();
                intent.putExtra("path", "http://img.hui2020.com"+result1);
                activity.setResult(RESULT_OK,intent);
                boolean delete = tempFile.delete();

                System.out.println("delete = " + delete);
                activity.finish();*/
                if(type.equals("0")){
                    pth="http://img.hui2020.com"+result1;
                }else if(type.equals("1")){
                    pth1="http://img.hui2020.com"+result1;
                }else if(type.equals("2")){
                    pth2="http://img.hui2020.com"+result1;
                }else if(type.equals("3")){
                    pth3="http://img.hui2020.com"+result1;
                }
            }
        }
    }

    public void UploadfilePost(File p ) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(type.equals("0")){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }else if(type.equals("1")){
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, out);}
        else if(type.equals("2")){
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, out);}
        else if(type.equals("3")){
            bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }

        out.flush();
        out.close();
        byte[] buffer = out.toByteArray();
        byte[] encode = Base64.encode(buffer, Base64.DEFAULT);
        String photo = new String(encode);

        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if(p != null){
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), p);
            String filename = p.getName();
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("myFile", p.getName(), body);
        }
        requestBody.addFormDataPart("imgType","1");
        Request request = new Request.Builder()
                .url("http://ht.hui2020.com/api/image/upload.action")
                .post(requestBody.build())
                .build();
        response1=null;
        response1  = client.newCall(request).execute();

        if (response1.isSuccessful()) {
            result1 = response1.body().string();
            Log.e("Upload",result1);
        } else {
            throw new IOException("Unexpected code " + response1);
        }
    }











}
