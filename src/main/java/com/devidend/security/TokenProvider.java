package com.devidend.security;

import com.devidend.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; //1시간
    private static final String KEY_ROLES = "roles";

    private final MemberService memberService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    /**
     * 토큰 생성(발급)
     * @param username
     * @param roles
     * @return
     */
    public String generateToken(String username, List<String> roles){
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles);
        var now = new Date();
        var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)  //토큰 생성 시간
                .setExpiration(expiredDate) //토큰 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 사용할 암호화 알고리즘과 시크릿 키
                .compact();
        log.info("Create  Token now : {}, expiredDate : {}", now, expiredDate);
        return token;
    }

    public Authentication getAuthentication(String jwt){
        UserDetails userDetails = memberService.loadUserByUsername(this.getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean validateToken(String token){
        if(!StringUtils.hasText(token)){
            return false;
        }
        var claims = this.parseClaims(token);
        log.info("validate Token {}", claims);
        return !claims.getExpiration().before(new Date());

    }

    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }

    private Claims parseClaims(String token){
        try{
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            //TODO
            return e.getClaims();
        }
    }
}
