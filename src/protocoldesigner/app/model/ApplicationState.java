package protocoldesigner.app.model;

import protocoldesigner.app.model.uidata.welcome.WelcomeMenuData;
import protocoldesigner.library.util.OptionalString;
import protocoldesigner.library.util.appcache.ApplicationCache;
import protocoldesigner.library.util.file.FileUtils;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;

public class ApplicationState implements Serializable {

    private static final String appCachePath = "appcache.bin";
    private ProjectManager projectManager;

    // Serialized
    private OptionalString projectFilePath = new OptionalString();
    private WelcomeMenuData welcomeMenuData = new WelcomeMenuData();

    public ApplicationState(){
        loadState();
    }

    public void storeState() {
        storeProject();
        storeAppData();
    }

    private void storeProject(){
        if(projectManager != null) {
            if(!projectFilePath.valid){
                JOptionPane.showMessageDialog(new JFrame(), "No file path selected!", "TODO", JOptionPane.ERROR_MESSAGE);
            }
            try {
                projectManager.save(projectFilePath.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(new JFrame(), "Unable to save project file: " + e, "Save Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void storeAppData(){
        Path cachePath = ApplicationCache.getCachePath().resolve(appCachePath);
        try {
            File cacheFile = FileUtils.getOrCreateFile(cachePath.toString());
            SerializingOutputStream out = new SerializingOutputStream();
            out.writeObj(this);
            try(FileOutputStream outputStream = new FileOutputStream(cacheFile)) {
                outputStream.write(out.toByteArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Unable to save application state: " + e, "Save Error!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadState() {
        Path cachePath = ApplicationCache.getCachePath().resolve(appCachePath);
        try {
            File cacheFile = FileUtils.getOrCreateFile(cachePath.toString());
            SerializingInputStream in = new SerializingInputStream(new FileInputStream(cacheFile));
            try{
                deserialize(in);
            }catch (Exception e){
                cacheFile.delete();
                reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Unable to read application state: " + e, "Load Error!", JOptionPane.ERROR_MESSAGE);
        }

        if(projectFilePath.valid){
            projectManager = new ProjectManager(projectFilePath.s);
        }
    }

    public boolean isProjectOpen(){
        return projectFilePath.valid;
    }
    public WelcomeMenuData getWelcomeMenuData(){
        return welcomeMenuData;
    }

    private void reset(){
        projectManager = null;
        projectFilePath.invalidate();
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeObj(projectFilePath);
        out.writeObj(welcomeMenuData);
    }

    private void deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        projectFilePath = in.readObj(OptionalString::new);
        welcomeMenuData = in.readObj(WelcomeMenuData::new);
    }

    public void closeProject() {
        storeProject();
        projectManager = null;
        projectFilePath.invalidate();
    }
    public boolean tryOpenProject(String path) {
        projectManager = new ProjectManager(path);
        if(projectManager.isValid()){
            this.projectFilePath.set(path);
            return true;
        }else{
            projectManager = null;
            return false;
        }
    }

}
