package pd.pool;

import pd.net.protocol.Protocol;
import pd.net.protocol.ProtocolManager;
import pd.pool.poolcontent.PoolContent;
import pd.util.Optional;
import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.concurrent.locks.ReentrantLock;

public class Pool<Key, Value extends Serializable> {
    private final boolean owner;
    private final PoolContent<Key, Value> content;
    private final ReentrantLock queueLock = new ReentrantLock();
    private final ArrayDeque<Action> requestQueue = new ArrayDeque<>();
    private final ProtocolManager protocolManager;
    private final Protocol protocol;

    public Pool(PoolContent<Key, Value> content, ProtocolManager protocolManager, boolean owner){
        this.content = content;
        this.protocolManager = protocolManager;
        this.owner = owner;
        this.protocol = new Protocol() {
            @Override
            public void accept(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
                if(owner) {
                    queueLock.lock();
                    requestQueue.push(new Action(in));
                    queueLock.unlock();
                }else{
                    try {
                        (new Action(in)).execute();
                    }catch (IOException ignored){}
                }
            }
        };
        protocolManager.addProtocol(protocol);
        if(!owner){
            try {
                sync();
            }catch (Exception ignored){}
        }
    }

    public void processActions() throws IOException, SerializingInputStream.InvalidStreamLengthException {
        queueLock.lock();
        while(!requestQueue.isEmpty()){
            requestQueue.pop().execute();
        }
        queueLock.unlock();
    }

    private enum ActionType{
        ADD,
        REMOVE,
        SYNC,
        SYNC_RESULT
    }

    private class KeyContainer implements Serializable {
        private Key key;
        KeyContainer(Key key){
            this.key = key;
        }
        @Override
        public void serialize(SerializingOutputStream out) {
            content.serializeKey(key, out);
        }

        public KeyContainer(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            this.key = content.deserializeKey(in);
        }
    }

    private static class ContentContainer implements Serializable {
        byte[] data;
        public ContentContainer(byte[] data){
            this.data = data;
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            out.writeByteArray(data);
        }

        public ContentContainer(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            data = in.readByteArray();
        }
    }

    private class Action implements Serializable {
        private ActionType type;
        private Optional<KeyContainer> key = new Optional<KeyContainer>(KeyContainer::new);
        private Optional<Value> value = new Optional<Value>(content::deserializeValue);
        private Optional<ContentContainer> data = new Optional<>(ContentContainer::new);
        public Action(ActionType type, Key key){
            this.type = type;
            this.key.set(new KeyContainer(key));
        }

        public Action(ActionType type, Value value){
            this.type = type;
            this.value.set(value);
        }

        public Action(ActionType type, Key key, Value value){
            this.type = type;
            this.key.set(new KeyContainer(key));
            this.value.set(value);
        }

        public Action(ActionType type, byte[] content) throws SerializingInputStream.InvalidStreamLengthException {
            this.type = type;
            this.data.set(new ContentContainer(content));
        }

        public Action(ActionType type) {
            this.type = type;
        }

        @Override
        public void serialize(SerializingOutputStream out) {
            out.writeInt(type.ordinal());
            key.write(out);
            value.write(out);
            data.write(out);
        }

        // Deserialize
        private Action(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
            type = ActionType.values()[in.readInt()];
            key.read(in);
            value.read(in);
            data.read(in);
        }

        public void execute() throws IOException, SerializingInputStream.InvalidStreamLengthException {
            switch (type){
                case ADD -> resolveAdd(key.content.key, value.content);
                case REMOVE -> resolveRemove(key.content.key);
                case SYNC -> sync();
                case SYNC_RESULT -> resolveSyncResult(data.content.data);
            }
        }
    }

    private void resolveAdd(Key key, Value value) {
        content.add(key, value);
    }

    public void add(Key key, Value value) throws IOException {
        if(owner){
            resolveAdd(key, value);
        }else{
            emit(new Action(ActionType.ADD, key, value));
        }
    }

    private void resolveRemove(Key key){
        content.remove(key);
    }

    public void remove(Key key) throws IOException {
        if(owner){
            resolveRemove(key);
        }else{
            emit(new Action(ActionType.REMOVE, key));
        }
    }

    public Key find(Value value) {
        return content.find(value);
    }

    public Value get(Key key) {
        return content.get(key);
    }

    public void sync() throws IOException, SerializingInputStream.InvalidStreamLengthException {
        if(owner){
            SerializingOutputStream out = new SerializingOutputStream();
            content.serialize(out);
            emit(new Action(ActionType.SYNC_RESULT, out.toByteArray()));
        }else{
            emit(new Action(ActionType.SYNC));
        }
    }

    private void resolveSyncResult(byte[] data) throws SerializingInputStream.InvalidStreamLengthException {
        SerializingInputStream in = new SerializingInputStream(data);
        content.deserialize(in);
    }

    private void emit(Action action) throws IOException {
        SerializingOutputStream out = protocolManager.getPacketStub(protocol);
        action.serialize(out);
        protocolManager.sendPacket(out);
    }
}
