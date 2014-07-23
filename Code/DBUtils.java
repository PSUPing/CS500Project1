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
 * @author Julia Stoyanovich (stoyanovich@drexel.edu) 
 * 
 */
public class DBUtils {

  /**
   * Open a database connection.
   * 
   * @param user
   * @param pass
   * @param SID
   * @param host
   * @return
   * @throws SQLException
   * @throws ClassNotFoundException
   */
    public static Connection openDBConnection(String user, String pass, String SID, String host, int port)
          throws SQLException, ClassNotFoundException {

        String driver = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@" + host + ":" + port + ":" + SID;
        String username = user;
        String password = pass;
        Class.forName(driver);

        return DriverManager.getConnection(url, username, password);
    }

    /**
     * Test the connection.
     * @param conn
     * @return 'servus' if the connection is open.  Otherwise an exception will be thrown.
     * @throws SQLException
     */
    public static String testConnection(Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select 'servus' res from dual");
        String res = "";

        while (rs.next()) {
            res = rs.getString("res");
        }

        rs.close();
        st.close();
        return res;
    }
  
    /**
     * Close the database connection.
     * @param conn
     * @throws SQLException if connection is already closed.
     */
    public static void closeDBConnection(Connection conn) throws SQLException {
        conn.close();
    }

    /**
     * Get an integer that is returned as a result of a query.
     * @param conn
     * @param query
     * @return result
     * @throws SQLException
     */
    public static int getIntFromDB(Connection conn, String query) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        int ret = Integer.MIN_VALUE;

        if (rs.next()) {
           ret = rs.getInt(1);
        }

        rs.close();
        st.close();

        return ret;
    }

    /**
     * Execute an update or a delete query.
     * @param conn
     * @param query
     * @throws SQLException
     */
    public static void executeUpdate(Connection conn, String query) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(query);
        st.close();
    }

    /**
     * Execute an update or a delete query using a prepared statement.
     * @param conn
     * @param query
     * @throws SQLException
     */
    public static void executeUpdate(Connection conn, String query, String[] bindValues ) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(query);
        // porting to Java 4, to run on linux.ischool
        //for (String val : bindValues) {

        for (int i=0; i<bindValues.length; i++) {
            String val = bindValues[i];
            pst.setString(1, val);
            pst.executeUpdate();
        }

        pst.close();
    }
}