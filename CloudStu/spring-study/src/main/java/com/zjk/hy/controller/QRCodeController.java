package com.zjk.hy.controller;

import com.zjk.hy.utils.QRCodeTools;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class QRCodeController {
    /**
     * 生成二维码
     * @return
     */
    @GetMapping("generateQRCode")
    public String generateQRCode() throws Exception {
        //String path = "/use/local/java/pro/jvm/files/"+ UUID.randomUUID().toString().replaceAll("-","")+".png";
        String path = "D:\\Workspace\\files\\" + UUID.randomUUID().toString().replaceAll("-","")+".png";
        QRCodeTools.generateQRCode("https://www.baidu.com/",300,300,path);
        return path;
    }
    @GetMapping("replaceQrByPath")
    public String replaceQrByPath() {
        String path = "/use/local/java/pro/jvm/files/";
        String QRPath = path + UUID.randomUUID().toString().replaceAll("-","")+".png";
        // String path = "D:\\Workspace\\files\\";
        try {
            QRCodeTools.generateQRCode("https://www.baidu.com/",300,300,QRPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  QRCodeTools.replaceQrByPath("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3583781653,2478564028&fm=26&gp=0.jpg",QRPath,path);
    }
}
