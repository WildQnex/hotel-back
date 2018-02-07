package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.ReservationDaoImpl;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Status;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * The Class ReservationDaoImplTest.
 */
public class ReservationDaoImplTest {

    /**
     * The script runner.
     */
    private ScriptRunner scriptRunner;

    /**
     * The connection.
     */
    private Connection connection;

    /**
     * The reservation dao.
     */
    private ReservationDao reservationDao;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
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

    /**
     * Before method set up.
     *
     * @throws Exception the exception
     */
    @BeforeMethod
    public void beforeMethodSetUp() throws Exception {
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));

    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @AfterClass
    public void tearDown() throws Exception {
        ConnectionPool.isTest = false;
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    /**
     * Read reservation by id test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void readReservationByIdTest() throws DaoException {
        assertTrue(reservationDao.readReservationById(2).isPresent());
    }

    /**
     * Adds the reservation test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void addReservationTest() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        reservation.setId(3);
        reservation.setCheckInDate(LocalDate.now().plusDays(10));
        reservation.setCheckOutDate(LocalDate.now().plusDays(12));
        assertTrue(reservationDao.addReservation(reservation.getApartment(), reservation.getUser(), reservation.getCheckInDate(),
                reservation.getCheckOutDate(), reservation.getTotalCost(), reservation.getPersonAmount()));
    }

    /**
     * Checks if is apartment available test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void isApartmentAvailableTest() throws DaoException {
        assertTrue(reservationDao.isApartmentAvailable(1, LocalDate.now().plusDays(10), LocalDate.now().plusDays(12)));
    }

    /**
     * Update reservation apartment and status test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void updateReservationApartmentAndStatusTest() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        reservationDao.updateReservationApartmentAndStatus(reservation, Status.APPROVED);
        assertEquals(reservationDao.readReservationById(2).get().getStatus(), Status.APPROVED);
    }

    /**
     * Read reservation by id.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void readReservationById() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        assertEquals(reservationDao.readAllReservationsByUserId(1).get(0), reservation);
    }

    /**
     * Read all reservations.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void readAllReservations() throws DaoException {
        assertEquals(reservationDao.readAllReservations().size(), 2);
    }

    /**
     * Read all reservations by status.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void readAllReservationsByStatus() throws DaoException {
        Reservation reservation = reservationDao.readReservationById(2).get();
        assertEquals(reservationDao.readAllReservationsByStatus(Status.WAITING_FOR_APPROVE).get(0), reservation);
    }
}