package protocoldesigner.app.generators.precompiler.dataobject;

import protocoldesigner.app.generators.precompiler.artifacts.Artifact;
import protocoldesigner.app.generators.precompiler.dataobject.types.Type;
import protocoldesigner.app.model.TypeManager;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

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
        fields.add(new Field(typeManager, 1, "y", true));
        fields.add(new Field(typeManager, 2, "z", true));
        fields.add(new Field(typeManager, 4, "aaa", true));
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
    public DataObject(SerializingInputStream in, TypeManager typeManager) throws SerializingInputStream.InvalidStreamLengthException {
        this.className = in.readString();
        this.buildLocals = in.readBoolean();
        this.fields = in.readArrayList((input) -> new Field(input, typeManager) );

        this.typeManager = typeManager;
    }


}
