package protocoldesigner.app.generators.precompiler.dataobject.types.serializers;

import protocoldesigner.app.generators.precompiler.dataobject.types.SerializationMode;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

public class PrimitiveSerializationMode implements SerializationMode, Serializable {

    private String writeMethod;
    private String readMethod;

    public PrimitiveSerializationMode(String writeMethod, String readMethod){
        this.writeMethod = writeMethod;
        this.readMethod = readMethod;
    }

    @Override
    public void getSerializationCode(StringBuilder sb, String name) {
        sb.append("\t\tout.");
        sb.append(writeMethod);
        sb.append("(");
        sb.append(name);
        sb.append(");\n");
    }

    @Override
    public void getDeserializationCode(StringBuilder sb, String name) {
        sb.append("\t\t");
        sb.append(name);
        sb.append(" = in.");
        sb.append(readMethod);
        sb.append("();\n");
    }

    @Override
    public void getUpdateCode(StringBuilder sb, String name) {
        sb.append("\t\t");
        sb.append(name);
        sb.append(" = in.");
        sb.append(readMethod);
        sb.append("();\n");
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeString(this.writeMethod);
        out.writeString(this.readMethod);
    }

    // Deserialize
    public PrimitiveSerializationMode(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.writeMethod = in.readString();
        this.readMethod = in.readString();
    }
}
