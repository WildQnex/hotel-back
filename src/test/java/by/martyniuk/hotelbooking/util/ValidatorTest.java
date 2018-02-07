package by.martyniuk.hotelbooking.util;

import by.martyniuk.hotelbooking.entity.Role;
import by.martyniuk.hotelbooking.entity.Status;
import by.martyniuk.hotelbooking.entity.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * The Class ValidatorTest.
 */
public class ValidatorTest {

    /**
     * The correct user.
     */
    private User correctUser;

    /**
     * The incorrect user.
     */
    private User incorrectUser;


    /**
     * Sets the up.
     *
     * @throws Exception the exception
     */
    @BeforeClass
    public void setUp() {
        correctUser = new User(2, "Ivan", "Ivanovich", "Ivanov", new BigDecimal("1000.00"),
                "user@gmail.com", "+375291234567", "$2a$10$dli9pv2bKHf9.OfGatlFrOFJaWRYR14C94VBX1jL33ckdbIiTEg9u", Role.USER, true);

        incorrectUser = new User(2, "I", "", null, new BigDecimal("1000.00"),
                "mail", "+375291234dfs2f", "", Role.USER, true);

    }

    /**
     * Validate user test.
     */
    @Test
    public void validateUserTest() {
        assertTrue(Validator.validateUser(correctUser));
    }

    /**
     * Validate incorrect user test.
     */
    @Test
    public void validateIncorrectUserTest() {
        assertFalse(Validator.validateUser(incorrectUser));
    }

    /**
     * Validate name test.
     */
    @Test
    public void validateNameTest() {
        assertTrue(Validator.validateName(correctUser.getFirstName()));
    }

    /**
     * Validate incorrect name test.
     */
    @Test
    public void validateIncorrectNameTest() {
        assertFalse(Validator.validateName(incorrectUser.getFirstName()));
    }

    /**
     * Validate id test.
     */
    @Test
    public void validateIdTest() {
        assertTrue(Validator.validateId("2355"));
    }

    /**
     * Validate incorrect id test.
     */
    @Test
    public void validateIncorrectIdTest() {
        assertFalse(Validator.validateName("42k"));
    }

    /**
     * Validate password equals test.
     */
    @Test
    public void validatePasswordEqualsTest() {
        assertTrue(Validator.validatePasswords("password", "password"));
    }

    /**
     * Validate password not equals test.
     */
    @Test
    public void validatePasswordNotEqualsTest() {
        assertFalse(Validator.validatePasswords("password", "123"));
    }

    /**
     * Validate date range test.
     */
    @Test
    public void validateDateRangeTest() {
        assertTrue(Validator.validateDateRange(LocalDate.now().plusDays(2), LocalDate.now().plusDays(5)));
    }

    /**
     * Validate invalid date range test.
     */
    @Test
    public void validateInvalidDateRangeTest() {
        assertFalse(Validator.validateDateRange(LocalDate.now().minusDays(2), LocalDate.now().minusDays(4)));
    }

    /**
     * Validate email test.
     */
    @Test
    public void validateEmailTest() {
        assertTrue(Validator.validateEmail(correctUser.getEmail()));
    }

    /**
     * Validate incorrect mail test.
     */
    @Test
    public void validateIncorrectMailTest() {
        assertFalse(Validator.validateEmail(incorrectUser.getEmail()));
    }

    /**
     * Validate person amount test.
     */
    @Test
    public void validatePersonAmountTest() {
        assertTrue(Validator.validatePersonAmount("5"));
    }

    /**
     * Validate incorrect person amount test.
     */
    @Test
    public void validateIncorrectPersonAmountTest() {
        assertFalse(Validator.validatePersonAmount("322gf"));
    }

    /**
     * Validate phone password test.
     */
    @Test
    public void validatePhonePasswordTest() {
        assertTrue(Validator.validatePhoneNumber(correctUser.getPhoneNumber()));
    }

    /**
     * Validate incorrect phone number test.
     */
    @Test
    public void validateIncorrectPhoneNumberTest() {
        assertFalse(Validator.validatePersonAmount(incorrectUser.getPhoneNumber()));
    }

    /**
     * Validate status test.
     */
    @Test
    public void validateStatusTest() {
        assertTrue(Validator.validateStatus(Status.WAITING_FOR_APPROVE.toString()));
    }

    /**
     * Validate incorrect status test.
     */
    @Test
    public void validateIncorrectStatusTest() {
        assertFalse(Validator.validateStatus(correctUser.getEmail()));
    }
}