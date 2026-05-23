package protocoldesigner.app.util;

import javax.swing.*;
import java.awt.*;

public class PanelList extends JPanel {
    private final JPanel contents = new JPanel();
    private final JScrollPane pane = new JScrollPane(contents);
    public PanelList(){
        this.setLayout(new BorderLayout());

        contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));

        contents.add(Box.createVerticalGlue());

        this.add(pane, BorderLayout.CENTER);
    }

    public void setScrollSensitivity(int sensitivity){
        pane.getVerticalScrollBar().setUnitIncrement(sensitivity);
    }

    public void addElement(JComponent element){
        contents.add(element, contents.getComponentCount() - 1);
        this.validate();
        this.repaint();
    }

    public void addElement(JComponent element, int index){
        contents.add(element, index);
        this.validate();
        this.repaint();
    }

    public void removeElement(int index){
        contents.remove(index);
        this.validate();
        this.repaint();
    }


}
