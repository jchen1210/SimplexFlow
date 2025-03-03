package persistence;

import org.json.JSONObject;

// Represents an object that can be written as a JSON Object
// This is taken from the provided JsonSerializationDemo
public interface Writeable {
    // EFFECTS: returns this as a JSON Object
    JSONObject toJson();
}
