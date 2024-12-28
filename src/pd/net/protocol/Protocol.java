package pd.net.protocol;

import pd.util.serial.SerializingInputStream;

public interface Protocol {
    void accept(SerializingInputStream in);
}
