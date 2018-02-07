package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.ApartmentServiceImpl;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * The Class ApartmentServiceImplTest.
 */
public class ApartmentServiceImplTest {

    /**
     * The apartment dao.
     */
    private ApartmentDao apartmentDao;

    /**
     * The reservation dao.
     */
    private ReservationDao reservationDao;

    /**
     * The apartment service.
     */
    private ApartmentServiceImpl apartmentService = new ApartmentServiceImpl();

    /**
     * The apartment one.
     */
    private Apartment apartmentOne;

    /**
     * The apartment two.
     */
    private Apartment apartmentTwo;

    /**
     * The apartment class one.
     */
    private ApartmentClass apartmentClassOne;

    /**
     * The apartment class two.
     */
    private ApartmentClass apartmentClassTwo;

    /**
     * The apartment list.
     */
    private List<Apartment> apartmentList;

    /**
     * The apartment list class one.
     */
    private List<Apartment> apartmentListClassOne;

    /**
     * The apartment list class two.
     */
    private List<Apartment> apartmentListClassTwo;

    /**
     * Sets the up.
     */
    @BeforeMethod
    public void setUp() {
        apartmentDao = mock(ApartmentDao.class);
        reservationDao = mock(ReservationDao.class);
        apartmentClassOne = new ApartmentClass(1, "first", 2, 3, new BigDecimal(100), new BigDecimal(50),
                "description", "img/path.jpg");
        apartmentClassTwo = new ApartmentClass(2, "second", 2, 3, new BigDecimal(100), new BigDecimal(50),
                "description", "img/path.jpg");
        apartmentOne = new Apartment(1, "204", 2, apartmentClassOne, true);
        apartmentTwo = new Apartment(2, "301", 3, apartmentClassTwo, true);
        apartmentList = new ArrayList<>();
        apartmentListClassOne = new ArrayList<>();
        apartmentListClassTwo = new ArrayList<>();
        apartmentList.add(apartmentOne);
        apartmentList.add(apartmentTwo);
        apartmentListClassOne.add(apartmentOne);
        apartmentListClassTwo.add(apartmentTwo);
    }

    /**
     * Insert apartment test.
     *
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void insertApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.addApartment(apartmentOne)).thenReturn(true);
        assertTrue(apartmentService.insertApartment(apartmentOne));
    }

    /**
     * Update apartment test.
     *
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void updateApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.updateApartment(apartmentOne)).thenReturn(true);
        assertTrue(apartmentService.updateApartment(apartmentOne));
    }

    /**
     * Delete apartment test.
     *
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void deleteApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.deleteApartment(apartmentOne.getId())).thenReturn(true);
        assertTrue(apartmentService.deleteApartment(apartmentOne.getId()));
    }

    /**
     * Gets the apartment test.
     *
     * @return the apartment test
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void getApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.findApartmentById(apartmentOne.getId())).thenReturn(Optional.of(apartmentOne));
        assertEquals(apartmentService.getApartment(apartmentOne.getId()), Optional.of(apartmentOne));
    }

    /**
     * Find all apartments test.
     *
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void findAllApartmentsTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.findAllApartments()).thenReturn(apartmentList);
        assertEquals(apartmentService.findAllApartments(), apartmentList);
    }

    /**
     * Find free apartments for reservations test.
     *
     * @throws DaoException     the dao exception
     * @throws ServiceException the service exception
     */
    @Test
    public void findFreeApartmentsForReservationsTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        ApartmentServiceImpl.reservationDao = reservationDao;
        when(apartmentDao.findApartmentListByClassId(apartmentClassOne.getId())).thenReturn(apartmentListClassOne);
        when(apartmentDao.findApartmentListByClassId(apartmentClassTwo.getId())).thenReturn(apartmentListClassTwo);
        when(reservationDao.isApartmentAvailable(eq(apartmentOne.getId()), anyObject(), anyObject())).thenReturn(true);
        when(reservationDao.isApartmentAvailable(eq(apartmentTwo.getId()), anyObject(), anyObject())).thenReturn(false);
        Map<Reservation, List<Apartment>> result = new HashMap<>();
        Reservation reservationOne = new Reservation();
        reservationOne.setCheckInDate(LocalDateTime.now().toLocalDate());
        reservationOne.setCheckOutDate(LocalDateTime.now().toLocalDate());
        reservationOne.setApartment(apartmentOne);
        Reservation reservationTwo = new Reservation();
        reservationTwo.setCheckInDate(LocalDateTime.now().toLocalDate());
        reservationTwo.setCheckOutDate(LocalDateTime.now().toLocalDate());
        reservationTwo.setApartment(apartmentTwo);
        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservationOne);
        reservationList.add(reservationTwo);
        result.put(reservationOne, apartmentListClassOne);
        result.put(reservationTwo, new ArrayList<>());
        assertEquals(apartmentService.findFreeApartmentsForReservations(reservationList), result);

    }


}