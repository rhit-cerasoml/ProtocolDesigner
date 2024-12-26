package pd.util.serial;

public interface Deserializer<T> {
    T deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
}
