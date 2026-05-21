package protocoldesigner.library.util;

import protocoldesigner.library.util.serial.Serializable;
import protocoldesigner.library.util.serial.SerializingInputStream;
import protocoldesigner.library.util.serial.SerializingOutputStream;

public class OptionalString implements Serializable {
    public String s;
    public boolean valid;
    public void set(String s){
        this.s = s;
        this.valid = true;
    }
    public void invalidate(){
        this.s = "";
        this.valid = false;
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeBoolean(valid);
        if(valid){
            out.writeString(s);
        }
    }

    public OptionalString() {
        invalidate();
    }

    public OptionalString(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        this.valid = in.readBoolean();
        if(this.valid){
            this.s = in.readString();
        }
    }

    @Override
    public String toString() {
        return s;
    }
}
