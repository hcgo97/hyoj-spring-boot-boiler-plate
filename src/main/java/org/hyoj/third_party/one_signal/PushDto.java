package org.hyoj.third_party.one_signal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PushDto {

    /**
     * OneSignal 기본 응답 구조
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class Response<T> {
        private String id;
        private Integer recipients;
        private String externalId;
        private T errors;
    }

    /**
     * OneSignal notification body
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class NotificationBody {

        private String appId; // app key

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<String> includedSegments; // 푸쉬 보낼 segments 그룹

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<String> includeExternalUserIds; // 푸쉬 보낼 external user ids

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String channelForExternalUserIds; // 설정한 채널에 있는 유저에게만 전송

        private MessageBody headings; // 푸쉬 제목
        private MessageBody contents; // 푸쉬 내용
    }

    /**
     * Notification headings, contents Object
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    public static class MessageBody {
        private String en;
    }

}
