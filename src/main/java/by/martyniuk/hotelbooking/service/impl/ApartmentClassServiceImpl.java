package by.martyniuk.hotelbooking.service.impl;

import by.martyniuk.hotelbooking.dao.ApartmentClassDao;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.exception.ServiceException;
import by.martyniuk.hotelbooking.service.ApartmentClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The Class ApartmentClassServiceImpl.
 */
@Service
public class ApartmentClassServiceImpl implements ApartmentClassService {

    /**
     * The apartment class dao.
     */
    private ApartmentClassDao apartmentClassDao;


    @Autowired
    public void setApartmentClassDao(ApartmentClassDao apartmentClassDao) {
        this.apartmentClassDao = apartmentClassDao;
    }

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


    @Override
    public boolean updateApartmentClass(ApartmentClass apartmentClass) throws ServiceException {
        try {
            return apartmentClassDao.updateApartmentClass(apartmentClass);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean addApartmentClass(ApartmentClass apartmentClass) throws ServiceException {
        try {
            return apartmentClassDao.addApartmentClass(apartmentClass);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean deleteApartmentClass(long id) throws ServiceException {
        try {
            return apartmentClassDao.deleteApartmentClass(id);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
}
