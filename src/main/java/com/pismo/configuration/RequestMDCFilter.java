package com.pismo.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestMDCFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            //Set a requestId for tracing
            StringBuilder requestContext = new StringBuilder().append("requestId='").append(UUID.randomUUID().toString()).append("' ");
            MDC.put("requestContext", requestContext.toString());

            //Continue processing
            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
