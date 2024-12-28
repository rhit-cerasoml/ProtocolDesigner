package pd.pool;

import pd.util.serial.Serializable;

import java.util.ArrayList;

public class Pool {
    private ArrayList<Serializable> elements;
    public Pool(){
        elements = new ArrayList<>();
    }
}
