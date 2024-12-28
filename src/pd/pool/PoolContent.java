package pd.pool;

import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public interface PoolContent<Key, Value> {
    void add(Key key, Value value);
    void remove(Key key);
    Key find(Value value);
    Value get(Key key);

    void serializeKey(Key key, SerializingOutputStream out);
    void serializeValue(Value value, SerializingOutputStream out);
    Key deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
    Value deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
}
