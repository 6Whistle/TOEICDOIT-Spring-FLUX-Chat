package site.toeicdoit.chat.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final ReactiveAuthenticationManager reactiveAthenticationManager;
    private final ServerAuthenticationConverter serverAuthenticationConverter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationWebFilter authenticationWebFilter() {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(serverAuthenticationConverter);
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return authenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http)
    {
        return http
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint((exchange, e) -> Mono.fromRunnable(() -> {    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);  }))
                .accessDeniedHandler((exchange, denied) -> Mono.fromRunnable(() -> {    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);  }))
            )
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .cors(ServerHttpSecurity.CorsSpec::disable)
            .authorizeExchange((authorize) -> authorize
                // .anyExchange().permitAll()
                .pathMatchers("/api/security/**").permitAll()
                .anyExchange().authenticated()
            )
            .addFilterAt(new AuthenticationWebFilter(reactiveAthenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
            .build();
    }
}