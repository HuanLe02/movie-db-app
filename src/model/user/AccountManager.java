package model.user;

// self packages
import dataio.DataIO;

public class AccountManager {
    private DataIO d;
    private User currentUser;

    /**
     * Constructor
     *
     */
    public AccountManager(DataIO d) {
        this.currentUser = null;
        this.d = d;
    }

    /**
     * sign user in
     * @param username: username
     * @param password: password
     */
    public void login(String username, String password) {
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
    }

    /**
     * Create new user on file
     * @param username: username string
     * @param password: password string
     * @param isAdmin: true if user is admin
     */
    public void createAccount(String username, String password, String securityQuestion, String securityAnswer,
                              Boolean isAdmin) {
        // username already exists
        if (d.userExists(username)) {
            throw new RuntimeException("Username already taken.");
        }
        // create new user and save
        User newUser = new User(username, password, securityQuestion, securityAnswer, isAdmin);
        d.saveUser(newUser);
    }

    /**
     * Force login, without password. ONLY used when resetting password
     * @param username: String
     */
    public void forceLogin(String username) {
        try {
            // try getting user from file
            this.currentUser = d.getUser(username);
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
    }

    /**
     * get current user
     * @return return User obj or null
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
