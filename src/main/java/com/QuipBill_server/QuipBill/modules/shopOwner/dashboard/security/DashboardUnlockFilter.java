package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DashboardUnlockFilter implements Filter {

    private static final String DASHBOARD_TOKEN = "dashboard_unlocked";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Only guard dashboard endpoints
        if (!path.startsWith("/api/dashboard")) {
            chain.doFilter(request, response);
            return;
        }

        // Allow unlock API without token
        if (path.equals("/api/dashboard/unlock")) {
            chain.doFilter(request, response);
            return;
        }

        // Check dashboard token
        String token = req.getHeader("Dashboard-Token");

        if (token == null || !token.equals(DASHBOARD_TOKEN)) {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("Dashboard Locked");
            return;
        }

        chain.doFilter(request, response);
    }
}
