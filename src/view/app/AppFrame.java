package view.app;

// self packages
import model.list.MovieLibrary;
import model.user.AccountManager;
import model.user.User;
import view.auth.WelcomeFrame;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

public class AppFrame extends JFrame implements ActionListener {

    // universal account manager
    private final AccountManager accManager;

    // home container, a JSplitPane
    private final JSplitPane homeContainer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    // left sidebar
    private final JPanel sidebar = new JPanel();

    // elements
    private final UserInfoPanel userPanel;
    private final LibraryPanel libraryPanel;
    private final JButton userButton = new JButton("My User");
    private final JButton libraryButton = new JButton("Movie Library");
    private final JButton addButton = new JButton("Add Collection...");
    private final JButton removeButton = new JButton("Remove Collection...");
    private String collectionName;


    // hash map of collection buttons, name as key
    // linked hashmap to preserve order
    private final LinkedHashMap<String, JButton> collectionButtonMap = new LinkedHashMap<>();

    /**
     * constructor
     * @param accManager: account manager object
     */
    public AppFrame(AccountManager accManager) {
        // initialize fields
        this.accManager = accManager;
        this.userPanel = new UserInfoPanel(this);
        this.libraryPanel = new LibraryPanel(this);

        // initialize collectionButtonMap with user's collection names
        for (String collectionName : accManager.getCurrentUser().getCollectionNames()) {
            JButton newButton = new JButton(collectionName);
            newButton.setName(collectionName);     // set component name
            newButton.addActionListener(this);   // add action listener
            collectionButtonMap.put(collectionName, newButton);  // put into hashmap
        }

        // set sidebar layout
        setSidebarLayout();

        // add elements + listeners
        addElements();
        addActionEvent();

        // add sub-panels
        homeContainer.setLeftComponent(this.sidebar);
        homeContainer.setRightComponent(this.userPanel);
        homeContainer.setEnabled(false);

        // frame attributes
        this.add(homeContainer);
        this.setTitle("Application");
        this.setResizable(true);
        this.setMinimumSize(new Dimension(1268,750));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * transition back to Welcome Page
     */
    void backToLogin() {
        // check that user is logged out
        assert !accManager.isLoggedIn();

        // new Welcome Frame
        WelcomeFrame welcomeFrame = new WelcomeFrame(accManager);
        welcomeFrame.setVisible(true);

        // dispose this frame
        this.dispose();
    }

    /**
     * set sidebar layout
     */
    private void setSidebarLayout() {
        int numOfCells = 5 + collectionButtonMap.size();
        sidebar.setLayout(new GridLayout(Math.max(numOfCells, 15),1,2,2));
    }

    /**
     * add elements
     */
    private void addElements() {
        sidebar.add(userButton);
        sidebar.add(libraryButton);
        sidebar.add(new JLabel("MY COLLECTIONS"));

        // add collection buttons
        for (JButton button : collectionButtonMap.values()) {
            sidebar.add(button);
        }

        sidebar.add(addButton);
        sidebar.add(removeButton);
    }

    /**
     * add event listeners
     */
    private void addActionEvent() {
        userButton.addActionListener(this);
        libraryButton.addActionListener(this);
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
    }

    /**
     * @return current AccountManager
     */
    AccountManager currManager() {
        return this.accManager;
    }

    /**
     * @return current User
     */
    User currUser() {
        return this.accManager.getCurrentUser();
    }

    /**
     * @return current Library
     */
    MovieLibrary currLibrary() {
        return this.accManager.getLibrary();
    }

    /**
     * add collection to sidebar
     * @param collectionName: String
     */
    void addCollectionToSidebar(String collectionName) {
        // temporarily bottom button
        sidebar.remove(removeButton);
        sidebar.remove(addButton);

        // add new button to map
        JButton newButton = new JButton(collectionName);
        newButton.setName(collectionName);                // set name
        newButton.addActionListener(this);    // set action listener
        collectionButtonMap.put(collectionName, newButton);

        // add button to sidebar
        sidebar.add(newButton);

        // re-add addButton
        sidebar.add(addButton);
        sidebar.add(removeButton);

        // reset sidebar layout
        setSidebarLayout();

        // repack frame
        this.pack();
    }


    /**
     * remove collection from sidebar
     * @param collectionName: String
     */
    void removeCollectionFromSidebar(String collectionName) {
        // remove button from sidebar
        sidebar.remove(collectionButtonMap.get(collectionName));
        // remove from button map
        collectionButtonMap.remove(collectionName);
        // reset sidebar layout
        setSidebarLayout();
        // repack frame
        this.pack();
    }

    /**
     * switch content panel
     */
    void switchContent(JPanel panel) {
        homeContainer.remove(2);    // remove right panel
        homeContainer.setRightComponent(panel);

        // resize based on panel
        if (panel == userPanel) {
            this.setExtendedState(JFrame.NORMAL);
            this.pack();
        }
        if (panel == libraryPanel || panel.getClass().getTypeName().equals("view.app.CollectionPanel")) {
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    /**
     * select collection
     */
    String selectCollection() {
        // if no collection
        if (collectionButtonMap.size() == 0) {
            JOptionPane.showMessageDialog(this, "No collection to select");
            return null;
        }
        // prompt user to select collection to remove
        String[] collectionList = collectionButtonMap.keySet().toArray(new String[0]);
        String selectedName = (String) JOptionPane.showInputDialog(null,
                "Select a collection below", "Select Collection",
                JOptionPane.QUESTION_MESSAGE, null,
                collectionList, collectionList[0]);
        return selectedName;
    }

    /**
     * Action functions
     * @param e: ActionEvent
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // user button => set right component to userPanel
        if (e.getSource() == userButton) {
            switchContent(userPanel);
            return;
        }

        // library button => switch right component to libraryPanel
        if (e.getSource() == libraryButton) {
            switchContent(libraryPanel);
            return;
        }

        // add collection
        if (e.getSource() == addButton) {
            // generate default name for collection
            int count = 1;
            String defaultName = "Collection " + Integer.toString(count);
            // loop until collectionButtonMap does NOT contain defaultName
            while (collectionButtonMap.containsKey(defaultName)) {
                count++;
                defaultName = "Collection " + Integer.toString(count);
            }

            collectionName = JOptionPane.showInputDialog(this,
                    "Enter name for new collection:",
                    defaultName);

            // if user hits cancel
            if (collectionName == null) return;

            // if user enters blank string
            if (collectionName.isBlank()) {
                JOptionPane.showMessageDialog(this,
                        "Invalid name entered. Please try again.");
                return;
            }

            // if name already taken
            if (collectionButtonMap.containsKey(collectionName)) {
                JOptionPane.showMessageDialog(this,
                        "You already had a collection with the same name. Please enter a new name.");
            }
            else {
                currUser().addCollection(collectionName);    // add collection to user
                addCollectionToSidebar(collectionName);      // add button to sidebar
                userPanel.updateInfo();                      // update user panel
            }
            return;
        }

        // remove collection
        if (e.getSource() == removeButton) {
            // select a collection
            String selectedName = selectCollection();

            // if user hits cancel
            if (selectedName == null) return;

            // remove that collection
            currUser().removeCollection(selectedName);   // remove from current user
            removeCollectionFromSidebar(selectedName);   // remove button from sidebar
            userPanel.updateInfo();                      // update user info panel

            // current right component
            Component rightComponent = homeContainer.getRightComponent();
            // 2 condition: rightComponent is a CollectionPanel AND it has the same name as removed collection
            // => switch to library panel
            // => else, stay
            if (rightComponent.getClass().getTypeName().equals("view.app.CollectionPanel")
                    && ((CollectionPanel) rightComponent).getCollectionName().equals(selectedName)) {
                switchContent(libraryPanel);
            }
            else {
                return;
            }
        }

        // if user selects one of the collection buttons
        String componentName = ((JButton) e.getSource()).getName();
        if (collectionButtonMap.containsKey(componentName)) {
            // current right component
            Component rightComponent = homeContainer.getRightComponent();

            // 2 condition: rightComponent is a CollectionPanel AND it has the same name as button
            // => stay at current panel
            // => else, new panel
            if (rightComponent.getClass().getTypeName().equals("view.app.CollectionPanel")
            && ((CollectionPanel) rightComponent).getCollectionName().equals(componentName)) {
                return;
            }
            else {
                // creates new CollectionPanel
                switchContent(new CollectionPanel(currUser().getCollection(componentName), this));
            }
        }

    }
    String getCollectionName() { return collectionName; }
    Set<String> getCollectionButtonSet () {return collectionButtonMap.keySet();}
}
