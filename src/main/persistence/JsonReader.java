package persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

import model.Constraint;
import model.LinearProgram;
import model.ObjectiveFunction;
import model.SolutionState;

// Represents a reader that reads SolutionState or LinearProgram from JSON data stored in file
// The structure of this class can be attributed to the provided JsonSerializationDemo
public class JsonReader {
    private String source;

    // MODIFIES: this
    // EFFECTS: constructs a reader to read JSON data from a source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads SolutionState data from file and throws IOException if
    // an error occurs while reading
    public SolutionState readSS() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSolutionState(jsonObject);
    }

    // EFFECTS: reads LinearProgram data from file and throws IOException if
    // an error occurs while reading
    public LinearProgram readLP() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseLinearProgram(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses LP from JSON Object and returns it
    private LinearProgram parseLinearProgram(JSONObject json) {
        int numVariables = json.getInt("numVariables");
        LinearProgram lp = new LinearProgram(numVariables);
        addConstraints(lp, json);
        setObjF(lp, json);

        return lp;
    }

    // MODIFIES: lp
    // EFFECTS: parses constraints from JSON Object and adds to LP
    private void addConstraints(LinearProgram lp, JSONObject json) {
        JSONArray jsonArray = json.getJSONArray("constraints");
        for (Object jsonObject : jsonArray) {
            JSONObject nextJsonConstraint = (JSONObject) jsonObject;
            addConstraint(lp, nextJsonConstraint);
        }
    }

    // MODIFIES: lp
    // EFFECTS: parses constraint from JSON Object and adds to LP
    private void addConstraint(LinearProgram lp, JSONObject json) {
        JSONObject jsonObjF = json.getJSONObject("objectiveFunction");

        int numVariables = jsonObjF.getInt("numVariables");
        double constantTerm = jsonObjF.getDouble("constantTerm");
        JSONArray jsonCoeffs = jsonObjF.getJSONArray("coefficients");

        double[] coeffs = new double[jsonCoeffs.length()];
        for (int i = 0; i < jsonCoeffs.length(); i++) {
            coeffs[i] = jsonCoeffs.getDouble(i);
        }

        ObjectiveFunction objF = new ObjectiveFunction(numVariables);
        objF.setCoefficients(coeffs);
        objF.setConstantTerm(constantTerm);
        
        lp.setObjF(objF);
    }

    // MODIFIES: lp
    // EFFECTS: parses ObjectiveFunction from JSON Object and sets it in LP
    private void setObjF(LinearProgram lp, JSONObject json) {
        int numVariables = json.getInt("numVariables");
        double constantTerm = json.getDouble("constantTerm");
        JSONArray jsonCoeffs = json.getJSONArray("coefficients");

        double[] coeffs = new double[jsonCoeffs.length()];
        for (int i = 0; i < jsonCoeffs.length(); i++) {
            coeffs[i] = jsonCoeffs.getDouble(i);
        }

        Constraint c = new Constraint(numVariables);
        c.setCoefficients(coeffs);
        c.setConstantTerm(constantTerm);
        
        lp.addConstraint(c);
    }

    // EFFECTS: parses SolutionState from JSON Object and returns it
    private SolutionState parseSolutionState(JSONObject json) {
        int numVariables = json.getInt("numVariables");
        int numConstraints = json.getInt("numConstraints");
        SolutionState ss = new SolutionState(numVariables, numConstraints);

        addPrevPivots(ss, json);
        addPrevTableaus(ss, json);
        ss.setTableau(parseTableau(json.getJSONArray("tableau")));

        return ss;
    }

    // MODIFIES: ss
    // EFFECTS: parses previous pivots from JSON Object and adds them to ss
    private void addPrevPivots(SolutionState ss, JSONObject json) {
        JSONArray jsonPrevPivots = json.getJSONArray("prevPivots");
        for (Object p : jsonPrevPivots) {
            JSONArray nextPrevPivot = (JSONArray) p;
            addPrevPivot(ss, nextPrevPivot);
        }
    }

    // MODIFIES: ss
    // EFFECTS: parses previous pivot from JSON Object and adds it to ss
    private void addPrevPivot(SolutionState ss, JSONArray jsonArray) {
        int[] pivotIndices = {jsonArray.getInt(0), jsonArray.getInt(1)};
        ss.getPrevPivots().add(pivotIndices);
    }

    // MODIFIES: ss
    // EFFECTS: parses previous tableaus from JSON Object and adds them to ss
    private void addPrevTableaus(SolutionState ss, JSONObject json) {
        JSONArray jsonPrevTableaus = json.getJSONArray("prevTableaus");
        for (Object t : jsonPrevTableaus) {
            JSONArray nextPrevTableau = (JSONArray) t;
            addPrevTableau(ss, nextPrevTableau);
        }
    }

    // MODIFIES: ss
    // EFFECTS: parses tableau from JSON Object and adds it to ss
    private void addPrevTableau(SolutionState ss, JSONArray jsonArray) {
        double[][] prevTableau = parseTableau(jsonArray);
        ss.getPrevTableaus().add(prevTableau);
    }

    // EFFECTS: parses tableau from JSON Object and returns it
    private double[][] parseTableau(JSONArray jsonArray) {

        double[][] tableau = new double[jsonArray.length()][];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONArray rowArray = jsonArray.getJSONArray(i);
            tableau[i] = new double[rowArray.length()];

            // Loop through each element in the row
            for (int j = 0; j < rowArray.length(); j++) {
                tableau[i][j] = rowArray.getDouble(j);
            }
        }
        return tableau;
    }

}
