package protocoldesigner.app;

import protocoldesigner.app.model.ApplicationState;
import protocoldesigner.app.ui.UIManager;
import protocoldesigner.app.util.PanelList;
import protocoldesigner.library.util.appcache.ApplicationCache;

import javax.swing.*;
import java.awt.*;


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




    }
}
