package com.tmqt.util;

/**
 * Created by chenw on 2018/12/16.
 */
public class IDUtils {
    private static byte[] lock = new byte[0];

    // 位数，默认是8位
    private final static long w = 10;

    public static String createID() {
        long r = 0;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * w);
        }

        return System.currentTimeMillis() + String.valueOf(r).substring(1);
    }
}
