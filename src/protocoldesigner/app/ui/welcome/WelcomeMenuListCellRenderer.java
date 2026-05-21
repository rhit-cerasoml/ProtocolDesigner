package protocoldesigner.app.ui.welcome;

import javax.swing.*;
import java.awt.*;

public class WelcomeMenuListCellRenderer extends DefaultListCellRenderer {
    private final int width;
    public WelcomeMenuListCellRenderer(int width){
        this.width = width;
    }

    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setPreferredSize(new Dimension(width, 60));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); // Set font size to 20
        return label;
    }
}
