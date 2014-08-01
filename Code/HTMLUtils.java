import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * An implementation of some basic database utilities.
 *
 * @author Matt Ping & Matt Bucci
 *
 */
public class HTMLUtils {
    public static String renderHeader(String pageName, String userName, String servletName) {
        String header = "<html>\n\t<head>\n\t\t<title>Internet Actor Database (IADB) - " + pageName +
                "</title>\n\t</head>\n\t<body>\n";

        if (servletName != "LoginServlet") {
            if (userName.equals(""))
                header += "\t\t<div style=\"text-align:right\"><a href=\"LoginServlet?page=" + servletName + "\">Login</a></div>\n";
            else
                header += "\t\t<div style=\"text-align:right\">User: " + userName +" <a href=\"" + servletName + "\">Logout</a></div>\n";
        }

        return header;
    }

    public static String renderClosingTags() {
        return "\t</body>\n</html>";
    }
}