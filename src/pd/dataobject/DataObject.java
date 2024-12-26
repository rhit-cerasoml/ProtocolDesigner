package pd.dataobject;

import pd.Artifact;
import pd.util.Deserializer;
import pd.util.Serializable;
import pd.util.SerializingInputStream;
import pd.util.SerializingOutputStream;

import java.util.ArrayList;

public class DataObject implements Serializable {
    private String className;
    private ArrayList<Field> fields;


    public DataObject(String name){
        this.className = name;
        fields = new ArrayList<>();
    }

    public Artifact buildNewBaseClass(){
        Artifact artifact = new Artifact("base/" + className + "Base.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public abstract class ");
        sb.append(className);
        sb.append("Base {\n");
        artifact.append(sb.toString());
        for(Field f : fields){
        }
        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    public Artifact buildNewServerClass(){
        Artifact artifact = new Artifact("server/" + className + "Server.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public class ");
        sb.append(className);
        sb.append("Server {\n");
        artifact.append(sb.toString());
        for(Field f : fields){
        }
        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    public Artifact buildNewClientClass(){
        Artifact artifact = new Artifact("client/" + className + "Client.java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();

        sb.append("public class ");
        sb.append(className);
        sb.append("Client {\n");
        artifact.append(sb.toString());
        for(Field f : fields){
        }
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



}
