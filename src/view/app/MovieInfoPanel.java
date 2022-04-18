package view.app;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovieInfoPanel extends JPanel implements ActionListener {

    private final MovieInfoDialog parentDialog;

    private final JButton backButton = new JButton("<< Go Back");
    private final JLabel titleLabel;
    private final JLabel plotLabel;
//    private final JLabel genreLabel; Not done yet
    private final JLabel ratingLabel;

    MovieInfoPanel(MovieInfoDialog parentDialog) {
        this.parentDialog = parentDialog;
        this.titleLabel = new JLabel((String) parentDialog.movie.get("Title"));
        this.plotLabel = new JLabel((String) parentDialog.movie.get("Plot"));
//        this.genreLabel = new JLabel((String) parentDialog.movie.get("Genre")); Not done yet
        this.ratingLabel = new JLabel((String) parentDialog.movie.get("Rated"));
        this.setLayout(new GridLayout(4, 2));

        addElements();

        backButton.addActionListener(this);
    }

    private void addElements() {
        this.add(backButton);
        this.add(titleLabel);
        this.add(plotLabel);
//        this.add(genreLabel); Not done yet
        this.add(ratingLabel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            parentDialog.dispose();
        }

    }
}
