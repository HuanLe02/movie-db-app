package dataio;

// self packages
import model.movie.Movie;
import model.user.*;

// java packages
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

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
            return;
        }

        // attach scanner to file
        Scanner fileScanner = new Scanner(infile);
        if (fileScanner.hasNext()) {
            // just to make sure that data field is always absolute path
            this.dataDirPath = Paths.get(fileScanner.next()).toAbsolutePath().toString();
        }

        // close
        fileScanner.close();
        try {
            infile.close();
        }
        catch (IOException e) {
            e.printStackTrace();
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
     * @param newDataDirPath : new path to set (absolute, or relative to config file)
     */
    public void setDataDirPath(String newDataDirPath) {
        // update field with absolute path
        this.dataDirPath = Paths.get(newDataDirPath).toAbsolutePath().toString();

        // update record on file
        this.overwriteAll(this.configFilePath, this.dataDirPath);
    }

    /**
     * get list of movies from movielib.json file
     * @return array list of Movie objects
     */
    public List<Movie> getMovieLibraryList() {
        String movielib_path = Paths.get(this.dataDirPath, "movielib.json").toString();
        String jsonstr = this.readAll(movielib_path);

        // add to result arraylist
        List<Movie> result = new ArrayList<>(); // LINKED OR ARRAY???
        Collections.addAll(result, gson.fromJson(jsonstr, Movie[].class));

        // return
        return result;
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
    public User getUser(String username) {
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
     * @param user
     */
    public void saveUser(User user) {
        String fname = user.getUsername() + ".json";
        Path dirpath = Paths.get(dataDirPath, "users");
        Path fpath = Paths.get(dataDirPath, "users", fname);

        // if there's no user folder, create one
        if (!Files.exists(dirpath)) {
            try { Files.createDirectory(dirpath); }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        // if user does not exist, create user file
        if (!userExists(user.getUsername())) {
            try {
                Files.createFile(fpath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // overwrite file with new json string
        String jsonstr = gson.toJson(user);
        this.overwriteAll(fpath.toString(), jsonstr);
    }


}
