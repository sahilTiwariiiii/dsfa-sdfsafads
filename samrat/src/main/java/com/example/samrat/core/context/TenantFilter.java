package com.example.samrat.core.context;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String hospitalIdHeader = request.getHeader("X-Hospital-Id");
        String branchIdHeader = request.getHeader("X-Branch-Id");

        try {
            if (hospitalIdHeader != null) {
                TenantContext.setHospitalId(Long.parseLong(hospitalIdHeader));
            }
            if (branchIdHeader != null) {
                TenantContext.setBranchId(Long.parseLong(branchIdHeader));
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}
