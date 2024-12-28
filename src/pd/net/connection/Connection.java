package pd.net.connection;

import java.io.IOException;

public interface Connection {
    void send(byte[] data) throws IOException;
    void setListener(Listener listener);
    void close() throws IOException;
}
