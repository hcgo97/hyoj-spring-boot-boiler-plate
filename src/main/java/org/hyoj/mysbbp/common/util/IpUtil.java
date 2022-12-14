package org.hyoj.mysbbp.common.util;

import com.google.common.base.Strings;

import javax.servlet.http.HttpServletRequest;

public class IpUtil {

    /**
     * localAddress 로 정의한 request header 의 X-Auth-Local-Address 가져오기
     *
     * @param request
     * @return
     */
    public static String getLocalIp(HttpServletRequest request) {

        return request.getHeader("X-Auth-Local-Address");
    }

    /**
     * http header (X-Forward-For) proxy 를 통하면 생성되는 클라이언트ip, proxy 주소 중 첫번째 주소를 리턴
     * X-Forward-For 가 없으면 getRemoteAddr 를 리턴
     *
     * @param request
     * @return
     */
    public static String getPublicIp(HttpServletRequest request) {
        //proxy (ALB) 를 통한 경우 header 의 X-FORWARDED-FOR 를 사용하고,
        String publicIp = request.getHeader("X-Forwarded-For");

        //X-FORWARDED-FOR 가 없으면 request.getRemoteAddr() 을 사용함
        if (publicIp == null) {
            publicIp = request.getRemoteAddr();
        } else {
            publicIp = getPublicIpFromXForwardedFor(publicIp);
        }
        return publicIp;
    }

    /**
     * http header 중 proxy 를 통하면 생성되는 클라이언트ip, proxy 주소 중 첫번째 주소를 리턴
     *
     * @param publicIp
     * @return
     */
    private static String getPublicIpFromXForwardedFor(String publicIp) {
        if (Strings.isNullOrEmpty(publicIp)) return "";
        String[] strings = publicIp.split(",");
        publicIp = strings[0];
        return publicIp.trim();
    }

}
