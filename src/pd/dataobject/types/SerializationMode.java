package pd.dataobject.types;

public interface SerializationMode {
    void getSerializationCode(StringBuilder sb, String name);
    void getDeserializationCode(StringBuilder sb, String name);
    void getUpdateCode(StringBuilder sb, String name);
}
