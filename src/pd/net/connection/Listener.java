package pd.net.connection;

import pd.util.serial.SerializingInputStream;

public interface Listener {
    void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException;
}
