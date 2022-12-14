package org.hyoj.mysbbp.common.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.exception.TokenException;
import org.hyoj.mysbbp.dto.UserTokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final JwtProvider jwtProvider;

    @Value("${api.path.default}")
    private String API_URL_PREFIX;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();


        // 다음과 같은 uri는 토큰검증x
        String[] equalsWith = {
                API_URL_PREFIX + "/users/sign-up",
                "/", "/webjars", "/health/ping",
        };

        // 다음으로 시작하는 uri는 토큰검증x
        String[] startWith = {
                API_URL_PREFIX + "/users/otp",
                "/error"
        };

        // 다음으로 끝나는 형식은 토큰검증x
        String[] endWith = {
                ".html", ".jpg", ".png", ".gif", ".ico", ".js", ".css"
        };

        boolean equalsWithPass = Arrays.asList(equalsWith).contains(uri);
        boolean startWithPass = Arrays.stream(startWith).anyMatch(uri::startsWith);
        boolean endWithPass = Arrays.stream(endWith).anyMatch(uri::endsWith);

        if (equalsWithPass || startWithPass || endWithPass) {
            filterChain.doFilter(request, response);
            return;
        }

        log.debug("doFilterInternal");
        log.debug("[url]" + uri);

        try {
            String token = getToken(request);

            if (jwtProvider.validateJwtToken(token)) {
                
                // 유저 권한 파싱
                List<GrantedAuthority> authorities = new ArrayList<>();
                List<String> roleCodeList = (List<String>) jwtProvider.getBodyValue(token, "roleCode");

                for (String roleCode : roleCodeList) {
                    authorities.add(new SimpleGrantedAuthority(roleCode));
                }

                // payload 파싱
                UserTokenDto userInfo = UserTokenDto.builder()
                        .userId(jwtProvider.getBodyValue(token, "userId").toString())
                        .authorities(authorities)
                        .build();

                log.debug("[userInfo]" + userInfo.toString());

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                request.setAttribute("Authentication", token);
                response.setHeader("Authentication", token);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (ExpiredJwtException e) { // 토큰 만료
            log.error("Expired Token -> Message: {}", e.getMessage());

        } catch (TokenException e) {
            log.error(e.getCode());
            log.error(e.getMessage());

            handlerExceptionResolver.resolveException(request, response, null, e);
            return;

        } catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

        log.info("Authenticate Success");
        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 유무 체크
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token == null) {
            throw new TokenException(ApiResultStatus.TOKEN_NOT_FOUND);
        }

        if (token.startsWith("Bearer ")) {
            return token.replace("Bearer ", "");
        }

        return token;
    }
}
