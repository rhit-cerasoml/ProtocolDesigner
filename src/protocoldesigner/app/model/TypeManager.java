package protocoldesigner.app.model;

import protocoldesigner.app.generators.precompiler.dataobject.types.SerializationMode;
import protocoldesigner.app.generators.precompiler.dataobject.types.Type;
import protocoldesigner.app.generators.precompiler.dataobject.types.serializers.PrimitiveSerializationMode;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class TypeManager implements Serializable {
    private static final Type INVALID = new Type("UnknownType", new SerializationMode() {
        @Override
        public void getSerializationCode(StringBuilder sb, String name) {}
        @Override
        public void getDeserializationCode(StringBuilder sb, String name) {}
        @Override
        public void getUpdateCode(StringBuilder sb, String name) {}
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

    public Type getType(Class<?> t){
        if (t.equals(int.class)) {
            return types.get(0);
        }else if(t.equals(String.class)){
            return types.get(1);
        }else if(t.equals(float.class)){
            return types.get(2);
        }else if(t.equals(boolean.class)){
            return types.get(3);
        }else if(t.equals(double.class)){
            return types.get(4);
        }else if(t.equals(long.class)){
            return types.get(5);
        }else{
            throw new RuntimeException("Unregistered type: " + t.getName());
        }
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
