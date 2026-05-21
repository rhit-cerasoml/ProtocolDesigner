package protocoldesigner.library.net.protocol;

import protocoldesigner.library.net.connection.Connection;
import protocoldesigner.library.net.connection.Listener;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

import java.io.IOException;
import java.util.ArrayList;

public class ProtocolManager implements Listener {
    private ArrayList<Protocol> protocols = new ArrayList<>();
    private Connection connection;

    public ProtocolManager(Connection connection){
        this.connection = connection;
        connection.setListener(this);
    }

    public int addProtocol(Protocol protocol){
        protocols.add(protocol);
        return protocols.size() - 1;
    }

    @Override
    public void accept(byte[] data) throws SerializingInputStream.InvalidStreamLengthException {
        SerializingInputStream in = new SerializingInputStream(data);
        int protocolID = in.readInt();
        protocols.get(protocolID).accept(in);
    }

    public SerializingOutputStream getPacketStub(Protocol p){
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(protocols.indexOf(p));
        return out;
    }

    public void sendPacket(SerializingOutputStream out) throws IOException {
        connection.send(out.toByteArray());
    }

}
