package by.martyniuk.hotelbooking.command;

import by.martyniuk.hotelbooking.constant.PagePath;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.CommandException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import by.martyniuk.hotelbooking.service.ApartmentService;
import by.martyniuk.hotelbooking.service.AuthorizationService;
import by.martyniuk.hotelbooking.service.ReservationService;
import by.martyniuk.hotelbooking.service.UserService;
import by.martyniuk.hotelbooking.service.impl.ApartmentClassServiceImpl;
import by.martyniuk.hotelbooking.service.impl.ApartmentServiceImpl;
import by.martyniuk.hotelbooking.service.impl.AuthorizationServiceImpl;
import by.martyniuk.hotelbooking.service.impl.ReservationServiceImpl;
import by.martyniuk.hotelbooking.service.impl.UserServiceImpl;
import by.martyniuk.hotelbooking.util.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum CommandType {

    ADD_APARTMENT {
        @Override//----------------------------------------------------------------
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    String number = request.getParameter("apartmentNumber");
                    String apartmentClassId = request.getParameter("apartmentClass");

                    int floor = Integer.parseInt(request.getParameter("apartmentFloor"));
                    Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(Long.parseLong(apartmentClassId));
                    if (!apartmentClass.isPresent()) {
                        request.getSession().setAttribute("add_apartment_error", "Apartment class not found");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }
                    Apartment apartment = new Apartment(0, number, floor, apartmentClass.get(), true);
                    apartmentService.insertApartment(apartment);
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    BOOK_APARTMENT {
        @Override//----------------------------------------------------------------
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    String pattern = "dd MMMMM, yyyy";
                    HttpSession session = request.getSession();
                    String checkIn = request.getParameter("check_in_date");
                    String checkOut = request.getParameter("check_out_date");
                    String stringPersonAmount = request.getParameter("person_amount");

                    if (!Validator.validatePersonAmount(stringPersonAmount)) {
                        session.setAttribute("booking_error", "Incorrect person amount");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    int personAmount = Integer.parseInt(stringPersonAmount);
                    long apartmentClassId = Long.parseLong(request.getParameter("apartment_id"));

                    Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(apartmentClassId);
                    if (apartmentClass.isPresent()) {
                        if (apartmentClass.get().getMaxCapacity() < personAmount) {
                            session.setAttribute("booking_error", "Incorrect person amount");
                            request.setAttribute("redirect", true);
                            return request.getHeader("referer");
                        }
                    } else {
                        session.setAttribute("booking_error", "Incorrect apartment class");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    SimpleDateFormat formatter = new SimpleDateFormat(pattern, new Locale("en"));
                    LocalDate checkInDate = formatter.parse(checkIn).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate checkOutDate = formatter.parse(checkOut).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    if (!Validator.validateDateRange(checkInDate, checkOutDate)) {
                        session.setAttribute("booking_error", "Incorrect date");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    if (!reservationService.bookApartment((User) session.getAttribute("user"), apartmentClassId,
                            checkInDate, checkOutDate, personAmount)) {
                        session.setAttribute("booking_error", "Apartment already booked");
                    } else {
                        session.setAttribute("booking_error", "Apartment booked successfully");
                    }
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                } catch (ServiceException | ParseException e) {
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
                    String mail = request.getParameter("email");
                    String password = request.getParameter("password");
                    Optional<User> user = authorizationService.login(mail, password);
                    if (user.isPresent()) {
                        session.setAttribute("user", user.get());
                    } else {
                        session.setAttribute("login_error", "Invalid e-mail or password");
                    }
                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
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
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            });
        }
    },
    SET_LOCALE {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                HttpSession session = request.getSession();
                session.setAttribute("locale", request.getParameter("value"));
                request.setAttribute("redirect", true);
                return request.getHeader("referer");
            });
        }
    },
    FORWARD {
        @Override
        public ActionCommand receiveCommand() {

            return (request -> {
                if (!PagePath.isPresent(request.getParameter("page"))) {
                    throw new CommandException("Page not found");
                }
                return PagePath.valueOf(request.getParameter("page").toUpperCase()).getPage();
            });
        }
    },
    REGISTER {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    User user = new User();
                    String password = request.getParameter("password");
                    String repeatPassword = request.getParameter("repeat_password");
                    user.setPassword(password);
                    user.setMiddleName(request.getParameter("middle_name"));
                    user.setFirstName(request.getParameter("first_name"));
                    user.setLastName(request.getParameter("last_name"));
                    user.setEmail(request.getParameter("email"));
                    user.setPhoneNumber(request.getParameter("phone_number"));
                    System.out.println(user);
                    if (!(Validator.validatePasswords(password, repeatPassword) && Validator.validateUser(user))) {
                        request.getSession().setAttribute("register_error", "Incorrect personal data");
                        return PagePath.REGISTER.getPage();
                    }

                    if (authorizationService.register(user)) {
                        request.setAttribute("redirect", true);
                        return request.getContextPath() + "/index.jsp";
                    }

                    request.setAttribute("redirect", true);
                    request.getSession().setAttribute("register_error", "Account with this e-mail already exists");
                    return request.getHeader("referer");

                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    SHOW_APARTMENT_CLASS {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    String stringId = request.getParameter("id");
                    if (!Validator.validateId(stringId)) {
                        request.setAttribute("redirect", true);
                        request.getSession().setAttribute("apartment_classes_error", "Incorrect apartment ID");
                        return request.getContextPath() + "/booking?action=show_apartment_classes";
                    }
                    long id = Long.parseLong(stringId);

                    Optional<ApartmentClass> apartmentClassOptional = apartmentClassService.findApartmentClassById(id);
                    if (!apartmentClassOptional.isPresent()) {
                        request.setAttribute("redirect", true);
                        request.getSession().setAttribute("apartment_classes_error", "Apartment class not found");
                        return request.getContextPath() + "/booking?action=show_apartment_classes";
                    }
                    request.setAttribute("apartmentClass", apartmentClassOptional.get());

                    return PagePath.BOOK_APARTMENT.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    SHOW_PERSONAL_RESERVATIONS {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    List<Reservation> reservations = reservationService.readAllReservationByUserId(((User) request.getSession().getAttribute("user")).getId());

                    request.setAttribute("reservations", reservations);
                    return PagePath.USER_RESERVATION.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
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

                    user.setFirstName(request.getParameter("first_name"));
                    user.setMiddleName(request.getParameter("middle_name"));
                    user.setLastName(request.getParameter("last_name"));
                    user.setPhoneNumber(request.getParameter("phone_number"));

                    if (!Validator.validateUser(user)) {
                        request.getSession().setAttribute("update_profile_error", "Incorrect personal data");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    if (userService.updateUserProfile(user)) {
                        request.getSession().setAttribute("user", user);
                    }

                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    UPDATE_PASSWORD {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    String currentPassword = request.getParameter("current_password");
                    String newPassword = request.getParameter("new_password");
                    String repeatPassword = request.getParameter("repeat_new_password");

                    User user = (User) request.getSession().getAttribute("user");

                    if (!Validator.validatePasswords(newPassword, repeatPassword)) {
                        request.getSession().setAttribute("update_profile_error", "Passwords do not match");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    if (!userService.changeUserPassword(user.getId(), currentPassword, newPassword)) {
                        request.getSession().setAttribute("update_profile_error", "Incorrect current password");
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    }

                    userService.findUserByMail(user.getEmail())
                            .ifPresent(u -> request.getSession().setAttribute("user", u));

                    request.setAttribute("redirect", true);
                    return request.getHeader("referer");
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    SHOW_ADMIN_PAGE {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {

                    List<Reservation> reservations = reservationService.readAllReservationByStatus(Status.WAITING_FOR_APPROVE);
                    Map<Reservation, List<Apartment>> freeApartments = apartmentService.findFreeApartmentsForReservations(reservations);

                    request.setAttribute("reservations", reservations);
                    request.setAttribute("freeApartments", freeApartments);

                    return PagePath.APPROVE_RESERVATIONS.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    SHOW_APARTMENT_CLASSES {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    request.setAttribute("apartmentClasses", apartmentClassService.findAllApartmentClasses());
                    return PagePath.CLASSES.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    SHOW_APARTMENT_EDITOR {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    request.setAttribute("apartments", apartmentService.findAllApartments());
                    request.setAttribute("apartmentClasses", apartmentClassService.findAllApartmentClasses());
                    return PagePath.APARTMENTS.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    APPROVE_RESERVATION {
        @Override//----------------------------------------------------------------
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    System.out.println(request.getQueryString());
                    System.out.println(request.getParameter("reservation_id"));
                    if (!Validator.validateId(request.getParameter("reservation_id"))) {
                        request.getSession().setAttribute("approve_reservation_error", "Incorrect reservation Id");
                        request.setAttribute("redirect", true);
                        return request.getContextPath() + "/booking?action=show_admin_page";
                    }
                    System.out.println(request.getParameter("apartment_id"));
                    if (!Validator.validateId(request.getParameter("apartment_id"))) {
                        request.getSession().setAttribute("approve_reservation_error", "Incorrect apartment Id");
                        request.setAttribute("redirect", true);
                        return request.getContextPath() + "/booking?action=show_admin_page";
                    }

                    if (!Validator.validateStatus(request.getParameter("status"))) {
                        request.getSession().setAttribute("approve_reservation_error", "Incorrect reservation status");
                        request.setAttribute("redirect", true);
                        return request.getContextPath() + "/booking?action=show_admin_page";
                    }

                    long reservationId = Long.parseLong(request.getParameter("reservation_id"));
                    long apartmentId = Long.parseLong(request.getParameter("apartment_id"));
                    Status status = Status.valueOf(request.getParameter("status").toUpperCase());

                    if (!reservationService.updateReservationStatus(reservationId, apartmentId, status)) {
                        request.getSession().setAttribute("approve_reservation_error", "Reservation not approved");
                    }

                    request.setAttribute("redirect", true);
                    return request.getContextPath() + "/booking?action=show_admin_page";
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    },
    EDIT_APARTMENT {
        @Override//----------------------------------------------------------------
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    boolean result = false;
                    if (request.getParameter("type").equalsIgnoreCase("delete")) {
                        result = apartmentService.deleteApartment(Long.parseLong(request.getParameter("apartmentId")));
                    } else if (request.getParameter("type").equalsIgnoreCase("update")) {
                        Optional<Apartment> apartmentOptional = apartmentService.getApartment(Long.parseLong(request.getParameter("apartmentId")));
                        String number = request.getParameter("number");
                        if (!apartmentOptional.isPresent()) {
                            request.setAttribute("redirect", true);
                            return request.getHeader("referer");
                        }
                        Apartment apartment = apartmentOptional.get();
                        apartment.setNumber(number);
                        Optional<ApartmentClass> apartmentClass = apartmentClassService.findApartmentClassById(Long.parseLong(request.getParameter("class")));
                        if (!apartmentClass.isPresent()) {
                            request.getSession().setAttribute("edit_apartment_error", "Apartment class not found");
                            request.setAttribute("redirect", true);
                            return request.getHeader("referer");
                        }
                        apartment.setApartmentClass(apartmentClass.get());
                        int floor = Integer.parseInt(request.getParameter("floor"));
                        apartment.setFloor(floor);
                        result = apartmentService.updateApartment(apartment);
                    }
                    if (result) {
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
                    } else {
                        request.setAttribute("redirect", true);
                        return request.getHeader("referer");
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
                throw new CommandException("Command operation not found");
            });
        }
    },
    SHOW_USER_MANAGER {
        @Override
        public ActionCommand receiveCommand() {
            return (request -> {
                try {
                    request.setAttribute("users", userService.findAllUsers());
                    return PagePath.USER_MANAGER.getPage();
                } catch (ServiceException e) {
                    throw new CommandException(e);
                }
            });
        }
    };


    private static final Logger LOGGER = LogManager.getLogger(CommandType.class);

    private static List<String> commands = Arrays.stream(CommandType.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

    public static ApartmentService apartmentService = new ApartmentServiceImpl();
    public static ApartmentClassService apartmentClassService = new ApartmentClassServiceImpl();
    public static AuthorizationService authorizationService = new AuthorizationServiceImpl();
    public static ReservationService reservationService = new ReservationServiceImpl();
    public static UserService userService = new UserServiceImpl();

    public abstract ActionCommand receiveCommand();

    public static boolean ifPresent(String command) {
        return (command != null) && (commands.contains(command.toUpperCase()));
    }

}
