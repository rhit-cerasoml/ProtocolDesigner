package pd.dataobject.types;

import pd.dataobject.types.serializers.ObjectSerializationMode;
import pd.dataobject.types.serializers.PrimitiveSerializationMode;
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
        if(serializationMode instanceof PrimitiveSerializationMode){
            out.writeBoolean(true);
            ((PrimitiveSerializationMode)serializationMode).serialize(out);
        }else{
            out.writeBoolean(false);
        }
    }

    // Deserialize
    public Type(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.typeName = in.readString();
        if(in.readBoolean()){
            this.serializationMode = new PrimitiveSerializationMode(in);
        }else{
            this.serializationMode = new ObjectSerializationMode(this);
        }
    }

    public void buildSerialize(StringBuilder sb, String name) {
        serializationMode.getSerializationCode(sb, name);
    }

    public void buildDeserialize(StringBuilder sb, String name) {
        serializationMode.getDeserializationCode(sb, name);
    }

    public void buildUpdate(StringBuilder sb, String name) {
        serializationMode.getUpdateCode(sb, name);
    }
}
