import java.sql.DatabaseMetaData;

/**
 *
 * Review Class
 *
 */
public class Review {

    private int reviewID;
    private int titleID;
    private int revScore;
    private String revSrc;
    private String revText;

    /********** Constructors **********/

    public Review(int revID, int tid, int score, String src, String rText) {
        reviewID = revID;
        titleID = tid;
        revScore = score;
        revSrc = src;
        revText = rText;
    }

    /********** Getters **********/

    public int getRevID() {
        return reviewID;
    }

    public int getTID() {
        return titleID;
    }

    public int getScore() {
        return revScore;
    }

    public String getReviewSource() {
        return revSrc;
    }

    public String getReviewText() {
        return revText;
    }

    /********** Setters **********/

    public void setRevID(int revID) {
        reviewID = revID;
    }

    public void setTID(int tid) {
        titleID = tid;
    }

    public void setScore(int score) {
        revScore = score;
    }

    public void setReviewSource(String src) {
        revSrc = src;
    }

    public void setGenre(String rText) {
        revText = rText;
    }

    /********** Output Methods **********/

    public String toString() {
        return "(RevID: " + String.valueOf(reviewID) + 
                " TID: " + String.valueOf(titleID) + 
                ") Score: " + String.valueOf(revScore) + 
                " Source: " + revSrc +
                " Text: " + revText;
    }

    public String toViewHTML() {
        return "<tr><td>" + String.valueOf(reviewID) + 
                "</td><td>" + String.valueOf(titleID) + 
                "</td><td>" + String.valueOf(revScore) + 
                "</td><td>" + revSrc +
                "</td><td>" + revText +
                "</td><td></tr>";
    }

    public String toEditHTML() {
        return "<tr><td><input type=\"text\" name=\"RevID\" />" + String.valueOf(reviewID) +
               "</td><td><input type=\"password\" name=\"TID\" />" + String.valueOf(titleID) +
               "</td><td><input type=\"text\" name=\"revScore\" />" + String.valueOf(revScore) +
               "</td><td><input type=\"text\" name=\"revSrc\" />" + revSrc +
               "</td><td><input type=\"text\" name=\"revText\" />" + revText + "</td><td></tr>";
    }
}
