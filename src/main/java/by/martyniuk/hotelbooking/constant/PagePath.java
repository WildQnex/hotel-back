package by.martyniuk.hotelbooking.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Enum PagePath.
 */
public enum PagePath {

    /**
     * The approve reservations.
     */
    APPROVE_RESERVATIONS("jsp/admin/reservations.jsp"),

    /**
     * The menu.
     */
    MENU("jsp/menus.jsp"),

    /**
     * The apartments.
     */
    APARTMENTS("jsp/admin/apartments.jsp"),

    /**
     * The book apartment.
     */
    BOOK_APARTMENT("jsp/user/apartment-booking.jsp"),

    /**
     * The classes.
     */
    CLASSES("jsp/classes.jsp"),

    /**
     * The error.
     */
    ERROR("jsp/error.jsp"),

    /**
     * The footer.
     */
    FOOTER("jsp/footer.jsp"),

    /**
     * The header.
     */
    HEADER("jsp/header.jsp"),

    /**
     * The main.
     */
    MAIN("jsp/main.jsp"),

    /**
     * The register.
     */
    REGISTER("jsp/register.jsp"),

    /**
     * The user.
     */
    USER("jsp/user/profile.jsp"),

    /**
     * The user reservation.
     */
    USER_RESERVATION("jsp/user/reservations.jsp"),

    /**
     * The user manager.
     */
    USER_MANAGER("jsp/admin/user-manager.jsp"),

    /**
     * The admin's user profile.
     */
    ADMIN_USER_PROFILE("jsp/admin/user-profile.jsp"),


    DOCUMENT("jsp/document.jsp");
    /**
     * The page.
     */
    private String page;

    /**
     * Instantiates a new page path.
     *
     * @param page the page
     */
    PagePath(String page) {
        this.page = page;
    }

    /**
     * Gets the page.
     *
     * @return the page
     */
    public String getPage() {
        return page;
    }

    /**
     * The pages.
     */
    private static List<String> pages = Arrays.stream(PagePath.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

    /**
     * Checks if is present.
     *
     * @param page the page
     * @return true, if is present
     */
    public static boolean isPresent(String page) {
        return (page != null) && (pages.contains(page.toUpperCase()));
    }
}
