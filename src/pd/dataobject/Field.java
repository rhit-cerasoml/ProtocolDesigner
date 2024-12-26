package pd.dataobject;

import pd.util.Serializable;
import pd.util.SerializingInputStream;
import pd.util.SerializingOutputStream;

public class Field implements Serializable {
    private String type;

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeString(type);
    }

    // Deserialize
    public Field(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.type = in.readString();
    }
}
