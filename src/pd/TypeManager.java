package pd;

import pd.dataobject.types.SerializationMode;
import pd.dataobject.types.Type;
import pd.dataobject.types.serializers.PrimitiveSerializationMode;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class TypeManager implements Serializable {
    private static final Type INVALID = new Type("UnknownType", new SerializationMode() {
        @Override
        public String getSerializationCode(String name) {
            return "";
        }

        @Override
        public String getDeserializationCode(String name) {
            return "";
        }

        @Override
        public String getUpdateCode(String name) {
            return "";
        }
    });

    private ArrayList<Type> types;
    public TypeManager(){
        types = new ArrayList<>();
        types.add(new Type("int", new PrimitiveSerializationMode("writeInt", "readInt")));
        types.add(new Type("String", new PrimitiveSerializationMode("writeString", "readString")));
        types.add(new Type("float", new PrimitiveSerializationMode("writeFloat", "readFloat")));
        types.add(new Type("boolean", new PrimitiveSerializationMode("writeBoolean", "readBoolean")));
        types.add(new Type("double", new PrimitiveSerializationMode("writeDouble", "readDouble")));
        types.add(new Type("long", new PrimitiveSerializationMode("writeLong", "readLong")));
    }

    public Type getType(int typeIndex) {
        return types.get(typeIndex);
    }

    public Type getInvalidType(){
        return INVALID;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeArrayList(types);
    }

    // Deserializer
    public TypeManager(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.types = in.readArrayList(Type::new);
    }
}
