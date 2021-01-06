package com.tongtech.auth.utils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ObjectParserUtil {
    public static Date toDate(Object date) throws ParseException {
        if (date == null) {
            return null;
        }
        Date result = null;
        String str = date.toString();
        String parse = str;
        parse = parse.replaceFirst("^[0-9]{4}([^0-9])", "yyyy$1");
        parse = parse.replaceFirst("^[0-9]{2}([^0-9])", "yy$1");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1MM$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}( ?)", "$1dd$2");
        parse = parse.replaceFirst("( )[0-9]{1,2}([^0-9])", "$1HH$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9])", "$1mm$2");
        parse = parse.replaceFirst("([^0-9])[0-9]{1,2}([^0-9]?)", "$1ss$2");
        SimpleDateFormat format = new SimpleDateFormat(parse);
        result = format.parse(str);
        return result;
    }

    public static boolean isEmptyOrNull(Object data) {
        if (data == null) {
            return true;
        }
        if (data.toString().length() == 0) {
            return true;
        }
        return false;
    }

    public static Integer toInteger(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        return Integer.valueOf(data.toString());
    }

    public static Integer toInteger(Object data, Integer defaultValue) {
        Integer value = toInteger(data);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }


    public static Integer doubleToInteger(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }

        double d = Double.valueOf(data.toString());
        return (int) d;
    }

    public static Long toLong(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        return Long.valueOf(data.toString());
    }

    public static Long doubleToLong(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        double d = Double.valueOf(data.toString());
        return (long) d;
    }

    public static Short toShort(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        return Short.valueOf(data.toString());
    }

    public static Double toDouble(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        return Double.valueOf(data.toString());
    }


    public static String toDoubleString(Object data, Integer scale) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        //        DecimalFormat df = new DecimalFormat("#.00000");
        //        return df.format(data);
        //
        NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(false);
        //小数点后保留几
        nf.setMaximumFractionDigits(scale);
        return nf.format(data);
    }


    public static String toString(Object data) {
        if (data == null) {
            return null;
        }
        return String.valueOf(data.toString());
    }

    public static Boolean toBoolean(Object data) {
        if (isEmptyOrNull(data)) {
            return null;
        }
        return Boolean.valueOf(data.toString());
    }
}
