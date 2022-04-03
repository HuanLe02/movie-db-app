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

    /**
     * constructor
     * @param collection: movie collection for the panel to be based on
     * @param parentFrame: frame that panel belongs to
     */
    CollectionPanel(MovieCollection collection, AppFrame parentFrame) {
        // setup backend data
        super(parentFrame);
        this.collection = collection;

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
