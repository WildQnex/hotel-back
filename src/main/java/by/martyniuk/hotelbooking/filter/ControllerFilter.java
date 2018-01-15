package by.martyniuk.hotelbooking.filter;

import by.martyniuk.hotelbooking.memento.Memento;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/invokeServlet"})
public class ControllerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession session = ((HttpServletRequest) request).getSession();
        session.removeAttribute("loginError");
        session.removeAttribute("registerError");
        Memento memento = (Memento) session.getAttribute("memento");
        if (memento == null) {
            memento = new Memento();
            memento.addState("index.jsp");
            session.setAttribute("memento", memento);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
