package example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);

        if (!checkExcluded(((HttpServletRequest)request).getServletPath()) &&
                (session == null || session.getAttribute("user") == null)) {
            ((HttpServletResponse) response).sendRedirect("/auth/login");
        } else {
            filterChain.doFilter(request, response);
        }
    }
    private boolean checkExcluded(String resource) {
        return resource.contains("/login") || resource.contains("/usercheck") ||
                resource.contains("/register") || resource.contains("/userregister");
    }
}
