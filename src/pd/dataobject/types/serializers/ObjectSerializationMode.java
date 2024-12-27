package pd.dataobject.types.serializers;

import pd.dataobject.types.SerializationMode;
import pd.dataobject.types.Type;

public class ObjectSerializationMode implements SerializationMode {
    private Type type;

    public ObjectSerializationMode(Type type){
        this.type = type;
    }

    @Override
    public void getSerializationCode(StringBuilder sb, String name) {
        sb.append("\t\t");
        sb.append(name);
        sb.append(".serialize(out);\n");
    }

    @Override
    public void getDeserializationCode(StringBuilder sb, String name) {
        sb.append("\t\tthis.");
        sb.append(name);
        sb.append(" = new ");
        sb.append(type.getTypeName());
        sb.append("(in);\n");
    }

    @Override
    public void getUpdateCode(StringBuilder sb, String name) {
        sb.append("\t\tthis.");
        sb.append(name);
        sb.append(".loadFromSerial(in);\n");
    }
}
