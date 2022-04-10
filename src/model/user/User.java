package model.user;

// self packages
import model.list.MovieCollection;

// java packages
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Set;

//import static model.list.MovieCollection.*;

public class User<collectionMap> {
    // private fields
    private final String username;
    private String password;
    private final String securityQuestion;
    private final String securityAnswer;
    private final boolean isAdmin;

    // hash map of MovieCollections, key is collection name
    // linked hashmap to preserve order
    private final LinkedHashMap<String, MovieCollection> collectionMap = new LinkedHashMap<>();


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
     * get admin status
     * @return True if user is admin, False otherwise
     */
    public boolean isAdmin() {
        return this.isAdmin;
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
     * @return number of collections User has
     */
    public int numOfCollections() {
        return this.collectionMap.size();
    }

    /**
     * @return Set of names of collections
     */
    public Set<String> getCollectionNames() {
        return this.collectionMap.keySet();
    }

    /**
     * Add new MovieCollection with name to collectionMap
     * @param name: name of collection to be added
     * @precondition user has no collection with given name
     */
    public void addCollection(String name) {
        // add collection with name to list
        this.collectionMap.put(name, new MovieCollection(name));
    }

    /**
     * Remove collection with name from collectionMap
     * @param name: name of collection to remove
     * @precondition user has collection with name
     */
    public void removeCollection(String name) {
        this.collectionMap.remove(name);
    }
    /**
     * rename a collection key
     * @precondiion user had collection with key oldName
     */
    public void renamecollectionKey(String oldName, String newName) {
        // remove entry of oldName key, then add it back with newName key
        this.collectionMap.put(newName, this.collectionMap.remove(oldName));
    }

    /**
     * get collection with name
     * @return MovieCollection with name
     * @precondition user has collection with name
     */
    public MovieCollection getCollection(String name) {
        return this.collectionMap.get(name);
    }

}
