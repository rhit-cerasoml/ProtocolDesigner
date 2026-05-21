package protocoldesigner.app.ui.editor;

import protocoldesigner.app.model.ApplicationState;
import protocoldesigner.app.ui.UIManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditorView extends JFrame {

    private final UIManager uiManager;
    private final ApplicationState appState;
    public EditorView(UIManager uiManager, ApplicationState appState){
        this.uiManager = uiManager;
        this.appState = appState;

        setSize(1200, 600);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        buildMenuBar();
    }
    private JMenuBar menuBar;
    private void buildMenuBar(){
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");

        JMenuItem closeProject = new JMenuItem("Close Project");
        closeProject.addActionListener((e) -> {
            appState.closeProject();
            uiManager.openWelcomeMenu();
            dispose();
        });
        fileMenu.add(closeProject);


        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void onClose(){
        appState.storeState();
    }
}
