import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Actor Role DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class ActorRoleMethods {

    private static Connection conn = null;
//    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

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
     * Create a new role for an actor in a title in the database.
     * @param newRole
     * @return
     */
    public ActorRole addRole(ActorRole newRole) {
        try {
            String query = "INSERT INTO Actor_Role_In VALUES (" + newRole.getAID() + ", " + newRole.getTID() + ", '" + newRole.getRole() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newRole;
    }

    /**
     * Update an actor's role in the database.
     * @param changedRole
     * @return
     */
    public ActorRole updateRole(ActorRole changedRole) {
        ActorRole role = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM Actor_Role_In WHERE aid = " + changedRole.getAID() + " AND " + changedRole.getTID());

            if (cnt == 0)
                return role;

            String query = "UPDATE titles SET role = " + changedRole.getRole() + " WHERE aid = " + changedRole.getAID() + 
                    " AND tid = " + changedRole.getTID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT aid, tid, role FROM Actor_Role_In WHERE aid = " + changedRole.getAID() + " tid = " + changedRole.getTID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            role = new ActorRole(result.getInt("aid"), result.getInt("tid"), result.getString("role"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return role;
    }

    /**
     * Get a title from the database by the primary key.
     * @param aid
     * @param tid
     * @return
     */
    public ActorRole getRole(int aid, int tid) {
        ActorRole role = null;

        try {
            String query = "SELECT aid, tid, role FROM Actor_Role_In WHERE aid = " + aid + " tid = " + tid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            role = new ActorRole(result.getInt("aid"), result.getInt("tid"), result.getString("role"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return role;
    }

    /**
     * Get a title from the database by its name.
     * @param name
     * @return
     *
    public ArrayList getTitleByName(String name) {
        ArrayList titles = new ArrayList();

        try {
            String query = "SELECT tid, name, genre, year, snyopsis, title_type FROM titles WHERE name LIKE '" + name + "%' ORDER BY tid DESC";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            // A loop may not be needed, but it will ensure that actor is null if there is no result
            while (result.next())
                titles.add(new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("snyopsis"), result.getString("title_type")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return titles;
    }*/

    /**
     * Grab the first 10 titles from the DB.
     * @return
     *
    public ArrayList getRecentTitles() {
        ArrayList titles = new ArrayList();
        int currCount = 0;

        try {
            String query = "SELECT tid, name, genre, year, snyopsis, title_type FROM titles ORDER BY tid DESC";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next() && currCount <= 10)
                titles.add(new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("snyopsis"), result.getString("title_type")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return titles;
    }*/
}
