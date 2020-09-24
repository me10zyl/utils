package com.yilnz.utils;

import com.yilnz.bluesteel.encoding.EncodingUtil;

import java.math.BigInteger;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class EncodingTests {
    public static void main(String[] args) throws Exception {
        String inputStr = "简单加密";
        System.err.println("原文:\n" + inputStr);

        byte[] inputData = inputStr.getBytes();
        String code = EncodingUtil.encryptBASE64(inputData);

        System.err.println("BASE64加密后:\n" + code);

        byte[] output = EncodingUtil.decryptBASE64(code);

        String outputStr = new String(output);

        System.err.println("BASE64解密后:\n" + outputStr);

        // 验证BASE64加密解密一致性  
        assertEquals(inputStr, outputStr);

        // 验证MD5对于同一内容加密是否一致  
        byte[] val = EncodingUtil.encryptMD5(inputData);
        assertArrayEquals(val, val);

        // 验证SHA对于同一内容加密是否一致  
        assertArrayEquals(EncodingUtil.encryptSHA(inputData), EncodingUtil
                .encryptSHA(inputData));

        String key = EncodingUtil.initMacKey();
        System.err.println("Mac密钥:\n" + key);

        // 验证HMAC对于同一内容，同一密钥加密是否一致  
        assertArrayEquals(EncodingUtil.encryptHMAC(inputData, key), EncodingUtil.encryptHMAC(
                inputData, key));

        BigInteger md5 = new BigInteger(val);
        //System.out.println(new BigInteger(DigestUtils.md5Digest(inputStr.getBytes())).toString(16));
        System.err.println("MD5:\n" + md5.toString(16));
        System.out.println(new String(getString(val)));

        BigInteger sha = new BigInteger(EncodingUtil.encryptSHA(inputData));
        System.err.println("SHA:\n" + sha.toString(16));
        System.err.println("SHA:\n" + getString(EncodingUtil.encryptSHA(inputData)));

        BigInteger mac = new BigInteger(EncodingUtil.encryptHMAC(inputData, inputStr));
        System.err.println("HMAC:\n" + mac.toString(16));
    }

    private static String getString( byte[] bytes )
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
}
