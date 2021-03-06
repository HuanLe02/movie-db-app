package view.app;

import model.movie.Movie;

import javax.swing.*;
import java.awt.*;

public class MovieInfoDialog extends JDialog {

    Movie movie = new Movie();
    // displayed fields

    /**
     * Constructor
     * @param mov: movie to display
     */
    public MovieInfoDialog(Movie mov, AppFrame parentFrame) {
        movie = mov;
        JPanel movieInfo = new MovieInfoPanel(this);
        JPanel reviews = new ReviewPanel(this, parentFrame);

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
