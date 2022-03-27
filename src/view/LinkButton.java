package view;

import javax.swing.*;
import java.awt.*;

/**
 * Special button for links (blue underlined text, translucent background)
 */
public class LinkButton extends JButton {
    public LinkButton(String text) {
        super(text);
        this.setForeground(Color.BLUE);
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
    }
}
