package pd;

import pd.dataobject.DataObject;

public class Main {
    public static void main(String[] args){
        try {
            ProjectManager pm = new ProjectManager("src/demo/demoproject.pd");
            pm.addDataObject(new DataObject("TestObject", false, pm.getTypeManager()));
            pm.setBuildDirectory("src/demo/build");
            pm.setCacheDirectory("src/demo/cache");
            pm.save();
            //pm.build();
            //pm.revertBuild();
            ProjectManager pm2 = new ProjectManager("src/demo/demoproject.pd");
            pm2.build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
