package protocoldesigner.app.ui.welcome;

import protocoldesigner.app.model.ApplicationState;
import protocoldesigner.app.ui.UIManager;
import protocoldesigner.app.util.ClickableLabel;
import protocoldesigner.app.util.NullMouseListener;
import protocoldesigner.app.util.PanelList;
import protocoldesigner.app.util.RecursiveFileFilter;


import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class WelcomeView extends JFrame {
    private final UIManager uiManager;
    private final ApplicationState appState;
    public WelcomeView(UIManager uiManager, ApplicationState appState) {
        this.appState = appState;
        this.uiManager = uiManager;

        setTitle("Protocol Designer - Project Selection");
        setSize(900, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        buildUI();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });
    }

    private void buildUI() {
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
        buildRightPanel();

        // Left Panel
        this.add(buildLeftPanel());

        this.add(new JSeparator(SwingConstants.VERTICAL));

        this.add(rightPanel);
    }

    private String[] menus = new String[]{"Projects", "About"};
    private CardLayout rightPanelLayout = new CardLayout();
    private JPanel rightPanel = new JPanel();
    private void buildRightPanel() {
       rightPanel.setLayout(rightPanelLayout);

       rightPanel.add(buildProjectPanel(), menus[0]);
       rightPanel.add(buildAboutPanel(), menus[1]);
    }

    private JPanel buildLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setPreferredSize(new Dimension(200, 600));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));

        JPanel infoPanel = new JPanel();
        infoPanel.setPreferredSize(new Dimension(200, 100));
        leftPanel.add(infoPanel);

        leftPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        JList<String> menu = new JList<String>(menus);
        menu.setPreferredSize(new Dimension(200, 500));
        menu.setCellRenderer(new WelcomeMenuListCellRenderer(200));
        leftPanel.add(menu);

        menu.getSelectionModel().addListSelectionListener((e) -> {
            changeMenu(menu.getSelectionModel().getSelectedIndices()[0]);
        });
        menu.getSelectionModel().addSelectionInterval(0, 0);
        return leftPanel;
    }

    private int lastIndex = -1;
    private void changeMenu(int index) {
        if(index != lastIndex){
            lastIndex = index;
            rightPanelLayout.show(rightPanel, menus[index]);
        }
    }

    private JPanel buildProjectPanel() {
        JPanel projectPanel = new JPanel();
        BorderLayout layout = new BorderLayout();
        projectPanel.setLayout(layout);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));

        JTextField searchBar = new JTextField(10);
        searchBar.setText("Not yet implemented...");
        searchBar.setEnabled(false);
        searchBar.setMinimumSize(new Dimension(100, 30));
        topPanel.add(searchBar);

        JButton openProjectButton = new JButton("Open Existing");
        topPanel.add(openProjectButton);
        openProjectButton.addActionListener((e) -> {
            openExisting();
        });
        topPanel.add(new JButton("Create New"));
        topPanel.setPreferredSize(new Dimension(200, searchBar.getPreferredSize().height));
        projectPanel.add(topPanel, BorderLayout.NORTH);

        PanelList list = new PanelList();
        list.setScrollSensitivity(16);
        for(String s : appState.getWelcomeMenuData().projectHistory) {
            JPanel panel = new JPanel(new BorderLayout());
            ClickableLabel label = new ClickableLabel(s) {
                @Override
                protected void onClick() {
                    if(appState.tryOpenProject(s)){
                        uiManager.openEditor();
                        dispose();
                    } else {
                        PromptRemoval(s, list);
                    }
                }
            };
            panel.add(label, BorderLayout.CENTER);
            JButton XButton = new JButton("X");
            XButton.addActionListener(e -> {
                int index = appState.getWelcomeMenuData().projectHistory.indexOf(s);
                appState.getWelcomeMenuData().projectHistory.remove(index);
                list.removeElement(index);
            });
            panel.add(XButton, BorderLayout.EAST);

            panel.setPreferredSize(new Dimension(0, 60));
            panel.setMaximumSize(new Dimension(6000, 60));
            list.addElement(panel);
        }
        projectPanel.add(list);
        list.setPreferredSize(new Dimension(800, 800));

        return projectPanel;
    }

    private void PromptRemoval(String s, PanelList list) {
        int result = JOptionPane.showConfirmDialog(this,
                "Could not find project at: " + s + "\nWould you like to remove this from the list?",
                "Unable to locate project",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if(result == JOptionPane.YES_OPTION){
            int index = appState.getWelcomeMenuData().projectHistory.indexOf(s);
            appState.getWelcomeMenuData().projectHistory.remove(index);
            list.removeElement(index);
        }
    }

    private JPanel buildAboutPanel() {
        return new JPanel();
    }

    private void onClose() {
        appState.storeState();
    }

    private void openExisting() {
        JFileChooser fileSelector = new JFileChooser();

        // If path is recorded, try to use
        if(appState.getWelcomeMenuData().fileSelectorLastLocation.valid) {
            try{
                fileSelector.setCurrentDirectory(new File(appState.getWelcomeMenuData().fileSelectorLastLocation.s));
            }catch (Exception e){
                appState.getWelcomeMenuData().fileSelectorLastLocation.invalidate();
            }
        }

        // Set to details mode by default
        fileSelector.getActionMap().get("viewTypeDetails").actionPerformed(null);

        fileSelector.setFileFilter(new RecursiveFileFilter(6, "Protocol Designer (*.pd)") {
            @Override
            public boolean acceptFile(File f) {
                return f.isFile() && f.getName().endsWith(".pd");
            }
        });

        int choice = fileSelector.showOpenDialog(this);

        // store path
        appState.getWelcomeMenuData().fileSelectorLastLocation.set(fileSelector.getCurrentDirectory().getPath());

        if (choice != JFileChooser.APPROVE_OPTION) return;

        File chosenFile = fileSelector.getSelectedFile();
        boolean success = appState.tryOpenProject(chosenFile.getPath());
        if(success){
            // Add to history if successful
            if(!appState.getWelcomeMenuData().projectHistory.contains(chosenFile.getPath())){
                appState.getWelcomeMenuData().projectHistory.add(chosenFile.getPath());
            }
            uiManager.openEditor();
            dispose();
        }
    }

}
