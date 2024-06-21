package site.toeicdoit.chat.security.service;

import java.util.Base64;
import java.util.Date;
import java.time.Instant;
import java.util.stream.Stream;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.user.domain.model.UserModel;
// import site.toeicdoit.chat.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider{
    // private final TokenRepository tokenRepository;
    // private final UserRepository userRepository;

    private SecretKey SECRET_KEY;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Getter
    @Value("${jwt.expired.access}")
    private Long accessTokenExpired;

    @Getter
    @Value("${jwt.expired.refresh}")
    private Long refreshTokenExpired;

    @PostConstruct
    protected void init() {
        SECRET_KEY = Keys.hmacShaKeyFor(Base64.getUrlEncoder().encode(secretKey.getBytes()));
    }

    public String createRefreshToken(UserModel user){
        return generateToken(user, true);
    }
    
    public String generateToken(UserModel user, Boolean isRefreshToken){
        return Jwts.builder()
        .issuer(issuer)
        .subject(isRefreshToken ? "refresh" : "access")
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusSeconds(isRefreshToken ? refreshTokenExpired : accessTokenExpired)))
        .claim("email", user.getEmail())
        .claim("roles", user.getRoles().stream().map(i -> new SimpleGrantedAuthority(i.name())).toList())
        .signWith(SECRET_KEY, Jwts.SIG.HS256)
        .compact();
    }

    public Authentication getAuthentication(String token){
        return Stream.of(token)
        .filter(i -> validateAccessToken(token))
        .map(i -> Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload())
        .filter(i -> i.get("email") != null)
        .filter(i -> i.get("roles") != null)
        .map(i -> new User(i.get("email", String.class), "", Stream.of(i.get("roles", List.class).toString().split(",")).map(SimpleGrantedAuthority::new).toList()))
        .map(i -> new UsernamePasswordAuthenticationToken(i, token, i.getAuthorities()))
        .findAny().orElse(null);

    }

    public Boolean validateAccessToken(String token){
        try {
            return Stream.of(token)
            .map(i -> Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload())
            .filter(i -> i.getIssuer() == issuer)
            .filter(i -> i.getSubject() == "access")
            .filter(i -> i.getExpiration().after(Date.from(Instant.now())))
            .map(i -> true).findFirst().orElse(false);
        } catch (Exception e) {
            return false;
        }
    }
}
