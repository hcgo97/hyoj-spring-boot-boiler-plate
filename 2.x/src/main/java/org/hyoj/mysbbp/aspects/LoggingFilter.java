package org.hyoj.mysbbp.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.RequestIdGenerator;
import org.hyoj.mysbbp.common.util.IpUtil;
import org.hyoj.mysbbp.common.util.StringUtil;
import org.springframework.util.StopWatch;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/v1/*")
public class LoggingFilter implements Filter {

    private final RequestIdGenerator requestIdGenerator;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // requestId 및 IP 셋팅
        initRequestId((HttpServletRequest) request);
        initPublicIp((HttpServletRequest) request);

        CustomRequestWrapper requestWrapper = new CustomRequestWrapper((HttpServletRequest) request);
        CustomResponseWrapper responseWrapper = new CustomResponseWrapper((HttpServletResponse) response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        String responseBody = new String(responseWrapper.getDataStream(), StandardCharsets.UTF_8);
        writeResponseLogs((HttpServletRequest) request, (HttpServletResponse) response, responseBody, stopWatch);
        response.getOutputStream().write(responseBody.getBytes());
    }

    /**
     * 메소드 실행 후 Response 로그 작성
     */
    private void writeResponseLogs(HttpServletRequest request, HttpServletResponse response, String responseBody, StopWatch stopWatch) {
        String requestId = (String) request.getAttribute("requestId");
        String requestMethod = request.getMethod();
        String servletPath = request.getServletPath();
        String queryString = !StringUtil.isEmpty(request.getQueryString()) ? "?" + request.getQueryString() : "";
        int statusCode = response.getStatus();

        if (statusCode >= 400) { // 오류
            log.error("-------------------------------------");
            log.error("[Response] => {}", requestId);
            log.error("{} [Exception] {} {} {}", requestId, statusCode, requestMethod, servletPath + queryString);
            log.error("{} response body=[{}]", requestId, responseBody);

        } else { // 성공
            log.info("-------------------------------------");
            log.info("[Response] => {}", requestId);
            log.info("{} [Success] {} {} {}", requestId, statusCode, requestMethod, servletPath + queryString);
            log.info("{} response body=[{}]", requestId, responseBody);
        }

        stopWatch.stop();
        long durationMs = stopWatch.getTotalTimeMillis();

        log.info("{} duration time:{}ms", requestId, durationMs);
        log.info("-------------------------------------");
    }

    private void initRequestId(HttpServletRequest request) {
        request.setAttribute("requestId", requestIdGenerator.getRequestId());
    }

    private void initPublicIp(HttpServletRequest request) {
        String publicIp = IpUtil.getPublicIp(request);
        request.setAttribute("publicIp", publicIp);
    }

}
