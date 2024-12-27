package pd.dataobject.types.serializers;

import pd.dataobject.types.SerializationMode;

public class PrimitiveSerializationMode implements SerializationMode {

    private String writeMethod;
    private String readMethod;

    public PrimitiveSerializationMode(String writeMethod, String readMethod){
        this.writeMethod = writeMethod;
        this.readMethod = readMethod;
    }

    @Override
    public void getSerializationCode(StringBuilder sb, String name) {
        sb.append("out.");
        sb.append(writeMethod);
        sb.append("(");
        sb.append(name);
        sb.append(");\n");
    }

    @Override
    public void getDeserializationCode(StringBuilder sb, String name) {
        sb.append(name);
        sb.append(" = in.");
        sb.append(readMethod);
        sb.append("();\n");
    }

    @Override
    public void getUpdateCode(StringBuilder sb, String name) {
        sb.append(name);
        sb.append(" = in.");
        sb.append(readMethod);
        sb.append("();\n");
    }
}
