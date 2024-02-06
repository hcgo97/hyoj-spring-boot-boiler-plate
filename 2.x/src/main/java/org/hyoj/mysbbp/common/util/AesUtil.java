package org.hyoj.mysbbp.common.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.hyoj.mysbbp.common.enums.AesEncodeTypeEnum;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Component
public class AesUtil {

    public static final String ALGORITHM = "AES";
    private static final String AES_MODE_CBC = "AES/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE = 256;

    public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};


    public static String encrypt(String str, String key, AesEncodeTypeEnum encodingType) throws Exception {
        byte[] keyData = key.getBytes();

        SecretKey secureKey = new SecretKeySpec(keyData, ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, secureKey, new IvParameterSpec(ivBytes));

        byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));

        return byteToStringWithType(encrypted, encodingType);
    }

    public static String decrypt(String encryptStr, String key, AesEncodeTypeEnum decodingType) throws Exception {
        byte[] keyData = key.getBytes();

        byte[] encryptBytes = stringToByteWithType(encryptStr, decodingType);

        SecretKey secureKey = new SecretKeySpec(keyData, ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        cipher.init(Cipher.DECRYPT_MODE, secureKey, new IvParameterSpec(ivBytes));

        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(encryptBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.debug(encryptStr);
        }
        return ""; // 실패시 빈 문자열 리턴
    }

    /**
     * 주어진 키와 IV로 메시지를 암호화 합니다.
     * 리턴은 HexCode로 합니다.
     *
     * @param str
     * @param encryptKeyHex
     * @param ivHex
     * @return
     */
    public static String encrypt(String str, String encryptKeyHex, String ivHex) throws Exception {
        byte[] keyBytes = Hex.decode(encryptKeyHex);
        byte[] ivBytes = Hex.decode(ivHex);
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] encryptedBytes = cipher.doFinal(strBytes);

        return Hex.toHexString(encryptedBytes);
    }

    public static String decrypt(String encryptStr, String encryptKeyHex, String ivHex) throws Exception {
        byte[] keyBytes = Hex.decode(encryptKeyHex);
        byte[] ivBytes = Hex.decode(ivHex);
        byte[] encryptBytes = Hex.decode(encryptStr);

        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKey key = new SecretKeySpec(keyBytes, ALGORITHM);

        Cipher cipher = Cipher.getInstance(AES_MODE_CBC);
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

        byte[] decryptedBytes;
        try {
            decryptedBytes = cipher.doFinal(encryptBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.debug(encryptStr);
        }
        return ""; // 실패시 빈 문자열 리턴
    }

    /**
     * 암호화된 메시지에 IV를 포함합니다.
     * "ENCRYPTED_HEX"."IV_HEX"
     *
     * @param encryptStr
     * @param encryptKeyHex
     * @param ivHex
     * @return
     */
    public static String encryptWithIvString(String encryptStr, String encryptKeyHex, String ivHex) throws Exception {
        return encrypt(encryptStr, encryptKeyHex, ivHex) + "." + ivHex;
    }

    /**
     * 인코딩, 디코딩시 Base64, HEX 중에서 선택
     *
     * @param byteData
     * @param encodingType
     * @return
     */
    private static String byteToStringWithType(byte[] byteData, AesEncodeTypeEnum encodingType) {
        switch (encodingType) {
            case BASE64:
                return Base64.getUrlEncoder().encodeToString(byteData);
            case HEX:
                return Hex.toHexString(byteData);
        }
        return null;
    }

    private static byte[] stringToByteWithType(String stringData, AesEncodeTypeEnum decodingType) {
        switch (decodingType) {
            case BASE64:
                return Base64.getUrlDecoder().decode(stringData);
            case HEX:
                return Hex.decode(stringData);
        }
        return null;
    }

}
