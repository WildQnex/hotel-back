package by.martyniuk.hotelbooking.filter;

import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.constant.PagePath;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Pattern;

@WebFilter(filterName = "PageFilter", urlPatterns = {"/*"})
public class PageFilter implements Filter {

    private static final Pattern URL_PATTERN = Pattern.compile("(/css/.*)|(/js/.*)|(/img/.*)|(/fonts/.*)|(/booking)|(/index\\.jsp)|(/)");

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (URL_PATTERN.matcher(request.getRequestURI()).matches()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            request.setAttribute(CommandConstant.ERROR_MESSAGE, "Page not found");
            request.getRequestDispatcher(PagePath.ERROR.getPage()).forward(servletRequest, servletResponse);
        }

    }


    @Override
    public void destroy() {

    }
}