package org.hyoj.mysbbp.common.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyoj.mysbbp.common.enums.ApiResultStatus;
import org.hyoj.mysbbp.common.enums.TokenTypeEnum;
import org.hyoj.mysbbp.common.exception.TokenException;
import org.hyoj.mysbbp.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtProvider {

    @Value("${jwt.token.issuer}")
    private String jwtIssuer;

    @Value("${jwt.token.secret}")
    private String secretKey;

    @Value("${jwt.token.length}")
    private long validityInHours;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserDto.UserInfoDto userInfo, TokenTypeEnum tokenType) {
        Claims claims = Jwts.claims();

        Date now = new Date();
        Date validity = null;

        try {
            if (tokenType.equals(TokenTypeEnum.ACCESS_TOKEN)) {
                claims.put("id", userInfo.getId());
                claims.put("userId", userInfo.getUserId());
                claims.put("roleCode", userInfo.getRoleCode());
                claims.put("tokenType", tokenType.getValue());

                validity = new Date(now.getTime() + validityInHours * 1000); // 1시간
            }

            if (tokenType.equals(TokenTypeEnum.REFRESH_TOKEN)) {
                claims.put("tokenType", tokenType.getValue());
                validity = new Date(now.getTime() + validityInHours * 1000 * 24 * 90); // 90일
            }

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuer(jwtIssuer)
                    .setIssuedAt(now)
                    .setExpiration(validity)
                    .signWith(SignatureAlgorithm.HS512, secretKey)
                    .compact();

        } catch (Exception e) { // 토큰 생성 중 에러
            throw new TokenException(ApiResultStatus.TOKEN_CREATED_FAILED, e.getMessage());
        }
    }

    /**
     * 토큰 파싱
     */
    public Object getBodyValue(String token, String field) {
        if (this.validateJwtToken(token)) {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get(field).toString();
        }
        return null;
    }

    /**
     * 토큰 유효성 검사
     */
    public boolean validateJwtToken(String authToken) throws JwtException {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;

        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token -> Message: {}", e.getMessage());
            throw new TokenException(ApiResultStatus.TOKEN_DATE_EXPIRED);

        } catch (SignatureException e) {
            log.error("Invalid JWT signature -> Message: {} ", e.getMessage());
            throw new TokenException(ApiResultStatus.TOKEN_INVALID);

        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token -> Message: {}", e.getMessage());
            throw new TokenException(ApiResultStatus.TOKEN_INVALID);

        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token -> Message: {}", e.getMessage());
            throw new TokenException(ApiResultStatus.TOKEN_INVALID);

        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty -> Message: {}", e.getMessage());
            throw new TokenException(ApiResultStatus.TOKEN_INVALID);

        }
    }
}
