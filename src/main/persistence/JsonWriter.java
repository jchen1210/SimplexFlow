package persistence;

import java.io.*;

import model.LinearProgram;
import model.SolutionState;

public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs a JsonWriter that writes to a file destination
    public JsonWriter(String destination) {

    }

    // MODIFIES: this
    // EFFECTS: opens a JSON writer, throws FileNotFoundException if destination file
    // cannot be opened for writing
    public void open() throws FileNotFoundException {

    }


    // MODIFIES: this
    // EFFECTS: writes a JSON representation of a SolutionState to file
    public void write(SolutionState ss) {

    }

    // MODIFIES: this
    // EFFECTS: writes a JSON representation of a LinearProgram to file
    public void write(LinearProgram lp) {

    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {

    }

    // MODIFIES: this
    // EFFECTS: writes a string to file
    private void saveToFile(String json) {

    }
}
