package protocoldesigner.library.net.host;

import protocoldesigner.library.net.connection.Connection;

public interface ConnectionAcceptor {
    void acceptConnection(Connection c);
}
