package org.hyoj.mysbbp.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiResultStatus {

    REQUEST_SUCCESS("20000", "정상적으로 처리되었습니다.", HttpStatus.OK),

    // 400 errors
    CLIENT_ERROR("40000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    CLIENT_INVALID_PARAM("40001", "%s", HttpStatus.BAD_REQUEST),

    // 401 errors
    UNAUTHORIZED("40100", "허가되지 않는 접근입니다.", HttpStatus.UNAUTHORIZED),
    API_KEY_IS_REQUIRED("40101", "Api-Key는 필수 값 입니다.", HttpStatus.UNAUTHORIZED),
    API_KEY_IS_INVALID("40102", "유효하지 않은 Api-Key 입니다.", HttpStatus.UNAUTHORIZED),

    // 403 errors
    FORBIDDEN("40300", "권한 없음 해당 권한은 호출이 불가합니다.", HttpStatus.FORBIDDEN),
    BLOCKED_BY_ADMIN("40301", "관리자에 의해 차단되었습니다.", HttpStatus.FORBIDDEN),

    // 404 errors
    NOT_FOUND("40400", "해당 데이터 또는 경로를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 사용자 관련 (10~14)
    LOGIN_FAILED("40410", "로그인 정보가 일치하지 않습니다.", HttpStatus.NOT_FOUND),
    ALREADY_SIGNED_UP("40911", "이미 가입된 유저입니다.", HttpStatus.CONFLICT),
    USER_NOT_FOUND("40412", "존재하지 않는 유저입니다.", HttpStatus.NOT_FOUND),

    // OTP 관련 (15~20)
    OTP_HAS_ALREADY_REQUESTED("40915", "인증번호가 이미 요청되었습니다. 1분 후 다시 요청해주세요.", HttpStatus.CONFLICT),
    OTP_NEVER_SENT("40416", "인증 요청한 이력이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    OTP_CERT_FAILED("40017", "인증번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    OTP_CERT_DATE_EXPIRED("40418", "인증번호가 만료되었습니다.", HttpStatus.NOT_FOUND),

    // 토큰 관련 (21~25)
    TOKEN_NOT_FOUND("40121", "토큰을 찾을 수 없습니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_DATE_EXPIRED("40122", "토큰이 만료되었습니다. 보안을 위해 다시 로그인 해주세요.", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALID("40123", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    TOKEN_CREATED_FAILED("50024", "토큰 생성 중 에러가 발생하였습니다. 다시 시도해 주세요.", HttpStatus.INTERNAL_SERVER_ERROR),

    // server error (90000~)
    INTERNAL_SERVER_ERROR("90000", "서버에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ACCESS_ERROR("90001", "Database Access 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("90002", "외부 서버와 통신 중 에러가 발생했습니다.", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    public static String messageOfCode(String code) {
        ApiResultStatus[] var1 = values();
        int enumLength = var1.length;

        for (ApiResultStatus resultStatus : var1) {
            if (resultStatus.code.equals(code)) {
                return resultStatus.getMessage();
            }
        }

        throw new IllegalArgumentException("No matching message for [" + code + "]");
    }

}
