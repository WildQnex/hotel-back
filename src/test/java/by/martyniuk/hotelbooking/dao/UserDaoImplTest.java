package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Role;
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
import java.util.Properties;

import static org.testng.Assert.assertEquals;

/**
 * The Class UserDaoImplTest.
 */
public class UserDaoImplTest {

    /**
     * The script runner.
     */
    private ScriptRunner scriptRunner;

    /**
     * The connection.
     */
    private Connection connection;

    /**
     * The user dao.
     */
    private UserDao userDao;

    /**
     * The user.
     */
    private User user;

    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public void setUp() throws Exception {
        userDao = new UserDaoImpl();
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
        user = new User(2, "Ivan", "Ivanovich", "Ivanov", new BigDecimal("1000.00"),
                "user@gmail.com", "+375291234567", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.USER, true);

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
     * Update user data test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void updateUserDataTest() throws DaoException {
        String testName = "TestName";
        user.setFirstName(testName);
        userDao.updateUserData(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get().getFirstName(), testName);
    }

    /**
     * Find user by mail test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findUserByMailTest() throws DaoException {
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    /**
     * Find all users test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void findAllUsersTest() throws DaoException {
        assertEquals(userDao.findAllUsers().get(1), user);
    }

    /**
     * Adds the user test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void addUserTest() throws DaoException {
        String mail = "mail@mail.ru";
        user.setId(5);
        user.setEmail(mail);
        userDao.addUser(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    /**
     * Adds the user same mail test.
     *
     * @throws DaoException the dao exception
     */
    @Test(expectedExceptions = DaoException.class)
    public void addUserSameMailTest() throws DaoException {
        user.setId(5);
        userDao.addUser(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    /**
     * Update user password test.
     *
     * @throws DaoException the dao exception
     */
    @Test
    public void updateUserPasswordTest() throws DaoException {
        String newPassword = "NewPassword";
        user.setPassword(newPassword);
        userDao.updateUserPassword(user.getId(), user.getPassword());
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }
}