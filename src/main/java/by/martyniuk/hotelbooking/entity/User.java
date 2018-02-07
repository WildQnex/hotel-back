package by.martyniuk.hotelbooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * The Class User.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable, Cloneable {

    /**
     * The id.
     */
    private long id;

    /**
     * The first name.
     */
    private String firstName;

    /**
     * The middle name.
     */
    private String middleName;

    /**
     * The last name.
     */
    private String lastName;

    /**
     * The balance.
     */
    private BigDecimal balance;

    /**
     * The email.
     */
    private String email;

    /**
     * The phone number.
     */
    private String phoneNumber;

    /**
     * The password.
     */
    private String password;

    /**
     * The role.
     */
    private Role role;

    /**
     * The active.
     */
    private boolean active;
}
