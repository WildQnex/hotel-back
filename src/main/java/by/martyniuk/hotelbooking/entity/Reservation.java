package by.martyniuk.hotelbooking.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The Class Reservation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation implements Serializable, Cloneable {

    /**
     * The id.
     */
    private long id;

    /**
     * The check in date.
     */
    private LocalDate checkInDate;

    /**
     * The check out date.
     */
    private LocalDate checkOutDate;

    /**
     * The order time.
     */
    private LocalDateTime orderTime;

    /**
     * The person amount.
     */
    private int personAmount;

    /**
     * The cost per person.
     */
    private BigDecimal costPerPerson;

    /**
     * The cost per night.
     */
    private BigDecimal costPerNight;

    /**
     * The total cost.
     */
    private BigDecimal totalCost;

    /**
     * The user.
     */
    private User user;

    /**
     * The apartment.
     */
    private Apartment apartment;

    /**
     * The status.
     */
    private Status status;
}
