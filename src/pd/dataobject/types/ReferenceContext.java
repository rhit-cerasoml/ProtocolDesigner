package pd.dataobject.types;

import pd.util.serial.Serializable;
import pd.util.serial.SerializingInputStream;
import pd.util.serial.SerializingOutputStream;

public class ReferenceContext implements Serializable {


    @Override
    public void serialize(SerializingOutputStream out) {

    }

    // Deserializer
    public ReferenceContext(SerializingInputStream in){

    }
}
