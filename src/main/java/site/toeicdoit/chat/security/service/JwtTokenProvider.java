package site.toeicdoit.chat.security.service;

import java.util.Base64;
import java.util.Date;
import java.time.Instant;
import java.util.stream.Stream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.toeicdoit.chat.security.domain.exception.JwtAuthenticationException;
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

    public String extractEmail(String jwt){
        return extractClaim(jwt, Claims::getSubject);
    }

    public List<String> extractRoles(String jwt){
        return extractClaim(jwt, i -> i.get("roles", List.class));
    }

    public String generateToken(UserDetails userDetails, boolean isRefreshToken){
        return generateToken(Map.of(), userDetails, isRefreshToken);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean isRefreshToken){
        return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuer(issuer)
        .claim("roles", userDetails.getAuthorities().stream().map(i -> i.getAuthority()).toList())
        .claim("type", isRefreshToken ? "refresh" : "access")
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusSeconds(isRefreshToken ? refreshTokenExpired : accessTokenExpired)))
        .signWith(SECRET_KEY, Jwts.SIG.HS256)
        .compact();
    }

    private <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver){
        return claimsResolver.apply(extractAllClaims(jwt));
    }

    private Claims extractAllClaims(String jwt){
        try {
            return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        } catch (Exception e) {
            throw new JwtAuthenticationException(e.getMessage());
        }
    }

    public Boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token){
        return extractClaim(token, Claims::getExpiration).before(Date.from(Instant.now()));
    }



    // public String createRefreshToken(UserModel user){
    //     return generateToken(user, true);
    // }
    
    // public String generateToken(UserModel user, Boolean isRefreshToken){
    //     return Jwts.builder()
    //     .issuer(issuer)
    //     .subject(isRefreshToken ? "refresh" : "access")
    //     .issuedAt(Date.from(Instant.now()))
    //     .expiration(Date.from(Instant.now().plusSeconds(isRefreshToken ? refreshTokenExpired : accessTokenExpired)))
    //     .claim("email", user.getEmail())
    //     .claim("roles", user.getRoles().stream().map(i -> new SimpleGrantedAuthority(i.name())).toList())
    //     .signWith(SECRET_KEY, Jwts.SIG.HS256)
    //     .compact();
    // }

    // public Authentication getAuthentication(String token){
    //     return Stream.of(token)
    //     .filter(i -> validateAccessToken(token))
    //     .map(i -> Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload())
    //     .filter(i -> i.get("email") != null)
    //     .filter(i -> i.get("roles") != null)
    //     .map(i -> new User(i.get("email", String.class), "", Stream.of(i.get("roles", List.class).toString().split(",")).map(SimpleGrantedAuthority::new).toList()))
    //     .map(i -> new UsernamePasswordAuthenticationToken(i, token, i.getAuthorities()))
    //     .findAny().orElse(null);

    // }

    // public Boolean validateAccessToken(String token){
    //     try {
    //         return Stream.of(token)
    //         .map(i -> Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload())
    //         .filter(i -> i.getIssuer() == issuer)
    //         .filter(i -> i.getSubject() == "access")
    //         .filter(i -> i.getExpiration().after(Date.from(Instant.now())))
    //         .map(i -> true).findFirst().orElse(false);
    //     } catch (Exception e) {
    //         return false;
    //     }
    // }
}
