package protocoldesigner.app.ui.welcome;

import javax.swing.*;
import java.awt.*;

public class WelcomeProjectListCellRenderer extends DefaultListCellRenderer {

    public Component getListCellRendererComponent(
            JList<?> list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus)
    {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        label.setPreferredSize(new Dimension(600, 60));
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20)); // Set font size to 20
        return label;
    }
}
