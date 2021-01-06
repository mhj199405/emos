package com.tongtech.auth.utils;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

/**
 * @author yxy
 * @version 1.0.0
 * @date 2020/04/06 14:06
 * @description 工具类
 */
public final class toolsUtil {

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj 对象
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        return isNullOrEmpty(obj, false);
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj         对象
     * @param ignoreBlank 是否忽略空格
     * @return
     */
    public static boolean isNullOrEmpty(Object obj, boolean ignoreBlank) {

        if (obj == null) {
            return true;
        }

        if (obj instanceof CharSequence) {
            return ignoreBlank ? ((CharSequence) obj).toString().trim().length() == 0 :
                    ((CharSequence) obj).length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i], ignoreBlank)) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }

        return false;
    }

    /**
     * Date转换为LocalDateTime
     *
     * @param date
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Instant instant = date.toInstant();//An instantaneous point on the time-line.(时间线上的一个瞬时点。)
        ZoneId zoneId = ZoneId.systemDefault();//A time-zone ID, such as {@code Europe/Paris}.(时区)
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return  localDateTime;
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param localDateTime
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);//Combines this date-time with a time-zone to create a  ZonedDateTime.
        Date date = Date.from(zdt.toInstant());
        return date;
    }
    /**
     * 获取指定日期所在月份开始的时间
     *
     * @return
     */
    public static LocalDateTime getMonthBegin(LocalDateTime specifiedDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(localDateTimeToDate(specifiedDay));
        //设置为1号,当前日期既为本月第一天
        c.set(Calendar.DAY_OF_MONTH, 1);
        //将小时至0
        c.set(Calendar.HOUR_OF_DAY, 0);
        //将分钟至0
        c.set(Calendar.MINUTE, 0);
        //将秒至0
        c.set(Calendar.SECOND, 0);
        //将毫秒至0
        c.set(Calendar.MILLISECOND, 0);
        Date date=new Date(new Long(c.getTimeInMillis()));
        return dateToLocalDateTime(date);
    }
    /**
     * 获取指定日期所在月份结束的时间
     * @return
     */
    public static LocalDateTime getMonthEnd(LocalDateTime specifiedDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(localDateTimeToDate(specifiedDay));
        //设置为当月最后一天
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //将小时至23
        c.set(Calendar.HOUR_OF_DAY, 23);
        //将分钟至59
        c.set(Calendar.MINUTE, 59);
        //将秒至59
        c.set(Calendar.SECOND, 59);
        //将毫秒至999
        //c.set(Calendar.MILLISECOND, 999);
        // 本月第一天的时间戳转换为字符串
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date(new Long(c.getTimeInMillis()));
        return dateToLocalDateTime(date);
    }
    /**
     * 计算两个时间点的天数差
     * @param dt1 第一个时间点
     * @param dt2 第二个时间点
     * @return int，即要计算的天数差
     */
    public static int dateDiff(LocalDateTime dt1,LocalDateTime dt2){
        //获取第一个时间点的时间戳对应的秒数
        long t1 = dt1.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第一个时间点在是1970年1月1日后的第几天
        long day1 = t1 /(60*60*24);
        //获取第二个时间点的时间戳对应的秒数
        long t2 = dt2.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第二个时间点在是1970年1月1日后的第几天
        long day2 = t2/(60*60*24);
        //返回两个时间点的天数差
        return (int)(day2 - day1);
    }
    /**
     * 获取两个时间点的月份差
     * @param dt1 第一个时间点
     * @param dt2 第二个时间点
     * @return int，即需求的月数差
     */
    public static int monthDiff(LocalDateTime dt1,LocalDateTime dt2){
        //获取第一个时间点的月份
        int month1 = dt1.getMonthValue();
        //获取第一个时间点的年份
        int year1 = dt1.getYear();
        //获取第一个时间点的月份
        int month2 = dt2.getMonthValue();
        //获取第一个时间点的年份
        int year2 = dt2.getYear();
        //返回两个时间点的月数差
        return (year2 - year1) *12 + (month2 - month1);
    }

}
