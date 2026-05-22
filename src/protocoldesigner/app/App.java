package protocoldesigner.app;

import protocoldesigner.app.model.ApplicationState;
import protocoldesigner.app.ui.UIManager;
import protocoldesigner.app.util.PanelList;
import protocoldesigner.library.util.appcache.ApplicationCache;

import javax.swing.*;


public class App {

    final ApplicationState applicationState;
    final UIManager uiManager;

    public static void main(String[] args) {
        ApplicationCache.setAppName("ProtocolDesigner");
        App app = new App();
    }

    public App() {




        applicationState = new ApplicationState();
        uiManager = new UIManager(applicationState);

        JFrame testFrame = new JFrame();

        PanelList panelList = new PanelList();
        panelList.addElement(new JButton("HI"));
        testFrame.add(panelList);
        testFrame.setVisible(true);
        testFrame.setSize(900, 600);
        panelList.addElement(new JButton("HI again"));



    }
}
