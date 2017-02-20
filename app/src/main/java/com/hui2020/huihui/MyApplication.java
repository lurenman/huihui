package com.hui2020.huihui;

import android.app.Application;
import android.database.Cursor;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2016/12/19.
 * Global variable
 * remember to read all information when app onResume
 * Fresco.initialize(this); will be called here to ensure it has been called only once
 * Note: Duplicate call will cause to OOM!!!!!
 */

public class MyApplication extends Application {
    private boolean sign = false;
    private String userId = "";
    private String userName = "";
    private String password = "";
    private String nickName = "";
    private String realName = "";
    private String avatar = "";
    private String gender = "";
    private String birthday = "";
    private String location = "";
    private String address = "";
    private String phone = "";
    private String qq = "";
    private String wechat = "";
    private String email = "";
    private String job = "";
    private String company = "";
    private String companyinfo = "";
    private String website = "";
    private String companyinfo_a = "";
    private String companyinfo_b = "";
    private String companyinfo_c = "";
    private String ispush = "";
    private String ishide = "";
    private ArrayList<String> user;
    private ArrayList<String> attention;
    private ArrayList<String> collection;
    private ArrayList<String> friend;
    private ArrayList<String> friendLst;
    private String filename = "User";
    private UserInfoDB userInfoDB;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        userInfoDB = new UserInfoDB(getApplicationContext());
        user = new ArrayList<String>();
        attention = new ArrayList<String>();
        collection = new ArrayList<String>();
        friend = new ArrayList<String>();
        friendLst = new ArrayList<String>();
        userInfoDB.getUserinfo(user); //To fill the user arraylist, then to avoid null pointer exception
        if(user.size()!=0) {
            this.sign=true;
            this.userId = user.get(0);
            this.userName = user.get(1);
            this.password = user.get(2);
            this.nickName = user.get(3);
            this.realName = user.get(4);
            this.avatar = user.get(5);
            this.gender = user.get(6);
            this.birthday = user.get(7);
            this.location = user.get(8);
            this.address = user.get(9);
            this.phone = user.get(10);
            this.qq = user.get(11);
            this.wechat = user.get(12);
            this.email = user.get(13);
            this.job = user.get(14);
            this.company = user.get(15);
            this.companyinfo = user.get(16);
            this.website = user.get(17);
            this.companyinfo_a = user.get(18);
            this.companyinfo_b = user.get(19);
            this.companyinfo_c = user.get(20);;
            this.ispush = user.get(21);
            this.ishide = user.get(22);
        }
        userInfoDB.getUserAttention(attention);
        userInfoDB.getUserCollection(collection);
        userInfoDB.getUserFriend(friend); //TODO If necessary, the full details should be call when the "card" activity has been called
        userInfoDB.getUserFriendId(friendLst);
    }



    public void iniApp(String userId, String userName, String password, String nickName, String realName,
                       String avatar, String gender, String birthday, String location, String address,
                       String phone, String qq, String wechat, String email, String job, String company,
                       String companyinfo, String website, String companyinfo_a, String companyinfo_b,
                       String companyinfo_c, String ishide, String ispush) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.nickName = nickName;
        this.realName = realName;
        this.avatar = avatar;
        this.gender = gender;
        this.birthday = birthday;
        this.location = location;
        this.address = address;
        this.phone = phone;
        this.qq = qq;
        this.wechat = wechat;
        this.email = email;
        this.job = job;
        this.company = company;
        this.companyinfo = companyinfo;
        this.website = website;
        this.companyinfo_a=companyinfo_a;
        this.companyinfo_b=companyinfo_b;
        this.companyinfo_c=companyinfo_c;
        this.ishide = ishide;
        this.ispush = ispush;
        userInfoDB.cleanUserinfo();
        userInfoDB.addUserinfo(userId,userName,password,nickName,realName,avatar,gender,birthday,
                location,address,phone,qq,wechat, email,job,company,companyinfo,website,
                companyinfo_a, companyinfo_b, companyinfo_c, ishide,ispush);
    }

    public boolean getSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompanyinfo() {
        return companyinfo;
    }

    public void setCompanyinfo(String companyinfo) {
        this.companyinfo = companyinfo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyinfo_a() {
        return companyinfo_a;
    }

    public void setCompanyinfo_a(String companyinfo_a) {
        this.companyinfo_a = companyinfo_a;
    }

    public String getCompanyinfo_b() {
        return companyinfo_b;
    }

    public void setCompanyinfo_b(String companyinfo_b) {
        this.companyinfo_b = companyinfo_b;
    }

    public String getCompanyinfo_c() {
        return companyinfo_c;
    }

    public void setCompanyinfo_c(String companyinfo_c) {
        this.companyinfo_c = companyinfo_c;
    }

    public String getIspush() {
        return ispush;
    }

    public void setIspush(String ispush) {
        this.ispush = ispush;
    }

    public String getIshide() {
        return ishide;
    }

    public void setIshide(String ishide) {
        this.ishide = ishide;
    }

    public UserInfoDB getUserInfoDB() {
        return userInfoDB;
    }

    public ArrayList<String> getAttention() {
        return attention;
    }

    public void upDateAttention() {
        this.attention = attention;
    }

    public ArrayList<String> getCollection() {
        return collection;
    }

    public void upDateCollection() {
        this.collection = collection;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
}
