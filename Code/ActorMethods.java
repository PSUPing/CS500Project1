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
public class ActorMethods {

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
     * Create a new actor in the database.
     * @param newActor
     * @return
     */
    public Actor addActor(Actor newActor) {
        try {
            int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(aid) FROM actors");

            String query = "INSERT INTO actors VALUES (" + sid + ", '" + newActor.getName() + "', " +
                    "to_date('" + df.format(newActor.getDOB()) + "', 'MM/dd/yyyy')" + ", '" +
                    newActor.getBio() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newActor;
    }

    /**
     * Update an new actor in the database.
     * @param changedActor
     * @return
     */
    public Actor updateActor(Actor changedActor) {
        Actor actor = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM actors WHERE aid = " + changedActor.getAID());

            if (cnt == 0)
                return actor;

            String query = "UPDATE actors SET name = '" + changedActor.getName() +
                    "', dob = to_date('" + df.format(changedActor.getDOB()) + "', 'MM/dd/yyyy')" +
                    ", bio = '" + changedActor.getBio() + "' WHERE aid = " + changedActor.getAID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT aid, name, dob, bio FROM actors WHERE aid = " + changedActor.getAID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(changedActor.getAID(), result.getString("name"), result.getDate("dob"), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Get an new actor from the database by the primary key.
     * @param aid
     * @return
     */
    public Actor getActor(int aid) {
        Actor actor = null;

        try {
            String query = "SELECT aid, name, dob, bio FROM actors WHERE aid = " + aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Get an new actor from the database by the primary key.
     * @param name
     * @return
     */
    public Actor getActorByName(String name) {
        Actor actor = null;

        try {
            String query = "SELECT aid, name, dob, bio FROM actors WHERE name = " + name;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            actor = new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return actor;
    }

    /**
     * Grab the first 10 actors from the DB.
     * @return
     */
    public ArrayList getFirst10Actors() {
        ArrayList actors = new ArrayList();
        int currCount = 0;

        try {
            String query = "SELECT aid, name, dob, bio FROM actors";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next() && currCount <= 10) {
                actors.add(new Actor(result.getInt("aid"), result.getString("name"), result.getDate("dob"), result.getString("bio")));
                currCount++;
            }

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return actors;
    }
}