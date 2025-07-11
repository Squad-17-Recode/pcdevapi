package com.squad17.pcdevapi.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.squad17.pcdevapi.service.conta.ContaDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;
    private final ContaDetailsServiceImpl candidatoDetailsService;
    private final ContaDetailsServiceImpl empresaDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, @Qualifier("candidatoDetailsService") ContaDetailsServiceImpl candidatoDetailsService, @Qualifier("empresaDetailsService") ContaDetailsServiceImpl empresaDetailsService ) {
        this.jwtUtils = jwtUtils;
        this.candidatoDetailsService = candidatoDetailsService;
        this.empresaDetailsService = empresaDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(TOKEN_PREFIX.length());
        username = jwtUtils.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;
            try {
                userDetails = candidatoDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e) {
                try {
                    userDetails = empresaDetailsService.loadUserByUsername(username);
                } catch (UsernameNotFoundException ignored) {

                }
            }

            if (userDetails != null && jwtUtils.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
