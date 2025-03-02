package persistence;

import java.io.IOException;

import org.json.*;

import model.LinearProgram;
import model.SolutionState;

public class JsonReader {
    private String source;

    // MODIFIES: this
    // EFFECTS: constructs a reader to read JSON data from a source file
    public JsonReader(String source) {

    }

    // EFFECTS: reads SolutionState data from file and throws IOException if
    // an error occurs while reading
    public SolutionState read() throws IOException {
        return null;
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        return "";
    }

    // EFFECTS: parses LP from JSON Object and returns it
    private LinearProgram parseLinearProgram(JSONObject json) {
        return null;
    }

    // MODIFIES: lp
    // EFFECTS: parses constraints from JSON Object and adds to LP
    private void addConstraints(LinearProgram lp, JSONObject json) {

    }

    // MODIFIES: lp
    // EFFECTS: parses constraint from JSON Object and adds to LP
    private void addConstraint(LinearProgram lp, JSONObject json) {

    }

    // MODIFIES: lp
    // EFFECTS: parses ObjectiveFunction from JSON Object and sets it in LP
    private void setObjF(LinearProgram lp, JSONObject json) {

    }

    // EFFECTS: parses coefficient array from JSON Object and returns it
    private double[] parseCoefficients(JSONObject json) {
        return null;
    }

    // EFFECTS: parses SolutionState from JSON Object and returns it
    private SolutionState parseSolutionState(JSONObject json) {
        return null;
    }

    // MODIFIES: ss
    // EFFECTS: parses previous pivots from JSON Object and adds them to ss
    private void addPrevPivots(SolutionState ss, JSONObject json) {

    }

    // MODIFIES: ss
    // EFFECTS: parses previous pivot from JSON Object and adds it to ss
    private void addPrevPivot(SolutionState ss, JSONObject json) {

    }

    // MODIFIES: ss
    // EFFECTS: parses previous tableaus from JSON Object and adds them to ss
    private void addPrevTableaus(SolutionState ss, JSONObject json) {

    }

    // MODIFIES: ss
    // EFFECTS: parses tableau from JSON Object and adds it to ss
    private void addPrevTableau(SolutionState ss, JSONObject json) {

    }

    // EFFECTS: parses tableau from JSON Object and returns it
    private double[][] parseTableau(JSONObject json) {
        return null;
    }

}
