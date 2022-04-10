package view.app;

import model.movie.Movie;

import javax.swing.*;
import java.awt.*;

public class MovieInfoDialog extends JDialog {


    // displayed fields

    /**
     * Constructor
     * @param mov: movie to display
     * @param parentFrame: parent frame
     */
    public MovieInfoDialog(Movie mov, AppFrame parentFrame) {
        JPanel movieInfo = new MovieInfoPanel(this, mov, parentFrame);
        JPanel reviews = new ReviewPanel(this);

        JSplitPane homeContainer = new JSplitPane(JSplitPane.VERTICAL_SPLIT, movieInfo, reviews);

        this.add(homeContainer);
        this.setTitle((String) mov.get("Title"));
        this.setMinimumSize(new Dimension(800, 800));
        this.setLocationRelativeTo(null);
        this.pack();
        // popup this new dialog
        this.setModal(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

}
