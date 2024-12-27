package pd.dataobject;

import pd.artifacts.Artifact;
import pd.TypeManager;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class DataObject implements Serializable {
    private String className;
    private ArrayList<Field> fields;

    // not saved
    private TypeManager typeManager;

    public DataObject(String name, TypeManager typeManager){
        this.className = name;
        this.typeManager = typeManager;
        fields = new ArrayList<>();

        fields.add(new Field(typeManager, 0, "x", true, Target.COMMON));
    }

    public Artifact buildClass(Target target) throws Exception {
        Artifact artifact;
        String suffix;
        if(target == Target.COMMON){
            artifact = new Artifact("base/" + className + "Base.java");
            suffix = "Base";
        }else if(target == Target.CLIENT){
            artifact = new Artifact("client/" + className + "Client.java");
            suffix = "Client";
        }else{
            artifact = new Artifact("server/" + className + "Server.java");
            suffix = "Server";
        }

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();
        sb.append("public ");
        if(target == Target.COMMON) sb.append("abstract ");
        sb.append("class ");
        sb.append(className);
        sb.append(suffix);
        sb.append(" {\n");
        for(Field f : fields){
            f.buildDeclaration(sb, target);
        }
        artifact.append(sb.toString()); // generated fields/functions

        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeString(className);
        out.writeArrayList(fields);
    }

    // Deserialize
    public DataObject(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.className = in.readString();
        this.fields = in.readArrayList(Field::new);
    }

    public void setTypeManager(TypeManager typeManager){
        this.typeManager = typeManager;
        for(Field f : fields){
            f.setTypeManager(typeManager);
        }
    }



}
