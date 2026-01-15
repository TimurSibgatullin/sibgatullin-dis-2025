package ru.freelib.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.freelib.model.User;

import java.io.IOException;
import java.util.Objects;

@WebFilter({"/upload", "/upload/*"})
public class UploadFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        User user =  (User) request.getSession().getAttribute("user");
        if (user == null) {
            String requestedUrl = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null) {
                requestedUrl += "?" + query;
            }
            request.getSession().setAttribute("redirectAfterLogin", requestedUrl);
            response.sendRedirect(request.getContextPath() + "/auth/usercheck");
        } else if (!Objects.equals(user.getRole(), "author") && !Objects.equals(user.getRole(), "admin")) {
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            chain.doFilter(req, res);
        }
    }
}
