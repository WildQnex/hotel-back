package by.martyniuk.hotelbooking.factory;

import by.martyniuk.hotelbooking.command.CommandType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ActionCommandFactoryTest {


    @Test
    public void getActionCommandLowerCaseTest() {
        assertEquals(ActionCommandFactory.getActionCommand("login"), CommandType.LOGIN.receiveCommand());
    }

    @Test
    public void getActionCommandIgnoreCaseTest() {
        assertEquals(ActionCommandFactory.getActionCommand("LoGouT"), CommandType.LOGOUT.receiveCommand());
    }

    @Test
    public void getActionCommandWrongCommandTest() {
        assertEquals(ActionCommandFactory.getActionCommand("log in"), CommandType.DEFAULT.receiveCommand());
    }
}