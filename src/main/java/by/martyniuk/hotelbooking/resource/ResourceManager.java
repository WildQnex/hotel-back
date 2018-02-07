package by.martyniuk.hotelbooking.resource;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Class ResourceManager.
 */
public class ResourceManager {

    /**
     * The current locale.
     */
    public static Locale currentLocale = Locale.getDefault();

    /**
     * Gets the resource bundle.
     *
     * @return the resource bundle
     */
    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("text", currentLocale);
    }
}
