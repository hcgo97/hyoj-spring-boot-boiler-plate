package org.hyoj.mysbbp.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class StringUtil {

    public static final String EMPTY_STRING = "";
    public static final int EMPTY_Integer = 0;


    /**
     * 스트링을 원하는 길이로 자르는 method
     *
     * @param src
     * @param length
     * @return
     */
    public static String truncateStr(String src, int length) {
        return src != null && src.length() > length ? src.substring(0, length) : src;
    }

    /**
     * NULL 안전한 string length
     *
     * @param src
     * @return
     */
    public static int getStrLength(String src) {
        int result = 0;
        if (!Strings.isNullOrEmpty(src)) {
            result = src.length();
        }
        return result;
    }


    /**
     * integer 로 변환하기, 숫자가 없으면 0 리턴
     *
     * @param src
     * @return
     */
    public static int parseInt(String src) {

        int result = 0;
        if (Strings.isNullOrEmpty(src)) {
            return 0;
        }

        try {
            result = Integer.parseInt(src);
        } catch (NumberFormatException e) {
        }
        return result;
    }


    /**
     * long 으로 변환하기, 숫자가 없으면 0 리턴
     *
     * @param src
     * @return
     */
    public static long parseLong(String src) {

        long result = 0L;
        if (Strings.isNullOrEmpty(src)) {
            return 0L;
        }

        try {
            result = Long.parseLong(src);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * double 로 변환하기, 숫자가 없으면 0 리턴
     *
     * @param src
     * @return
     */
    public static double parseDouble(String src) {

        double result = 0D;
        if (Strings.isNullOrEmpty(src)) {
            return 0D;
        }

        try {
            result = Double.parseDouble(src);
        } catch (NumberFormatException e) {
        }
        return result;
    }

    /**
     * integer 를 스트링으로 변환
     *
     * @param src
     * @return
     */
    public static String getIntToString(int src) {
        return Integer.toString(src);
    }

    /**
     * long 을 스트링으로 변환
     *
     * @param src
     * @return
     */
    public static String getLongToString(long src) {
        return Long.toString(src);
    }

    /**
     * 왼쪽에 원하는 문자 채우기
     *
     * @param inputString
     * @param length
     * @param padChar
     * @return
     */
    public static String padLeft(String inputString, int length, String padChar) {
        if (inputString.length() >= length) {
            return inputString;
        }

        if (Strings.isNullOrEmpty(padChar)) padChar = " ";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(padChar);
        }
        return sb.toString() + inputString;
    }

    /**
     * 오늘쪽에 원하는 문자 채우기
     *
     * @param inputString
     * @param length
     * @param padChar
     * @return
     */
    public static String padRight(String inputString, int length, String padChar) {
        if (inputString.length() >= length) {
            return inputString;
        }

        if (Strings.isNullOrEmpty(padChar)) padChar = " ";

        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append(padChar);
        }

        return inputString + sb.toString();
    }

    /**
     * null과 empty 에 안전하게 공백만 제거
     *
     * @param src
     * @return
     */
    public static String trimAll(String src) {
        if (!Strings.isNullOrEmpty(src)) {
            return src.replaceAll("\\s+", "");
        } else {
            return "";
        }
    }


    /**
     * 문자열에서 홀수번째 문자열만 가져오기
     *
     * @param src
     * @return
     */
    public static String getOddChar(String src) {
        String result = "";
        if (Strings.isNullOrEmpty(src)) return result;

        char[] charArray = src.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (((i + 1) % 2) != 0) {
                result += String.valueOf(charArray[i]);
            }
        }
        return result;
    }

    /**
     * 문자열에서 짝수번째 문자열만 가져오기
     *
     * @param src
     * @return
     */
    public static String getEvenChar(String src) {
        String result = "";
        if (Strings.isNullOrEmpty(src)) return result;

        char[] charArray = src.toCharArray();

        for (int i = 0; i < charArray.length; i++) {
            if (((i + 1) % 2) == 0) {
                result += String.valueOf(charArray[i]);
            }
        }
        return result;
    }


    /**
     * null과 empty 에 안전한 문자열 트림
     *
     * @param src
     * @return
     */
    public static String trim(String src) {
        if (!Strings.isNullOrEmpty(src)) {
            return src.trim();
        } else {
            return "";
        }
    }


    /**
     * null과 empty 에 안전하게 소문자로 변환
     *
     * @param src
     * @return
     */
    public static String toLowerCase(String src) {
        if (!Strings.isNullOrEmpty(src)) {
            return src.toLowerCase();
        } else {
            return "";
        }
    }

    /**
     * null과 empty 에 안전하게 대문자로 변환
     *
     * @param src
     * @return
     */
    public static String toUpperCase(String src) {
        if (!Strings.isNullOrEmpty(src)) {
            return src.toUpperCase();
        } else {
            return "";
        }
    }

    /**
     * Return NVL ''
     *
     * @param obj
     * @return
     */
    public static String nvl(Object obj) {

        String retValue = "";

        if (obj == null)
            return retValue;
        else
            retValue = (String) obj;

        return retValue;
    }

    /**
     * Return NVL value
     *
     * @param src
     * @param ret
     * @return
     */
    public static String nvl(String src, String ret) {
        if (src == null || src.equals("null") || src.equals(""))
            return ret;
        else
            return src;
    }


    public static String replace(String src, String from, String to) {
        if (src == null || src.length() == 0 || from == null
                || from.length() == 0 || to == null) {
            return src;
        }

        StringBuffer dBuff = new StringBuffer();
        int sIdx = 0; // start
        int eIdx = src.indexOf(from); // end

        int srcLen = from.length();
        while (eIdx >= sIdx) {
            dBuff.append(src.substring(sIdx, eIdx));
            dBuff.append(to);
            sIdx = eIdx + srcLen;
            eIdx = src.indexOf(from, sIdx);
        }

        dBuff.append(src.substring(sIdx));

        return dBuff.toString();
    }

    public static String fillLeftString(String src, char fill, int len) {
        StringBuffer ret;
        if (src == null) {
            ret = new StringBuffer(0);
            for (int i = 0; i < len; i++)
                ret.append(fill);
            return ret.toString();
        }

        byte[] bText = src.getBytes(); // Text
        int src_len = bText.length;

        if (src_len >= len)
            return src;

        ret = new StringBuffer(0);
        for (int i = 0; i < (len - src_len); i++)
            ret.append(fill);
        ret.append(src);
        return ret.toString();
    }


    public static int nvl(Object src, int ret) {
        if (src == null)
            return ret;
        else
            return Integer.valueOf(src.toString());
    }

    public static String nvlnull(String src) {
        if (src == null)
            return EMPTY_STRING;
        else if (src.equals("null"))
            return EMPTY_STRING;
        else
            return src;
    }

    public static int nvlnull(Integer src) {
        if (src == null)
            return EMPTY_Integer;
        else
            return src;
    }

    public static long nvlnull(Long src) {
        if (src == null)
            return EMPTY_Integer;
        else
            return src;
    }

    /**
     * 문자열이 null, isEmpty, 빈 공백인지 확인
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null) return true;
        if (str.length() > 0) return false;
        if ("".equals(str)) return true;
        return true;
    }


    public static String randomStringGenerator(int size) {

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < size; i++) {
            if (i % 3 == 0) {
                sb.append(randomLowerCharacter());
            } else if (i % 3 == 1) {
                sb.append(randomUpperCharacter());
            } else {
                sb.append(randomDigitCharacter());
            }
        }

        return sb.toString();
    }


    public static char randomLowerCharacter() {
        // 97(a) ~ 122(z)
        return (char) (Math.floor(Math.random() * (122 - 97 + 1)) + 97);
    }


    public static char randomUpperCharacter() {
        // 65(A) ~ 90(Z)
        return (char) (Math.floor(Math.random() * (90 - 65 + 1)) + 65);
    }

    public static char randomDigitCharacter() {
        // 48(0) ~ 57(9)
        return (char) (Math.floor(Math.random() * (57 - 48 + 1)) + 48);
    }

    public static double getDouble(Object number) {
        double result = 0;

        String str_number = nvl(number + "", "");

        if (!"".equals(nvl(str_number))) {
            result = Double.parseDouble(str_number);
        }

        return result;
    }

    public static int getInt(Object number) {
        int result = 0;

        String str_number = nvl(number + "", "");

        if (!"".equals(nvl(str_number))) {
            result = Integer.parseInt(str_number);
        }

        return result;
    }

    public String getCookie(HttpServletRequest req, String key) {

        String ret = "";
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            ret = "";
        } else {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(key)) {
                    ret = cookies[i].getValue();
                }
            }
        }

        return ret;
    }

    public void setCookie(HttpServletResponse res, String key, String data, int expiryDay, String path) {
        Cookie cookie = new Cookie(key, data);
        cookie.setMaxAge(60 * 60 * 24 * expiryDay);
        cookie.setPath(path);
        res.addCookie(cookie);
    }

    public static String sha256String(String str) {

        String SHA = "";
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            SHA = str;
        }

        return SHA;
    }

    /**
     * @param source
     * @param length
     * @param lParam
     * @return
     * @throws Exception
     */
    public static final String lpad(int source, int length, String lParam) throws Exception {
        String val = new Integer(source).toString();

        int cnt = length - val.length();

        for (int i = 0; i < cnt; i++) {
            val = lParam.concat(val);
        }

        return val;
    }

    /**
     * @param source
     * @param length
     * @param rParam
     * @return
     * @throws Exception
     */
    public static final String rpad(String source, int length, String rParam) throws Exception {
        String val = source;

        int cnt = length - val.length();

        for (int i = 0; i < cnt; i++) {
            val = val.concat(rParam);
        }

        return val;
    }

    /**
     * 주민등록번호로 남자, 여자 구분
     *
     * @param ssn
     * @return
     * @throws Exception
     */
    public static final String isSexOfSsn(String ssn) throws Exception {
        String sexChar = ssn.substring(6, 7);

        if (sexChar.equals("1") || sexChar.equals("3")) {
            return "M"; // 남자
        }
        // 2 or 4
        else {
            return "F"; // 여자
        }
    }

    public static final boolean getCompriseStr(String source, String word) {
        if (source.indexOf(word) > -1)
            return true;
        else
            return false;
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (Exception ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            //ex.printStackTrace();
            try {
                new Gson().toJson(test, JsonArray.class);
            } catch (Exception ex1) {
                //ex1.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * Hex String -> Base64 String 변환
     * (Hex String -> byte[] -> Base64 String)
     *
     * @param secretKeyHex
     * @return
     * @throws DecoderException
     */
    public static String hexStringToBase64(String secretKeyHex) throws DecoderException {
        return Base64.getUrlEncoder().encodeToString(Hex.decodeHex(secretKeyHex));
    }


    /**
     * json string을 Object로 변환
     *
     * @param jsonData
     * @param convertType
     * @return
     * @throws IOException
     */
    public static Object jsonData2Object(String jsonData, Object convertType) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonData, convertType.getClass());
    }

    /**
     * 휴대폰 번호에 '-' 붙여서 변환
     *
     * @param phoneNumber 01012341234
     * @return 010-1234-1234
     */
    public static String convertPhoneNumberFormat(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }

        if (phoneNumber.length() == 8) {
            return phoneNumber.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");

        } else if (phoneNumber.length() == 12) {
            return phoneNumber.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }

        return phoneNumber.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }

}
