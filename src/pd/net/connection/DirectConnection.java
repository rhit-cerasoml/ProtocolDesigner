package pd.net.connection;

import pd.util.serial.SerializingInputStream;

import java.io.IOException;

public class DirectConnection implements Connection {
    private Listener listener = new NullListener();
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
        try {
            this.endpoint.recv(data);
        }catch (Exception e){
            throw new IOException("Direct Receive Failed");
        }
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

    @Override
    public boolean isClosed() {
        return !open;
    }

    private void recv(byte[] data) throws SerializingInputStream.InvalidStreamLengthException, IOException {
        listener.accept(data);
    }
}
