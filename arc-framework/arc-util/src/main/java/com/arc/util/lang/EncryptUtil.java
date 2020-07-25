package com.arc.util.lang;


import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

/**
 * 提供加密解密辅助功能。
 *
 * @author tianxiuquan
 */
public class EncryptUtil {
    private static final byte[] DFT_AES_KEY = {(byte) 0xf3, (byte) 0x91, (byte) 0xd6, (byte) 0xff, 0x32, 0x1f, 0x4a, 0x02, (byte) 0xe4, (byte) 0x88, 0x25, (byte) 0x90, 0x72, (byte) 0xb3, (byte) 0xa7, 0x11};
    private static final byte[] DFT_AES_IV = {0x15, 0x33, 0x21, (byte) 0x9f, (byte) 0xb6, (byte) 0xdc, (byte) 0xaf, (byte) 0xd8, (byte) 0xd4, 0x37, 0x5f, (byte) 0x95, 0x13, (byte) 0xe4, 0x72, (byte) 0xdd};
    private static final int SALT_LENGTH = 16;
    private static final String ENCODING = "utf-8";
    private static final String AES_CIPHER = "AES/CBC/PKCS5Padding";

    public static String aesEncrypt(String plainText) {
        return aesEncrypt(plainText, DFT_AES_KEY);
    }

    public static String aesEncrypt(String plainText, String key) {
        Objects.requireNonNull(key, "arg key");
        return aesEncrypt(plainText, getBytes(key));
    }

    private static String aesEncrypt(String plainText, byte[] key) {
        Objects.requireNonNull(plainText, "arg plainText");
        Objects.requireNonNull(key, "arg key");
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key, "AES"), new IvParameterSpec(DFT_AES_IV));
            // TODO: workaround to merge bcz cipher.update not work well???
            byte[] plainBytes = getBytes(plainText);
            byte[] saltBytes = genSaltBytes();
            byte[] plainAllBytes = ArrayUtils.addAll(plainBytes, saltBytes);
            byte[] bytes = cipher.doFinal(plainAllBytes);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception ex) {
            throw new UncheckedException(ex);
        }
    }

    public static String aesDecrypt(String encryptedText) {
        return aesDecrypt(encryptedText, DFT_AES_KEY);
    }

    public static String aesDecrypt(String encryptedText, String key) {
        Objects.requireNonNull(key, "arg key");
        return aesDecrypt(encryptedText, getBytes(key));
    }

    private static String aesDecrypt(String encryptedText, byte[] key) {
        Objects.requireNonNull(encryptedText, "arg encryptedText");
        Objects.requireNonNull(key, "arg key");
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(key, "AES"), new IvParameterSpec(DFT_AES_IV));
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] plainBytes = cipher.doFinal(encryptedBytes);
            return new String(plainBytes, 0, plainBytes.length - SALT_LENGTH, ENCODING);
        } catch (Exception ex) {
            throw new UncheckedException(ex);
        }
    }

    private static String aesDecrypt(String encryptedText, byte[] key, String iv) {
        Objects.requireNonNull(encryptedText, "arg encryptedText");
        Objects.requireNonNull(key, "arg key");
        try {
            Cipher cipher = Cipher.getInstance(AES_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(key, "AES"), new IvParameterSpec(getBytes(iv)));
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            byte[] plainBytes = cipher.doFinal(encryptedBytes);
            return new String(plainBytes, 0, plainBytes.length - SALT_LENGTH, ENCODING);
        } catch (Exception ex) {
            throw new UncheckedException(ex);
        }
    }

    public static String aesDecrypt(String encryptedText, String key, String iv) {
        Objects.requireNonNull(key, "arg key");
        return aesDecrypt(encryptedText, getBytes(key), iv);
    }

    private static byte[] getBytes(String s) {
        try {
            return s.getBytes(ENCODING);
        } catch (Exception ex) {
            throw new UncheckedException(ex);
        }
    }

    private static byte[] genSaltBytes() {
        Random random = new Random();
        byte[] bytes = new byte[SALT_LENGTH];
        random.nextBytes(bytes);
        return bytes;
    }

    /**
     * 使用 MD5 加密，返回十六进制表示的密文字符串。
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        return hash("MD5", text);
    }

    /**
     * 使用 SHA-256 加密，返回十六进制表示的密文字符串。
     *
     * @param text
     * @return
     */
    public static String sha256(String text) {
        return hash("SHA-256", text);
    }

    /**
     * Hash
     *
     * @param algorithm 算法, 如: MD5/SHA-256/SHA-512 等
     * @param text
     * @return 返回十六进制表示的密文字符串
     */
    public static String hash(final String algorithm, final String text) {
        Objects.requireNonNull(algorithm, "algorithm can't be null");
        Objects.requireNonNull(text, "text can't be null");
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte bytes[] = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new UncheckedException(e);
        }
    }

    /**
     * MD5 动态口令
     *
     * @param sinData
     * @return
     */
    public static String transfer(byte[] sinData) {
        /**
         * 取16长度数组前15个元素来构成前五位数字，最后一个元素作为第六位
         * 循环六次
         * */
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < 6; i++) {

            if (i == 5) {
                int x = sinData[5] & 192 >>> 6;
                int z = sinData[5] & 3;
                int y = sinData[5] & 12 >>> 2;
                buffer.append(x + z + y);
            } else {

                int x = sinData[i * 3] & 192 >>> 6;
                int z = sinData[i * 3 + 1] & 3;
                int y = sinData[i * 3 + 2] & 12 >>> 2;
                buffer.append(x + z + y);
            }

        }

        return buffer.toString();

    }


    private static char[] base64EncodeChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/'};

    public static String base64Encode(byte[] data) {
        StringBuffer sb = new StringBuffer();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len) {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

}
