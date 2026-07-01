package com.example.FYP2.Config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting filter to prevent brute-force attacks and API abuse.
 * Uses per-IP token bucket algorithm via Bucket4j.
 *
 * Limits:
 * - /auth/login, /auth/register: 5 requests/minute per IP
 * - /forgotPassword/**: 3 requests/minute per IP
 * - All other endpoints: 60 requests/minute per IP
 */
@Slf4j
@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Separate caches for different endpoint categories
    private final Map<String, Bucket> authBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> forgotPasswordBuckets = new ConcurrentHashMap<>();
    private final Map<String, Bucket> generalBuckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);
        String path = request.getRequestURI();

        Bucket bucket = resolveBucket(clientIp, path);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.warn("Rate limit exceeded for IP: {} on path: {}", clientIp, path);
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"statusCode\":429,\"error\":\"Too many requests. Please try again later.\"}"
            );
        }
    }

    private Bucket resolveBucket(String clientIp, String path) {
        if (path.startsWith("/auth/login") || path.startsWith("/auth/register")) {
            // Strict limit for auth endpoints: 5 requests per minute
            return authBuckets.computeIfAbsent(clientIp, k -> createBucket(5, Duration.ofMinutes(1)));
        } else if (path.startsWith("/forgotPassword")) {
            // Very strict for forgot password: 3 requests per minute
            return forgotPasswordBuckets.computeIfAbsent(clientIp, k -> createBucket(3, Duration.ofMinutes(1)));
        } else {
            // General endpoints: 60 requests per minute
            return generalBuckets.computeIfAbsent(clientIp, k -> createBucket(60, Duration.ofMinutes(1)));
        }
    }

    private Bucket createBucket(int capacity, Duration refillDuration) {
        Bandwidth limit = Bandwidth.classic(capacity, Refill.greedy(capacity, refillDuration));
        return Bucket.builder().addLimit(limit).build();
    }

    /**
     * Extracts the real client IP, considering reverse proxy headers.
     */
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            // Take the first IP (original client) from the chain
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
