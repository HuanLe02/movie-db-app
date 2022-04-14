package model.movie;

public class Review {
    private double userScore;
    private String userName;
    private String userComment;
    private String imdbID;

    public Review(String imdbID, String userName, double userScore, String userComment) {
        this.userScore = userScore;
        this.userName = userName;
        this.imdbID = imdbID;
        this.userComment = userComment;
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
