package model.movie;

public class Reviews {
    private double userScore;
    private String userName;
    private String userComment;
    private String imdbID;

    Reviews(String imdbID, String userName, double userScore, String userComment) {

    }

    public double getUserScore() {
        return userScore;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public String getImdbID() {
        return imdbID;
    }

}
