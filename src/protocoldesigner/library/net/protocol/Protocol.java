package protocoldesigner.library.net.protocol;

import protocoldesigner.library.util.serial.SerializingInputStream;

public interface Protocol {
    void accept(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
}
