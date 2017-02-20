package com.hui2020.huihui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by FD-GHOST on 2017/1/9.
 * This class is for read & save user-related information to local database
 */

public class UserInfoDB extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "UserInfoDB";
    private final static int DATABASE_VERSION = 1;
    private final static String USERINFO = "USERINFO";
    private final static String USERCOLLECTION = "USERCOLLECTION";
    private final static String USERATTENTION = "USERATTENTION";
    private final static String USERFRIEND = "USERFRIEND";

    public UserInfoDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String creatUserInfo = "create table USERINFO(userId varchar(20) primary key, " +
                "userName varchar(20), password varchar(20), nickName varchar(20), realName varchar(20), " +
                "avatar varchar(300), gender varchar(2), birthday varchar(20), location varchar(20), " +
                "address varchar(200),phone varchar(20),qq varchar(20),wechat varchar(20),email varchar(50)," +
                "job varchar(50),company varchar(50),companyinfo varchar(200),website varchar(100)," +
                "companyimagea varchar(200), companyimageb varchar(200), companyimagec varchar(200)," +
                "ispush varchar(2), ishide varchar(2))";
        String creatUserCollection  = "create table USERCOLLECTION(id varchar(20))";
        String creatUserAttention  = "create table USERATTENTION(id varchar(20))";
        String creatUserFriends  = "create table USERFRIEND(userId varchar(20) primary key, " +
                "avatar varchar(300),realName varchar(20),job varchar(50),company varchar(100),phone varchar(20))";
        sqLiteDatabase.execSQL(creatUserInfo);
        sqLiteDatabase.execSQL(creatUserAttention);
        sqLiteDatabase.execSQL(creatUserCollection);
        sqLiteDatabase.execSQL(creatUserFriends);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * For userinfo
     */
    //Clean userinfo
    public void cleanUserinfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(USERINFO,null,null);
        db.close();
    }

    //Add userinfo
    public void addUserinfo(String userId, String userName, String password, String nickName, String realName,
                            String avatar, String gender, String birthday, String location, String address,
                            String phone, String qq, String wechat, String email, String job, String company,
                            String companyinfo, String website, String companyimagea, String companyimageb,
                            String companyimagec,String ishide,String ispush) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("userName", userName);
        values.put("password", password);
        values.put("nickName", nickName);
        values.put("realName", realName);
        values.put("avatar", avatar);
        values.put("gender", gender);
        values.put("birthday", birthday);
        values.put("location", location);
        values.put("address", address);
        values.put("phone", phone);
        values.put("qq", qq);
        values.put("wechat", wechat);
        values.put("email", email);
        values.put("job", job);
        values.put("company", company);
        values.put("companyinfo", companyinfo);
        values.put("website", website);
        values.put("companyimagea", companyimagea);
        values.put("companyimageb", companyimageb);
        values.put("companyimagec", companyimagec);
        values.put("ishide", ishide);
        values.put("ispush", ispush);
        db.insert(USERINFO, null, values);
        db.close();
    }

    //Get userinfo
    public void getUserinfo(ArrayList<String> arrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERINFO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("userId")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("userName")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("password")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("nickName")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("realName")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("avatar")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("gender")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("birthday")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("location")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("address")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("phone")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("qq")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("wechat")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("email")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("job")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("company")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("companyinfo")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("website")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("companyimagea")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("companyimageb")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("companyimagec")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("ishide")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("ispush")));
        }
        cursor.close();
        db.close();

    }

    //Update userinfo
    public void updateUserinfo(String userId, String what, String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(what, value);
        String whereClause = "userId=?";
        String[] whereArgs = new String[] { String.valueOf(userId) };
        db.update(USERINFO, values, whereClause, whereArgs);
        db.close();
    }



    /**
     * For userAttention
     */
    //Clean userAttention
    public void cleanUserAttention() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(USERATTENTION,null,null);
        db.close();
    }

    //Get userAttention
    public void getUserAttention(ArrayList<String> arrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERATTENTION, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("id")));
        }
        cursor.close();
        db.close();
    }

    //Add userAttention
    public void addUserAttention(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", userId);
        db.insert(USERATTENTION, null, values);
        db.close();
    }

    //remove userAttention
    public void removeUserAttention(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause =  "id=?";
        String[] whereArgs = new String[] {String.valueOf(userId)};
        db.delete(USERATTENTION, whereClause, whereArgs);
        db.close();
    }

    /**
     * For userCollection
     */
    //Clean userCollection
    public void cleanUserCollection() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(USERCOLLECTION,null,null);
        db.close();
    }

    //Get userCollection
    public void getUserCollection(ArrayList<String> arrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERCOLLECTION, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("id")));
        }
        cursor.close();
        db.close();
    }

    //Add userCollection
    public void addUserCollection(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", userId);
        db.insert(USERCOLLECTION, null, values);
        db.close();
    }

    //remove userAttention
    public void removeUserCollection(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause =  "id=?";
        String[] whereArgs = new String[] {String.valueOf(userId)};
        db.delete(USERCOLLECTION, whereClause, whereArgs);
        db.close();
    }

    /**
     * For userFriend
     */
    //Clean userAttention
    public void cleanUserFriend() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(USERFRIEND,null,null);
        db.close();
    }

    //Get userFriend
    public void getUserFriend (ArrayList<String> arrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USERFRIEND, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("userId")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("avatar")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("realName")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("job")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("company")));
            arrayList.add(cursor.getString(cursor.getColumnIndex("phone")));
        }
        cursor.close();
        db.close();
    }

    //Get userFriendIdLst
    public void getUserFriendId (ArrayList<String> arrayList) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] select = {"userId"};
        Cursor cursor = db.query(USERFRIEND, select, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            arrayList.add(cursor.getString(cursor.getColumnIndex("userId")));
        }
        cursor.close();
        db.close();
    }

    //Add userFriend
    public void addUserFriend(String userId, String avatar, String realName, String job, String company, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", userId);
        values.put("avatar", avatar);
        values.put("realName", realName);
        values.put("job", job);
        values.put("company", company);
        values.put("phone", phone);
        db.insert(USERFRIEND, null, values);
        db.close();
    }

    //remove userFriend
    public void removeUserFriend(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String whereClause =  "id=?";
        String[] whereArgs = new String[] {String.valueOf(userId)};
        db.delete(USERFRIEND, whereClause, whereArgs);
        db.close();
    }


    /**
     * Clean All data
     */
    public void cleanAll(){
        cleanUserinfo();
        cleanUserAttention();
        cleanUserCollection();
        cleanUserFriend();
    }



}
