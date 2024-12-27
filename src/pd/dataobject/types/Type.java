package pd.dataobject.types;

import pd.dataobject.types.serializers.ObjectSerializationMode;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public class Type implements Serializable {
    private String typeName;
    private SerializationMode serializationMode;

    public Type(String typeName, SerializationMode serializationMode){
        this.typeName = typeName;
        this.serializationMode = serializationMode;
    }

    public Type(String typeName){
        this.typeName = typeName;
        this.serializationMode = new ObjectSerializationMode(this);
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

    public void buildSerialize(StringBuilder sb, String name) {
        serializationMode.getSerializationCode(sb, name);
    }
}
