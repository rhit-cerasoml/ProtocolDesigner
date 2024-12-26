package pd.dataobject.types;

import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public class Type implements Serializable {
    private String typeName;
    private SerializationMode serializationMode;

    public Type(String typeName){
        this.typeName = typeName;
    }

    public String getTypeName(){
        return this.typeName;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeString(typeName);
    }

    // Deserialize
    public Type(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.typeName = in.readString();
    }
}
