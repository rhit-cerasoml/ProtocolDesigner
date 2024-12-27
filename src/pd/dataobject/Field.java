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

    public Field(TypeManager typeManager, int typeIndex, String name, boolean visible, Retention retention){
        this.typeManager = typeManager;
        this.typeIndex = typeIndex;
        this.name = name;
        this.visible = visible;
        this.retention = retention;
    }

    public void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    public void buildBaseDeclaration(StringBuilder sb) throws Exception {
        if(this.retention == Retention.COMMON) {
            buildDeclaration(sb, this.visible ? "public" : "protected");
        }
    }

    public void buildServerDeclaration(StringBuilder sb) throws Exception {
        if(this.retention == Retention.SERVER) {
            buildDeclaration(sb, this.visible ? "public" : "private");
        }
    }

    public void buildClientDeclaration(StringBuilder sb) throws Exception {
        if(this.retention == Retention.CLIENT) {
            buildDeclaration(sb, this.visible ? "public" : "private");
        }
    }

    public void buildBaseSerialize(StringBuilder sb) throws Exception {
        if(this.retention == Retention.CLIENT) {
            getType().buildSerialize(sb, name);
        }
    }

    private void buildDeclaration(StringBuilder sb, String modifier) throws Exception {
        sb.append("\t");
        sb.append(modifier);
        sb.append(" ");
        sb.append(getType().getTypeName());
        sb.append(" ");
        sb.append(name);
        sb.append(";\n");
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
