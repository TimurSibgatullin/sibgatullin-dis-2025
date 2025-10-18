package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);


        if (((HttpServletRequest) servletRequest).getServletPath().contains("/login")
                && (session == null || session.getAttribute("user") == null)) {
//            servletRequest.getRequestDispatcher("/login").forward(servletRequest, servletResponse);
            ((HttpServletResponse) servletResponse).sendRedirect("/login");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    private boolean checkExcluded(String resource) {
        return resource.contains("/login") || resource.contains("/usercheck");
    }
}
