package com.tongtech.common.utils;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * SysUtils
 */
public class SysUtils {

    /**
     * 获取一个对象中，值为null的所有属性
     *
     * @param source
     * @return
     */
    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // java 1.8 lambda
//    public static String[] getNullPropertyNames(Object source) {
//        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
//        return Stream.of(wrappedSource.getPropertyDescriptors())
//                .map(FeatureDescriptor::getName)
//                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
//                .toArray(String[]::new);
//    }
    
    /**
     * 
     * @param birth
     * @return 
     */
    public static Integer getAgeFromBirth(Date birth) {
        if (birth == null) {
            return -1;
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(birth);

            int birthYear = calendar.get(Calendar.YEAR);

            calendar.clear();
            calendar.setTime(new Date());
            int nowYear = calendar.get(Calendar.YEAR);

            return nowYear - birthYear;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
/*
    public static Integer betweenDays(Date begin, Date end){
//        Interval interval = new Interval(end.getTime(), begin.getTime());
        Interval interval = new Interval(begin.getTime(), end.getTime());
        Period period = interval.toPeriod(PeriodType.days());
        return period.getDays();
    }
    public static Integer betweenWeeks(Date begin, Date end){
        Interval interval = new Interval(end.getTime(), begin.getTime());
        Period period = interval.toPeriod();
        return period.getWeeks();
    }
    public static Integer betweenMonths(Date begin, Date end){
        Interval interval = new Interval(end.getTime(), begin.getTime());
        Period period = interval.toPeriod();
        return period.getMonths();
    }
*/
}
