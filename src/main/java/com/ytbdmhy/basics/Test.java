package com.ytbdmhy.basics;

import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;

public class Test {

    public static void main(String[] args) throws UnsupportedEncodingException {

        String a = "adgfasdgsadg==undo";

        System.out.println(a.substring(0, a.length() - 4));

//        String date1 = "2018-12-30";
//
//        String date2 = "2019-01-31";
//
//        System.out.println(date1.compareTo(date2));
//
//        System.out.println(System.currentTimeMillis());

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


    private static String removeTelephoneUndo(String telephone) {
        if (StringUtils.isEmpty(telephone))
            return null;
        return telephone.length() > 4 && telephone.endsWith("undo") ? telephone.substring(0, telephone.length() - 4) : telephone;
    }

}
