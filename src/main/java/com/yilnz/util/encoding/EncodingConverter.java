package com.yilnz.util.encoding;

import java.nio.charset.Charset;


/**
 * The type Encoding converter.
 * @author zyl
 * @date 2019年10月17日10:08:03
 */
public class EncodingConverter {

    private String string;
    private Charset charset;

    /**
     * Instantiates a new Encoding converter.
     *
     * @param string  the string
     * @param charset the charset
     */
    public EncodingConverter(String string, Charset charset){
        this.string = new String(string.getBytes(), charset);
        this.charset = charset;
    }

    /**
     * Instantiates a new Encoding converter.
     *
     * @param string the string
     */
    public EncodingConverter(String string) {
        this(string, Charset.forName("UTF-8"));
    }

    /**
     * To utf 8 binary string.
     *
     * @return the string
     */
    public String toUTF8Binary(){
        final byte[] bytes = this.string.getBytes(Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length;i++){
            sb.append(String.format("%8s,", Integer.toBinaryString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * To utf 8 hex string.
     *
     * @return the string
     */
    public String toUTF8Hex(){
        final byte[] bytes = this.string.getBytes(Charset.forName("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length;i++){
            sb.append(String.format("0x%4s,", Integer.toHexString(bytes[i] & 0xFF)).replace(' ', '0'));
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * To utf 32 hex string.
     *
     * @return the string
     */
    public String toUTF32Hex(){
        final byte[] bytes = this.string.getBytes(Charset.forName("UTF-32"));
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length;i++){
            sb.append(Integer.toHexString(bytes[i] & 0xFF));
        }
        sb = new StringBuilder(String.format("%8s", sb.toString()).replace(' ', '0'));
        sb.insert(0, "\\U");
        return sb.toString();
    }

    /**
     * To utf 16 hex string.
     *
     * @return the string
     */
    public String toUTF16Hex(){
        final byte[] bytes = this.string.getBytes(Charset.forName("UTF-16"));
        StringBuilder sb = new StringBuilder();
        final int code = Integer.parseInt(toUTF32Hex().replace("\\U", ""), 16);
        int offset = 4;
        if (code <= 0xFFFF) {
            offset = 2;
        }
        for(int i = bytes.length - offset; i < bytes.length;i++){
            if (i % 2 == 0) {
                sb.append("\\u");
            }
            sb.append(Integer.toHexString(bytes[i] & 0xFF));
        }
        return sb.toString();
    }

    /**
     * To html entity hex string.
     *
     * @return the string
     */
    public String toHtmlEntityHex(){
        final int i = Integer.parseInt(toUTF32Hex().replace("\\U", ""), 16);
        return "&#x" + Integer.toHexString(i);
    }

    /**
     * To html entity decimal string.
     *
     * @return the string
     */
    public String toHtmlEntityDecimal(){
        int i = Integer.parseInt(toUTF32Hex().replace("\\U", ""), 16);
        return "&#" + i;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        final EncodingConverter encodingConverter = new EncodingConverter(	"👽", Charset.forName("UTF-8"));
        System.out.println(encodingConverter.toUTF8Binary());
        System.out.println(encodingConverter.toUTF8Hex());
        System.out.println(encodingConverter.toUTF32Hex());
        System.out.println(encodingConverter.toUTF16Hex());
        System.out.println(encodingConverter.toHtmlEntityHex());
        System.out.println(encodingConverter.toHtmlEntityDecimal());
    }

}
