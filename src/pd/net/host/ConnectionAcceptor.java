package pd.net.host;

import pd.net.connection.Connection;

public interface ConnectionAcceptor {
    void acceptConnection(Connection c);
}
