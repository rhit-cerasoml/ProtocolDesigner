package pd.dataobject.types.serializers;

import pd.dataobject.types.SerializationMode;
import pd.dataobject.types.Type;

public class ObjectSerializationMode implements SerializationMode {
    private Type type;

    public ObjectSerializationMode(Type type){
        this.type = type;
    }

    @Override
    public String getSerializationCode(String name) {
        return "\t\t" +
                name +
                ".serialize(out);\n";
    }

    @Override
    public String getDeserializationCode(String name) {
        return "\t\tthis." +
                name +
                " = new " +
                type.getTypeName() +
                "(in);\n";
    }

    @Override
    public String getUpdateCode(String name) {
        return "\t\tthis." +
                name +
                ".loadFromSerial(in);\n";
    }
}
