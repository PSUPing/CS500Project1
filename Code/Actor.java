import java.sql.DatabaseMetaData;

/**
 *
 * Actor Class
 *
 */
public class Actor {

    private String actorName;
    private java.sql.Date actorDob;
    private String actorBio;

    /********** Constructors **********/

    public Actor(String name, java.sql.Date dob) {
        actorName = name;
        actorDob = dob;
    }

    public Actor(String name, java.sql.Date dob, String bio) {
        actorName = name;
        actorDob = dob;
        actorBio = bio;
    }

    /********** Getters **********/

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
        return "(Name: " + actorName + " DOB: " + actorDob + ") Bio: " + actorBio;
    }

    public String toViewHTML() {
        return "<tr><td>" + actorName + "</td><td>" + actorDob + "</td><td>" + actorBio + "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"aName\" />" + actorName +
               "</td><td><input type=\"text\" name=\"aDob\" />" + actorDob +
               "</td><td><input type=\"text\" name=\"aBio\" />" + actorBio + "</td><td></tr>";
    }
}