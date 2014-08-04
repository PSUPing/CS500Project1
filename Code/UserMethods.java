import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Actor DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class UserMethods {

    private static Connection conn = null;
    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

    public String openDBConnection(String dbUser, String dbPass, String dbSID, String dbHost, int port) {

        String res="";
        if (conn != null) {
            closeDBConnection();
        }

        try {
            conn = DBUtils.openDBConnection(dbUser, dbPass, dbSID, dbHost, port);
            System.out.println("Opened a connection");
            res = DBUtils.testConnection(conn);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace(System.err);
        }
        return res;
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

    /**
     * Create a new user in the database.
     * @param newUser
     * @return
     */
    public User addUser(User newUser) {
        try {
            String query = "INSERT INTO users (userid, pwd, dob, date_joined) VALUES ('" +
                    newUser.getUID() + "', '" + newUser.getPassword() +
                    "', to_date('" + df.format(newUser.getDOB()) + "', 'MM/DD/YYYY')" +
                    ", to_date('" + df.format(new Date()) + "', 'MM/DD/YYYY'))";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newUser;
    }

    /**
     * Update a user in the database.
     * @param changedUser
     * @return
     */
    public User updateUser(User changedUser) {
        User user = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM users WHERE userid = '" + changedUser.getUID() + "'");

            if (cnt == 0) {
                return user;
            }

            String query = "UPDATE users SET pwd = '" + changedUser.getPassword() +
                    "', to_date('" + df.format(changedUser.getDOB()) + "', 'MM/DD/YYYY')" +
                    " WHERE userid = '" + changedUser.getUID() + "'";

            DBUtils.executeUpdate(conn, query);
            query = "SELECT userid, pwd, dob, date_joined FROM users WHERE userid = '" + changedUser.getUID() +"'";

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            user = new User(changedUser.getUID(), result.getString("pwd"), result.getDate("dob"), changedUser.getDateJoined());

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return user;
    }

    /**
     * Get a user from the database by the primary key and password.
     * @param name
     * @param dob
     * @return
     */
    public User getUser(String uid, String pwd) {
        User user = null;

        try {
            String query = "SELECT userID, pwd, dob, date_joined FROM users WHERE userID = '" + uid + "' AND pwd = '" + pwd + "'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            user = new User(result.getString("userID"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return user;
    }

    /**
     * Get a user from the database by the primary key.
     * @param name
     * @return
     */
    public User getUser(String uid) {
        User user = null;

        try {
            String query = "SELECT userID, pwd, dob, date_joined FROM users WHERE userID = '" + uid + "'";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            user = new User(result.getString("userID"), result.getString("pwd"), result.getDate("dob"), result.getDate("date_joined"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return user;
    }
}