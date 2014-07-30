import java.sql.DatabaseMetaData;

/**
 *
 * Actor Class
 *
 */
public class Actor {

    private int actorID;
    private String actorName;
    private java.sql.Date actorDob;
    private String actorBio;

    /********** Constructors **********/

    public Actor(int aid) {
        actorID = aid;
    }

    public Actor(int aid, String name, java.sql.Date dob, String bio) {
        actorID = aid;
        actorName = name;
        actorDob = dob;
        actorBio = bio;
    }

    /********** Getters **********/

    public String getAID() {
        return actorID;
    }

    public String getName() {
        return actorName;
    }

    public java.sql.Date getDOB() {
        return actorDob;
    }

    public String getBio() {
        return actorBio;
    }

    /********** Setters **********/

    public void setAID(int aid) {
        actorID = aid;
    }

    public void setName(String name) {
        actorName = name;
    }

    public void setDOB(java.sql.Date dob) {
        actorDob = dob;
    }

    public void setBio(String bio) {
        actorBio = bio;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(AID: " + aid + ") Name: " + actorName + " DOB: " + actorDob + " Bio: " + actorBio;
    }

    public String toViewHTML() {
        return "<tr><td>" + actorID + "</td><td>" + actorName + "</td><td>" + actorDob + "</td><td>" + actorBio + "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"aID\" />" + actorID +
               "</td><td><input type=\"text\" name=\"aName\" />" + actorName +
               "</td><td><input type=\"text\" name=\"aDob\" />" + actorDob +
               "</td><td><input type=\"text\" name=\"aBio\" />" + actorBio + "</td><td></tr>";
    }
}
