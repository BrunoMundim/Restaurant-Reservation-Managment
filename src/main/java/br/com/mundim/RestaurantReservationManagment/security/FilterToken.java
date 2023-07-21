package br.com.mundim.RestaurantReservationManagment.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class FilterToken extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final AuthenticationService authenticationService;

    @Autowired
    public FilterToken(
            TokenService tokenService,
            AuthenticationService authenticationService
    ) {
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token;

        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            token = authorizationHeader.replace("Bearer ", "");
            var subject = this.tokenService.getSubject(token);

            var user = authenticationService.loadUserByUsername(subject);

            if (user == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired !");
                return;
            }

            var authentication = new UsernamePasswordAuthenticationToken(user,
                    null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

}
