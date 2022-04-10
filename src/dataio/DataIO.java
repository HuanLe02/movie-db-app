package dataio;

// self packages
import model.movie.Movie;
import model.movie.Reviews;
import model.user.*;

// java packages
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

// 3rd party
import com.google.gson.*;

public class DataIO {
    // private fields
    final private String configFilePath = Paths.get(System.getProperty("user.dir"), "data.config").toString();
    final private Gson gson = new Gson();
    private String dataDirPath;

    /**
     * private helper function to read all content of file
     * @param filePathStr: string of path to file
     * @return string content of file
     */
    private String readAll(String filePathStr) {
        // open file to read
        FileReader infile;
        try {
            infile = new FileReader(filePathStr);
        }
        catch (FileNotFoundException exception) {
            exception.printStackTrace();
            throw new RuntimeException("File not found");
        }

        // attach scanner and read all, delimiter = '\\A'
        Scanner fileScanner = new Scanner(infile);
        String result = fileScanner.useDelimiter("\\A").next();

        // close files
        fileScanner.close();
        try {
            infile.close();
        } catch (IOException e) {
            System.err.println("Error occurred when closing.");
            e.printStackTrace();
        }

        // return
        return result;
    }

    /**
     * private helper function to overwrite content of file
     * @param filePathStr: path of file
     * @param newContent: new content to be written
     */
    private void overwriteAll(String filePathStr, String newContent) {
        FileWriter fwriter;
        try {
            fwriter = new FileWriter(filePathStr);
            fwriter.write(newContent);
            fwriter.close();
        } catch (java.io.IOException e) {
            System.err.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Constructor for DataIO class
     * Populate dataDirectory field, reading straight from config file
     * if no config file, create a default one
     */
    public DataIO() {
        // read from config file
        FileReader infile;
        try {
            infile = new FileReader(this.configFilePath);
        }
        catch (FileNotFoundException exception) {
            // if not found => create new file with default data path
            System.out.println("No config file at default path. Default config created.");
            // set default data path
            String defaultDataDir = Paths.get(System.getProperty("user.dir"), "data").toString();
            this.dataDirPath = defaultDataDir;
            List<String> line = List.of(defaultDataDir);
            try {
                Files.write(Paths.get(this.configFilePath), line, StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // return constructor early after creating and writing file
            return;
        }

        // IF CONFIG FILE ALREADY EXISTS
        // attach scanner to file
        Scanner fileScanner = new Scanner(infile);
        if (fileScanner.hasNextLine()) {
            // just to make sure that data field is always absolute path
            this.dataDirPath = Paths.get(fileScanner.nextLine()).toAbsolutePath().toString();
        }

        // close
        fileScanner.close();
        try {
            infile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // CREATE REVIEWS.JSON FILE
        Path reviewFilePath = Paths.get(this.dataDirPath, "reviews.json");
        // if path does not exist create one
        if (!Files.exists(reviewFilePath)) {
            try {
                Files.createFile(reviewFilePath);
                overwriteAll(reviewFilePath.toString(), "[]");
            } catch (IOException e) {
                throw new RuntimeException("No data folder");
            }
        }
    }

    /**
     * get path of configuration file
     * @return absolute path to configuration file
     */
    public String getConfigFilePath() {
        return configFilePath;
    }

    /**
     * get path of data directory
     * @return path to data directory file (absolute, or relative to config file)
     */
    public String getDataDirPath() {
        return this.dataDirPath;
    }

    /**
     * set path of data directory
     * after calling this once, config file will always contain absolute path
     * can only be called by admin user
     * AFTER CALLING, PROGRAM MUST BE CLOSED
     * @param newDataDirPath : new path to set (MUST BE ABSOLUTE)
     */
    public void setDataDirPath(String newDataDirPath) {
        // convert to Path
        Path currMovielibPath = Paths.get(this.dataDirPath, "movielib.json");
        Path currReviewsPath = Paths.get(this.dataDirPath, "reviews.json");
        Path currUsersPath = Paths.get(this.dataDirPath, "users");
        Path newMovielibPath = Paths.get(newDataDirPath, "movielib.json");
        Path newReviewsPath = Paths.get(newDataDirPath, "reviews.json");
        Path newUsersPath = Paths.get(newDataDirPath, "users");

        // copy movielib.json
        try {
            Files.copy(currMovielibPath, newMovielibPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("No movie library file at current path");
        }

        // copy reviews.json
        try {
            Files.copy(currReviewsPath, newReviewsPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.err.println("No reviews.json file at current path");
        }

        // if there's no user folder at newPath, create one
        if (!Files.exists(newUsersPath)) {
            try {
                Files.createDirectory(newUsersPath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Can't create folder");
            }
        }

        // copy over all users
        try {
            Files.walk(currUsersPath)
                    .forEach(path -> {
                                try {
                                    if (!path.equals(currUsersPath)) { // skip directory when walking
                                        // copy file to new users directory
                                        Files.copy(path, Paths.get(newDataDirPath, "users", path.getFileName().toString()),
                                                StandardCopyOption.REPLACE_EXISTING);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("Can't copy file");
                                }
                            }
                    );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("No users folder at current path");
        }

        // overwrite config file
        overwriteAll(configFilePath, newDataDirPath);

        // close program
        System.exit(0);
    }
    /**
     * get list of movies from movielib.json file
     * @return list of Movie objects
     */
    public List<Movie> getMovieLibraryList() {
        String movielib_path = Paths.get(this.dataDirPath, "movielib.json").toString();
        String jsonstr = this.readAll(movielib_path);

        // return List<Movie>
        return Arrays.asList(gson.fromJson(jsonstr, Movie[].class));
    }

    /**
     * check if user with username exists
     * @param username: username
     * @return true if user exists
     */
    public Boolean userExists(String username) {
        String fname = username + ".json";
        return Files.exists(Paths.get(dataDirPath, "users", fname));
    }

    /**
     * get User with username
     * @param username: username
     * @return User object
     */
    public User getUser(String username) throws RuntimeException {
        if (!userExists(username)) {
            throw new RuntimeException("User not found");
        }
        // read string from user file
        String fname = username + ".json";
        String fpath = Paths.get(dataDirPath, "users", fname).toString();
        String jsonstr = this.readAll(fpath);

        // parse obj from json string
        User u = gson.fromJson(jsonstr, User.class);
        return u;
    }

    /**
     * get User object to json file
     * @param user: User
     */
    public void saveUser(User user) {
        String fname = user.getUsername() + ".json";
        Path dirpath = Paths.get(dataDirPath, "users");
        Path fpath = Paths.get(dataDirPath, "users", fname);

//        System.out.println(dirpath.toString());
//        System.out.println(fpath.toString());

        // if there's no user folder, create one
        if (!Files.exists(dirpath)) {
            try { Files.createDirectory(dirpath); }
            catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Read Error");
            }
        }

        // if user does not exist, create user file
        if (!userExists(user.getUsername())) {
            try {
                Files.createFile(fpath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Read Error");
            }
        }

        // overwrite file with new json string
        String jsonstr = gson.toJson(user);
        this.overwriteAll(fpath.toString(), jsonstr);

//        System.out.printf("User data saved at %s\n", fpath.toString());
    }

    /**
     * get List<Review> associated with a movie
     * @param imdbID: imdbID of movie
     * @return List<Reviews>
     */
    public List<Reviews> getReviews(String imdbID) {
        String fpath = Paths.get(dataDirPath, "reviews.json").toString();

        // read all reviews from reviews.json
        String jsonStr = readAll(fpath);
        List<Reviews> result = Arrays.asList(gson.fromJson(jsonStr, Reviews[].class));

        // filter the list
        return result.stream().filter(reviews -> reviews.getImdbID().equals(imdbID))
                .collect(Collectors.toList());
    }

    /**
     * add new Review to file
     */
    public void addReview(Reviews rev) {
        String fpath = Paths.get(dataDirPath, "reviews.json").toString();

        // read all reviews from reviews.json
        String jsonStr = readAll(fpath);
        ArrayList<Reviews> result = new ArrayList<>(Arrays.asList(gson.fromJson(jsonStr, Reviews[].class)));

        // add new review
        result.add(rev);

        // overwrite file
        overwriteAll(fpath, gson.toJson(result));
    }

}
