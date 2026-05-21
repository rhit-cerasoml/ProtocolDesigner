package protocoldesigner.app.generators.precompiler.dataobject.types;

import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

public class ReferenceContext implements Serializable {


    @Override
    public void serialize(SerializingOutputStream out) {

    }

    // Deserializer
    public ReferenceContext(SerializingInputStream in){

    }
}
