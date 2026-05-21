package protocoldesigner.library.net.connection;

import protocoldesigner.library.util.serial.SerializingInputStream;

public interface Listener {
    void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException;
}
