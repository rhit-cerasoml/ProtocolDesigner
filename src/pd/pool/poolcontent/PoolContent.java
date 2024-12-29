package pd.pool.poolcontent;

import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.Map;

public interface PoolContent<Key, Value extends Serializable> extends Iterable<Map.Entry<Key, Value>> {
    void add(Key key, Value value);
    void remove(Key key);
    Key find(Value value);
    Value get(Key key);
    void update(Key key, Value value);
    int size();

    void serializeKey(Key key, SerializingOutputStream out);
    Key deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;
    Value deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;

    void serialize(SerializingOutputStream out);
    void deserialize(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException;

}
