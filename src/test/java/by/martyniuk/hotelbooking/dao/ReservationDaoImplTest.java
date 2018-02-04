package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;
import by.martyniuk.hotelbooking.pool.ConnectionPoolTest;
import com.ibatis.common.jdbc.ScriptRunner;
import com.mysql.cj.jdbc.Driver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ReservationDaoImplTest {

    private ScriptRunner scriptRunner;
    private Connection connection;
    private ReservationDao reservationDao;

    @BeforeClass
    public void setUp() throws Exception {
        reservationDao = new ReservationDaoImpl();
        Properties properties = new Properties();
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection(properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
        scriptRunner = new ScriptRunner(connection, false, true);
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));
        ConnectionPool.isTest = true;
    }

    @BeforeMethod
    public void beforeMethodSetUp() throws Exception {
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));

    }

    @AfterClass
    public void tearDown() throws Exception {
        ConnectionPool.isTest = false;
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    @Test
    public void readReservationByIdTest() throws DaoException {
        assertTrue(reservationDao.readReservationById(2).isPresent());
    }

    @Test
    public void addReservationTest() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        reservation.setId(3);
        reservation.setCheckInDate(LocalDate.now().plusDays(10));
        reservation.setCheckOutDate(LocalDate.now().plusDays(12));
        assertTrue(reservationDao.addReservation(reservation.getApartment(), reservation.getUser(), reservation.getCheckInDate(),
                reservation.getCheckOutDate(), reservation.getTotalCost(), reservation.getPersonAmount()));
    }

    @Test
    public void isApartmentAvailableTest() throws DaoException {
        assertTrue(reservationDao.isApartmentAvailable(1, LocalDate.now().plusDays(10), LocalDate.now().plusDays(12)));
    }

    @Test
    public void updateReservationApartmentAndStatusTest() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        reservationDao.updateReservationApartmentAndStatus(reservation, Status.APPROVED);
        assertEquals(reservationDao.readReservationById(2).get().getStatus(), Status.APPROVED);
    }

    @Test
    public void readReservationById() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        assertEquals(reservationDao.readAllReservationsByUserId(1).get(0), reservation);
    }

    @Test
    public void readAllReservations() throws DaoException {
        assertEquals(reservationDao.readAllReservations().size(), 2);
    }

    @Test
    public void readAllReservationsByStatus() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        assertEquals(reservationDao.readAllReservationsByStatus(Status.WAITING_FOR_APPROVE).get(0), reservation);
    }
}