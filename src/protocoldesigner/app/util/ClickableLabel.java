package protocoldesigner.app.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public abstract class ClickableLabel extends JLabel {
    public ClickableLabel(String s){
        super(s);
        this.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        this.addMouseListener(new NullMouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                onClick();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Color.orange);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(null);
            }
        });
    }

    protected abstract void onClick();
}
