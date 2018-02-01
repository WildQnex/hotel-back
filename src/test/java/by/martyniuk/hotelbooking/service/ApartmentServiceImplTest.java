package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentDao;
import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.ApartmentServiceImpl;
import org.testng.annotations.AfterMethod;
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

public class ApartmentServiceImplTest {

    private ApartmentDao apartmentDao;
    private ReservationDao reservationDao;
    private ApartmentServiceImpl apartmentService = new ApartmentServiceImpl();
    private Apartment apartmentOne;
    private Apartment apartmentTwo;
    private ApartmentClass apartmentClassOne;
    private ApartmentClass apartmentClassTwo;
    private List<Apartment> apartmentList;
    private List<Apartment> apartmentListClassOne;
    private List<Apartment> apartmentListClassTwo;

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

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void insertApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.addApartment(apartmentOne)).thenReturn(true);
        assertTrue(apartmentService.insertApartment(apartmentOne));
    }

    @Test
    public void updateApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.updateApartment(apartmentOne)).thenReturn(true);
        assertTrue(apartmentService.updateApartment(apartmentOne));
    }

    @Test
    public void deleteApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.deleteApartment(apartmentOne.getId())).thenReturn(true);
        assertTrue(apartmentService.deleteApartment(apartmentOne.getId()));
    }

    @Test
    public void getApartmentTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.findApartmentById(apartmentOne.getId())).thenReturn(Optional.of(apartmentOne));
        assertEquals(apartmentService.getApartment(apartmentOne.getId()), Optional.of(apartmentOne));
    }

    @Test
    public void findAllApartmentsTest() throws DaoException, ServiceException {
        ApartmentServiceImpl.apartmentDao = apartmentDao;
        when(apartmentDao.findAllApartments()).thenReturn(apartmentList);
        assertEquals(apartmentService.findAllApartments(), apartmentList);
    }

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