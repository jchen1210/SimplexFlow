package model;

import java.util.Arrays;

import org.json.JSONObject;
import persistence.Writeable;

// Represents a inequality constraint consisting of a linear combination
// of arbitrarily valued variables each multiplied by a coefficient <= a constant term
// ie, ax_1 + bx_2 + ... <= C

// INVARIANT: coefficients.length = numVariables
public class Constraint implements Writeable {
    private double constantTerm;
    private double[] coefficients;
    private int numVariables;

    // MODIFIES: this, eventLog
    // REQUIRES: n >= 1
    // EFFECTS: creates a constraint with n variables (and thus n coefficients)
    // where all coefficients and the constantTerm are zeroes
    public Constraint(int n) {
        numVariables = n;
        coefficients = new double[n];
        constantTerm = 0;
        EventLog.getInstance().logEvent(new Event("Created constraint with " + numVariables + " variables."));
    }

    // REQUIRES: c >= 0
    // MODIFIES: this, eventLog
    // EFFECTS: sets the constant term to c
    public void setConstantTerm(double c) {
        constantTerm = c;
        EventLog.getInstance().logEvent(new Event("Set constant term to " + c));
    }

    // REQUIRES: coeffs.length = numVariables
    // MODIFIES: this, eventLog
    // EFFECTS: sets the coefficients to coeffs
    public void setCoefficients(double[] coeffs) {
        coefficients = coeffs;
        EventLog.getInstance().logEvent(new Event("Set coefficients to " + Arrays.toString(coeffs)));
    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns true if a given solution satisfies the constraint,
    // returns false otherwise
    public boolean isFeasible(double[] solution) {
        double value = 0;
        for (int i = 0; i < solution.length; i++) {
            value += solution[i] * coefficients[i];
        }
        return value <= constantTerm;
    }

    // EFFECTS: returns a string representation of constraint
    public String toString() {
        String output = "";
        boolean isFirst = true;

        for (int i = 0; i < numVariables; i++) {
            double currentCoeff = coefficients[i];

            if (!isFirst) {
                if (currentCoeff >= 0) {
                    output += " + ";
                } else {
                    output += " - ";
                }
                output += String.format("%.2f*x_%d", Math.abs(currentCoeff), i + 1);
            } else {
                output += String.format("%.2f*x_%d", currentCoeff, i + 1);
                isFirst = false;
            }

        }
        output += String.format(" <= %.2f", Math.abs(constantTerm));
        return output;
    }

    @Override
    // EFFECTS: returns this as a JSON Object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("numVariables", numVariables);
        json.put("coefficients", coefficients);
        json.put("constantTerm", constantTerm);

        return json;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public double getConstantTerm() {
        return constantTerm;
    }

    public int getNumVariables() {
        return numVariables;
    }
}
