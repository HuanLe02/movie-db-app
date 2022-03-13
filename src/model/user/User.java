package model.user;

// self packages
import model.list.MovieCollection;

// java packages
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class User {
    // private fields
    private String username;
    private String password;
//    private String securityQuestion;
//    private String securityAnswer;
    private Boolean isAdmin;
    private List<MovieCollection> listOfCollections;

    /**
     * Generate an MD5 hash string given input
     * @param input: input string
     */
    private String getMD5(String input) {
        try {
            // Message dgiest instance, using MD5 algorithm
            MessageDigest msgDst = MessageDigest.getInstance("MD5");
            byte[] msgArr = msgDst.digest(input.getBytes());
            // getting BigInteger representation from byte array msgArr
            BigInteger bi = new BigInteger(1, msgArr);

            // Converting into hex value
            String hshtxt = bi.toString(16);

            // pad zeros to get length-32 string
            while (hshtxt.length() < 32)
            {
                hshtxt = "0" + hshtxt;
            }
            return hshtxt;
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Default constructor
     * @param username: username
     * @param password: password
     */
    public User(String username, String password) {
        // store username
        this.username = username;
        // store hashed password
        this.password = getMD5(password);
        // set type
        this.isAdmin = false;
        // empty collection list
        this.listOfCollections = new LinkedList<>();
    }

    /**
     * Constructor, taking optional isAdmin param
     * @param username: username
     * @param password: password
     * @param isAdmin: is user admin?
     */
    public User(String username, String password, Boolean isAdmin) {
        // store username
        this.username = username;
        // store hashed password
        this.password = getMD5(password);
        // set type
        this.isAdmin = isAdmin;
        // empty collection list
        this.listOfCollections = new LinkedList<>();
    }

    /**
     * get username
     * @return username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * set new username
     * @param newUsername: new username
     */
    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    /**
     * Verify input password against stored user password
     * @param input
     * @return True if password matches, False otherwise
     */
    public Boolean verifyPassword(String input) {
        return getMD5(input).equals(this.password);
    }

    /**
     * Verify if user has collection with name
     * @return True if listOfCollections has a collection with name, False otherwise
     */
    public Boolean isInCollectionList(String name) {
        for (MovieCollection collection: this.listOfCollections) {
            if (collection.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Add MovieCollection object by reference to listOfCollections
     * Changes in mc => changes of state in User
     * @param mc: MovieCollection object
     */
    public void addCollection(MovieCollection mc) {
        // stop if collection with name already in list
        if (isInCollectionList(mc.getName())) {
            System.out.println("Collection with name already in list");
            return;
        }
        this.listOfCollections.add(mc);
    }

    /**
     * Remove collection with name
     * @param name: name of collection to remove
     */
    public void removeCollection(String name) {
        this.listOfCollections.removeIf(movieCollection -> movieCollection.getName().equals(name));
    }

    /**
     * get list of collections owned by users, at the moment of function call
     * Not subscriptable to future changes
     * @return List of Movie Collection
     */
    public List<MovieCollection> getListOfCollections() {
        // add clones of each MovieCollection to new List
        List<MovieCollection> copy = new LinkedList<>();
        for (MovieCollection c: this.listOfCollections) {
            copy.add(c.clone());
        }
        return copy;
    }
}
