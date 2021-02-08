package com.zjk.hy.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class QRCodeTools {


    /**
     * 根据内容，生成指定宽高的二维码图片
     *
     * @param text   内容
     * @param width  宽
     * @param height 高
     * @param QRPath
     * @return 生成的二维码图片路径
     * @throws Exception
     */
    public static void generateQRCode(String text, int width, int height, String QRPath) throws Exception {
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File(QRPath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", outputFile.toPath());
    }

    /**
     * 替换图片中的二维码
     *
     * @param image    原图地址 网络图片
     * @param qrcode   二维码地址 网络图片
     * @param location 本地缓存路径前缀
     * @return 图片上传后返回的ID
     */
    public static String replaceQrByPath(String image, String qrcode, String location) {
        System.out.println("start deEncodeByPath ====== " + image);
        try {
            File locaFile = new File(location);
            if (!locaFile.exists()) {  //基础目录不存在则创建
                System.out.println("start mkdirs ====== " + location);
                locaFile.mkdirs();
            }
            String targetUrl = location + File.separatorChar + UUID.randomUUID().toString().replaceAll("-", "") + "_QRCodeTarget.png";
            File target = new File(targetUrl);
            File qrFile = new File(qrcode);
            QRCodeTools.replaceQr(getInputStream(image),new FileInputStream(qrFile), target);
            target.delete();
            qrFile.delete();
            return targetUrl;
        } catch (Exception e) {
            throw new RuntimeException("替换二维码信息异常");
        }
    }

    /**
     * 替换原图片里面的二维码
     *
     * @param image   原图
     * @param qrimage 二维码图片
     * @param target  本地文件
     */
    public static void replaceQr(InputStream image, InputStream qrimage, File target) {
        try {
            // 将远程文件转换为流
            BufferedImage readImage = ImageIO.read(image);
            LuminanceSource source = new BufferedImageLuminanceSource(readImage);
            Binarizer binarizer = new HybridBinarizer(source);
            //Binarizer binarizer = new GlobalHistogramBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = null;
            try {
                result = new MultiFormatReader().decode(binaryBitmap, hints);
            } catch (NotFoundException e) {
                e.printStackTrace();
            }

            // 解码
            ResultPoint[] resultPoint = result.getResultPoints();

            // 获得二维码坐标
            float point1X = resultPoint[0].getX();
            float point1Y = resultPoint[0].getY();
            float point2X = resultPoint[1].getX();
            float point2Y = resultPoint[1].getY();

            // 替换二维码的图片文件路径
            BufferedImage writeFile = ImageIO.read(qrimage);

            // 宽高
            final int w = (int) Math
                    .sqrt(Math.abs(point1X - point2X) * Math.abs(point1X - point2X) + Math.abs(point1Y - point2Y) * Math.abs(point1Y - point2Y))
                    + 12 * (7 - 1);
            final int h = w;

            Hashtable<EncodeHintType, Object> hints2 = new Hashtable<EncodeHintType, Object>();
            hints2.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints2.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints2.put(EncodeHintType.MARGIN, 1);

            Graphics2D graphics = readImage.createGraphics();
            //此处,偏移,会有定位问题
            int x = Math.round(point1X) - 36;
            int y = Math.round(point2Y) - 36;

            // 开始合并绘制图片
            graphics.drawImage(writeFile, x, y, w, h, null);
            // logo边框大小
            graphics.setStroke(new BasicStroke(2));
            // //logo边框颜色
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, y, w, h);
            readImage.flush();
            graphics.dispose();

            // 打印替换后的图片
            //NewImageUtils.generateWaterFile(readImage, "F:\\image\\save.jpg");
            if (!ImageIO.write(readImage, "png", target)) {
                throw new IOException("Could not write an image of format ");
            }
        } catch (IOException e) {
            System.out.println("资源读取失败" + e.getMessage());
            e.printStackTrace();
        }
    }





    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    public static InputStream getInputStream(String path) throws Exception {
        URL url = new URL(path);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        return conn.getInputStream();
    }

    /**
     * 随机生成指定长度的验证码
     *
     * @param length 验证码长度
     * @return 生成的验证码
     */
    private static String generateNumCode(int length) {
        String val = "";
        String charStr = "char";
        String numStr = "num";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? charStr : numStr;
            //输出字母还是数字
            if (charStr.equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (random.nextInt(26) + temp);
            } else if (numStr.equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


}

