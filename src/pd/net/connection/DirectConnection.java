package pd.net.connection;

import java.io.IOException;

public class DirectConnection implements Connection {
    private Listener listener;
    private DirectConnection endpoint;
    private boolean open = true;

    public DirectConnection(){
        this.endpoint = new DirectConnection(this);
    }

    private DirectConnection(DirectConnection endpoint){
        this.endpoint = endpoint;
    }

    public DirectConnection getEndpoint() {
        return endpoint;
    }

    @Override
    public void send(byte[] data) throws IOException {
        if(!open) throw new IOException("Connection Closed");
        this.endpoint.recv(data);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() {
        if(open) {
            endpoint.endpoint = null;
            endpoint.open = false;
            endpoint = null;
            open = false;
        }
    }

    private void recv(byte[] data){
        listener.accept(data);
    }
}
