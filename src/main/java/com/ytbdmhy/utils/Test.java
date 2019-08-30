package com.ytbdmhy.utils;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.ytbdmhy.utils.DateStringValidateUtil.getDateString;

public class Test {

    public static void main(String[] args) {
        String dateString = "1990527";
        System.out.println(getDateString(dateString));
    }
}
