package com.mashibing;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author: 马士兵教育
 * @create: 2019-10-12 21:15
 */
public class InetAddressDemo {
    public static void main(String[] args) throws UnknownHostException {
        InetAddress localHost = InetAddress.getLocalHost();
        System.out.println(localHost);
        InetAddress inetAdd = InetAddress.getByName("www.baidu.com");
        System.out.println(inetAdd);
        System.out.println(inetAdd.getHostAddress());
        System.out.println(inetAdd.getHostName());
    }
}
