package pd.net.connection;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionGroup implements Connection {
    private ArrayList<Connection> connections = new ArrayList<>();
    private Listener listener = new NullListener();
    private boolean open = true;

    public void addConnection(Connection connection){
        if(!open) return;
        connection.setListener(listener);
        connections.add(connection);
    }

    @Override
    public void send(byte[] data) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        for(int i = connections.size() - 1; i >= 0; i--){
            if(connections.get(i).isClosed()){
                connections.remove(i);
                continue;
            }
            connections.get(i).send(data);
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
        open = false;
        for(Connection connection : connections){
            connection.close();
        }
    }

    @Override
    public boolean isClosed() {
        return !open;
    }

}
