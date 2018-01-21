package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentClassDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.memento.Memento;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import by.martyniuk.hotelbooking.service.ReservationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
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
                    Memento memento = (Memento) session.getAttribute("memento");
                    long apartmentClassId = Long.parseLong(request.getParameter("apartmentId"));
                    String pattern = "dd MMMMM, yyyy";
                    SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale("en"));
                    Date checkIn = formatter.parse(request.getParameter("checkInDate"));
                    Date checkOut = formatter.parse(request.getParameter("checkOutDate"));
                    LocalDate checkInDate = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate checkOutDate = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    boolean result = ReservationService.bookApartment((User) session.getAttribute("user"), apartmentClassId,
                            checkInDate, checkOutDate, Integer.parseInt(request.getParameter("personsAmount")));
                    if (!result) {
                        session.setAttribute("bookingError", "Apartment already booked");
                    }
                    request.setAttribute("redirect", true);
                    return memento.getState();
                } catch (ServiceException | ParseException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
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
                    request.setAttribute("redirect", true);
                    return ((Memento) session.getAttribute("memento")).getState();
                } catch (ServiceException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
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
                request.setAttribute("redirect", true);
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
                    session.setAttribute("apartments", dao.findAllApartments());
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
                request.setAttribute("redirect", true);
                return ((Memento) session.getAttribute("memento")).getState();
            });
        }
    },
    FORWARD {
        @Override
        public ActionCommand receiveCommand() {

            return (request -> {
                addToMemento(request);
                return request.getParameter("page");
            });
        }
    },
    REGISTER {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    HttpSession session = request.getSession();
                    if (!request.getParameter("password").equals(request.getParameter("repeat_password"))) {
                        request.getSession().setAttribute("registerError", "Passwords do not match");
                        return "jsp/register.jsp";
                    }
                    Memento memento = (Memento) session.getAttribute("memento");
                    if (AuthorizationService.register(request.getParameter("first_name"), request.getParameter("middle_name"),
                            request.getParameter("last_name"), request.getParameter("email"), request.getParameter("phone_number"),
                            request.getParameter("password"))) {
                        memento.addState(request.getContextPath() + "/booking?action=forward&page=jsp/main.jsp");
                        session.setAttribute("memento", memento);
                        request.setAttribute("redirect", true);
                        return memento.getState();
                    } else {
                        request.setAttribute("redirect", true);
                        request.getSession().setAttribute("registerError", "Account already exists");
                        return memento.getState();
                    }
                } catch (ServiceException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
            });
        }
    },
    SHOW_APARTMENT_CLASS {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    addToMemento(request);
                    long id = Long.parseLong(request.getParameter("id"));
                    ApartmentClassDao dao = new ApartmentClassDaoImpl();
                    request.setAttribute("apartmentClass", dao.findApartmentClassById(id));
                    return "jsp/bookApartment.jsp";
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
            });
        }
    },
    UPDATE_PROFILE {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    User user = (User) request.getSession().getAttribute("user");
                    user.setFirstName(request.getParameter("firstName"));
                    user.setMiddleName(request.getParameter("middleName"));
                    user.setLastName(request.getParameter("lastName"));
                    user.setPhoneNumber(request.getParameter("phoneNumber"));
                    if (AuthorizationService.updateUserProfile(user)) {
                        request.getSession().setAttribute("user", user);
                    }
                    request.setAttribute("redirect", true);
                    return ((Memento) request.getSession().getAttribute("memento")).getState();
                } catch (ServiceException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
            });
        }
    },
    SHOW_ADMIN_PAGE {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    addToMemento(request);
                    ReservationDao dao = new ReservationDaoImpl();
                    request.setAttribute("reservations", dao.readAllReservations());
                    return "jsp/admin.jsp";
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
            });
        }
    },
    SHOW_APARTMENT_CLASSES {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    addToMemento(request);
                    ApartmentClassDao dao = new ApartmentClassDaoImpl();
                    request.setAttribute("apartmentClasses", dao.findAllApartmentClasses());
                    return "jsp/classes.jsp";
                } catch (DaoException e) {
                    LOGGER.log(Level.ERROR, e);
                    request.setAttribute("errorMessage", e.getMessage() + '\n' + Arrays.toString(e.getStackTrace()));
                    return "jsp/error.jsp";
                }
            });
        }
    },
    DEFAULT {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                LOGGER.log(Level.ERROR, "Command operation not found");
                request.getSession().setAttribute("errorMessage", "Command operation not found");
                return "jsp/error.jsp";
            });
        }
    };

    private static final Logger LOGGER = LogManager.getLogger(CommandType.class);

    public abstract ActionCommand receiveCommand();

    private static void addToMemento(HttpServletRequest request) {
        Memento memento = (Memento) request.getSession().getAttribute("memento");
        memento.addState(request.getContextPath() + "/booking?" + request.getQueryString());
        request.getSession().setAttribute("memento", memento);
    }
}
