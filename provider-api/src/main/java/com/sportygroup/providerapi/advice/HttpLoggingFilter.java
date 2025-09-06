package com.sportygroup.providerapi.advice;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {


    private static final Set<String> IGNORE_PATH = Set.of(
            "/actuator/health"
    );

    private static final Set<String> SENSITIVE_HEADERS = Set.of(
            "authorization", "cookie", "set-cookie"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        for (String ignorePath : IGNORE_PATH) {
            if (path.startsWith(ignorePath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper res = new ContentCachingResponseWrapper(response);

        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;

            String headers = extractHeaders(request);

            log.info(">>> {} {}?{} headers=[{}]",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString() == null ? "" : request.getQueryString(),
                    headers);

            log.info("<<< {} {} -> {} ({} ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration
            );

            res.copyBodyToResponse();
        }

    }

    private static String extractHeaders(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        var names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            String value = request.getHeader(name);
            if (SENSITIVE_HEADERS.contains(name.toLowerCase())) {
                value = "***";
            }
            if (!sb.isEmpty()) sb.append(", ");
            sb.append(name).append('=').append(value);
        }
        return sb.toString();
    }
}
