package pd.net.connection;

import pd.util.serial.SerializingInputStream;

import java.io.IOException;

public interface Listener {
    void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException;
}
