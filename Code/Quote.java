import java.sql.DatabaseMetaData;

/**
 *
 * Quote Class
 *
 */
public class Quote {

    private int quoteID;
    private int actorID;
    private String aQuote;

    /********** Constructors **********/

    public Quote(int qid, int aid, String quote) {
        quoteID = qid;
        actorID = aid;
        aQuote = quote;
    }

    /********** Getters **********/

    public int getQID() {
        return quoteID;
    }

    public int getAID() {
        return actorID;
    }

    public String getQuote() {
        return aQuote;
    }

    /********** Setters **********/

    public void setQID(int qid) {
        quoteID = qid;
    }

    public void setTID(int aid) {
        actorID = aid;
    }

    public void setQuote(String quote) {
        aQuote = quote;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(QID: " + String.valueOf(quoteID) + 
                ") AID: " + String.valueOf(actorID) + 
                " Quote: " + aQuote;
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(quoteID) + 
                "</td><td>" + String.valueOf(actorID) + 
                "</td><td>" + aQuote +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"RevID\" />" + String.valueOf(quoteID) +
               "</td><td><input type=\"password\" name=\"TID\" />" + String.valueOf(actorID) +
               "</td><td><input type=\"text\" name=\"revSrc\" />" + aQuote +
               "</td><td></tr>";
    }
}
