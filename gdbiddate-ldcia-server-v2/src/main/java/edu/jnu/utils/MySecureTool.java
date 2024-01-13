package edu.jnu.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @作者: 郭梓繁
 * @邮箱: 826148267@qq.com
 * @版本: 1.0
 * @创建日期: 2023年05月09日 14时59分
 * @功能描述: 自己的安全工具类
 */
public class MySecureTool {

    private static byte[] encryptECB(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    private static byte[] decryptECB(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    /**
     * AES加密算法
     * 要求密钥和数据都必须是Base64形式的字符串
     * @param data 要加密的数据，必须是base64格式的字符串
     * @param key 密钥，必须是base64格式的字符串，解密后需要为16位，24位，32位
     * @return 加密后的字符串base64形式的密文字符串
     */
    public static String encrypt(String data, String key) {
        try {
            return
                    Base64.getEncoder().encodeToString(
                        encryptECB(
                                Base64.getDecoder().decode(data),
                                Base64.getDecoder().decode(key)
                        )
                    );
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES解密算法
     * 要求密钥和密文都必须是Base64形式的字符串
     * @param cipherText 要解密的数据，必须是base64格式的字符串
     * @param key 密钥，必须是base64格式的字符串，解密后需要为16位，24位，32位
     * @return 解密后的字符串base64形式的明文字符串
     */
    public static String decrypt(String cipherText, String key) {
        try {
            // 密钥的字符串长度只能是16，24，32 （128bit, 192bit, 256bit） MD5的结果是128bit
            return
                    Base64.getEncoder().encodeToString(
                            decryptECB(
                                    Base64.getDecoder().decode(cipherText),
                                    Base64.getDecoder().decode(key)
                            )
                    );
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException |
                 IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 利用base64格式读取出文件内容，并据文件内容获取去重加密密钥，得到16位的去重加密密钥，并将密钥用base64进行编码
     * @param file  文件
     * @return 16位的加密密钥
     */
    public static String getDedupKey(File file) {
        try {
            String content = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
            MessageDigest md5 = MessageDigest.getInstance( "MD5");
            byte[] digestedKey = md5.digest(content.getBytes());
            return Base64.getEncoder().encodeToString(digestedKey);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptDedupKey(String dedupKey, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digestedKey = md5.digest(key.getBytes());
            return encrypt(dedupKey, Base64.getEncoder().encodeToString(digestedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptDedupKey(String dedupKeyCipher, String key) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digestedKey = md5.digest(key.getBytes());
            return decrypt(dedupKeyCipher, Base64.getEncoder().encodeToString(digestedKey));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
