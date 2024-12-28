package pd.pool;

import pd.util.serial.Serializable;

import java.util.ArrayDeque;

public class Pool<Key, Value> {
    PoolContent<Key, Value> content;
    ArrayDeque<Action> requestQueue;



    private static class Action<Key, Value> {
        
    }
}
