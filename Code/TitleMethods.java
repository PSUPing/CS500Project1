import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Title DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class TitleMethods {

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
     * Create a new title in the database.
     * @param newTitle
     * @return
     */
    public Title addTitle(Title newTitle) {
        try {
            int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(tid) FROM titles");

            String query = "INSERT INTO titles VALUES (" + sid + ", '" + newTitle.getName() + "', '" + newTitle.getGenre() + "', " +
                    newTitle.getYear() + ", '" + newTitle.getSynopsis() + "', '" + newTitle.getTitleType() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newTitle;
    }

    /**
     * Update an new title in the database.
     * @param changedTitle
     * @return
     */
    public Title updateTitle(Title changedTitle) {
        Title title = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM titles WHERE tid = " + changedTitle.getTID());

            if (cnt == 0)
                return title;

            String query = "UPDATE titles SET name = '" + changedTitle.getName() + "', genre = '" + changedTitle.getGenre() +
                    "', year = " + changedTitle.getYear() + "', synopsis = '" + changedTitle.getSynopsis() + "', title_type = '" + 
                    changedTitle.getTitleType() + "' WHERE tid = " + changedTitle.getTID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT tid, name, genre, year, snyopsis, title_type FROM titles WHERE tid = " + changedTitle.getTID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            title = new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("snyopsis"), result.getString("title_type"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return title;
    }

    /**
     * Get a title from the database by the primary key.
     * @param tid
     * @return
     */
    public Title getTitle(int tid) {
        Title title = null;

        try {
            String query = "SELECT tid, name, genre, year, snyopsis, title_type FROM titles WHERE tid = " + tid + "%' ORDER BY tid DESC";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            title = new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("snyopsis"), result.getString("title_type"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return title;
    }

    /**
     * Get a title from the database by its name.
     * @param name
     * @return
     */
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
    }

    /**
     * Grab the first 10 titles from the DB.
     * @return
     */
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
    }

    /**
     * Grab the titles for an Actor and their role in that title.
     * @return
     */
    public ArrayList getTitlesAndRole(int aid) {
        ArrayList extendedTitles = new ArrayList();
        int currCount = 0;

        try {
            String query = "SELECT tid, name, genre, year, snyopsis, title_type, role FROM titles t INNER JOIN Actor_Role_In ar ON (t.tid = ar.tid) WHERE ar.aid = " +
                            aid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next() && currCount <= 10)
                extendedTitles.add(new TitleActorRole(result.getInt("tid"), result.getString("name"), result.getString("genre"), 
                    result.getInt("year"), result.getString("snyopsis"), result.getString("title_type"), result.getString("role")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return extendedTitles;
    }
}
