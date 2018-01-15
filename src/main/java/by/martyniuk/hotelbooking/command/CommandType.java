package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.memento.Memento;
import by.martyniuk.hotelbooking.service.AuthorizationService;

import javax.servlet.http.HttpSession;
import java.util.List;

public enum CommandType {
    ADD_APARTMENT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    HttpSession session = request.getSession();
                    ApartmentDao dao = new ApartmentDaoImpl();
                    List<Apartment> apartmentList = dao.findAllApartments();
                    session.setAttribute("apartments", apartmentList);
                    // dao.addApartment(new Apartment(0, ApartmentClass.valueOf(request.getParameter("class").toUpperCase()), request.getParameter("number")));
                } catch (DaoException e) {
                    throw new CommandException(e);
                }
                return "jsp/apartments.jsp";
            });
        }
    },
    BOOK_APARTMENT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {

                return "jsp/apartments.jsp";
            });
        }
    },
    LOGIN {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    HttpSession session = request.getSession();
                    String mail = request.getParameter("mail");
                    String password = request.getParameter("password");
                    User user = AuthorizationService.login(mail, password);
                    if (user != null) {
                        session.setAttribute("user", user);
                    } else {
                        session.setAttribute("loginError", "Invalid e-mail or password");
                    }
                    return ((Memento) session.getAttribute("memento")).getState();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    LOGOUT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                HttpSession session = request.getSession();
                session.removeAttribute("user");
                return ((Memento) session.getAttribute("memento")).getState();
            });
        }
    },
    SET_LOCALE {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                HttpSession session = request.getSession();
                session.setAttribute("locale", request.getParameter("value"));
                ApartmentDao dao = new ApartmentDaoImpl();
                try {
                    System.out.println(dao.findAllApartments());
                    session.setAttribute("apartments", dao.findAllApartments());
                } catch (DaoException e) {
                    System.out.println(e.getMessage());
                }
                return ((Memento) session.getAttribute("memento")).getState();
            });
        }
    },
    FORWARD {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> (String) request.getParameter("page"));
        }
    },
    REGISTER {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    if (!request.getParameter("password").equals(request.getParameter("repeat_password"))) {
                        request.getSession().setAttribute("registerError", "Passwords do not match");
                        return "jsp/register.jsp";
                    }
                    if (AuthorizationService.register(request.getParameter("first_name"), request.getParameter("middle_name"),
                            request.getParameter("last_name"), request.getParameter("email"), request.getParameter("phone_number"),
                            request.getParameter("password"))) {
                        return "jsp/main.jsp";
                    } else {
                        request.getSession().setAttribute("registerError", "Account already exists");
                        return "jsp/register.jsp";
                    }
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    DEFAULT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                request.getSession().setAttribute("error", "Command operation not found");
                return "jsp/error.jsp";
            });
        }
    };

    public abstract ActionCommand receiveCommand();
}
