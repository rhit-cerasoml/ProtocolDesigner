package pd.dataobject;

import pd.util.serial.Serializable;
import pd.util.serial.SerializingOutputStream;

public enum Retention implements Serializable {
    COMMON,
    SERVER,
    CLIENT;

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeInt(this.ordinal());
    }
}
