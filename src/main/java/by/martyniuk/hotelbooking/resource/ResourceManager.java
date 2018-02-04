package by.martyniuk.hotelbooking.resource;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {
    public static Locale currentLocale = Locale.getDefault();

    public static ResourceBundle getResourceBundle(){
        return ResourceBundle.getBundle("text", currentLocale);
    }
}
