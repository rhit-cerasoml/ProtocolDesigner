package pd.dataobject.types.serializers;

import pd.dataobject.types.SerializationMode;
import pd.dataobject.types.Type;

public class DefaultSerializationMode implements SerializationMode {
    private Type type;

    DefaultSerializationMode(Type type){
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
        return "\t\t" +
                "this." +
                name +
                " = new " +
                type.getTypeName() +
                "(in);\n";
    }
}
