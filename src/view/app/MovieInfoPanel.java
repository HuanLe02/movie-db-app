package view.app;

// java packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MovieInfoPanel extends JPanel implements ActionListener {

    private final MovieInfoDialog parentDialog;

    private final JButton backButton = new JButton("<< Go Back");

    MovieInfoPanel(MovieInfoDialog parentDialog) {
        this.parentDialog = parentDialog;
        this.setLayout(new GridLayout(4, 2));

        addElements();
    }

    private void addElements() {
        this.add(backButton);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
