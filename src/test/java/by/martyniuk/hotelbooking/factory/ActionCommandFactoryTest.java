package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.CommandType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * The Class ActionCommandFactoryTest.
 */
public class ActionCommandFactoryTest {


    /**
     * Gets the action command lower case test.
     *
     * @return the action command lower case test
     */
    @Test
    public void getActionCommandLowerCaseTest() {
        assertEquals(ActionCommandFactory.getActionCommand("login"), CommandType.LOGIN.receiveCommand());
    }

    /**
     * Gets the action command ignore case test.
     *
     * @return the action command ignore case test
     */
    @Test
    public void getActionCommandIgnoreCaseTest() {
        assertEquals(ActionCommandFactory.getActionCommand("LoGouT"), CommandType.LOGOUT.receiveCommand());
    }

    /**
     * Gets the action command wrong command test.
     *
     * @return the action command wrong command test
     */
    @Test
    public void getActionCommandWrongCommandTest() {
        assertEquals(ActionCommandFactory.getActionCommand("log in"), CommandType.DEFAULT.receiveCommand());
    }
}