package pd;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class PregenBuilder {
    public static void main(String[] args) throws IOException {
        String output = "package pd.generated;\n" +
                "\n" +
                "public class PregeneratedFiles {\n" +
                "    \n" +
                "\tpublic static final String DESERIALIZER = \"\"\"\n" + readFile("src/pd/util/serial/Deserializer.java") + "\"\"\";\n" +
                "\tpublic static final String SERIALIZABLE = \"\"\"\n" + readFile("src/pd/util/serial/Serializable.java") + "\"\"\";\n" +
                "\tpublic static final String SERIALIZING_INPUT_STREAM = \"\"\"\n" + readFile("src/pd/util/serial/SerializingInputStream.java") + "\"\"\";\n" +
                "\tpublic static final String SERIALIZING_OUTPUT_STREAM = \"\"\"\n" + readFile("src/pd/util/serial/SerializingOutputStream.java") + "\"\"\";\n" +
                "\n" +
                "\tpublic static final String CONNECTION = \"\"\"\n" + readFile("src/pd/net/connection/Connection.java") + "\"\"\";\n" +
                "\tpublic static final String LISTENER = \"\"\"\n" + readFile("src/pd/net/connection/Listener.java") + "\"\"\";\n" +
                "\tpublic static final String DIRECT_CONNECTION = \"\"\"\n" + readFile("src/pd/net/connection/DirectConnection.java") + "\"\"\";\n" +
                "\tpublic static final String SOCKET_CONNECTION = \"\"\"\n" + readFile("src/pd/net/connection/SocketConnection.java") + "\"\"\";\n" +
                "\n" +
                "\tpublic static final String CONNECTION_ACCEPTOR = \"\"\"\n" + readFile("src/pd/net/host/ConnectionAcceptor.java") + "\"\"\";\n" +
                "\tpublic static final String TCP_HOST = \"\"\"\n" + readFile("src/pd/net/host/TCPHost.java") + "\"\"\";\n" +
                "\n" +
                "\tpublic static final String PROTOCOL = \"\"\"\n" + readFile("src/pd/net/protocol/Protocol.java") + "\"\"\";\n" +
                "\tpublic static final String PROTOCOL_MANAGER = \"\"\"\n" + readFile("src/pd/net/protocol/ProtocolManager.java") + "\"\"\";\n" +
                "}";
        FileWriter fout = new FileWriter("src/pd/generated/PregeneratedFiles.java");
        fout.write(output);
        fout.flush();
        fout.close();
    }

    static String readFile(String path) throws IOException {
        return Files.readString(Path.of(path));
    }
}