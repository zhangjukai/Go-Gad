package com.zjk.hy.se.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author zhangjukai
 * @Date 2023-05-12 11:00
 * @Description
 */
public class ServerApplication {
    public ServerApplication() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9090);
            while (true) {
                Socket socket = serverSocket.accept(); //阻塞
                System.out.println("客户端地址："+socket.getRemoteSocketAddress().toString());
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
