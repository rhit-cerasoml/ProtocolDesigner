package protocoldesigner;

import protocoldesigner.app.generators.precompiler.dataobject.DataObject;
import protocoldesigner.app.model.ProjectManager;

public class Main {
    public static void main(String[] args){
        try {
            //ProjectManager pm = new ProjectManager("H:/2026Code/tanktrouble.pd");
            //pm.addDataObject(new DataObject("TestObject", false, pm.getTypeManager()));
            //pm.setBuildDirectory("C:/Users/Twig/Documents/repos/TankTroubleLAN/src/tanktrouble/generated/");
            //pm.setCacheDirectory("src/demo/cache");
            //pm.save();
            //pm.build();

            ProjectManager pm = new ProjectManager("src/demo/demoproject.pd");
            pm.setBuildDirectory("src/demo/build");
            pm.setCacheDirectory("src/demo/cache");
            pm.addDataObject(new DataObject("TestObject", false, pm.getTypeManager()));
            pm.save("src/demo/demoproject.pd");
            pm.build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
