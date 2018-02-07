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

/**
 * The Class ApartmentClassDaoImplTest.
 */
public class ApartmentClassDaoImplTest {

    /**
     * The script runner.
     */
    private ScriptRunner scriptRunner;

    /**
     * The connection.
     */
    private Connection connection;

    /**
     * The apartment class dao.
     */
    private ApartmentClassDao apartmentClassDao;

    /**
     * The apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        apartmentClassDao = new ApartmentClassDaoImpl();
        Properties properties = new Properties();
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql"));
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection(properties.getProperty("jdbc.url"),
                properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
        scriptRunner = new ScriptRunner(connection, false, true);
        scriptRunner.runScript(reader);
        apartmentClass = new ApartmentClass(1, "Single", 1, 1, new BigDecimal("100.00"), new BigDecimal("45.00"),
                "A comfortable classic room with a single bed for one person.", "img/single.jpg");
        ConnectionPool.isTest = true;
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
     * Find apartment class by id test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findApartmentClassByIdTest() throws DaoException {
        assertEquals(apartmentClassDao.findApartmentClassById(1L).get(), apartmentClass);
    }

    /**
     * Find all apartment test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findAllApartmentTest() throws DaoException {
        assertEquals(apartmentClassDao.findAllApartmentClasses().size(), 9);
    }

}