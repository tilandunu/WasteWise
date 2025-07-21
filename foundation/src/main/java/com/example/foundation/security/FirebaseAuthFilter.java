package com.example.foundation.security;

import com.example.foundation.model.User;
import com.example.foundation.service.AuthService;
import com.google.firebase.auth.FirebaseAuthException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FirebaseAuthFilter extends HttpFilter {

    private final AuthService authService;

    public FirebaseAuthFilter(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String path = request.getRequestURI();
        String method = request.getMethod();

        // Skip authentication for public endpoints and OPTIONS preflight requests
        if (method.equalsIgnoreCase("OPTIONS")
                || path.equals("/")
                || path.equals("/api/auth/google-login")
                // Add more public paths here if needed
        ) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        String idToken = header.substring(7);

        try {
            User user = authService.verifyIdTokenAndGetUser(idToken);
            request.setAttribute("user", user); // Attach user to request
            chain.doFilter(request, response);
        } catch (FirebaseAuthException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token");
        }
    }
}
