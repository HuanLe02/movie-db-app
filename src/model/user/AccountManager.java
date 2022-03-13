package model.user;

// self packages
import dataio.DataIO;

public class AccountManager {
    private User currentUser;

    /**
     * Constructor
     */
    public AccountManager() {
        this.currentUser = null;
    }

    /**
     * sign user in
     * @param d: DataIO object
     * @param username: username
     * @param password: password
     */
    public void login(DataIO d, String username, String password) {
        // username not found
        if (!d.userExists(username)) {
            throw new RuntimeException("Your username/password is incorrect");
        }
        User tempUser = d.getUser(username);
        // if password does not match
        if (!tempUser.verifyPassword(password)) {
            throw new RuntimeException("Your username/password is incorrect");
        }
        // if both are correct
        this.currentUser = tempUser;
    }

    /**
     * Create new user on file
     * @param d: DataIO object
     * @param username: username string
     * @param password: password string
     * @param isAdmin: true if user is admin
     */
    public void createAccount(DataIO d, String username, String password, Boolean isAdmin) {
        // username already exists
        if (d.userExists(username)) {
            throw new RuntimeException("Username already taken.");
        }
        // create new user and save
        User newUser = new User(username, password, isAdmin);
        d.saveUser(newUser);
    }

    /**
     * get current user
     * @return return User obj or null
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
