package org.hyoj.mysbbp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

public class UserDto {

    /**
     * 로그인 Input
     */
    @Getter
    @Setter
    @ToString
    public static class SignInDto {
        private String userId;

        // password는 쓰기전용
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;
    }

    /**
     * 회원가입 Input
     */
    @Getter
    @Setter
    @ToString
    public static class SignUpDto {
        private String userId;

        // password는 쓰기전용
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String nickname;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String phoneNumber;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String email;
    }

    /**
     * 토큰 정보
     */
    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
    }

    /**
     * 유저 정보
     */
    @Getter
    @Setter
    @ToString
    public static class UserInfoDto implements Serializable {
        private String id;

        private String userId;

        private String roleCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String nickname;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String phoneNumber;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String email;
    }

}
