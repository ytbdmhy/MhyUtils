package com.ytbdmhy.utils;

import java.io.File;
import java.io.IOException;

public class IncreaseImageSizeUtil {

    // 压缩文件的路径
    private static final String rarPath = "C:\\Users\\Administrator\\Desktop\\increaseSize.rar";

    // 图片导出的文件夹路径
    private static final String exportPath = "D:\\IncreaseSize";

    public static void increaseImageSize(String imagePath){
        File file = new File(imagePath);
        if (file.length() < 1024 * 1024) {
            StringBuffer cmdTxt = new StringBuffer("cmd /c copy /b ")
                    .append(imagePath)
                    .append("+")
                    .append(rarPath)
                    .append(" ")
                    // 此处是增大图片后导出的绝对路径
                    .append(exportPath + imagePath.substring(imagePath.lastIndexOf("\\")));
                    // 如果不使用导出路径，图片可直接替换原文件
//                    .append(imagePath);
            System.out.println(String.valueOf(cmdTxt));
            try {
                Runtime.getRuntime().exec(String.valueOf(cmdTxt));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String imagePath = "C:\\Users\\Administrator\\Desktop\\微信图片_20190702191917.jpg";
        increaseImageSize(imagePath);
        System.out.println("over,耗时:" + (System.currentTimeMillis() - startTime) + "毫秒");
    }
}
