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
    private Target target;

    // not saved
    private TypeManager typeManager;

    public Field(TypeManager typeManager, int typeIndex, String name, boolean visible, Target retention){
        this.typeManager = typeManager;
        this.typeIndex = typeIndex;
        this.name = name;
        this.visible = visible;
        this.target = retention;
    }

    public void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    public void buildDeclaration(StringBuilder sb, Target outputTarget) throws Exception {
        if(this.target == outputTarget) {
            buildDeclaration(sb, this.visible ? "public" : "protected");
        }
    }

    public void buildSerialize(StringBuilder sb, Target outputTarget) throws Exception {
        if(this.target == outputTarget) {
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
        target.serialize(out);
    }

    // Deserialize
    public Field(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.typeIndex = in.readInt();
        this.name = in.readString();
        this.visible = in.readBoolean();
        target = Target.values()[in.readInt()];
    }
}
