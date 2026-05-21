package protocoldesigner.library.util.serial;

public class Deserializers {
    public static Deserializer<String> STRING_DESERIALIZER = SerializingInputStream::readString;
}
