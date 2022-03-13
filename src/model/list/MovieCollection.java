package model.list;

// self packages
import model.movie.Movie;

public class MovieCollection extends MovieLibrary {
    private String name;

    /**
     * Constructor, creating custom collection with given name
     * @param name: name for collection
     */
    public MovieCollection(String name) {
        super();  // call constructor of parent class
        this.name = name;
    }

    /**
     * get name of collection
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * rename collection
     * @param newName: new name of collection
     */
    public void rename(String newName) {
        this.name = newName;
    }

    /**
     * add movie object M to collection
     * @param newMovie: Movie object to be added
     */
    public void addMovie(Movie newMovie) {
        // search if list not empty
        if (this.getSize() != 0) {
            for (Movie movie: this.movieList) {
                // if movie with imdbID already exists in collection => early return
                if (movie.equals(newMovie)) return;
            }
        }
        // movie not in list
        this.movieList.add(newMovie.clone());
    }

    /**
     * delete movie(s) equal to a movie m in collection
     * @param m: movie to delete
     */
    public void deleteMovie(Movie m) {
        // remove movie(s) if their imdbID is equal to given input
        this.movieList.removeIf(movie -> movie.equals(m));
    }

}
