import java.sql.DatabaseMetaData;

/**
 *
 * User Class
 *
 */
public class User {

    private String userID;
    private String userPwd;
    private java.sql.Date userDOB;
    private java.sql.Date userJoined;

    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

    /********** Constructors **********/

    public User(String uid) {
        userID = uid;
    }

    public User(String uid, String pwd, java.sql.Date dob, java.sql.Date joined) {
        userID = uid;
        userPwd = pwd;
        userDOB = dob;
        userJoined = joined;
    }

    /********** Getters **********/

    public String getUID() {
        return userID;
    }

    public String getPassword() {
        return userPwd;
    }

    public java.sql.Date getDOB() {
        return userDOB;
    }

    public java.sql.Date getDateJoined() {
        return userJoined;
    }

    /********** Setters **********/

    public void setUID(String uid) {
        userID = uid;
    }

    public void setPassword(String pwd) {
        userPwd = pwd;
    }

    public void setDOB(java.sql.Date dob) {
        userDOB = dob;
    }

    public void setBio(java.sql.Date joined) {
        userJoined = joined;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(UID: " + userID + 
                ") Password: " + userPwd + 
                " DOB: " + df.format(userDOB) + 
                " Date Joined: " + df.format(userJoined);
    }

    public String toViewHTML() {
        return "<tr><td>" + userID + 
                "</td><td>" + userPwd + 
                "</td><td>" + df.format(userDOB) + 
                "</td><td>" + df.format(userJoined) + 
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"uID\" />" + userID +
               "</td><td><input type=\"password\" name=\"uPwd\" />" + userPwd +
               "</td><td><input type=\"text\" name=\"uDob\" />" + df.format(userDOB) +
               "</td><td><input type=\"text\" name=\"uJoined\" />" + df.format(userJoined) + "</td><td></tr>";
    }
}
