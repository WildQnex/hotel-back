package by.martyniuk.hotelbooking.servlet;

import by.martyniuk.hotelbooking.command.ActionCommand;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.factory.ActionCommandFactory;
import by.martyniuk.hotelbooking.memento.Memento;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/invokeServlet")
public class InvokeServlet extends HttpServlet {

    @Override
    public void init() {

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        ActionCommand command = ActionCommandFactory.getActionCommand(request.getParameter("action"));
        try {
            String page = command.execute(request);
            Memento memento = (Memento) request.getSession().getAttribute("memento");
            memento.addState(page);
            System.out.println(memento.getStates());
            request.getSession().setAttribute("memento", memento);
            request.getRequestDispatcher(page).forward(request, response);
        } catch (CommandException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionCommand command = ActionCommandFactory.getActionCommand(request.getParameter("action"));
        try {
            String page = command.execute(request);
            Memento memento = (Memento) request.getSession().getAttribute("memento");
            memento.addState(page);
            System.out.println(memento.getStates());
            request.getSession().setAttribute("memento", memento);
            request.getRequestDispatcher(page).forward(request, response);
        } catch (CommandException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        ConnectionPool.getInstance().destroy();
    }
}