package protocoldesigner.app.ui;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;
import protocoldesigner.app.model.ApplicationState;
import protocoldesigner.app.ui.editor.EditorView;
import protocoldesigner.app.ui.welcome.WelcomeView;

import javax.swing.*;

public class UIManager {

    private final ApplicationState appState;

    private JFrame mainContext;
    public UIManager(ApplicationState appState) {
        this.appState = appState;

        try {
            LafManager.install(new DarculaTheme());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(new JFrame(), "Unable to apply theme: " + e, "Theme Error!", JOptionPane.ERROR_MESSAGE);
        }

        if(appState.isProjectOpen()){
            mainContext = new EditorView(this, appState);
        }else{
            mainContext = new WelcomeView(this, appState);
        }
    }

    public void openEditor(){
        mainContext = new EditorView(this, appState);
    }

    public void openWelcomeMenu(){
        mainContext = new WelcomeView(this, appState);
    }
}
