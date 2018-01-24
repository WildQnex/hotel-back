package by.martyniuk.hotelbooking.dao;

import by.martyniuk.hotelbooking.dao.impl.ApartmentDaoImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static org.mockito.Mockito.mock;

public class ApartmentClassDaoImplTest {

    @BeforeMethod
    public void setUp() throws Exception {
        mock(ApartmentDaoImpl.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }
}