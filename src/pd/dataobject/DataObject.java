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

        fields.add(new Field(typeManager, 0, "x", true, Retention.COMMON));
    }

    public Artifact buildNewBaseClass() throws Exception {
        Artifact artifact = new Artifact("base/" + className + "Base.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public abstract class ");
        sb.append(className);
        sb.append("Base {\n");

        for(Field f : fields){
            f.buildBaseDeclaration(sb);
        }
        artifact.append(sb.toString());
        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    public Artifact buildNewServerClass() throws Exception {
        Artifact artifact = new Artifact("server/" + className + "Server.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public class ");
        sb.append(className);
        sb.append("Server {\n");
        for(Field f : fields){
            f.buildServerDeclaration(sb);
        }
        artifact.append(sb.toString());
        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    public Artifact buildNewClientClass() throws Exception {
        Artifact artifact = new Artifact("client/" + className + "Client.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public class ");
        sb.append(className);
        sb.append("Client {\n");
        for(Field f : fields){
            f.buildClientDeclaration(sb);
        }
        artifact.append(sb.toString());
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
