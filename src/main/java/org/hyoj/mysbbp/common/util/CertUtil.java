package org.hyoj.mysbbp.common.util;

import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class CertUtil {

    // '0', '1', 'i', 'l', 'o' 제외
    private static final char[] CHAR_LIST = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};

    private static final List<Character> EXCEPT_CHAR_LIST = Arrays.asList('0', '1', 'i', 'l', 'o');


    /**
     * Char List를 이용한 난수생성 - 임시비밀번호 생성에 사용
     *
     * @param length 자리수
     * @return
     */
    public static String genRandomCode(int length) {

        Random random = new Random(System.currentTimeMillis());
        int tablelength = CHAR_LIST.length;
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < length; i++) {

            char rChar = CHAR_LIST[random.nextInt(tablelength)];
            buf.append(rChar);
        }

        return buf.toString();
    }

    /**
     * UUID를 이용한 난수생성 - 임시비밀번호 생성에 사용
     *
     * @param length 난수자리수
     * @return
     */
    public static String genRandomCodeByUUID(int length) {

        String uuid = UUID.randomUUID().toString().replaceAll("-", ""); // -를 제거.
        uuid = uuid.substring(0, length); //uuid를 앞에서부터 10자리 잘라줌.

        return uuid;
    }

    /**
     * 요청자리수에 해당하는 랜덤숫자 생성 - SMS 본인인증에 사용
     *
     * @param certNumLength 자리수
     * @return
     */
    public static int genCertNumber(int certNumLength) {
        Random random = new Random(System.currentTimeMillis());

        int range = (int) Math.pow(10, certNumLength);
        int trim = (int) Math.pow(10, certNumLength - 1);
        int result = random.nextInt(range) + trim;

        if (result > range) {
            result = result - trim;
        }

        return result;
    }

    /**
     * 랜덤 Hex 생성
     *
     * @return
     */
    public static String createRandomIvHex() {
        int bytesLength = 16;
        byte[] array = new byte[bytesLength];
        new Random().nextBytes(array);
        return Hex.toHexString(array);
    }

}
