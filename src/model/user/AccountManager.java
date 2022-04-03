package model.user;

// self packages
import dataio.DataIO;
import model.list.MovieLibrary;

public class AccountManager {
    private final DataIO d;
    private final MovieLibrary library;
    private User currentUser;

    /**
     * Constructor
     *
     */
    public AccountManager(DataIO d) {
        this.currentUser = null;
        this.d = d;
        this.library = new MovieLibrary(d);
    }

    /**
     * sign user in
     * @param username: username
     * @param password: password
     */
    public void login(String username, String password) throws RuntimeException {
        User tempUser;
        try {
            // try to get user from file
            tempUser = d.getUser(username);
        }
        catch (RuntimeException noUser) {
            // no user exists
            throw new RuntimeException("Your username/password is incorrect");
        }
        // if password does not match
        if (!tempUser.verifyPassword(password)) {
            throw new RuntimeException("Your username/password is incorrect");
        }
        // if both are correct
        this.currentUser = tempUser;
        System.out.println("Logged in");
    }

    /**
     * Create new user on file
     * @param username: username string
     * @param password: password string
     * @param isAdmin: true if user is admin
     */
    public void createAccount(String username, String password, String securityQuestion, String securityAnswer,
                              Boolean isAdmin) throws RuntimeException {
        // username already exists
        if (d.userExists(username)) {
            throw new RuntimeException("Username already taken.");
        }
        // create new user and save
        User newUser = new User(username, password, securityQuestion, securityAnswer, isAdmin);
        d.saveUser(newUser);
        System.out.println("Account Created");
    }

    /**
     * Force login, without password. ONLY used when forget password
     * @param username: String
     */
    public void forceLogin(String username) {
        try {
            // try getting user from file
            this.currentUser = d.getUser(username);
            System.out.println("Forced Login");
        }
        catch (RuntimeException noUser) {
            throw new RuntimeException("No user with that username");
        }
    }

    /**
     * logout
     */
    public void logout() {
        this.currentUser = null;
        System.out.println("Logged out");
    }

    /**
     * get current user
     * @return return User obj (a reference)
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @return DataIO object associated with account manager
     */
    public DataIO getDataIO() {
        return d;
    }

    /**
     * @return MovieLibrary of app
     */
    public MovieLibrary getLibrary() {
        return library;
    }

    /**
     * check if user is logged in
     * @return True if there's a user logged in, False otherwise
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
