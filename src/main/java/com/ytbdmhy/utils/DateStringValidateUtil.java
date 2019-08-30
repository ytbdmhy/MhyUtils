package com.ytbdmhy.utils;

import java.text.SimpleDateFormat;

public class DateStringValidateUtil {

    public static String getDateString(String dateString) {
        dateString = dateString.replace("/", "-");
        if (dateString.length() == 8 || (!dateString.contains("-") && dateString.length() > 8)) {
            dateString = dateString.substring(0, 4) + "-" + dateString.substring(4, 6) + "-" + dateString.substring(6);
        }
        if ((dateString.contains("-") && dateString.replace("-", "").length() == 8)) {
            dateString += " 00:00:00";
        }
        if (!dateString.contains(" ") && dateString.length() > 11) {
            dateString = dateString.substring(0, 10) + " " + dateString.substring(10);
        }
        if (dateString.replace(":", "").length() == dateString.length()) {
            if (dateString.length() < 17) {
                int temp = dateString.length();
                for (int i = 0; i < 17 - temp; i++) {
                    dateString += "0";
                }
            }
            dateString = dateString.substring(0, 13) + ":" + dateString.substring(13, 15) + ":" + dateString.substring(15);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateFormat.parse(dateString);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            return null;
        }
        return dateString;
    }
}
