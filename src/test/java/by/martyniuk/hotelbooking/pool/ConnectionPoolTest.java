package by.martyniuk.hotelbooking.pool;

import com.ibatis.common.jdbc.ScriptRunner;
import com.mysql.cj.jdbc.Driver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ConnectionPoolTest {

    private ScriptRunner scriptRunner;
    private Connection connection;

    @BeforeClass
    public void setUp() throws Exception {
        Properties properties = new Properties();
        properties.load(ConnectionPool.class.getResourceAsStream("/db.properties"));
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Insert.sql"));
        DriverManager.registerDriver(new Driver());
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useUnicode=true&serverTimezone=GMT",
                properties.getProperty("jdbc.username"), properties.getProperty("jdbc.password"));
        scriptRunner = new ScriptRunner(connection, false, true);
        scriptRunner.runScript(reader);
        ConnectionPool.getInstance().initConnectionPool(2, "jdbc:mysql://localhost:3306/hotel_booking_test?useUnicode=true&serverTimezone=GMT");

    }

    @AfterClass
    public void tearDown() throws Exception {
        Reader reader = new InputStreamReader(ConnectionPoolTest.class.getResourceAsStream("/Drop.sql"));
        scriptRunner.runScript(reader);
        connection.close();
    }

    @Test
    public void getConnectionTest() {
        Connection connectionOne = ConnectionPool.getInstance().getConnection();
        Connection connectionTwo = ConnectionPool.getInstance().getConnection();
        assertEquals(2, ConnectionPool.getInstance().getAmountBusyConnections());
        ConnectionPool.getInstance().returnConnection(connectionOne);
        ConnectionPool.getInstance().returnConnection(connectionTwo);
    }

    @Test
    public void returnConnectionTest() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        ConnectionPool.getInstance().returnConnection(connection);
        assertEquals(0, ConnectionPool.getInstance().getAmountBusyConnections());
    }

    @Test
    public void connectionNotNullTest() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        assertNotNull(connection);
        ConnectionPool.getInstance().returnConnection(connection);
    }

    @Test
    public void connectionWorkTest() throws SQLException {
        Connection connection = ConnectionPool.getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT `type` FROM `hotel_booking_test`.`apartment_class` WHERE `id_apartment_class` = 1");
        if (resultSet.next()) {
            assertEquals(resultSet.getString(1), "Single");
        }
        ConnectionPool.getInstance().returnConnection(connection);
    }

}