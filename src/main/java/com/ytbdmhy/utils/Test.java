package com.ytbdmhy.utils;

public class Test {

    public static void main(String[] args) {
        TimeThread timeThread = new TimeThread(60);
        timeThread.run();

//        try {
//            Thread.sleep(3105);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        timeThread.tellStop();
    }
}
