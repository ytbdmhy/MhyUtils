package com.ytbdmhy.utils;

public class TimeThread extends Thread {

    private volatile boolean stopMark = false;

    // 间隔时间
    private int interval;

    public TimeThread(int interval) {
        this.interval = interval;
    }

    @Override
    public void run() {
        int i = 0;
        while (!stopMark) {
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
        this.stopMark = true;
    }
}
