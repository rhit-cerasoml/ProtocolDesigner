package pd.artifacts;

import java.util.ArrayList;

public class UtilityArtifacts {
    public static ArrayList<Artifact> artifacts;
    private static final UtilityArtifacts INSTANCE = new UtilityArtifacts();
    private UtilityArtifacts(){
        artifacts = new ArrayList<>();
        buildSerializationClasses();
    }

    private void buildSerializationClasses(){
        Artifact deserializer = new Artifact("util/serial/Deserializer.java");
        deserializer.appendFreeSegment("package");
        deserializer.append("""
                public interface Deserializer<T> {
                    T deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
                }""");
        artifacts.add(deserializer);

        Artifact serializable = new Artifact("util/serial/Serializable.java");
        serializable.appendFreeSegment("package");
        serializable.append("""
                public interface Serializable {
                    void serialize(SerializingOutputStream out);
                }""");
        artifacts.add(serializable);

        Artifact serializingInputStream = new Artifact("util/serial/SerializingInputStream.java");
        serializingInputStream.appendFreeSegment("package");
        serializingInputStream.append("""
                import java.io.ByteArrayInputStream;
                import java.io.FileInputStream;
                import java.io.IOException;
                import java.nio.ByteBuffer;
                import java.util.ArrayList;

                public class SerializingInputStream extends ByteArrayInputStream {
                    public SerializingInputStream(byte[] buf) { super(buf); }
                    public SerializingInputStream(byte[] buf, int offset, int length) { super(buf, offset, length); }
                    public SerializingInputStream(FileInputStream fis) throws IOException {
                        super(fis.readAllBytes());
                    }

                    public int readInt() throws InvalidStreamLengthException { // not thread safe rn
                        if(available() < 4) {
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type int");
                        }
                        byte[] buf = new byte[4];
                        read(buf, 0, 4); // since we already checked size, this shouldn't ever fail; if it does, good luck :)

                        int val = 0;
                        for(int i = 0; i < 4; i++) {
                            val <<= 8;
                            val |= buf[i];
                        }
                        return val;
                    }

                    public long readLong() throws InvalidStreamLengthException { // not thread safe rn
                        if(available() < 8) {
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
                        }
                        byte[] buf = new byte[8];
                        read(buf, 0, 8); // since we already checked size, this shouldn't ever fail; if it does, good luck :)

                        int val = 0;
                        for(int i = 0; i < 8; i++) {
                            val <<= 8;
                            val |= buf[0];
                        }
                        return val;
                    }

                    public double readDouble() throws InvalidStreamLengthException { // not thread safe rn
                        if(available() < 8) {
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
                        }
                        byte[] buf = new byte[8];
                        read(buf, 0, 8); // since we already checked size, this shouldn't ever fail; if it does, good luck :)
                        return ByteBuffer.wrap(buf).getDouble();
                    }

                    public float readFloat() throws InvalidStreamLengthException { // not thread safe rn
                        if(available() < 4) {
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type long");
                        }
                        byte[] buf = new byte[4];
                        read(buf, 0, 4); // since we already checked size, this shouldn't ever fail; if it does, good luck :)
                        return ByteBuffer.wrap(buf).getFloat();
                    }

                    public String readString() throws InvalidStreamLengthException {
                        int len = readInt();
                        StringBuilder sb = new StringBuilder();
                        if(available() < len){
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type string with len " + len);
                        }
                        for(int i = 0; i < len; i++){
                            sb.append((char)read());
                        }
                        return sb.toString();
                    }

                    public boolean readBoolean() throws InvalidStreamLengthException {
                        if(available() < 0){
                            throw new InvalidStreamLengthException("Stream of "+available()+" bytes is invalid for type boolean");
                        }
                        return read() == 0x01;
                    }

                    public <T> ArrayList<T> readArrayList(Deserializer<T> deserializer) throws InvalidStreamLengthException {
                        ArrayList<T> arrayList = new ArrayList<>();
                        int len = readInt();
                        for(int i = 0; i < len; i++){
                            arrayList.add(deserializer.deserialize(this));
                        }
                        return arrayList;
                    }

                    public class InvalidStreamLengthException extends Exception {
                        InvalidStreamLengthException(String msg){
                            super(msg);
                        }
                    }
                }""");
        artifacts.add(serializingInputStream);

        Artifact serializingOutputStream = new Artifact("util/serial/SerializingOutputStream.java");
        serializingOutputStream.appendFreeSegment("package");
        serializingOutputStream.append("""
                import java.io.ByteArrayOutputStream;
                import java.nio.ByteBuffer;
                import java.util.ArrayList;

                public class SerializingOutputStream extends ByteArrayOutputStream {

                    public SerializingOutputStream() { super(); }
                    public SerializingOutputStream(int length) { super(length); }

                    public void writeInt(int val) {
                        byte[] buf = new byte[4];

                        for(int i = 4; i != 0; i--) {
                            buf[i-1] = (byte) (val & 0xFF);
                            val <<= 8;
                        }
                        writeBytes(buf);
                    }

                    public void writeLong(long val) {
                        byte[] buf = new byte[8];

                        for(int i = 8; i != 0; i--) {
                            buf[i-1] = (byte) (val & 0xFF);
                            val <<= 8;
                        }
                        writeBytes(buf);
                    }

                    public void writeDouble(double val) {
                        ByteBuffer bb = ByteBuffer.allocate(8);
                        bb.putDouble(val);
                        writeBytes(bb.array());
                    }

                    public void writeFloat(float val) {
                        ByteBuffer bb = ByteBuffer.allocate(4);
                        bb.putFloat(val);
                        writeBytes(bb.array());
                    }

                    public void writeString(String s){
                        writeInt(s.length());
                        writeBytes(s.getBytes());
                    }

                    public void writeBoolean(boolean b){
                        write(b ? 0x01 : 0x00);
                    }

                    public <T extends Serializable> void writeArrayList(ArrayList<T> array){
                        writeInt(array.size());
                        for(Serializable element : array){
                            element.serialize(this);
                        }
                    }

                }""");
        artifacts.add(serializingOutputStream);
    }
}
