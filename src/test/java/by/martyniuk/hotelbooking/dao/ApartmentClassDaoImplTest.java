package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.ApartmentClassDaoImpl;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;
import by.martyniuk.hotelbooking.pool.ConnectionPoolTest;
import com.ibatis.common.jdbc.ScriptRunner;
import com.mysql.cj.jdbc.Driver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class ApartmentClassDaoImplTest {

    private ScriptRunner scriptRunner;
    private Connection connection;
    private ApartmentClassDao apartmentClassDao;
    private ApartmentClass apartmentClass;

    @BeforeClass
    public void setUp() throws Exception {
        apartmentClassDao = new ApartmentClassDaoImpl();
        Properties properties = new Properties();
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql"));
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&serverTimezone=GMT",
                properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
        scriptRunner = new ScriptRunner(connection, false, true);
        scriptRunner.runScript(reader);
        ConnectionPool.getInstance().initConnectionPool(5, "jdbc:mysql://localhost:3306/hotel_booking_test?useUnicode=true&serverTimezone=GMT");
        apartmentClass = new ApartmentClass(1, "Single", 1, 1, new BigDecimal("100.00"), new BigDecimal("45.00"),
                "A comfortable classic room with a single bed for one person.", "img/single.jpg");
    }

    @AfterClass
    public void tearDown() throws Exception {
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    @Test
    public void findApartmentClassByIdTest() throws DaoException {
        assertEquals(apartmentClassDao.findApartmentClassById(1L).get(), apartmentClass);
    }

    @Test
    public void findAllApartmentTest() throws DaoException {
        assertEquals(apartmentClassDao.findAllApartmentClasses().size(), 9);
    }

}