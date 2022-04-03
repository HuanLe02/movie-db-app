package view.app;

import model.list.MovieCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CollectionPanel extends LibraryPanel {

    private final MovieCollection collection;
    private final UserInfoPanel userPanel;

    /**
     * constructor
     * @param collection: movie collection for the panel to be based on
     * @param parentFrame: frame that panel belongs to
     */
    CollectionPanel(MovieCollection collection, AppFrame parentFrame) {
        // setup backend data
        super(parentFrame);
        this.collection = collection;
        this.userPanel = new UserInfoPanel(parentFrame);

        // new coreList
        this.coreList = new ArrayList<>();

        // add only movies in collection to coreList
        for (String imdbID : collection.idSet()) {
            this.coreList.add(this.library.getMovie(imdbID));
        }

        // set label texts/fonts
        descLabel.setText("MOVIES IN COLLECTION");
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        nameLabel.setText(collection.getName());

        // set table view
        setTableData();

        // set visibility
        nameLabel.setVisible(true);
        renameButton.setVisible(true);
        renameButton.setEnabled(true);
        removeMovieButton.setVisible(true);
        removeMovieButton.setEnabled(true);
        addMovieButton.setVisible(false);
        addMovieButton.setEnabled(false);

        // add/modify button listeners
        // REMOVE MOVIE FROM COLLECTION
        removeMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ID of selected movie
                String selectedID = selectedID();

                // remove from collection
                if (selectedID != null) {
                    collection.removeMovie(selectedID);
                    JOptionPane.showMessageDialog(parentFrame, "Movie removed from collection");
                    reset();
                }
                else {
                    JOptionPane.showMessageDialog(parentFrame, "Please select a movie from list to remove");
                }

            }
        });

        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String collectionName = JOptionPane.showInputDialog(
                        "Enter new name for collection:",parentFrame.getCollectionName());
                if (collectionName == null) return;

                // if user enters blank string
                if (collectionName.isBlank()) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "Invalid name entered. Please try again.");
                    return;
                }

                // if name already taken
                if (parentFrame.getCollectionButtonMap().contains(collectionName)) {
                    JOptionPane.showMessageDialog(parentFrame,
                            "You already had a collection with the same name. Please enter a new name.");

                }
                else {

                }
                return;
            }

        });
    }

    /**
     * reset
     */
    private void reset() {
        // new coreList
        this.coreList = new ArrayList<>();

        // add only movies in collection to coreList (refetch again)
        for (String imdbID : collection.idSet()) {
            this.coreList.add(this.library.getMovie(imdbID));
        }

        // clear text fields
        clearTextFields();

        // set view in table
        setTableData();
    }

    /**
     * get name of collection that panel is based on
     */
    String getCollectionName() {
        return collection.getName();
    }
}
