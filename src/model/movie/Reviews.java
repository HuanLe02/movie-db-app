package model.movie;

public class Reviews {
    private double userScore;
    private String userName;
    private String userComment;
    private String imdbID;

    Reviews(String imdbID, String userName, double userScore, String userComment) {

    }

    double getUserScore() {
        return userScore;
    }

    String getUserName() {
        return userName;
    }

    String getUserComment() {
        return userComment;
    }

    String getImdbID() {
        return imdbID;
    }

}
