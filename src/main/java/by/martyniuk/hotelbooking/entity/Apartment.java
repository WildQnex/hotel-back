package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * The Class Apartment.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment implements Serializable, Cloneable {

    /**
     * The id.
     */
    private long id;

    /**
     * The number.
     */
    private String number;

    /**
     * The floor.
     */
    private int floor;

    /**
     * The apartment class.
     */
    private ApartmentClass apartmentClass;

    /**
     * The active.
     */
    private boolean active;
}
