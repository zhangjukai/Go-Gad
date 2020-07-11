package com.zjk.hy.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtils extends org.apache.commons.lang3.time.DateUtils {
    private static final long ONE_MILLIS = 1000;
    private static final long ONE_MINUTE = 60;
    private static final long ONE_HOUR = 3600;
    private static final long ONE_DAY = 86400;
    private static final long ONE_MONTH = 2592000;
    private static final long ONE_YEAR = 31104000;

    public static final String YYYY_MM_DD_H = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS_H = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_H = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_H = "yyyy-MM";
    public static final String YYYY_MM_DD_X = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_HH_MM_SS_X = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_X = "yyyy/MM/dd HH:mm";
    public static final String YYYY_MM_X = "yyyy/MM";
    public static final String YYYY_MM_DD_D = "yyyy.MM.dd";
    public static final String YYYY_MM_DD_HH_MM_SS_D = "yyyy.MM.dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_D = "yyyy.MM.dd HH:mm";
    public static final String YYYY_MM_D = "yyyy.MM";
    public static final String YYYY_MM_DD = "yyyyMMdd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyyMMddHHmmss";
    public static final String YYYY_MM_DD_HH_MM = "yyyyMMddHHmm";
    public static final String YYYY_MM = "yyyyMM";


    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM",
            "yyyyMMdd", "yyyyMMddHHmmss", "yyyyMMddHHmm", "yyyyMM"};

    /**
     * 将时间字符串转换为时间
     * @param str 时间字符串 需要是parsePatterns格式的
     * @return
     */
    public static Date parseDate(String str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str, parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取当前时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static String formatCurrentDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 时间转字符串
     * @param date 时间
     * @param pattern 字符串格式 不传默认yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (pattern != null) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, YYYY_MM_DD_HH_MM_SS_H);
        }
        return formatDate;
    }


    /**
     * 获取当前时间戳（yyyyMMddHHmmss）
     * @return
     */
    public static long getCurrentTimestamp() {
        return Long.parseLong(getCurrentTimestampStr());
    }

    /**
     * 获取当前时间戳（yyyyMMddHHmmss）
     * @return
     */
    public static String getCurrentTimestampStr() {
        return formatDate(new Date(), YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取Unix时间戳
     * @return
     */
    public static long getCurrentUnixTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 获取Unix时间戳
     * @return
     */
    public static String getCurrentUnixTimestampStr() {
        return String.valueOf(getCurrentUnixTimestamp());
    }

    /**
     * 转换Unix时间戳
     * @return nowTimeStamp
     */
    public static long parseUnixTimeStamp(long time) {
        return time / ONE_MILLIS;
    }

    /**
     * 获取前一周
     *
     * @param date
     * @return
     */
    public static Date getBeforeWeek(Date date) {
        return getAddDate(date, Calendar.WEEK_OF_YEAR, -1);
    }

    /**
     * 获取前一天
     *
     * @param date
     * @return
     */
    public static Date getBeforeDay(Date date) {
        return getAddDate(date, Calendar.DAY_OF_YEAR, -1);
    }

    /**
     * 获取前一月
     *
     * @param date
     * @return
     */
    public static Date getBeforeMouth(Date date) {
        return getAddDate(date, Calendar.MONTH, -1);
    }

    /**
     * 获取前一年
     *
     * @param date
     * @return
     */
    public static Date getBeforeYear(Date date) {
        return getAddDate(date, Calendar.YEAR, -1);
    }


    /**
     * 获取前一周
     *
     * @param date
     * @return
     */
    public static Date getAfterWeek(Date date) {
        return getAddDate(date, Calendar.WEEK_OF_YEAR, 1);
    }

    /**
     * 获取前一天
     *
     * @param date
     * @return
     */
    public static Date getAfterDay(Date date) {
        return getAddDate(date, Calendar.DAY_OF_YEAR, 1);
    }

    /**
     * 获取前一月
     *
     * @param date
     * @return
     */
    public static Date getAfterMouth(Date date) {
        return getAddDate(date, Calendar.MONTH, 1);
    }

    /**
     * 获取前一年
     *
     * @param date
     * @return
     */
    public static Date getAfterYear(Date date) {
        return getAddDate(date, Calendar.YEAR, 1);
    }


    /**
     * 增加日期
     *
     * @param date
     * @param field  Calendar.MONTH,Calendar.DAY_OF_YEAR
     * @param amount 正数为将来时间, 负数为过去时间
     * @return
     */
    public static Date getAddDate(Date date, int field, int amount) {
        Calendar cl = Calendar.getInstance();
        cl.setTime(date);
        cl.add(field, amount);
        Date dateFrom = cl.getTime();
        return dateFrom;
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long getBeforeDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (ONE_DAY * ONE_MILLIS);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long getBeforeHours(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (ONE_HOUR * ONE_MILLIS);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long getBeforeMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (ONE_MINUTE * ONE_MILLIS);
    }

    /**
     * 获取过去的秒
     *
     * @param date
     * @return
     */
    public static long getBeforeSeconds(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / ONE_MILLIS;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDays(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (ONE_MILLIS * ONE_DAY);
    }


    /**
     * 距离今天多久
     *
     * @param createAt
     * @return
     */
    public static String formatTextFromtoday(Date createAt) {
        // 定义最终返回的结果字符串。
        String interval = null;
        if (createAt == null) {
            return "";
        }
        long millisecond = System.currentTimeMillis() - createAt.getTime();

        long second = millisecond / ONE_MILLIS;

        if (second <= 0) {
            second = 0;
        }
        //*--------------微博体（标准）
        if (second == 0) {
            interval = "刚刚";
        } else if (second < ONE_MINUTE / 2) {
            interval = second + "秒以前";
        } else if (second >= ONE_MINUTE / 2 && second < ONE_MINUTE) {
            interval = "半分钟前";
        } else if (second >= ONE_MINUTE && second < ONE_MINUTE * ONE_MINUTE) {
            //大于1分钟 小于1小时
            long minute = second / ONE_MINUTE;
            interval = minute + "分钟前";
        } else if (second >= ONE_HOUR && second < ONE_DAY) {
            //大于1小时 小于24小时
            long hour = (second / ONE_MINUTE) / ONE_MINUTE;
            interval = hour + "小时前";
        } else if (second >= ONE_DAY && second <= ONE_DAY * 2) {
            //大于1D 小于2D
            interval = "昨天" + formatDate(createAt, "HH:mm");
        } else if (second >= ONE_DAY * 2 && second <= ONE_DAY * 7) {
            //大于2D小时 小于 7天
            long day = ((second / ONE_MINUTE) / ONE_MINUTE) / 24;
            interval = day + "天前";
        } else if (second <= ONE_DAY * 365 && second >= ONE_DAY * 7) {
            //大于7天小于365天
            interval = formatDate(createAt, "MM-dd HH:mm");
        } else if (second >= ONE_DAY * 365) {
            //大于365天
            interval = formatDate(createAt, "yyyy-MM-dd HH:mm");
        } else {
            interval = "0";
        }
        return interval;
    }


    /**
     * 距离截止日期还有多长时间
     *
     * @param date
     * @return
     */
    public static String formatTextFromDeadline(Date date) {
        long deadline = date.getTime() / ONE_MILLIS;
        long now = (System.currentTimeMillis()) / ONE_MILLIS;
        long remain = deadline - now;
        if (remain <= ONE_HOUR) {
            return "只剩下" + remain / ONE_MINUTE + "分钟";
        } else if (remain <= ONE_DAY) {
            return "只剩下" + remain / ONE_HOUR + "小时"
                    + (remain % ONE_HOUR / ONE_MINUTE) + "分钟";
        } else {
            long day = remain / ONE_DAY;
            long hour = remain % ONE_DAY / ONE_HOUR;
            long minute = remain % ONE_DAY % ONE_HOUR / ONE_MINUTE;
            return "只剩下" + day + "天" + hour + "小时" + minute + "分钟";
        }

    }


    /**
     * Unix时间戳转换成指定格式日期字符串
     *
     * @param timestampString 时间戳 如："1473048265";
     * @param pattern         要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String unixTimeStamp2Date(String timestampString, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        Long timestamp = Long.parseLong(timestampString) * ONE_MINUTE;
        String date = new SimpleDateFormat(pattern, Locale.CHINA).format(new Date(timestamp));
        return date;
    }

    /**
     * 日期格式字符串转换成Unix时间戳
     *
     * @param dateStr 字符串日期
     * @param pattern 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2UnixTimeStamp(String dateStr, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return String.valueOf(sdf.parse(dateStr).getTime() / ONE_MINUTE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取两个月份间的月份，包好首/末
     * @param startMonth 开始月份 2020-05
     * @param endMonth  结束月份 2020-10
     * @return
     */
    public static List<String> getTwoMothData(String startMonth, String endMonth) {
        List<String> reuslt = new ArrayList<>();
        reuslt.add(startMonth);
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        try {
            start.set(Integer.valueOf(startMonth.substring(0,4)), Integer.valueOf(startMonth.substring(5,7)), 1);
            end.set(Integer.valueOf(endMonth.substring(0,4)), Integer.valueOf(endMonth.substring(5,7)), 1);
        } catch (Exception e){
            throw new IllegalArgumentException("时间参数个数不正确");
        }
        while (start.before(end)) {
            int m = start.get(Calendar.MONTH)+1;
            reuslt.add(start.get(Calendar.YEAR)+(m<10?"-0"+m:"-"+m));
            start.add(Calendar.MONTH, 1);
        }
        return reuslt;
    }


    public static void main(String[] args) {
        System.out.println(formatDate(getBeforeDay(new Date()), "yyyy-MM-dd"));
        System.out.println(formatDate(getBeforeWeek(new Date()), "yyyy-MM-dd"));
        System.out.println(formatDate(getBeforeYear(new Date()), "yyyy-MM-dd"));
        System.out.println(formatDate(getAfterDay(new Date()), "yyyy-MM-dd"));
        System.out.println(formatDate(getAfterWeek(new Date()), "yyyy-MM-dd"));
        System.out.println(formatDate(getAfterYear(new Date()), "yyyy-MM-dd"));
    }
}

