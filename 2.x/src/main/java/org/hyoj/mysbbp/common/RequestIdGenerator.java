package org.hyoj.mysbbp.common;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.UUID;

@Component
@RequestScope
public class RequestIdGenerator {

    private String requestId;


    public RequestIdGenerator() {
        this.requestId = createRequestId();
    }


    public String getRequestId() {
        return requestId;
    }

    private String createRequestId() {
        // 요청 트랜잭션 ID 생성
        return UUID.randomUUID().toString();
    }
}
