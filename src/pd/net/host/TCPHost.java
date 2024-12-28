package pd.net.host;

import pd.net.connection.SocketConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHost extends Thread {

    private ServerSocket serverSocket;
    private boolean open = true;
    private ConnectionAcceptor acceptor;

    public TCPHost(ConnectionAcceptor acceptor, int port) throws IOException {
        this.acceptor = acceptor;
        serverSocket = new ServerSocket(port);
        start();
    }

    public void close() throws IOException {
        open = false;
        serverSocket.close();
    }

    @Override
    public void run() {
        super.run();
        while(open && !serverSocket.isClosed()){
            try {
                Socket s = serverSocket.accept();
                SocketConnection connection = new SocketConnection(s);
                acceptor.acceptConnection(connection);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
