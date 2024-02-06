package org.hyoj.mysbbp.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.hyoj.mysbbp.common.EndPointHelper;
import org.hyoj.mysbbp.common.RequestIdGenerator;
import org.hyoj.mysbbp.model.VendorRequestHistories;
import org.hyoj.mysbbp.repository.VendorRequestHistoryRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Aspect
@RequiredArgsConstructor
@Component
public class VendorRequestAspect {

    private final RequestIdGenerator requestIdGenerator;
    private final VendorRequestHistoryRepository vendorRequestHistoryRepository;

    @Pointcut("execution(* org.hyoj.mysbbp.common.EndPointHelper.execute(..))")
    public void vendorEndPointHelpers() {
    }

    @AfterReturning(value = "vendorEndPointHelpers()", returning = "result")
    public void afterSuccess(JoinPoint joinPoint, Object result) throws Throwable {
        log.debug("[Success]");
        EndPointHelper joinedObject = (EndPointHelper) joinPoint.getTarget();
        saveHistory(joinedObject.createRequest(), joinedObject.createResponse());
    }

    @AfterThrowing(value = "vendorEndPointHelpers()", throwing = "throwing")
    public void afterExceptionThrowing(JoinPoint joinPoint, Throwable throwing) {
        log.info("[Exception]");
        EndPointHelper joinedObject = (EndPointHelper) joinPoint.getTarget();
        saveHistory(joinedObject.createRequest(), joinedObject.createResponse());
    }

    public void saveHistory(Request request, Response response) {
        String requestBody = null;
        String responseBody = null;

        try {
            if (request.body() != null) {
                final Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                requestBody = buffer.readUtf8();
            }

            if (response.body() != null) {
                responseBody = response.body().string();
            }

        } catch (IOException e) {
            responseBody = "[IOException: Cannot convert into String]";
            log.error("IOException Occurred");
        }

        VendorRequestHistories requestHistory = new VendorRequestHistories();
        requestHistory.setRequestId(requestIdGenerator.getRequestId());
        requestHistory.setEndPoint(request.url().toString());
        requestHistory.setRequestHeader(request.headers().toString());
        requestHistory.setRequestMethod(request.method());
        requestHistory.setRequestBody(requestBody);
        requestHistory.setResponseHeader(response.headers().toString());
        requestHistory.setResponseStatusCode(String.valueOf(response.code()));
        requestHistory.setResponseBody(responseBody);

        vendorRequestHistoryRepository.save(requestHistory);
    }

}

