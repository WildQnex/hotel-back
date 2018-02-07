package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

/**
 * The Class ApartmentDaoImplTest.
 */
public class ApartmentDaoImplTest {

    /**
     * The script runner.
     */
    private ScriptRunner scriptRunner;

    /**
     * The connection.
     */
    private Connection connection;

    /**
     * The apartment dao.
     */
    private ApartmentDao apartmentDao;

    /**
     * The apartment list.
     */
    private List<Apartment> apartmentList;

    /**
     * The apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * The apartment one.
     */
    private Apartment apartmentOne;

    /**
     * The apartment two.
     */
    private Apartment apartmentTwo;

    /**
     * The new apartment.
     */
    private Apartment newApartment;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        apartmentDao = new ApartmentDaoImpl();
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
     * Method set up.
     *
     * @throws Exception the exception
     */
    @BeforeMethod
    public void methodSetUp() throws Exception {
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));
        apartmentClass = new ApartmentClass(4, "King size Deluxe", 1, 2, new BigDecimal("145.00"), new BigDecimal("40.00"),
                "A spacious and cozy deluxe room appointed in a classic style with a magnificent view of the City Hall.", "img/king-size-deluxe.jpg");
        apartmentOne = new Apartment(5, "105", 1, apartmentClass, true);
        apartmentTwo = new Apartment(11, "302", 3, apartmentClass, true);
        newApartment = new Apartment(20, "405", 4, apartmentClass, true);
        apartmentList = new ArrayList<>();
        apartmentList.add(apartmentOne);
        apartmentList.add(apartmentTwo);
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
     * Find apartment lst by class id test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findApartmentLstByClassIdTest() throws DaoException {
        assertEquals(apartmentDao.findApartmentListByClassId(4), apartmentList);
    }

    /**
     * Find all apartments test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findAllApartmentsTest() throws DaoException {
        assertEquals(apartmentDao.findAllApartments().size(), 19);
    }

    /**
     * Find apartment by id test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findApartmentByIdTest() throws DaoException {
        assertEquals(apartmentDao.findApartmentById(5).get(), apartmentOne);
    }

    /**
     * Adds the apartment test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void addApartmentTest() throws DaoException {
        apartmentDao.addApartment(newApartment);
        assertEquals(apartmentDao.findApartmentById(20).get(), newApartment);
    }

    /**
     * Delete apartment test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void deleteApartmentTest() throws DaoException {
        apartmentDao.deleteApartment(5);
        assertEquals(apartmentDao.findApartmentById(5), Optional.empty());
    }

    /**
     * Update apartment test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void updateApartmentTest() throws DaoException {
        apartmentTwo.setFloor(6);
        apartmentDao.updateApartment(apartmentTwo);
        assertEquals(apartmentDao.findApartmentById(11), Optional.of(apartmentTwo));
    }
}