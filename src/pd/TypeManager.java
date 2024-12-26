package pd;

import pd.dataobject.types.Type;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class TypeManager implements Serializable {
    private static final Type INVALID = new Type("UnknownType");
    ArrayList<Type> types;
    public TypeManager(){
        types = new ArrayList<>();
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
