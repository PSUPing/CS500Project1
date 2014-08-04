import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;

/**
 *
 * Class used for managing connection basics
 *
 */
public class MethodBase {

    private static Connection conn = null;
    private String dbUser = "";
    private String dbPass = "";
    private String dbSID = "";
    private String dbHost = "";
    private int port;
    private String message = "";
    private java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

    /**
     * Open the database connection.
     */
    public void openDBConnection() {
        if (conn != null)
            closeDBConnection();

        try {
            conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            System.out.println("Opened a connection");
            message = DBUtils.testConnection(conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
        }
    }

    /**
     * Close the database connection.
     */
    public void closeDBConnection() {
        try {
            DBUtils.closeDBConnection(conn);
            System.out.println("Closed a connection");
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }
    }

    /********** Getters **********/

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public String getDbSID() {
        return dbSID;
    }

    public String getHost() {
        return dbHost;
    }

    public int getPort() {
        return port;
    }

    public String getMessage() {
        return message;
    }

    public java.text.DateFormat getDf() {
        return dateFormat;
    }

    public Connection getConn() {
        return conn;
    }

    /********** Setters **********/

    public void setDbUser(String DBUser) {
        dbUser = DBUser;
    }

    public void setDbPass(String DBPass) {
        dbPass = DBPass;
    }

    public void setDbSID(String DBSID) {
        dbSID = DBSID;
    }

    public void setHost(String DBHost) {
        dbHost = DBHost;
    }

    public void setPort(int DBPort) {
        port = DBPort;
    }

    public void setMessage(String msg) {
        message = msg;
    }

    public void setConn(Connection cnx) {
        conn = cnx;
    }
}