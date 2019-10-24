package com.ytbdmhy.utils;

import java.math.BigDecimal;

public class Test2 {

    public static void main(String[] args) {
        double d1 = 0.15;
        double d2 = 0.6468;
        double d3 = 0.1456987984849;
        System.out.println(d1 + d2);
        System.out.println(d3);
        // 0.7968000000000001
        // 0.1456987984849
        // double 精度有误

        float f1 = 0.15f;
        float f2 = 0.6468f;
        float f3 = 0.1456987984849f;
        System.out.println(f1 + f2);
        System.out.println(f3);
        // 0.7968
        // 0.1456988
        // float只有8位，过长的后面会被舍弃

        BigDecimal b1 = new BigDecimal(Double.toString(0.15));
        BigDecimal b2 = new BigDecimal(Double.toString(0.6468));
        BigDecimal b3 = new BigDecimal(Double.toString(0.1456987984849));
        System.out.println(b1.add(b2));
        System.out.println(b3);
        // 0.7968
        // 0.1456987984849
        // BigDecimal精准，但是构造及使用复杂
    }


}
