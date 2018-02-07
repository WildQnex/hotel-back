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

/**
 * The Class PageFilter.
 */
@WebFilter(filterName = "PageFilter", urlPatterns = {"/*"})
public class PageFilter implements Filter {

    /**
     * The Constant URL_PATTERN.
     */
    private static final Pattern URL_PATTERN = Pattern.compile("(/css/.*)|(/js/.*)|(/img/.*)|(/fonts/.*)|(/booking)|(/index\\.jsp)|(/)");

    /**
     * Inits the.
     *
     * @param filterConfig the filter config
     */
    @Override
    public void init(FilterConfig filterConfig) {

    }

    /**
     * Do filter.
     *
     * @param servletRequest  the servlet request
     * @param servletResponse the servlet response
     * @param filterChain     the filter chain
     * @throws IOException      Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
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


    /**
     * Destroy.
     */
    @Override
    public void destroy() {

    }
}