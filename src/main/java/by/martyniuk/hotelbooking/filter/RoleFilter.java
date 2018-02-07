package by.martyniuk.hotelbooking.filter;

import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

/**
 * The Class RoleFilter.
 */
@WebFilter(filterName = "RoleFilter", urlPatterns = {"/booking"})
public class RoleFilter implements Filter {

    /**
     * The admin's commands
     */
    private Set<CommandType> admin = EnumSet.of(CommandType.ADD_APARTMENT, CommandType.APPROVE_RESERVATION, CommandType.EDIT_APARTMENT,
            CommandType.SHOW_ADMIN_PAGE, CommandType.SHOW_APARTMENT_EDITOR, CommandType.SHOW_USER_MANAGER, CommandType.ADMIN_SHOW_USER_PROFILE,
            CommandType.ADMIN_UPDATE_USER_PROFILE);

    /**
     * The user's commands.
     */
    private Set<CommandType> user = EnumSet.of(CommandType.BOOK_APARTMENT, CommandType.LOGOUT, CommandType.SHOW_PERSONAL_RESERVATIONS,
            CommandType.UPDATE_PROFILE, CommandType.UPDATE_PASSWORD, CommandType.SHOW_USER_PROFILE, CommandType.ADD_MONEY);

    /**
     * The guest's commands.
     */
    private Set<CommandType> guest = EnumSet.of(CommandType.LOGIN, CommandType.REGISTER);

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
     * @param request  the request
     * @param response the response
     * @param chain    the chain
     * @throws IOException      Signals that an I/O exception has occurred.
     * @throws ServletException the servlet exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();

        CommandType commandType;
        if (CommandType.ifPresent(httpServletRequest.getParameter(CommandConstant.ACTION))) {
            commandType = CommandType.valueOf(httpServletRequest.getParameter(CommandConstant.ACTION).toUpperCase());
        } else {
            commandType = CommandType.DEFAULT;
        }

        User user = (User) session.getAttribute(CommandConstant.USER);

        boolean isContinue = true;

        if (user != null) {
            Role role = user.getRole();
            if (role.equals(Role.ADMIN)) {
                if (guest.contains(commandType)) {
                    isContinue = false;
                }
            } else {
                if (admin.contains(commandType) || guest.contains(commandType)) {
                    isContinue = false;
                }
            }
        } else {
            if (admin.contains(commandType) || this.user.contains(commandType)) {
                isContinue = false;
            }
        }
        if (isContinue) {
            chain.doFilter(request, response);
        } else {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + CommandConstant.INDEX);
        }

    }

    /**
     * Destroy.
     */
    @Override
    public void destroy() {

    }
}
