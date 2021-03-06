package com.ytbdmhy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeThread extends Thread {

    private volatile boolean stopMark = false;

    // 间隔时间
    private int interval;

    public TimeThread(int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
        int i = 0;
        while (!stopMark) {
            System.out.println("现在是:" + dateFormat.format(new Date()));
            System.out.println("第" + i + "个" + interval + "秒过去了。。。");
            i++;
            try {
                Thread.sleep(interval * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tellStop() {
        stopMark = true;
    }
}
