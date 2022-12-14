package org.hyoj.third_party.sens;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class SensMessageRequest {

    private String type;        // SMS,LMS  필수
    private String countryCode = "82"; // 국가번호
    private String from;        // 사전 등록된 발신번호  필수
    private String subject;     // 기본 메시지 제목. (LMS에서만 사용가능)
    private String contentType = "COMM"; // (COMM: 일반메시지, AD: 광고메시지)
    private String content;     // 기본 메시지 컨텐츠 내용    필수

    private List<Message> messages;    // 수신번호 목록

    @Getter
    @Setter
    @ToString
    @AllArgsConstructor
    public static class Message {
        String to; // 수신번호
    }

}
