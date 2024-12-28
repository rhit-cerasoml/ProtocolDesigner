package pd.net.connection;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionGroup implements Connection {
    private ArrayList<Connection> connections = new ArrayList<>();
    private Listener listener = new NullListener();

    public void addConnection(Connection connection){
        connection.setListener(listener);
        connections.add(connection);
    }

    @Override
    public void send(byte[] data) throws IOException {
        for(Connection connection : connections){
            connection.send(data);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
        for(Connection connection : connections){
            connection.setListener(listener);
        }
    }

    @Override
    public void close() throws IOException {
        for(Connection connection : connections){
            connection.close();
        }
    }
}
