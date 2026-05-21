package protocoldesigner.app.model.uidata.welcome;

import protocoldesigner.library.util.OptionalString;
import protocoldesigner.library.util.serial.*;

import java.util.ArrayList;

public class WelcomeMenuData implements Serializable {

    public OptionalString fileSelectorLastLocation = new OptionalString();
    public ArrayList<String> projectHistory = new ArrayList<>();

    public WelcomeMenuData(){
        
    }

    public WelcomeMenuData(SerializingInputStream in) throws SerializingInputStream.InvalidStreamLengthException {
        fileSelectorLastLocation = in.readObj(OptionalString::new);
        projectHistory = in.readArrayList(Deserializers.STRING_DESERIALIZER);
    }

    @Override
    public void serialize(SerializingOutputStream out) {
        out.writeObj(fileSelectorLastLocation);
        out.writeArrayList(projectHistory, Serializers.STRING_SERIALIZER);
    }
}
