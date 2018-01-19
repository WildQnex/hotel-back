package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class ReservationDaoImpl implements ReservationDao {

    private static final String SQL_RESERVE_APARTMENT = "INSERT INTO `hotel_booking`.`reservation` (`check_in_date`, `check_out_date`, " +
            "`order_time`, `person_amount`, `cost_per_person`, `cost_per_night`, `cost_animal`, `total_cost`, `user_id_fk`, " +
            "`apartment_id_fk`, `status_id_fk`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_CHECK_AVAILABILITY = "SELECT `id_reservation` " +
            "FROM `hotel_booking`.`reservation` " +
            "WHERE (NOT ((`check_in_date` > ? AND `check_in_date` >= ?) OR (check_out_date <= ? AND `check_out_date` < ?))) " +
            "AND (`apartment_id_fk` = ?) " +
            "AND (`status_id_fk` = (SELECT `id_status` FROM `hotel_booking`.`status` WHERE UPPER(`status`) LIKE UPPER(?)))";


    @Override
    public boolean addReservation(Apartment apartment, User user, LocalDate checkInDate, LocalDate checkOutDate, BigDecimal totalCost, int personsAmount) throws DaoException {
        try (Connection cn = ConnectionPool.getInstance().getConnection();
             PreparedStatement ps = cn.prepareStatement(SQL_RESERVE_APARTMENT)) {
            ps.setDate(1, Date.valueOf(checkInDate), Calendar.getInstance());
            ps.setDate(2, Date.valueOf(checkOutDate), Calendar.getInstance());
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(4, personsAmount);
            ps.setBigDecimal(5, apartment.getApartmentClass().getCostPerPerson());
            ps.setBigDecimal(6, apartment.getApartmentClass().getCostPerNight());
            ps.setBigDecimal(7, apartment.getApartmentClass().getAnimalCost());
            ps.setBigDecimal(8, totalCost);
            ps.setLong(9, user.getId());
            ps.setLong(10, apartment.getId());
            ps.setLong(11, 1);
            return (ps.executeUpdate() != 0);
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public boolean isApartmentAvailable(Apartment apartment, LocalDate checkInDate, LocalDate checkOutDate) throws DaoException {
        if (checkInDate.compareTo(checkOutDate) >= 0) {
            return false;
        }
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = connection.prepareStatement(SQL_CHECK_AVAILABILITY);
            Date checkIn = Date.valueOf(checkInDate);
            Date checkOut = Date.valueOf(checkOutDate);
            ps.setDate(1, checkIn, Calendar.getInstance());
            ps.setDate(2, checkOut, Calendar.getInstance());
            ps.setDate(3, checkIn, Calendar.getInstance());
            ps.setDate(4, checkOut, Calendar.getInstance());
            ps.setLong(5, apartment.getId());
            ps.setString(6, "Approved");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return false;
            }
            return true;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
