package com.example.demo.security;

import com.example.demo.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;


@Slf4j
@Service
public class TokenProvider {
    private static final String SECRET_KEY = "NM48JPctFuna59f5";

    /**
     * JWT 라이브러리를 이용해 JWT 토큰을 생성
     * @param userEntity
     * @return
     */
    public String create (UserEntity userEntity) {
        // 기한은 현재부터 1일
        Date expiryDate = Date.from(
            Instant.now()
            .plus(1, ChronoUnit.DAYS));

        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userEntity.getId())
                .setIssuer("demo app")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .compact();
    }

    /**
     * 토큰을 디코딩 및 파싱하고 토큰의 위조 여부를 확인
     * @param token
     * @return
     */
    public String validateAndGetUserId (String token) {
        // parseClaimsJws 메서드가 Base64로 디코딩 및 파싱
        // 헤더와 페이로드를 setSigninKey로 넘어온 시크릿을 이용해 서명한 후 token의 서명과 비교
        // 위조되지 않았다면 페이로드(Claims) 리턴, 위조라면 예외를 날림
        // 그 중 우리는 userId가 필요하므로 getBody를 부른다.
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
