package com.ytbdmhy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * Cron表达式工具类
 */
public class CronUtil {

    private static final SimpleDateFormat cronFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 日期Date转换cron表达式
     * @param date
     * @return
     */
    public static String getCron(Date date) {
        return Objects.nonNull(date) ? cronFormat.format(date) : null;
    }

    /**
     * 日期String转换cron表达式
     * @param dateString
     * @return
     */
    public static String getCron(String dateString) {
        Date date;
        try {
            date = dateTimeFormat.parse(dateString);
        } catch (ParseException e) {
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e1) {
                return null;
            }
        }
        return getCron(date);
    }

}
