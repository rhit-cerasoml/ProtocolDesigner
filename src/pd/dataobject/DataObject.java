package pd.dataobject;

import pd.artifacts.Artifact;
import pd.TypeManager;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class DataObject implements Serializable {
    private String className;
    private boolean buildLocals;
    private ArrayList<Field> fields;

    // not saved
    private TypeManager typeManager;

    public DataObject(String name, boolean buildLocals, TypeManager typeManager){
        this.className = name;
        this.buildLocals = buildLocals;
        this.typeManager = typeManager;
        fields = new ArrayList<>();

        fields.add(new Field(typeManager, 0, "x", true));
        fields.add(new Field(typeManager, 0, "y", true));
        fields.add(new Field(typeManager, 0, "z", true));
    }

    public Artifact buildClass() throws Exception {
        Artifact artifact;
        String suffix;
        artifact = new Artifact(className + ".java");

        artifact.appendFreeSegment("imports");

        StringBuilder sb = new StringBuilder();
        sb.append("public ");
        sb.append("class ");
        sb.append(className);
        sb.append(" implements Serializable {\n");
        for(Field f : fields){
            f.buildDeclaration(sb);
        }
        buildSerializationFunction(sb);
        buildDeserializationFunction(sb);
        buildUpdateFunction(sb);
        artifact.append(sb.toString()); // generated fields/functions

        artifact.appendFreeSegment("body");

        artifact.append("}");

        return artifact;
    }

    private void buildSerializationFunction(StringBuilder sb) throws Exception {
        sb.append("\tpublic void serialize(SerializingInputStream in) {\n");
        for(Field f : fields){
            f.buildSerialize(sb);
        }
        sb.append("\t}\n");
    }

    private void buildDeserializationFunction(StringBuilder sb) throws Exception {
        sb.append("\tpublic ");
        sb.append(className);
        sb.append("(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {\n");
        for(Field f : fields){
            f.buildDeserialize(sb);
        }
        sb.append("\t}\n");
    }

    private void buildUpdateFunction(StringBuilder sb) throws Exception {
        sb.append("\tpublic void update(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {\n");
        for(Field f : fields){
            f.buildUpdate(sb);
        }
        sb.append("\t}\n");
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeString(className);
        out.writeBoolean(buildLocals);
        out.writeArrayList(fields);
    }

    // Deserialize
    public DataObject(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.className = in.readString();
        this.buildLocals = in.readBoolean();
        this.fields = in.readArrayList(Field::new);
    }

    public void setTypeManager(TypeManager typeManager){
        this.typeManager = typeManager;
        for(Field f : fields){
            f.setTypeManager(typeManager);
        }
    }

}
