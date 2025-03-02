package model;

import org.json.JSONObject;
import persistence.Writeable;

// represents a linear function of arbitrarily valued variables each multiplied
// by a coefficient with a constant term added
// ie, f(x_1,x_2,...) = ax_1 + bx_2 + ... + C
public class ObjectiveFunction implements Writeable {
    private int numVariables;
    private double[] coefficients;
    private double constantTerm;

    // REQUIRES: n >= 1
    // EFFECTS: creates an objective function with n variables
    // (and thus n coefficients) where all coefficients and the constantTerm are
    // zeroes
    public ObjectiveFunction(int n) {
        numVariables = n;
        coefficients = new double[n];
        constantTerm = 0;
    }

    // MODIFIES: this
    // EFFECTS: sets the constant term to c
    public void setConstantTerm(double c) {
        constantTerm = c;
    }

    // REQUIRES: coeffs.length = numVariables
    // MODIFIES: this
    // EFFECTS: sets the coefficients to coeffs
    public void setCoefficients(double[] coeffs) {
        coefficients = coeffs;
    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns the value of a given solution in the objective function
    public double computeValue(double[] solution) {
        double value = 0;
        for (int i = 0; i < solution.length; i++) {
            value += solution[i] * coefficients[i];
        }
        value += constantTerm;
        return value;
    }

    // EFFECTS: returns a string representation of objective function with precision
    // to 2 decimal places.
    public String toString() {
        String output = "f = ";

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
        if (constantTerm >= 0) {
            output += String.format(" + %.2f", constantTerm);
        } else {
            output += String.format(" - %.2f", Math.abs(constantTerm));
        }
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

    public int getNumVariables() {
        return numVariables;
    }

    public double[] getCoefficients() {
        return coefficients;
    }

    public double getConstantTerm() {
        return constantTerm;
    }
}
