package com.ytbdmhy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Copyright: weface
 * @Description: 日期分组工具类
 * @author: miaohaoyun
 * @since:
 * @history: created in 13:31 2019-06-24 created by miaohaoyun
 */
public class DateGroupUtilMhy {

    public static List<String[]> getWeekGroup(String start, String end) {
        try {
            int count = start.length() - start.replaceAll("-", "").length();
            if (count == 2 || start.length() > 7) {
                SimpleDateFormat yMdFormat = new SimpleDateFormat("yyyy-MM-dd");
                return getWeekGroup(yMdFormat.parse(start), yMdFormat.parse(end), 2);
            } else if (count == 1 || start.length() < 6) {
                SimpleDateFormat MdFormat = new SimpleDateFormat("MM-dd");
                return getWeekGroup(MdFormat.parse(start), MdFormat.parse(end), 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<String[]> getWeekGroup(Date start, Date end, int mark) {
        SimpleDateFormat dateFormat;
        if (mark == 1) {
            dateFormat = new SimpleDateFormat("MM-dd");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }

        long days = (end.getTime() - start.getTime()) / 3600 / 24 / 1000;

        Calendar instance = Calendar.getInstance();
        instance.setTime(start);
        List<String[]> list = new ArrayList<>();
        String[] ne;
        int fd = instance.get(Calendar.DAY_OF_WEEK);
        int fwrd;
        Long adrd = null;
        long weeks;

        if (mark == 2) {
            if (days > 0) {
                fwrd = fd == 1 ? 0 : 7 - fd + 1;
                adrd = days - fwrd;
                weeks = adrd / 7;
                if (fwrd == 0) {
                    list.add(new String[]{dateFormat.format(start)});
                } else {
                    instance.add(Calendar.DAY_OF_YEAR, fwrd);
                    list.add(new String[]{dateFormat.format(start), dateFormat.format(instance.getTime())});
                }
            } else if (days < 0) {
                fwrd = fd == 1 ? 6 : fd - 2;
                adrd = days + fwrd;
                weeks = adrd / 7;
                if (fwrd == 0) {
                    list.add(new String[]{dateFormat.format(start)});
                } else {
                    instance.add(Calendar.DAY_OF_YEAR, -fwrd);
                    list.add(new String[]{dateFormat.format(start), dateFormat.format(instance.getTime())});
                }
            } else {
                list.add(new String[]{dateFormat.format(start)});
                return list;
            }
            instance.add(Calendar.DAY_OF_YEAR, adrd > 0 ? 1 : -1);
        } else {
            weeks = days / 7;
        }

        if (weeks > 0) {
            for (int i = 0;i < weeks;i++) {
                Date startDate = instance.getTime();
                instance.add(Calendar.DAY_OF_YEAR, 6);
                Date endDate = instance.getTime();
                ne = new String[]{dateFormat.format(startDate), dateFormat.format(endDate)};
                list.add(ne);
                instance.add(Calendar.DAY_OF_YEAR, 1);
            }
        } else if (weeks < 0) {
            for (int i = 0;i > weeks;i--) {
                Date startDate = instance.getTime();
                instance.add(Calendar.DAY_OF_YEAR, -6);
                Date endDate = instance.getTime();
                ne = new String[]{dateFormat.format(startDate), dateFormat.format(endDate)};
                list.add(ne);
                instance.add(Calendar.DAY_OF_YEAR, -1);
            }
        }

        if (mark == 2) {
            if (Math.abs(adrd % 7) == 1) {
                list.add(new String[]{dateFormat.format(end)});
            } else if (Math.abs(adrd % 7) > 0) {
                list.add(new String[]{dateFormat.format(instance.getTime()), dateFormat.format(end)});
            }
        } else {
            if (Math.abs(days % 7) == 1) {
                list.add(new String[]{dateFormat.format(end)});
            } else if (Math.abs(days % 7) > 0) {
                list.add(new String[]{dateFormat.format(instance.getTime()), dateFormat.format(end)});
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String start = "2019-05-22";
        String end = "2019-06-24";
        for (String[] week : getWeekGroup(start, end)) {
            System.out.println(Arrays.toString(week));
        }
        System.out.println("------------------------------");
        for (String[] week : getWeekGroup(end, start)) {
            System.out.println(Arrays.toString(week));
        }
    }
}
