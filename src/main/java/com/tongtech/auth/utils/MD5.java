package com.tongtech.auth.utils;

import org.springframework.util.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5 {
    public static String md5DigestAsHex(String str) {
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }
    /**
     * 字符串的MD5
     * @param plainText
     * @return
     */
    public static String encode(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件的MD5
     * @param filePath
     * @return
     */
    public static String getFileMD5(String filePath){
        String value = null;
        FileInputStream in = null;
        try {
            File file = new File(filePath);
            in = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int c;
            while ((c = in.read(buffer)) != -1) {
                md5.update(buffer, 0, c);
            }
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16).toUpperCase();
        } catch (Exception e) {

        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {

                }
            }
        }
        return value;
    }

//    public static void main(String[] args) {
//        String encode = encode("12345");
//        System.out.println(encode);
//    }
}
