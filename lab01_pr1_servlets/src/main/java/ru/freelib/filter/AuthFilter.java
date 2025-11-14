package ru.freelib.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/upload", "/upload/*", "/profile", "/profile/*", "/comment/*"})
public class AuthFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (request.getSession().getAttribute("user") == null) {
            String requestedUrl = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null) {
                requestedUrl += "?" + query;
            }
            request.getSession().setAttribute("redirectAfterLogin", requestedUrl);
            response.sendRedirect(request.getContextPath() + "/auth/usercheck");
        } else {
            chain.doFilter(req, res);
        }
    }
}
