package pd.net.connection;

import pd.util.serial.SerializingInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketConnection extends Thread implements Connection {

    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Listener listener = new NullListener();
    private volatile boolean open = true; // no lock needed since we assign this value prior to the closing of socket

    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        start();
    }

    @Override
    public void send(byte[] data) throws IOException {
        outputStream.write(data);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void close() throws IOException {
        open = false;
        socket.close();
    }

    @Override
    public void run() {
        super.run();
        while(open){
            try {
                listener.accept(inputStream.readAllBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SerializingInputStream.InvalidStreamLengthException ignored) {
            }
        }
    }
}
