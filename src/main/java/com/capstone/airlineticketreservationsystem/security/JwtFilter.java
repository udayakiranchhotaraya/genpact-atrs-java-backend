package com.capstone.airlineticketreservationsystem.security;

import com.capstone.airlineticketreservationsystem.users.models.User;
import com.capstone.airlineticketreservationsystem.users.repositories.UserRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    public JwtFilter(JwtTokenUtil jwtTokenUtil, UserRepositoryDAO userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepositoryDAO userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();

        // Skip filter for public endpoints
        if (path.startsWith("/auth/") || path.startsWith("/api/users/start-onboarding") ||
                path.startsWith("/api/users/set-password")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = parseJwt(request);
            if (token != null && jwtTokenUtil.validateToken(token)) {
                // Extract claims from token
                String userUUID = jwtTokenUtil.getUserUUIDFromToken(token); // Now from subject
                String email = jwtTokenUtil.getEmailFromToken(token);
                String role = jwtTokenUtil.getRoleFromToken(token);

                User user = userRepository.findByUUID(userUUID)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                // Verify email consistency (optional but good for security)
                if (!user.getEmail().equals(email)) {
                    throw new RuntimeException("Token email mismatch");
                }

                // Create authorities from role
                List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority(role)
                );

                // Create authentication token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Set user in request context for use in controllers
                request.setAttribute("currentUser", user);
                request.setAttribute("userUUID", userUUID);
            } else {
                throw new RuntimeException("Invalid or missing token");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authentication failed: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
