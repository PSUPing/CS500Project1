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
        return "(UID: " + userID + ") Password: " + userPwd + " DOB: " + userDOB + " Date Joined: " + userJoined;
    }

    public String toViewHTML() {
        return "<tr><td>" + userID + "</td><td>" + userPwd + "</td><td>" + userDOB + "</td><td>" + userJoined + "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"uID\" />" + userID +
               "</td><td><input type=\"password\" name=\"uPwd\" />" + userPwd +
               "</td><td><input type=\"text\" name=\"uDob\" />" + userDOB +
               "</td><td><input type=\"text\" name=\"uJoined\" />" + userJoined + "</td><td></tr>";
    }
}
