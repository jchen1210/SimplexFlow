package persistence;

import java.io.*;

import org.json.JSONObject;

import model.LinearProgram;
import model.SolutionState;

// Represents a writer that writes JSON representation of workroom to file
// The structure of this class can be attributed to the provided JsonSerializationDemo
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a JsonWriter that writes to a file destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens a JSON writer, throws FileNotFoundException if destination file
    // cannot be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }


    // MODIFIES: this
    // EFFECTS: writes a JSON representation of a SolutionState to file
    public void write(SolutionState ss) {
        JSONObject json = ss.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of a LinearProgram to file
    public void write(LinearProgram lp) {
        JSONObject json = lp.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes a string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}
