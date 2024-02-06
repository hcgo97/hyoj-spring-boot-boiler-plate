package org.hyoj.mysbbp.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthCheckController {
    private String ELB_CHECKER = "ELB-HealthChecker";

    @GetMapping(value="/ping")
    public String ping(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");

        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName = "";

        Map<String, String> headersMap = new HashMap<>();
        String logHeaders = "";
        List<String> headerValues = new ArrayList<>();
        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();
            logHeaders +=  headerName + ": [{}],";

            headerValues.add(request.getHeader(headerName));
        }


        // ELB 에서 체크할 경우 로그 남기지 않도록 한다.
        if (!userAgent.startsWith(ELB_CHECKER)) {
            log.info("call ping " + logHeaders, headerValues.toArray(new String[headerValues.size()]));
        }

        return "pong";
    }
}
