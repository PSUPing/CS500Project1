import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Actor DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class UserMethods {

    private static Connection conn = null;

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
     * Create a new actor in the database.
     * @param newActor
     * @return

    public Actor addActor(Actor newActor) {
        try {
            String query = "INSERT INTO actors (name, dob, bio) VALUES ('" +
                    newActor.getName() + "', '" + newActor.getDOB() + "', '" + newActor.getBio() + "');";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newActor;
    }*/

    /**
     * Update an new actor in the database.
     * @param changedActor
     * @return

    public Actor updateActor(Actor changedActor) {
        Actor actor = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM actors WHERE name = '" + changedActor.getName() +
                    "' AND '" + changedActor.getDOB() + "';");

            if (cnt == 0)
                return actor;

            String query = "UPDATE actors SET bio = '" + changedActor.getBio() + "' WHERE name = '" + changedActor.getName() +
                    "' AND '" + changedActor.getDOB() + "';";
            DBUtils.executeUpdate(conn, query);

            query = "SELECT name, dob, bio FROM actors WHERE name = '" + changedActor.getName() +
                    "' AND '" + changedActor.getDOB() + "';";

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(changedActor.getAID(), changedActor.getName(), changedActor.getDOB(), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }*/

    /**
     * Get an new actor from the database by the primary key.
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
}