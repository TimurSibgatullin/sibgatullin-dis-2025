package ru.itis.dis403.lab2_6.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.itis.dis403.lab2_6.service.JWTService;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// ru.itis.dis403.lab2_6.config.JwtAuthenticationFilter

// Добавьте импорт для логирования
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JWTService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        logger.debug("Processing request for URI: {}, Authorization Header: {}", request.getRequestURI(), authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found or invalid format.");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);
        logger.debug("Extracted JWT token: {}", jwt);

        // Проверяем, пустой ли токен
        if (jwt.isEmpty()) {
            logger.warn("JWT Token is empty after removing Bearer prefix.");
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            username = jwtService.extractUsername(jwt);
            logger.debug("Successfully extracted username from JWT: {}", username);
        } catch (Exception e) {
            logger.error("Could not extract username from JWT: {}", e.getMessage(), e);
            filterChain.doFilter(request, response);
            return;
        }

        // Проверяем, есть ли имя пользователя и нет ли уже аутентификации
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("Loaded UserDetails for username: {}", username);
            } catch (Exception e) {
                logger.error("Could not load user details for username: {}", username, e);
                filterChain.doFilter(request, response);
                return;
            }

            // Проверяем валидность токена
            boolean isTokenValid = false;
            try {
                isTokenValid = jwtService.isTokenValid(jwt, userDetails);
                logger.debug("Token validity check result for user {}: {}", username, isTokenValid);
            } catch (Exception e) {
                logger.error("Error validating token for user: {}", username, e);
                filterChain.doFilter(request, response);
                return;
            }


            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.debug("Set authentication in SecurityContext for user: {}", username);
            } else {
                logger.warn("JWT Token is valid but not valid for user: {} or is expired.", username);
            }
        } else {
            logger.debug("Username was null OR there was already an authentication object in SecurityContext.");
        }

        // ВСЕГДА вызываем chain.doFilter, чтобы Spring Security мог обработать авторизацию
        filterChain.doFilter(request, response);
    }
}