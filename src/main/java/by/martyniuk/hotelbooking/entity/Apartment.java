package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment implements Serializable, Cloneable {
    private long id;
    private String number;
    private int floor;
    private boolean isAnimalsAllowed;
    private boolean isSmokingAllowed;
    private ApartmentClass apartmentClass;
}
