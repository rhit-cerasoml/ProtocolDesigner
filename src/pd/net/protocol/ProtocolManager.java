package pd.net.protocol;

import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class ProtocolManager {
    private ArrayList<Protocol> protocols = new ArrayList<>();

    public int addProtocol(Protocol protocol){
        protocols.add(protocol);
        return protocols.size() - 1;
    }

    public void acceptPacket(byte[] packet) throws SerializingInputStream.InvalidStreamLengthException {
        SerializingInputStream in = new SerializingInputStream(packet);
        int protocolID = in.readInt();
        protocols.get(protocolID).accept(in);
    }

    public SerializingOutputStream getPacketStub(Protocol p){
        SerializingOutputStream out = new SerializingOutputStream();
        out.writeInt(protocols.indexOf(p));
        return out;
    }
}
