package com.ahcloud.common.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @program: ahcloud-common
 * @description:
 * @author: YuKai Fan
 * @create: 2023/3/9 16:45
 **/
public class IpUtils {

    /**
     * 获取当前服务的真实ip
     * @return
     */
    public static InetAddress getCurrentIp() throws SocketException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface ni = networkInterfaces.nextElement();
            Enumeration<InetAddress> nias = ni.getInetAddresses();
            while (nias.hasMoreElements()) {
                InetAddress ia = nias.nextElement();
                if (!ia.isLinkLocalAddress() && !ia.isLoopbackAddress() && ia instanceof Inet4Address) {
                    return ia;
                }
            }
        }
        return null;
    }
}
