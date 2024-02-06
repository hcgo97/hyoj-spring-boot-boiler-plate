package org.hyoj.third_party.kakao;

public class KakaoException extends RuntimeException {

    private String message;

    public KakaoException() {
    }

    public KakaoException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return message;
    }

}