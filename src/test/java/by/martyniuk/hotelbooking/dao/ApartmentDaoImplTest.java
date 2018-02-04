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

public class ApartmentDaoImplTest {

    private ScriptRunner scriptRunner;
    private Connection connection;
    private ApartmentDao apartmentDao;
    private List<Apartment> apartmentList;
    private ApartmentClass apartmentClass;
    private Apartment apartmentOne;
    private Apartment apartmentTwo;
    private Apartment newApartment;

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

    @AfterClass
    public void tearDown() throws Exception {
        ConnectionPool.isTest = false;
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    @Test
    public void findApartmentLstByClassIdTest() throws DaoException {
        assertEquals(apartmentDao.findApartmentListByClassId(4), apartmentList);
    }

    @Test
    public void findAllApartmentsTest() throws DaoException {
        assertEquals(apartmentDao.findAllApartments().size(), 19);
    }

    @Test
    public void findApartmentByIdTest() throws DaoException {
        assertEquals(apartmentDao.findApartmentById(5).get(), apartmentOne);
    }

    @Test
    public void addApartmentTest() throws DaoException {
        apartmentDao.addApartment(newApartment);
        assertEquals(apartmentDao.findApartmentById(20).get(), newApartment);
    }

    @Test
    public void deleteApartmentTest() throws DaoException {
        apartmentDao.deleteApartment(5);
        assertEquals(apartmentDao.findApartmentById(5), Optional.empty());
    }

    @Test
    public void updateApartmentTest() throws DaoException {
        apartmentTwo.setFloor(6);
        apartmentDao.updateApartment(apartmentTwo);
        assertEquals(apartmentDao.findApartmentById(11), Optional.of(apartmentTwo));
    }
}