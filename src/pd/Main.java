package pd;

import pd.dataobject.DataObject;

public class Main {
    public static void main(String[] args){
//        DataObject testObject = new DataObject("TestObject");
//        System.out.println(testObject.buildNewBaseClass().writeNew());
//
//        System.out.println("\n\n\n");
//
//        String test = "//----- End segment : [imports] -----\n" +
//                "//----- Start segment : [body] -----\n" +
//                "THIS IS SOME INJECTED CODE\n" +
//                "//----- End segment : [body] -----\n";
//        try {
//            System.out.println(testObject.buildNewBaseClass().updateExisting(test));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        try {
            ProjectManager pm = new ProjectManager("src/demo/demoproject.pd");
            pm.addDataObject(new DataObject("TestObject"));
            pm.setBuildDirectory("src/demo/build");
            pm.setCacheDirectory("src/demo/cache");
            pm.save();
            pm.build();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
