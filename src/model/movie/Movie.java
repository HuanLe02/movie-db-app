package model.movie;


import dataio.DataIO;

import java.util.HashMap;
import java.util.List;

/**
 * Object containing information of a movie. This is a hashmap, with fields as keys
 * No constructor. Object is created through GSON parsing
 */
public class Movie extends HashMap<String, Object>{
    /**
     * get list of reviews associated with movie
     */
    public List<Reviews> getReviews() {
        return (new DataIO()).getReviews((String) this.get("imdbID"));
    }
}