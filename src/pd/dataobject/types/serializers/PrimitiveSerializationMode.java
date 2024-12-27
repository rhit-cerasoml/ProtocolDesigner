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
    public String getSerializationCode(String name) {
        return "out." + writeMethod + "(" + name + ");\n";
    }

    @Override
    public String getDeserializationCode(String name) {
        return name + " = in." + readMethod + "();\n";
    }

    @Override
    public String getUpdateCode(String name) {
        return name + " = in." + readMethod + "();\n";
    }
}
