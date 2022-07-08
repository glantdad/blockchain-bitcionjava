package com.undie.crash.demo.util;

import java.security.MessageDigest;
import java.security.interfaces.ECPublicKey;
import java.util.Base64;

/**
 * @Description
 * @Author glant
 * @Date 2022/6/19 16:07
 **/
public class SharUtil {

    private final static char[] digits = "0123456789ABCDEF".toCharArray();

    /**
 　　* 利用java原生的摘要实现SHA256加密
 　　* @param str 加密后的报文
 　　* @return
     */
    public static String shar256Str(String str){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * https://blog.csdn.net/rejames/article/details/83311397
     * @param publicKey 公钥封装对象
     * @return
     */
    public static String publicKey2Address(ECPublicKey publicKey) {
        MessageDigest sha;
        String encodeStr = "";
        try {
            //execute sha256
            sha = MessageDigest.getInstance("SHA-256");
            sha.update(publicKey.getEncoded());
            byte[] s1 = sha.digest();

            //execute ripemd160
            MessageDigest rmd = MessageDigest.getInstance("RipeMD160", "BC");
            byte[] r1 = rmd.digest(s1);

            //add 0x00 to first
            byte[] r2 = new byte[r1.length + 1];
            r2[0] = 0;
            for (int i = 0 ; i < r1.length ; i++) {
                r2[i+1] = r1[i];
            };

            //make check num   , double shar256
            byte[] s2 = sha.digest(r2);
            byte[] s3 = sha.digest(s2);

            //get first 4 bytes
            byte[] a1 = new byte[25];
            for (int i = 0 ; i < r2.length ; i++) a1[i] = r2[i];
            for (int i = 0 ; i < 5 ; i++) a1[20 + i] = s3[i];

            //base58 encode


        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
 　　* 将byte转为16进制
 　　* @param bytes
 　　* @return
 　　*/
    @Deprecated
    private static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length()==1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }

        return stringBuffer.toString();
    }

    /**
     * 高效写法 16进制字符串转成byte数组
     *
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    @Deprecated
    public static byte[] hexStringToBytes(String hex) {
        byte[] result = new byte[hex.length() / 2];
        char[] chars = hex.toCharArray();
        for (int i = 0, j = 0; i < result.length; i++) {
            result[i] = (byte) (toByte(chars[j++]) << 4 | toByte(chars[j++]));
        }
        return result;
    }

    private static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 0x0A);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 0x0a);
        throw new RuntimeException("invalid hex char '" + c + "'");
    }


    /**
     * 高效写法 byte数组转成16进制字符串
     *
     * @param bytes byte数组
     * @return 16进制字符串
     */
    @Deprecated
    public static String bytesToHexString(byte[] bytes) {
        char[] buf = new char[bytes.length * 2];
        int c = 0;
        for (byte b : bytes) {
            buf[c++] = digits[(b >> 4) & 0x0F];
            buf[c++] = digits[b & 0x0F];
        }
        return new String(buf);
    }

    /**
     * 简洁写法 16进制字符串转成byte数组
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    @Deprecated
    public static byte[] hexStringToBytesSimple(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    /**
     * 简洁写法 byte数组转成16进制字符串
     * @param bytes byte数组
     * @return 16进制字符串
     */
    @Deprecated
    public static String bytesToHexStringSimple(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }


}
