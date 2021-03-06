package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.ReservationServiceImpl;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * The Class ReservationServiceImplTest.
 */
public class ReservationServiceImplTest {

    /**
     * The reservation dao.
     */
    private ReservationDao reservationDao;

    /**
     * The apartment dao.
     */
    private ApartmentDao apartmentDao;

    /**
     * The reservation service.
     */
    private ReservationServiceImpl reservationService = new ReservationServiceImpl();

    /**
     * The user.
     */
    private User user;

    /**
     * The apartment one.
     */
    private Apartment apartmentOne;

    /**
     * The apartment two.
     */
    private Apartment apartmentTwo;

    /**
     * The apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * The reservation.
     */
    private Reservation reservation;

    /**
     * The reservation list.
     */
    private List<Reservation> reservationList;

    /**
     * The apartment list.
     */
    private List<Apartment> apartmentList;


    /**
     * Sets the up.
     */
    @BeforeClass
    public void setUp() {
        reservationDao = mock(ReservationDao.class);
        apartmentDao = mock(ApartmentDao.class);
        reservationService.setReservationDao(reservationDao);
        reservationService.setApartmentDao(apartmentDao);
        user = new User(1, "Vadim", "Alekseevich", "Martyniuk", new BigDecimal(0),
                "mail@gmail.com", "+375251712452", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.ADMIN, true);
        apartmentClass = new ApartmentClass(1, "first", 2, 3, new BigDecimal(100), new BigDecimal(50),
                "description", "img/path.jpg");
        apartmentOne = new Apartment(1, "204", 2, apartmentClass, true);
        apartmentTwo = new Apartment(2, "301", 3, apartmentClass, true);
        apartmentList = new ArrayList<>();
        reservationList = new ArrayList<>();
        apartmentList.add(apartmentOne);
        apartmentList.add(apartmentTwo);
        reservation = new Reservation(1, LocalDate.now(), LocalDate.now(), LocalDateTime.now(), 2, new BigDecimal(100), new BigDecimal(10), new BigDecimal(50), user, apartmentOne, Status.WAITING_FOR_APPROVE);
        reservationList.add(new Reservation());
        reservationList.add(new Reservation());

    }

    /**
     * Book apartment test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void bookApartmentTest() throws ServiceException, DaoException {
        when(apartmentDao.findApartmentListByClassId(apartmentClass.getId())).thenReturn(apartmentList);
        when(reservationDao.isApartmentAvailable(anyLong(), anyObject(), anyObject())).thenReturn(true);
        when(reservationDao.addReservation(eq(apartmentOne), eq(user), anyObject(), anyObject(), anyObject(), eq(1))).thenReturn(true);

        assertTrue(reservationService.bookApartment(user, apartmentClass.getId(), LocalDate.now(), LocalDate.now(), 1));
    }

    /**
     * Read all reservations test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void readAllReservationsTest() throws ServiceException, DaoException {
        when(reservationDao.readAllReservationsByUserId(user.getId())).thenReturn(reservationList);
        assertEquals(reservationService.readAllReservationByUserId(user.getId()), reservationList);
    }

    /**
     * Read all reservation by status.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void readAllReservationByStatus() throws ServiceException, DaoException {
        when(reservationDao.readAllReservationsByStatus(Status.WAITING_FOR_APPROVE)).thenReturn(reservationList);
        assertEquals(reservationService.readAllReservationByStatus(Status.WAITING_FOR_APPROVE), reservationList);
    }

    /**
     * Update reservation status test.
     *
     * @throws ServiceException the service exception
     * @throws DaoException     the dao exception
     */
    @Test
    public void updateReservationStatusTest() throws ServiceException, DaoException {
        when(reservationDao.readReservationById(reservation.getId())).thenReturn(Optional.of(reservation));
        when(apartmentDao.findApartmentById(apartmentOne.getId())).thenReturn(Optional.of(apartmentOne));
        when(reservationDao.isApartmentAvailable(anyLong(), anyObject(), anyObject())).thenReturn(true);
        when(reservationDao.updateReservationApartmentAndStatus(reservation, Status.APPROVED)).thenReturn(true);
        assertTrue(reservationService.updateReservationStatus(reservation.getId(), apartmentOne.getId(), Status.APPROVED));
    }
}