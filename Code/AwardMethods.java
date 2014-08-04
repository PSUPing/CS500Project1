import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
/**
*
* Award DB CRUD (minus the D) Methods for use in the Servlet classes.
*
*/
public class AwardMethods {

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
    * Create a new award in the database.
    * @param newAward
    * @return
    */
   public Award addAward(Award newAward) {
       try {
           int sid = 1 + DBUtils.getIntFromDB(conn, "SELECT MAX(awid) FROM awards");

           String query = "INSERT INTO awards (awid, aid, nomination_date, award_date) VALUES (" + sid + ", " + newAward.getAID() +
                   ", to_date('" + df.format(newAward.getNominationDate()) + "', 'MM/DD/YYYY')" +
                   ", to_date('" + df.format(newAward.getAwardDate()) + "', 'MM/DD/YYYY'))";
           DBUtils.executeUpdate(conn, query);
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return newAward;
   }

   /**
    * Update a award in the database.
    * @param changedAward
    * @return
    */
   public Award updateAward(Award changedAward) {
       try {
           int cnt = DBUtils.getIntFromDB(conn, "SELECT COUNT(*) FROM awards WHERE awid = " + changedAward.getAWID());

           if (cnt == 0)
               return changedAward;

           String query = "UPDATE awards SET aid = " + changedAward.getAID() + ", nomination_date = to_date('" + df.format(changedAward.getNominationDate()) + "', 'MM/DD/YYYY')" +
                   ", award_date = to_date('" + df.format(changedAward.getAwardDate()) + "', 'MM/DD/YYYY')" +
                   " WHERE awid = " + changedAward.getAWID();
           DBUtils.executeUpdate(conn, query);

           query = "SELECT awid, nomination_date, aid, award_date FROM Awards WHERE awid = " + changedAward.getAWID();

           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           changedAward = new Award(result.getInt("awid"), result.getInt("aid"), result.getDate("nomination_date"), result.getDate("award_date"));
 
           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return changedAward;
   }

   /**
    * Get a award from the database by the primary key.
    * @param awid
    * @return
    */
   public Award getAward(int awid) {
       Award award = null;

       try {
           String query = "SELECT awid, aid, nomination_date, award_date FROM Awards WHERE awid = " + awid;
           Statement stmt = conn.createStatement();
           ResultSet result = stmt.executeQuery(query);

           result.next();
           award = new Award(result.getInt("awid"), result.getInt("aid"), result.getDate("nomination_date"), result.getDate("award_date"));

           result.close();
           stmt.close();
       } catch (SQLException sqlEx) {
           sqlEx.printStackTrace(System.err);
       }

       return award;
   }
}