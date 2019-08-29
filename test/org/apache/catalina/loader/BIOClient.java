package org.apache.catalina.loader;

import java.net.Socket;
import java.util.Date;

/**
 * @program: Tomcat8.5
 * @description: BIO通信
 * @author: Mr.Feng
 * @create: 2019-06-03 11:32
 **/
public class BIOClient {

    public static void main(String[] args) {

        //todo: 创建多个线程，模拟多个客户端连接服务端
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 3333);
                while (true) {
                    socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
