package protocoldesigner.app.generators.precompiler.dataobject.types;

import protocoldesigner.app.generators.precompiler.dataobject.types.serializers.ObjectSerializationMode;
import protocoldesigner.app.generators.precompiler.dataobject.types.serializers.PrimitiveSerializationMode;
import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

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
