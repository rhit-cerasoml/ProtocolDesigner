package protocoldesigner.app.util;

import javax.swing.*;
import java.awt.*;

public class PanelList extends JPanel {
    private final JPanel contents = new JPanel();
    public PanelList(){
        this.setLayout(new BorderLayout());

        contents.setLayout(new BoxLayout(contents, BoxLayout.PAGE_AXIS));

        contents.add(Box.createVerticalGlue());

        JScrollPane pane = new JScrollPane(contents);
        this.add(pane);
    }

    public void addElement(JComponent element){
        contents.add(element, contents.getComponentCount() - 1);
    }

    public void addElement(JComponent element, int index){
        contents.add(element, index);
    }

    public void removeElement(int index){
        contents.remove(index);
    }
}
