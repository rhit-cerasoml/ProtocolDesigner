package protocoldesigner.library.util.serial;

public interface Deserializer<T> {
    T deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
}
