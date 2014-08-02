import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * Review DB CRUD (minus the D) Methods for use in the Servlet classes.
 *
 */
public class ReviewMethods {

    private static Connection conn = null;
    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

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
     * Create a new review in the database.
     * @param newReview
     * @return
     */
    public Review addReview(Review newReview) {
        try {
            int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(revid) FROM reviews");

            String query = "INSERT INTO reviews (revid, rating, tid, rs, rt) VALUES (" + sid + ", " + newReview.getScore() +
                    ", " + newReview.getTID() + ", '" + newReview.getReviewSource() + "', '" + newReview.getReviewText() + "')";
            DBUtils.executeUpdate(conn, query);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return newReview;
    }

    /**
     * Update a review in the database.
     * @param changedReview
     * @return
     */
    public Review updateReview(Review changedReview) {
        try {
            int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM reviews WHERE revid = " + changedReview.getRevID());

            if (cnt == 0)
                return changedReview;

            String query = "UPDATE review SET rating = " + changedReview.getScore() + ", tid = " + changedReview.getTID() +
                    ", rs = '" + changedReview.getReviewSource() + "', '" + changedReview.getReviewText() +
                    "' WHERE revid = " + changedReview.getRevID();
            DBUtils.executeUpdate(conn, query);

            query = "SELECT revid, rating, tid, rs, rt FROM reviews WHERE revid = " + changedReview.getRevID();

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            changedReview = new Review(result.getInt("revid"), result.getInt("tid"), result.getInt("score"), result.getString("rs"), result.getString("rt"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return changedReview;
    }

    /**
     * Get a review from the database by the primary key.
     * @param revid
     * @return
     */
    public Review getReview(int revid) {
        Review review = null;

        try {
            String query = "SELECT revid, rating, tid, rs, rt FROM reviews WHERE revid = " + revid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            result.next();
            review = new Review(result.getInt("revid"), result.getInt("tid"), result.getInt("score"), result.getString("rs"), result.getString("rt"));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return review;
    }

    /**
     * Get a review from the database by the title ID.
     * @param tid
     * @return
     */
    public ArrayList getReivewByTitle(int tid) {
        ArrayList reviews = new ArrayList();

        try {
            String query = "SELECT revid, rating, tid, rs, rt FROM reviews WHERE tid= " + tid;
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(query);

            while (result.next())
                reviews.add(new Review(result.getInt("revid"), result.getInt("tid"), result.getInt("score"), result.getString("rs"), result.getString("rt")));

            result.close();
            stmt.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace(System.err);
        }

        return reviews;
    }
}
