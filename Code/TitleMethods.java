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

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection connect) {
        conn = connect;
    }

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
     * Create a new role for an actor in the database.
     * @param addRole
     * @return
     */
    public ActorRole addActorRole(ActorRole addRole) {
        try {
            String query = "INSERT INTO Actors_Role_In (aid, tid, role) VALUES (" + addRole.getAID() + ", " + addRole.getTID() +
                    ", '" + addRole.getRole() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return addRole;
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
                    "', year = " + changedTitle.getYear() + ", synopsis = '" + changedTitle.getSynopsis() + "', title_type = '" +
                    changedTitle.getTitleType() + "' WHERE tid = " + changedTitle.getTID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT tid, name, genre, year, synopsis, title_type FROM titles WHERE tid = " + changedTitle.getTID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            title = new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("synopsis"), result.getString("title_type"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return title;
    }

    /**
     * Update an existing role in the database.
     * @param changedRole
     * @return
     */
    public ActorRole updateActorRole(ActorRole changedRole) {
        ActorRole role = null;

        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM titles WHERE tid = " + changedRole.getTID());

            if (cnt == 0)
                return role;

            String query = "UPDATE titles SET role = '" + changedRole.getRole() + "' WHERE tid = " + changedRole.getTID() +
                    " AND aid = " + changedRole.getAID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT aid, tid, role FROM Actors_Role_In WHERE aid = " + changedRole.getAID() + " tid = " + changedRole.getTID();

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
     * @param tid
     * @return
     */
    public Title getTitle(int tid) {
        Title title = null;

        try {
            String query = "SELECT tid, name, genre, year, synopsis, title_type FROM titles WHERE tid = " + tid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            title = new Title(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"), result.getString("synopsis"), result.getString("title_type"));

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
            String query = "SELECT tid, name, genre, year, synopsis, title_type FROM titles WHERE name LIKE '" + name + "%' ORDER BY tid DESC";
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
     * Grab a single for an Actor and their role in that title.
     * @param aid
     * @param tid
     * @return
     */
    public TitleActorRole getSingleTitleAndRole(int aid, int tid) {
        TitleActorRole titleRole = null;

        try {
            String query = "SELECT t.tid AS tid, name, genre, year, synopsis, title_type, role FROM titles t INNER JOIN Actors_Role_In ar ON (t.tid = ar.tid) WHERE ar.aid = " +
                    aid + " AND ar.tid = " + tid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            titleRole = new TitleActorRole(result.getInt("tid"), result.getString("name"), result.getString("genre"), result.getInt("year"),
                    result.getString("synopsis"), result.getString("title_type"), result.getString("role"));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return titleRole;
    }

    /**
     * Grab the titles for an Actor and their role in that title.
     * @return
     */
    public ArrayList getTitlesAndRole(String titleName) {
        ArrayList extendedTitles = new ArrayList();

        try {
            String query = "SELECT t.tid AS tid, name, genre, year, synopsis, title_type, role FROM titles t INNER JOIN Actors_Role_In ar ON (t.tid = ar.tid) WHERE t.name = " +
                    titleName + " ORDER BY title_type, name";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next())
                extendedTitles.add(new TitleActorRole(result.getInt("tid"), result.getString("name"), result.getString("genre"),
                        result.getInt("year"), result.getString("synopsis"), result.getString("title_type"), result.getString("role")));

            result.close();
            stmt.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
        }

        return extendedTitles;
    }

    /**
     * Get a review from the database by the title ID.
     * @param tid
     * @return
     */
    public ArrayList getReivewByTitle(int tid) {
        ArrayList reviews = new ArrayList();

        try {
            String query = "SELECT revid, rating, tid, rs, rt FROM reviews WHERE tid = " + tid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next())
                reviews.add(new Review(result.getInt("revid"), result.getInt("tid"), result.getInt("rating"), result.getString("rs"), result.getString("rt")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return reviews;
    }
}