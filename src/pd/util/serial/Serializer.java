package pd.util.serial;

public interface Serializer<T> {
    void serialize(T item, SerializingOutputStream out);
}
