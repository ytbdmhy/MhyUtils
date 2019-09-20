package com.ytbdmhy.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String test = "1@%E5%BE%90";
//        System.out.println(new String(test.getBytes(StandardCharsets.UTF_8), "GBK"));
        System.out.println(test.indexOf("@"));
        System.out.println(test.substring(0,6));
        System.out.println(test.substring(1));

        System.out.println(System.currentTimeMillis());

//        TreeMap<String, String> params = new TreeMap<>();
//        params.put("recommendId", "1");
//        params.put("telephone", "2");
//        params.put("recommendedId", "3");
//        params.put("recommendedTelphone", "4");
//        params.put("scoreType", "1009");
//        params.put("reqTimesTamp", String.valueOf(System.currentTimeMillis()));
//        params.put("recommendRecordId", String.valueOf(System.currentTimeMillis()));
//
//        System.out.println("over");
    }
}
