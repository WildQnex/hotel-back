package by.martyniuk.hotelbooking.util;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * The Class MessagePrintTag.
 */
public class MessagePrintTag extends BodyTagSupport {

    /**
     * The key.
     */
    private String key;

    /**
     * The color.
     */
    private String color;

    /**
     * Sets the key.
     *
     * @param key the new key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Sets the color.
     *
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Do start tag.
     *
     * @return the int
     */
    @Override
    public int doStartTag() {
        try {
            String error = (String) this.pageContext.getSession().getAttribute(key);
            if (error != null) {
                this.pageContext.getSession().removeAttribute(key);
                JspWriter out = this.pageContext.getOut();
                out.write("<div class=\"row\"></div>");
                out.write("<div class=\"row\">");
                out.write("<div id=\"" + key + "\" class=\"col s8 m6 offset-m3 offset-s2 center " + color + "-text\">" + error + "</div>");
                out.write("</div>");
                out.write("<div class=\"row\"></div>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
