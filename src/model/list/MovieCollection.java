package model.list;

// self packages
import model.movie.Movie;

// java packages
import java.util.LinkedList;
import java.util.List;

public class MovieCollection extends MovieLibrary implements Cloneable {
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
     * Private constructor, only used for cloning
     * @param mlist: List<Movie>
     */
    private MovieCollection(List<Movie> mlist) {
        this.movieList = mlist;
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
     * add a movie object M to collection
     * @param newMovie: Movie object to be added
     */
    public void addMovie(Movie newMovie) {
        // search list
        for (Movie movie: this.movieList) {
            // if movie with imdbID already exists in collection => early return
            if (movie.equals(newMovie)) {
                System.out.println("Movie already in list");
                return;
            }
        }
        // movie not in list
        this.movieList.add(newMovie);
    }

    /**
     * remove movie(s) equal to a movie m in collection
     * @param m: movie to remove
     */
    public void removeMovie(Movie m) {
        // remove movie(s) if their imdbID is equal to given input
        this.movieList.removeIf(movie -> movie.equals(m));
    }


    /**
     * Clone MovieCollection object
     * @return A MovieCollection object that is a clone
     */
    public MovieCollection clone() {
        // create a copy list, add clones of movies one-by-one
        List<Movie> listcopy = new LinkedList<>();
        for (Movie m: this.movieList) {
            listcopy.add(m);
        }
        // new copy object to return
        MovieCollection copy = new MovieCollection(listcopy);
        return copy;
    }

}
