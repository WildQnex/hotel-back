package by.martyniuk.hotelbooking.dao.impl;

import by.martyniuk.hotelbooking.dao.ReservationDao;
import by.martyniuk.hotelbooking.entity.Apartment;
import by.martyniuk.hotelbooking.entity.ApartmentClass;
import by.martyniuk.hotelbooking.entity.Reservation;
import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import by.martyniuk.hotelbooking.exception.DaoException;
import by.martyniuk.hotelbooking.pool.ConnectionPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReservationDaoImpl implements ReservationDao {

    private static final String SQL_RESERVE_APARTMENT = "INSERT INTO `hotel_booking`.`reservation` (`check_in_date`, `check_out_date`, " +
            "`order_time`, `person_amount`, `cost_per_person`, `cost_per_night`, `cost_animal`, `total_cost`, `user_id_fk`, " +
            "`apartment_id_fk`, `status_id_fk`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_CHECK_AVAILABILITY = "SELECT `id_reservation` " +
            "FROM `hotel_booking`.`reservation` " +
            "WHERE (NOT ((`check_in_date` > ? AND `check_in_date` >= ?) OR (check_out_date <= ? AND `check_out_date` < ?))) " +
            "AND (`apartment_id_fk` = ?) " +
            "AND (`status_id_fk` = (SELECT `id_status` FROM `hotel_booking`.`status` WHERE UPPER(`status`) LIKE UPPER(?)))";

    private static final String SQL_SELECT_ALL_RESERVATIONS = "SELECT `id_reservation`, `check_in_date`, `check_out_date`, `order_time`," +
            " `person_amount`, `id_user`, `first_name`, `middle_name`, `last_name`, `balance`," +
            " `email`, `phone_number`, `password`, `role`, `active`, `id_apartment`, `number`, `floor`, `animals_allowed`," +
            " `smoking_allowed`, `id_apartment_class`, `type`, `rooms_amount`, `max_capacity`, `reservation`.`cost_per_night` AS `reservation_cost_per_night`, " +
            " `reservation`.`cost_per_person` AS `reservation_cost_per_person`, `reservation`.`cost_animal` AS `reservation_animal_cost`, " +
            " `image_path`, `id_status`, `status`, `total_cost`, `apartment_class`.`cost_per_night` AS `apartment_cost_per_night`, " +
            " `apartment_class`.`cost_per_person` AS `apartment_cost_per_person`, `apartment_class`.`animal_cost` AS `apartment_animal_cost` " +
            "FROM `hotel_booking`.`reservation` " +
            "LEFT JOIN (`user` LEFT JOIN `role` ON `role`.`id_role` = `user`.`role_id_fk`) ON `user`.`id_user` = `reservation`.`user_id_fk` " +
            "LEFT JOIN (`apartment` LEFT JOIN `apartment_class` ON `apartment_class`.`id_apartment_class` = `apartment`.`apartment_class_id_fk`) " +
            "ON `apartment`.`id_apartment` = `reservation`.`apartment_id_fk` " +
            "LEFT JOIN `status` ON `status`.`id_status` = `reservation`.`status_id_fk`";


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

    @Override
    public List<Reservation> readAllReservations() throws DaoException {
        List<Reservation> reservations = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection()) {
            Statement ps = connection.createStatement();
            ResultSet resultSet = ps.executeQuery(SQL_SELECT_ALL_RESERVATIONS);
            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id_user"), resultSet.getString("first_name"),
                        resultSet.getString("middle_name"), resultSet.getString("last_name"),
                        new BigDecimal(resultSet.getString("balance")), resultSet.getString("email"),
                        resultSet.getString("phone_number"), resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase()),
                        resultSet.getInt("active") != 0);
                Apartment apartment = new Apartment(resultSet.getLong("id_apartment"), resultSet.getString("number"),
                        resultSet.getInt("floor"), resultSet.getInt("animals_allowed") != 0,
                        resultSet.getInt("smoking_allowed") != 0,
                        new ApartmentClass(resultSet.getLong("id_apartment_class"), resultSet.getString("type"),
                                resultSet.getInt("rooms_amount"), resultSet.getInt("max_capacity"),
                                resultSet.getBigDecimal("apartment_cost_per_night"), resultSet.getBigDecimal("apartment_cost_per_person"),
                                resultSet.getBigDecimal("apartment_animal_cost"), resultSet.getString("image_path")));
                Status status = Status.valueOf(resultSet.getString("status").toUpperCase());
                Reservation reservation = new Reservation(resultSet.getLong("id_reservation"), resultSet.getDate("check_in_date").toLocalDate(),
                        resultSet.getDate("check_out_date").toLocalDate(), resultSet.getTimestamp("order_time").toLocalDateTime(),
                        resultSet.getInt("person_amount"), resultSet.getBigDecimal("reservation_cost_per_person"),
                        resultSet.getBigDecimal("reservation_cost_per_night"), resultSet.getBigDecimal("reservation_animal_cost"),
                        resultSet.getBigDecimal("total_cost"), user, apartment, status);
                reservations.add(reservation);
            }
            return reservations;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}
