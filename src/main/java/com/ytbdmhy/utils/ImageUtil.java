package com.ytbdmhy.utils;

import net.coobird.thumbnailator.Thumbnails;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Copyright: weface
 * @Description:
 * @author: miaohaoyun
 * @since:
 * @history: created in 10:36 2019-06-21 created by miaohaoyun
 * @Remarks: thumbnailator 0.48
 */
public class ImageUtil {

    public static String imageToBase64() {
        String imagePath = "C:\\Users\\Administrator\\Desktop\\circle.png";
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imagePath);
            data = new byte[inputStream.available()];
            inputStream.read(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String base64 = encoder.encode(data);
        System.out.println(base64);
        return base64;
    }

    public static void main(String[] args) {


//        String imagePath = "C:/Users/Administrator/Desktop/微信图片_20190702191917.jpg";
//        String pre = imagePath.substring(0, imagePath.indexOf(".jpg"));
//        long start = System.currentTimeMillis();
//        try {
//            // 该方法可至1.06MB 但dpi将至96，影响打印效果
//            Thumbnails.of(imagePath)
//
//                    // 图片尺寸不变，压缩图片文件大小
//                    .scale(1f)
//                    .outputFormat("png")
//                    .outputQuality(1f)
//                    .toFile(pre + "_png");
//
//            Thumbnails.of(pre + "_png.png")
//                    .scale(1f)
//                    .outputQuality(0.94f)
//                    .outputFormat("jpg")
//                    .toFile(pre + "_0.1QJpg");

//            Thumbnails.of(imagePath)
//
//                    // 图片尺寸不变，压缩图片文件大小
//                    .scale(1f)
//                    .outputFormat("bmp")
//                    .outputQuality(1f)
//                    .toFile(pre + "_bmp");
//
//            Thumbnails.of(pre + "_bmp.bmp")
//                    .scale(1f)
//                    .outputQuality(0.94f)
//                    .outputFormat("jpg")
//                    .toFile(pre + "_0.1QJpg");

                    // 裁剪
//                    .sourceRegion(Positions.CENTER, 400, 400)
//                    .size(200, 200)
//                    .keepAspectRatio(false)
//                    .toFile(pre + "_region_center.jpg");

                    // 水印
//                    .size(1280, 1024)
//                    .watermark(Positions.BOTTOM_RIGHT, )
//                    .outputQuality(0.8f)
//                    .toFile(pre + "_water_bottom_right.jpg");

                    // 旋转
//                    .size(1280, 1024)
//                    .rotate(90)
//                    .toFile(pre + "_rotate+90.jpg");

                    // 不按照比例，指定大小进行缩放
//                    .size(200, 200)
//                    .keepAspectRatio(false)
//                    .toFile(pre + "_200×200keepAR.jpg");

                    // 按照比例进行缩放
//                    .scale(1.2f)
//                    .toFile(pre + "_120%.jpg");

                    // 按照比例进行缩放
//                    .scale(0.25f)
//                    .toFile(pre + "_25%.jpg");

                    // 指定大小进行缩放
//                    .size(2560, 2048)
//                    .toFile(pre + "_2560×2048.jpg");

                    // 指定大小进行缩放
//                    .size(200, 300)
//                    .toFile(imagePath.substring(0, imagePath.indexOf(".jpg")) + "_200×300.jpg");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("over,耗时:" + (System.currentTimeMillis() - start) + "毫秒");
    }
}
