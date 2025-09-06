package com.sportygroup.providerapi.advice;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter implements Filter {
    private final static String CORRELATION_ID_HEADER = "X-Correlation-Id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String cid = req.getHeader(CORRELATION_ID_HEADER);
        if (cid == null || cid.isBlank()) {
            cid = UUID.randomUUID().toString();
        }

        MDC.put("cid", cid);
        try {
            chain.doFilter(request, response);
        }
        finally {
            MDC.remove("cid");
        }
    }
}
