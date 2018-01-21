package by.martyniuk.hotelbooking.constant;

public enum PagePath {
    ADMIN("jsp/admin.jsp"),
    MENU("jsp/menus.jsp"),
    APARTMENTS("jsp/apartments.jsp"),
    BOOK_APARTMENT("jsp/bookApartment.jsp"),
    CLASSES("jsp/classes.jsp"),
    ERROR("jsp/error.jsp"),
    FOOTER("jsp/footer.jsp"),
    HEADER("jsp/header.jsp"),
    MAIN("jsp/main.jsp"),
    REGISTER("jsp/register.jsp"),
    USER("jsp/user.jsp");

    private String page;

    PagePath(String page){
        this.page = page;
    }

    public String getPage(){
        return page;
    }
}
