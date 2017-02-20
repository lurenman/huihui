package com.hui2020.huihui;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by FD-GHOST on 2016/12/19.
 * For generate MD5 code which will be use to encrypt password
 */

public class Mdfive {

    public String Mdfive(String s) throws NoSuchAlgorithmException {
        byte[] hash;

        String a = s;
        Log.e("MD5 String", s);

        try {
            hash = MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }

        return hex.toString();
    }

}
