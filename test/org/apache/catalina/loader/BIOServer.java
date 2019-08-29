package org.apache.catalina.loader;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @program: Tomcat8.5
 * @description: BIO通信
 * @author: Mr.Feng
 * @create: 2019-06-03 11:38
 **/
public class BIOServer {


    public static void main(String[] args) {
        //todo:服务端处理客户端连接请求
        try {
            ServerSocket serverSocket = new ServerSocket(3333);
            //接受大盘客户端请求后为每个客户端创建一个线程
            new Thread(() -> {
                while (true) {
                    try {
                        //阻塞方法获取新的连接
                        Socket accept = serverSocket.accept();
                        new Thread(() -> {
                            try {
                                int len;
                                byte[] data = new byte[1024];
                                InputStream inputStream = accept.getInputStream();
                                while ((len = inputStream.read(data)) != -1) {
                                    System.out.println(new String(data, 0, len));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
