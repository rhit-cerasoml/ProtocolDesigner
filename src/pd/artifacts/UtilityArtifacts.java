package pd.artifacts;

import pd.generated.PregeneratedFiles;

import java.util.ArrayList;

public class UtilityArtifacts {
    public static ArrayList<Artifact> artifacts;
    private static final UtilityArtifacts INSTANCE = new UtilityArtifacts();
    private UtilityArtifacts(){
        artifacts = new ArrayList<>();
        buildSerializationClasses();
        buildNetClasses();
        buildPoolClasses();
    }

    private void buildSerializationClasses(){
        Artifact deserializer = new Artifact("util/serial/Deserializer.java");
        deserializer.appendFreeSegment("package");
        deserializer.append(PregeneratedFiles.DESERIALIZER);
        artifacts.add(deserializer);

        Artifact serializer = new Artifact("util/serial/Serializer.java");
        serializer.appendFreeSegment("package");
        serializer.append(PregeneratedFiles.SERIALIZER);
        artifacts.add(serializer);

        Artifact serializable = new Artifact("util/serial/Serializable.java");
        serializable.appendFreeSegment("package");
        serializable.append(PregeneratedFiles.SERIALIZABLE);
        artifacts.add(serializable);

        Artifact serializingInputStream = new Artifact("util/serial/SerializingInputStream.java");
        serializingInputStream.appendFreeSegment("package");
        serializingInputStream.append(PregeneratedFiles.SERIALIZING_INPUT_STREAM);
        artifacts.add(serializingInputStream);

        Artifact serializingOutputStream = new Artifact("util/serial/SerializingOutputStream.java");
        serializingOutputStream.appendFreeSegment("package");
        serializingOutputStream.append(PregeneratedFiles.SERIALIZING_OUTPUT_STREAM);
        artifacts.add(serializingOutputStream);

        Artifact optional = new Artifact("util/serial/Optional.java");
        optional.appendFreeSegment("package");
        optional.append(PregeneratedFiles.OPTIONAL);
        artifacts.add(optional);
    }

    private void buildNetClasses() {
        Artifact connection = new Artifact("util/net/connection/Connection.java");
        connection.appendFreeSegment("package");
        connection.append(PregeneratedFiles.CONNECTION);
        artifacts.add(connection);

        Artifact directConnection = new Artifact("util/net/connection/DirectConnection.java");
        directConnection.appendFreeSegment("package");
        directConnection.append(PregeneratedFiles.DIRECT_CONNECTION);
        artifacts.add(directConnection);

        Artifact listener = new Artifact("util/net/connection/Listener.java");
        listener.appendFreeSegment("package");
        listener.append(PregeneratedFiles.LISTENER);
        artifacts.add(listener);

        Artifact nullListener = new Artifact("util/net/connection/NullListener.java");
        nullListener.appendFreeSegment("package");
        nullListener.append(PregeneratedFiles.NULL_LISTENER);
        artifacts.add(nullListener);

        Artifact connectionGroup = new Artifact("util/net/connection/ConnectionGroup.java");
        connectionGroup.appendFreeSegment("package");
        connectionGroup.append(PregeneratedFiles.CONNECTION_GROUP);
        artifacts.add(connectionGroup);

        Artifact socketConnection = new Artifact("util/net/connection/SocketConnection.java");
        socketConnection.appendFreeSegment("package");
        socketConnection.append(PregeneratedFiles.SOCKET_CONNECTION);
        artifacts.add(socketConnection);

        Artifact connectionAcceptor = new Artifact("util/net/host/ConnectionAcceptor.java");
        connectionAcceptor.appendFreeSegment("package");
        connectionAcceptor.append(PregeneratedFiles.CONNECTION_ACCEPTOR);
        artifacts.add(connectionAcceptor);

        Artifact TCPHost = new Artifact("util/net/host/TCPHost.java");
        TCPHost.appendFreeSegment("package");
        TCPHost.append(PregeneratedFiles.TCP_HOST);
        artifacts.add(TCPHost);

        Artifact protocol = new Artifact("util/net/protocol/Protocol.java");
        protocol.appendFreeSegment("package");
        protocol.append(PregeneratedFiles.PROTOCOL);
        artifacts.add(protocol);

        Artifact protocolManager = new Artifact("util/net/protocol/ProtocolManager.java");
        protocolManager.appendFreeSegment("package");
        protocolManager.append(PregeneratedFiles.PROTOCOL_MANAGER);
        artifacts.add(protocolManager);
    }

    private void buildPoolClasses() {
        Artifact pool = new Artifact("util/pool/Pool.java");
        pool.appendFreeSegment("package");
        pool.append(PregeneratedFiles.POOL);
        artifacts.add(pool);

        Artifact poolContent = new Artifact("util/pool/PoolContent.java");
        poolContent.appendFreeSegment("package");
        poolContent.append(PregeneratedFiles.POOL_CONTENT);
        artifacts.add(poolContent);

        Artifact arrayListPool = new Artifact("util/pool/ArrayListPool.java");
        arrayListPool.appendFreeSegment("package");
        arrayListPool.append(PregeneratedFiles.ARRAYLIST_POOL);
        artifacts.add(arrayListPool);

        Artifact hashMapPool = new Artifact("util/pool/HashMapPool.java");
        hashMapPool.appendFreeSegment("package");
        hashMapPool.append(PregeneratedFiles.HASHMAP_POOL);
        artifacts.add(hashMapPool);
    }
}
