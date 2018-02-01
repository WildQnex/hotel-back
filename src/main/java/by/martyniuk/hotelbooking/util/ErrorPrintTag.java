package by.martyniuk.hotelbooking.util;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Map;

public class ErrorPrintTag extends BodyTagSupport {
    private Map<String, Object> session;
    private String key;

    public void setScope(Map<String, Object> session) {
        this.session = session;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int doStartTag() {
        try {
            String error = (String) session.get(key);
            if (error != null) {
                session.remove(key);
                JspWriter out = this.pageContext.getOut();
                out.write("<div class=\"row\"></div>");
                out.write("<div class=\"row\">");
                out.write("<div class=\"col s8 m6 offset-m3 offset-s2 center red-text\">" + error + "</div>");
                out.write("</div>");
                out.write("<div class=\"row\"></div>");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SKIP_BODY;
    }
}
