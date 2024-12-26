package pd.dataobject.types;

public interface SerializationMode {
    String getSerializationCode(String name);
    String getDeserializationCode(String name);
}
