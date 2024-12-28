package pd.pool;

import pd.util.serial.Deserializer;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.HashMap;
import java.util.Map;

public class HashMapPool<Key extends Serializable, Value extends Serializable> implements PoolContent<Key, Value> {
    HashMap<Key, Value> content = new HashMap<>();
    Deserializer<Key> keyDeserializer;
    Deserializer<Value> valueDeserializer;

    public HashMapPool(Deserializer<Key> keyDeserializer, Deserializer<Value> valueDeserializer){
        this.keyDeserializer = keyDeserializer;
        this.valueDeserializer = valueDeserializer;
    }

    @Override
    public void add(Key key, Value value) {
        content.put(key, value);
    }

    @Override
    public void remove(Key key) {
        content.remove(key);
    }

    @Override
    public Key find(Value value) {
        for(Map.Entry<Key, Value> entry : content.entrySet()){
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }

    @Override
    public Value get(Key key) {
        return content.get(key);
    }

    @Override
    public void serializeKey(Key key, SerializingOutputStream out) {
        key.serialize(out);
    }

    @Override
    public void serializeValue(Value value, SerializingOutputStream out) {
        value.serialize(out);
    }

    @Override
    public Key deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return keyDeserializer.deserialize(in);
    }

    @Override
    public Value deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return valueDeserializer.deserialize(in);
    }
}
