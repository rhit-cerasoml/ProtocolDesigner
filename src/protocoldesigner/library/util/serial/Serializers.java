package protocoldesigner.library.util.serial;

public class Serializers {
    public static Serializer<String> STRING_SERIALIZER = (item, out) -> out.writeString(item);
}
