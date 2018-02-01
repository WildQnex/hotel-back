package by.martyniuk.hotelbooking.service;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.impl.ApartmentClassServiceImpl;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class ApartmentClassServiceImplTest {

    private ApartmentClassDao apartmentClassDao;
    private ApartmentClassServiceImpl apartmentClassServiceImpl = new ApartmentClassServiceImpl();
    private ApartmentClass apartmentClassOne;
    private ApartmentClass apartmentClassTwo;
    private List<ApartmentClass> apartmentClassList;

    @BeforeMethod
    public void setUp() {
        apartmentClassDao = mock(ApartmentClassDao.class);
        apartmentClassOne = new ApartmentClass(1, "first", 2, 3, new BigDecimal(100), new BigDecimal(50),
                "description", "img/path.jpg");
        apartmentClassTwo = new ApartmentClass(2, "second", 2, 3, new BigDecimal(100), new BigDecimal(50),
                "description", "img/path.jpg");
        apartmentClassList = new ArrayList<>();
        apartmentClassList.add(apartmentClassOne);
        apartmentClassList.add(apartmentClassTwo);
    }

    @AfterMethod
    public void tearDown() {
    }

    @Test
    public void findApartmentClassByIdTest() throws DaoException, ServiceException {
        ApartmentClassServiceImpl.apartmentClassDao = apartmentClassDao;
        when(apartmentClassDao.findApartmentClassById(apartmentClassOne.getId())).thenReturn(Optional.of(apartmentClassOne));
        assertEquals(apartmentClassServiceImpl.findApartmentClassById(apartmentClassOne.getId()), Optional.of(apartmentClassOne));
    }

    @Test(expectedExceptions = ServiceException.class)
    public void findApartmentClassByIdExceptionTest() throws DaoException, ServiceException {
        ApartmentClassServiceImpl.apartmentClassDao = apartmentClassDao;
        when(apartmentClassDao.findApartmentClassById(apartmentClassOne.getId())).thenThrow(new DaoException());
        apartmentClassServiceImpl.findApartmentClassById(apartmentClassOne.getId());
    }

    @Test
    public void findApartmentClassesTest() throws DaoException, ServiceException {
        ApartmentClassServiceImpl.apartmentClassDao = apartmentClassDao;
        when(apartmentClassDao.findAllApartmentClasses()).thenReturn(apartmentClassList);
        assertEquals(apartmentClassServiceImpl.findAllApartmentClasses(), apartmentClassList);
    }


}