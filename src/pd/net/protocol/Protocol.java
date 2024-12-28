package pd.net.protocol;

import pd.util.serial.SerializingInputStream;

import java.io.IOException;

public interface Protocol {
    void accept(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
}
