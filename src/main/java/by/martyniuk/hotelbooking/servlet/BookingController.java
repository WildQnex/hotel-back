package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.command.CommandType;
import by.martyniuk.hotelbooking.config.Application;
import by.martyniuk.hotelbooking.config.BeanConfig;
import by.martyniuk.hotelbooking.constant.CommandConstant;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.service.DocumentService;
import by.martyniuk.hotelbooking.service.impl.DocumentServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The Class BookingController.
 */
@Controller
public class BookingController {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LogManager.getLogger(BookingController.class);

    @Autowired
    private CommandType commandType;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView post() {
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        return model;

    }

    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doAction(request, response);
    }

    /**
     * Do get.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doAction(request, response);
    }

    /**
     * Do action.
     *
     * @param request  the request
     * @param response the response
     * @throws ServletException the servlet exception
     * @throws IOException      Signals that an I/O exception has occurred.
     */
    private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionCommand command = commandType.receiveCommand(request.getParameter(CommandConstant.ACTION).toUpperCase());
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

}