package com.zjk.hy.utils;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * @author 未知
 */
public class IdUtils {

    private static final SnowFlakeUtils SNOW_FLAKE_UTIL = new SnowFlakeUtils(1000);
    private static Long ID_MIN = 100000L;

    private static SecureRandom random = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextInt(999999));
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static Integer randomInt() {
        Integer a = Math.abs(random.nextInt(999999));
        if (a < 900000) {
            a = randomInt();
        }
        return a;
    }

    /**
     * 使用雪花算法生成ID
     */
    public static long nextSnowFlakeId() {
        return SNOW_FLAKE_UTIL.nextId();
    }

    /**
     * 使用雪花算法生成字符串ID
     */
    public static String nextSnowFlakeStringId() {
        return Long.toString(SNOW_FLAKE_UTIL.nextId());
    }

    private IdUtils() {
        throw new AssertionError("本类是一个工具类，不期望被实例化");
    }
}
