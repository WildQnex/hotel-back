package by.martyniuk.hotelbooking.constant;

import by.martyniuk.hotelbooking.command.CommandType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PagePath {
    APPROVE_RESERVATIONS("jsp/admin/reservations.jsp"),
    MENU("jsp/menus.jsp"),
    APARTMENTS("jsp/admin/apartments.jsp"),
    BOOK_APARTMENT("jsp/user/apartment-booking.jsp"),
    CLASSES("jsp/classes.jsp"),
    ERROR("jsp/error.jsp"),
    FOOTER("jsp/footer.jsp"),
    HEADER("jsp/header.jsp"),
    MAIN("jsp/main.jsp"),
    REGISTER("jsp/register.jsp"),
    USER("jsp/user/profile.jsp"),
    USER_RESERVATION("jsp/user/reservations.jsp"),
    USER_MANAGER("jsp/admin/user-manager.jsp");

    private String page;

    PagePath(String page){
        this.page = page;
    }

    public String getPage(){
        return page;
    }

    private static List<String> pages = Arrays.stream(PagePath.values())
            .map(Enum::toString)
            .collect(Collectors.toList());

    public static boolean isPresent(String page) {
        return (page != null) && (pages.contains(page.toUpperCase()));
    }
}
