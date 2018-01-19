package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.memento.Memento;
import by.martyniuk.hotelbooking.service.ApartmentService;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import by.martyniuk.hotelbooking.service.ReservationService;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
                try {
                    HttpSession session = request.getSession();
                    Long apartmentId = Long.parseLong(request.getParameter("apartmentId"));
                    DateTimeFormatter formatter = new DateTimeFormatterBuilder().toFormatter();
                    LocalDate checkInDate = LocalDate.parse(request.getParameter("checkInDate"), formatter);
                    LocalDate checkOutDate = LocalDate.parse(request.getParameter("checkOutDate"), formatter);
                    boolean result = ReservationService.bookApartment((User) session.getAttribute("user"), apartmentId,
                            checkInDate, checkOutDate, Integer.parseInt(request.getParameter("personsAmount")));
                    if (result) {
                        return "jsp/apartments.jsp";
                    } else {
                        return "jsp/main.jsp";
                    }

                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
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
    SHOW_APARTMENT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {

                    long id = Long.parseLong(request.getParameter("id"));
                    request.setAttribute("apartment", ApartmentService.getApartment(id));
                    return "jsp/apartment.jsp";
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
