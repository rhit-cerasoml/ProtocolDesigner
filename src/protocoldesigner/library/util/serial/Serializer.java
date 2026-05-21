package protocoldesigner.library.util.serial;

public interface Serializer<T> {
    void serialize(T item, SerializingOutputStream out);
}
