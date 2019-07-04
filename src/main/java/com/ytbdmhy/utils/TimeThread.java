package com.ytbdmhy.utils;

public class TimeThread extends Thread {

    private volatile boolean stopMark = false;

    @Override
    public void run() {
        int i = 0;
        // 间隔时间
        int interval = 1;
        while (!this.stopMark) {
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
