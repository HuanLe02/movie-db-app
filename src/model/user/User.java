package model.user;

// self packages
import model.list.MovieCollection;

// java packages
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class User {
    // private fields
    private final String username;
    private String password;
    private final String securityQuestion;
    private final String securityAnswer;
    private final boolean isAdmin;
    private final List<MovieCollection> listOfCollections;

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
     * Constructor
     * @param username: username
     * @param password: password
     * @param isAdmin: is user admin?
     */
    public User(String username, String password, String securityQuestion, String securityAnswer,
                boolean isAdmin) {
        // store username
        this.username = username;
        // store hashed password
        this.password = getMD5(password);
        // store security question + answers
        this.securityQuestion = securityQuestion;
        this.securityAnswer = getMD5(securityAnswer.toLowerCase());
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
     * get security question
     * @return security question
     */
    public String getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * set new password
     * @param newPassword: new password
     */
    public void setPassword(String newPassword) {
        // store hashed newPassword
        this.password = getMD5(newPassword);
    }

    /**
     * Verify input password against stored user password
     * @param input: String
     * @return True if password matches, False otherwise
     */
    public boolean verifyPassword(String input) {
        return getMD5(input).equals(this.password);
    }

    /**
     * Verify input password against stored user password
     * @param input: String
     * @return True if security answer matches, False otherwise
     */
    public boolean verifySecurityAnswer(String input) {
        return getMD5(input.toLowerCase()).equals(this.securityAnswer);
    }

    /**
     * Verify if user has collection with name
     * @return True if listOfCollections has a collection with name, False otherwise
     */
    public boolean isInCollectionList(String name) {
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
    public void addCollection(MovieCollection mc) throws RuntimeException {
        // stop if collection with name already in list
        if (isInCollectionList(mc.getName())) {
            throw new RuntimeException("Collection with name already in list");
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
        // add clones of each MovieCollection to copy List to return
        List<MovieCollection> copy = new LinkedList<>();
        for (MovieCollection c: this.listOfCollections) {
            copy.add(c.clone());
        }
        return copy;
    }
}
