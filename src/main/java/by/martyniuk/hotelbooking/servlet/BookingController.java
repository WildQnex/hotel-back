package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.factory.ActionCommandFactory;
import by.martyniuk.hotelbooking.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(BookingController.class);

    @Override
    public void init() {

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ActionCommand command = ActionCommandFactory.getActionCommand(request.getParameter("action"));
        try {
            String page = command.execute(request);
            if (request.getAttribute("redirect") != null) {
                response.sendRedirect(page);
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (CommandException e) {
            LOGGER.log(Level.ERROR, e);
            request.getSession().setAttribute("error_message", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
            response.sendRedirect(request.getContextPath() + "/booking?action=forward&page=error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionCommand command = ActionCommandFactory.getActionCommand(request.getParameter("action"));
        try {
            String page = command.execute(request);
            if (request.getAttribute("redirect") != null) {
                response.sendRedirect(page);
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (CommandException e) {
            LOGGER.log(Level.ERROR, e);
            request.getSession().setAttribute("error_message", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
            response.sendRedirect(request.getContextPath() + "/booking?action=forward&page=error");
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
    }
}