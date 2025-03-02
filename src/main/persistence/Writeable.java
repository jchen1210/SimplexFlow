package persistence;

import org.json.JSONObject;

public interface Writeable {
    // EFFECTS: returns this as a JSON Object
    JSONObject toJson();
}
