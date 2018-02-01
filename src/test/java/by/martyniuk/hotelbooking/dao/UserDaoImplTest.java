package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.UserDaoImpl;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
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
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

public class UserDaoImplTest {

    private ScriptRunner scriptRunner;
    private Connection connection;
    private UserDao userDao;
    private User user;

    @BeforeClass
    public void setUp() throws Exception {
        userDao = new UserDaoImpl();
        Properties properties = new Properties();
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&serverTimezone=GMT",
                properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
        scriptRunner = new ScriptRunner(connection, false, true);
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));
        ConnectionPool.getInstance().initConnectionPool(5, "jdbc:mysql://localhost:3306/hotel_booking_test?useUnicode=true&serverTimezone=GMT");
    }

    @BeforeMethod
    public void beforeMethodSetUp() throws Exception {
        scriptRunner.runScript(new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql")));
        user = new User(2, "Ivan", "Ivanovich", "Ivanov", new BigDecimal("1000.00"),
                "user@gmail.com", "+375291234567", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.USER, true);

    }

    @AfterClass
    public void tearDown() throws Exception {
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    @Test
    public void updateUserDataTest() throws DaoException {
        String testName = "TestName";
        user.setFirstName(testName);
        userDao.updateUserData(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get().getFirstName(), testName);
    }

    @Test
    public void findUserByMailTest() throws DaoException {
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    @Test
    public void findAllUsersTest() throws DaoException {
        assertEquals(userDao.findAllUsers().get(1), user);
    }

    @Test
    public void addUserTest() throws DaoException {
        String mail = "mail@mail.ru";
        user.setId(5);
        user.setEmail(mail);
        userDao.addUser(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    @Test(expectedExceptions = DaoException.class)
    public void addUserSameMailTest() throws DaoException {
        user.setId(5);
        userDao.addUser(user);
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }

    @Test
    public void updateUserPasswordTest() throws DaoException {
        String newPassword = "NewPassword";
        user.setPassword(newPassword);
        userDao.updateUserPassword(user.getId(), user.getPassword());
        assertEquals(userDao.findUserByMail(user.getEmail()).get(), user);
    }
}