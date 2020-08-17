package com.yilnz.util.base;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public abstract class EncodingUtil {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * MAC算法可选以下多种算法
     *
     * <pre>
     * HmacMD5
     * HmacSHA1
     * HmacSHA256
     * HmacSHA384
     * HmacSHA512
     * </pre>
     */
    public static final String KEY_MAC = "HmacMD5";

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * 
     */
    public static byte[] decryptBASE64(String key)  {
        try {
            return (new BASE64Decoder()).decodeBuffer(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * 
     */
    public static String encryptBASE64(byte[] key)  {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * 
     */
    public static byte[] encryptMD5(byte[] data)  {

        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance(KEY_MD5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md5.update(data);

        return md5.digest();

    }

    /**
     * MD5加密
     * @param data
     * @return
     */
    public static String encryptMD5(String data){
        return getString(encryptMD5(data.getBytes()));
    }

    /**
     * bytes to hex
     * @param bytes
     * @return
     */
    public static String getString( byte[] bytes )
    {
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<bytes.length; i++ )
        {
            byte b = bytes[ i ];
            String hex = Integer.toHexString((int) 0x00FF & b);
            if (hex.length() == 1)
            {
                sb.append("0");
            }
            sb.append( hex );
        }
        return sb.toString();
    }
    

    /**
     * SHA加密
     *
     * @param data
     * @return
     * 
     */
    public static byte[] encryptSHA(byte[] data)  {

        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance(KEY_SHA);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        sha.update(data);

        return sha.digest();

    }

    /**
     * SHA 加密
     * @param str
     * @return
     */
    public static String encryptSHA(String str){
        return getString(encryptSHA(str.getBytes()));
    }

    /**
     * HMAC加密
     * @param data
     * @param key
     * @return
     */
    public static String encryptHMAC(String data, String key){
        return getString(encryptHMAC(data.getBytes(), key));
    }

    /**
     * 初始化HMAC密钥
     *
     * @return
     * 
     */
    public static String initMacKey()  {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        SecretKey secretKey = keyGenerator.generateKey();
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * HMAC加密
     *
     * @param data
     * @param key
     * @return
     * 
     */
    public static byte[] encryptHMAC(byte[] data, String key)  {

        SecretKey secretKey = new SecretKeySpec(decryptBASE64(key), KEY_MAC);
        Mac mac = null;
        try {
            mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return mac.doFinal(data);

    }
}