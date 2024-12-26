package pd.util;

public class OptionalString {
    public String s = "";
    public boolean valid = false;
    public void set(String s){
        this.s = s;
        this.valid = true;
    }
    public void invalidate(){
        this.s = "";
        this.valid = false;
    }
    public void write(SerializingOutputStream out){
        if(valid){
            out.writeBoolean(true);
            out.writeString(s);
        }else{
            out.writeBoolean(false);
        }
    }
    public void read(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
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
