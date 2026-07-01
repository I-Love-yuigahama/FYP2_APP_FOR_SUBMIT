package com.example.FYP2.Config;

import com.example.FYP2.Service.JWTUtils;
import com.example.FYP2.Service.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UserDetailService userDetailService;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        final String jwtToken;
//
//        final String userEmail;
//
//        if(authHeader == null || authHeader.isBlank())
//        {
//            filterChain.doFilter(request, response);
//            return ;
//        }
//
//        jwtToken = authHeader.substring(7);
//        userEmail = jwtUtils.extractUsername(jwtToken);
//
//        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)
//        {
//            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);
//
//
//            if(jwtUtils.isTokenValid(jwtToken, userDetails))
//            {
//                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                    userDetails, null,userDetails.getAuthorities()
//                );
//
//                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                securityContext.setAuthentication(token);
//                SecurityContextHolder.setContext(securityContext);
//            }
//
//        }
//
//        filterChain.doFilter(request, response); // ← ADD THIS LINE
//
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        log.debug("Auth header: {}", authHeader);

        // ✅ Skip filter if no token or not a Bearer token
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith("Bearer ")) {
            log.debug("No Bearer token, skipping filter");
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authHeader.substring(7);

        // ✅ Guard against empty token after "Bearer "
        if (jwtToken.isBlank()) {

            filterChain.doFilter(request, response);
            return;
        }

        final String userEmail;

        try {
            userEmail = jwtUtils.extractUsername(jwtToken);
        } catch (Exception e) {
            // ✅ Bad/expired token — don't crash, just continue unauthenticated
            log.warn("JWT token rejected for {}: {}", request.getRequestURI(), e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // ← return 401
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.loadUserByUsername(userEmail);

            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                securityContext.setAuthentication(token);
                SecurityContextHolder.setContext(securityContext);
            } else {
                // ← Token expired or invalid — return 401 so frontend can refresh
                log.warn("Token invalid/expired for user: {}", userEmail);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
