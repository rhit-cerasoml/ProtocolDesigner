package pd.pool;

import java.util.ArrayDeque;

public class Pool<Key, Value> {
    PoolContent<Key, Value> content;
    ArrayDeque<Action> requestQueue;



    private static class Action<Key, Value> {

    }
}
