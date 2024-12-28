package pd.pool;

import pd.util.serial.Deserializer;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.util.ArrayList;

public class ArrayListPool<T extends Serializable> implements PoolContent<Integer, T> {
    private ArrayList<T> content = new ArrayList<>();
    private Deserializer<T> deserializer;
    public ArrayListPool(Deserializer<T> deserializer){
        this.deserializer = deserializer;
    }

    @Override
    public void add(Integer index, T element) {
        content.add(index, element);
    }

    @Override
    public void remove(Integer index) {
        content.remove((int)index);
    }

    @Override
    public Integer find(T element) {
        return content.indexOf(element);
    }

    @Override
    public T get(Integer index) {
        return content.get(index);
    }

    @Override
    public void serializeKey(Integer index, SerializingOutputStream out) {
        out.writeInt(index);
    }

    @Override
    public void serializeValue(T element, SerializingOutputStream out) {
        element.serialize(out);
    }

    @Override
    public Integer deserializeKey(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return in.readInt();
    }

    @Override
    public T deserializeValue(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        return deserializer.deserialize(in);
    }
}
