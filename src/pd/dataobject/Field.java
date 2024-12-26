package pd.dataobject;

import pd.TypeManager;
import pd.dataobject.types.Type;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public class Field implements Serializable {
    private int typeIndex;
    private String name;
    private boolean visible;
    private Retention retention;

    // not saved
    private TypeManager typeManager;

    public Field(TypeManager typeManager){
        this.typeManager = typeManager;
        this.typeIndex = -1;
        this.name = "";
        this.visible = false;
        this.retention = Retention.COMMON;
    }

    public void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    private Type getType() throws Exception {
        if(typeManager == null) throw new Exception("Invalid type manager");
        if(typeIndex == -1) typeManager.getInvalidType();
        return typeManager.getType(typeIndex);
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeInt(typeIndex);
        out.writeString(name);
        out.writeBoolean(visible);
        retention.serialize(out);
    }

    // Deserialize
    public Field(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.typeIndex = in.readInt();
        this.name = in.readString();
        this.visible = in.readBoolean();
        retention = Retention.values()[in.readInt()];
    }
}
