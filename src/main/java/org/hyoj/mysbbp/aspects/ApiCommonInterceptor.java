package org.hyoj.mysbbp.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.ApiServiceClient;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
@Component
public class ApiCommonInterceptor extends HandlerInterceptorAdapter {

    private final ApiServiceClient apiServiceClient;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        checkApiKey(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    /**
     * Api-Key 체크
     */
    private void checkApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("Api-Key");

        String uri = request.getRequestURI();

        // 다음과 같은 uri는 체크 x
        String[] equalsWith = {
                "/", "/webjars", "/health/ping",
        };

        boolean equalsWithPass = Arrays.asList(equalsWith).contains(uri);

        if (!equalsWithPass) {
            if (apiKey == null) {
                throw new UnauthorizedException(ApiResultStatus.API_KEY_IS_REQUIRED);

            } else { // apiKey != null
                request.setAttribute("appKey", apiKey);
                apiServiceClient.findByApiKey(apiKey);
            }
        }
    }

}
