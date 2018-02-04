package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.DaoException;
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

@WebServlet("/booking")
public class BookingController extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(BookingController.class);

    @Override
    public void init() {
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doAction(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAction(request, response);
    }

    private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ActionCommand command = ActionCommandFactory.getActionCommand(request.getParameter(CommandConstant.ACTION));
        try {
            String page = command.execute(request);
            if (request.getAttribute(CommandConstant.REDIRECT) != null) {
                response.sendRedirect(page);
            } else {
                request.getRequestDispatcher(page).forward(request, response);
            }
        } catch (CommandException e) {
            LOGGER.log(Level.ERROR, e);
            request.getSession().setAttribute(CommandConstant.ERROR_MESSAGE, e.getMessage());
            response.sendRedirect(request.getContextPath() + CommandConstant.SHOW_ERROR_PAGE);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
    }
}