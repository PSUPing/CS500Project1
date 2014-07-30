import java.sql.DatabaseMetaData;

/**
 *
 * Award Class
 *
 */
public class Award {

    private int awardID;
    private int actorID;
    private java.sql.Date awNomDate;
    private java.sql.Date awardDate;

    private java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy"); // Used for outputting the date

    /********** Constructors **********/

    public Award(int awid, int aid, java.sql.Date nomDate, java.sql.Date awDate) {
        awardID = awid;
        actorID = aid;
        awNomDate = nomDate;
        awardDate = awDate;
    }

    /********** Getters **********/

    public int getAWID() {
        return awardID;
    }

    public int getAID() {
        return actorID;
    }

    public java.sql.Date getNominationDate() {
        return awNomDate;
    }

    public java.sql.Date getAwardDate() {
        return awardDate;
    }

    /********** Setters **********/

    public void setUID(int awid) {
        awardID = awid;
    }

    public void setPassword(int aid) {
        actorID = aid;
    }

    public void setDOB(java.sql.Date nomDate) {
        awNomDate = nomDate;
    }

    public void setBio(java.sql.Date awDate) {
        awardDate = awDate;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(AWID: " + String.valueOf(awardID) + 
                ") AID: " + String.valueOf(actorID) + 
                " DOB: " + df.format(awNomDate) + 
                " Date Joined: " + df.format(awardDate);
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(awardID) + 
                "</td><td>" + String.valueOf(actorID) + 
                "</td><td>" + df.format(awNomDate) + 
                "</td><td>" + df.format(awardDate) + 
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"UID\" />" + String.valueOf(awardID) +
               "</td><td><input type=\"password\" name=\"AID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"text\" name=\"nomDt\" />" + df.format(awNomDate) +
               "</td><td><input type=\"text\" name=\"awardDt\" />" + df.format(awardDate) + "</td><td></tr>";
    }
}
