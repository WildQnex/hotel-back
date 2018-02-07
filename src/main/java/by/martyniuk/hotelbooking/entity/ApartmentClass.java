package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class ApartmentClass.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentClass implements Serializable, Cloneable {

    /**
     * The id.
     */
    private long id;

    /**
     * The type.
     */
    private String type;

    /**
     * The rooms amount.
     */
    private int roomsAmount;

    /**
     * The max capacity.
     */
    private int maxCapacity;

    /**
     * The cost per night.
     */
    private BigDecimal costPerNight;

    /**
     * The cost per person.
     */
    private BigDecimal costPerPerson;

    /**
     * The description.
     */
    private String description;

    /**
     * The image path.
     */
    private String imagePath;
}
