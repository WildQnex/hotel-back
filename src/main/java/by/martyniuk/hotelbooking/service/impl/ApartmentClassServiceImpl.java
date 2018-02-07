package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.dao.impl.ApartmentClassDaoImpl;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentClassService;

import java.util.List;
import java.util.Optional;

/**
 * The Class ApartmentClassServiceImpl.
 */
public class ApartmentClassServiceImpl implements ApartmentClassService {

    /**
     * The apartment class dao.
     */
    public static ApartmentClassDao apartmentClassDao = new ApartmentClassDaoImpl();


    @Override
    public Optional<ApartmentClass> findApartmentClassById(long id) throws ServiceException {
        try {
            return apartmentClassDao.findApartmentClassById(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<ApartmentClass> findAllApartmentClasses() throws ServiceException {
        try {
            return apartmentClassDao.findAllApartmentClasses();
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
