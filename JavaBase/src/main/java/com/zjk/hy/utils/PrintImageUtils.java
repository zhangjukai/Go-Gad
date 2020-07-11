package com.zjk.hy.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class PrintImageUtils {
    private static Font font = new Font("黑体", Font.PLAIN, 30); // 添加字体的属性设置
    private Graphics2D g = null;
    private int fontsize = 0;

    /**
     * 导入本地图片到缓冲区
     */
    public static BufferedImage loadImageLocal(String imgName) {
        try {
            return ImageIO.read(new File(imgName));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 导入网络图片到缓冲区
     */
    public static BufferedImage loadImageUrl(String imgName) {
        try {
            URL url = new URL(imgName);
            return ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * 生成新图片到本地
     */
    public static void writeImageLocal(String newImage, BufferedImage img) {
        if (newImage != null && img != null) {
            try {
                File outputfile = new File(newImage);
                ImageIO.write(img, "jpg", outputfile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 设定文字的字体等
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     */
    public static BufferedImage modifyImage(BufferedImage img, Object content, int x, int y, Color color) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            if (x < 1 || x > w) {
                x = 100;
            }
            if (y < 1 || y > h) {
                y = 750;
            }
            Graphics2D g = img.createGraphics();
            g.setBackground(Color.BLUE);
            g.setColor(color);//设置字体颜色
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setStroke(new BasicStroke(3));
            if (font != null)
                g.setFont(font);
            if (content != null) {
                g.translate(0, 0);
                g.drawString(content.toString(), x, y);
            }
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return img;
    }
    public static BufferedImage modifyImage1(BufferedImage img, Object content, int x, int y, Color color) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            if (x < 1 || x > w) {
                x = 100;
            }
            y = h - y;
            Graphics2D g = img.createGraphics();
            g.setBackground(Color.BLUE);
            g.setColor(color);//设置字体颜色
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setStroke(new BasicStroke(3));
            if (font != null)
                g.setFont(font);
            if (content != null) {
                g.translate(0, 0);
                g.drawString(content.toString(), x, y);
            }
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return img;
    }


    /**
     * 修改图片,返回修改后的图片缓冲区（只输出一行文本）
     * <p>
     * 时间:2007-10-8
     *
     * @param img
     * @return
     */
    public BufferedImage modifyImageYe(BufferedImage img) {
        try {
            int w = img.getWidth();
            int h = img.getHeight();
            g = img.createGraphics();
            g.setBackground(Color.WHITE);
            g.setColor(Color.blue);//设置字体颜色
            if (this.font != null)
                g.setFont(this.font);
            g.drawString("www.hi.baidu.com?xia_mingjian", w - 85, h - 5);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return img;
    }

    public BufferedImage modifyImagetogeter(BufferedImage b, BufferedImage d) {
        try {
            int w = b.getWidth();
            int h = b.getHeight();
            g = d.createGraphics();
            g.drawImage(b, 100, 10, w, h, null);
            g.dispose();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return d;
    }

    /***
     * 插入描边的字体
     * @param img
     * @param content
     * @param w
     * @param h
     * @return
     */
    public BufferedImage modifyShapImg(BufferedImage img, String content, int w, int h) {
        g = img.createGraphics();

        GlyphVector v = font.createGlyphVector(g.getFontMetrics(font).getFontRenderContext(), content);
        Shape shape = v.getOutline();
        if (content != null) {
            g.translate(w, h);
        }
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setColor(new Color(0, 90, 160));
        g.fill(shape);
        g.setColor(new Color(248, 248, 255));
        g.setStroke(new BasicStroke(2));
        g.draw(shape);
        return img;
    }

    public static Color fromStrToARGB(String str) {
        if (str.indexOf("#") == 0) {
            str = str.substring(1, str.length());
        }
        String str1 = str.substring(0, 2);
        String str2 = str.substring(2, 4);
        String str3 = str.substring(4, 6);
        //String str4 = str.substring(6, 8);
        //int alpha = Integer.parseInt(str1, 16);
        int red = Integer.parseInt(str1, 16);
        int green = Integer.parseInt(str2, 16);
        int blue = Integer.parseInt(str3, 16);
        Color color = new Color(red, green, blue);
        return color;
    }
}