package pd.dataobject;

import pd.TypeManager;
import pd.dataobject.types.ReferenceContext;
import pd.dataobject.types.Type;
import pd.util.Optional;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public class Field implements Serializable {
    private int typeIndex;
    private String name;
    private boolean visible;
    private Optional<ReferenceContext> referenceContext = new Optional<>(ReferenceContext::new);

    // not saved
    private TypeManager typeManager;

    public Field(TypeManager typeManager, int typeIndex, String name, boolean visible){
        this.typeManager = typeManager;
        this.typeIndex = typeIndex;
        this.name = name;
        this.visible = visible;
    }

    public Field(TypeManager typeManager, int typeIndex, String name, boolean visible, ReferenceContext referenceContext){
        this.typeManager = typeManager;
        this.typeIndex = typeIndex;
        this.name = name;
        this.visible = visible;
        this.referenceContext.set(referenceContext);
    }

    public void setTypeManager(TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    public void buildDeclaration(StringBuilder sb) throws Exception {
        buildDeclaration(sb, this.visible ? "public" : "protected");
    }

    public void buildSerialize(StringBuilder sb) throws Exception {
        getType().buildSerialize(sb, name);
    }

    public void buildDeserialize(StringBuilder sb) throws Exception {
        getType().buildDeserialize(sb, name);
    }

    public void buildUpdate(StringBuilder sb) throws Exception {
        getType().buildUpdate(sb, name);
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
        referenceContext.write(out);
    }

    // Deserialize
    public Field(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.typeIndex = in.readInt();
        this.name = in.readString();
        this.visible = in.readBoolean();
        this.referenceContext.read(in);
    }
}
