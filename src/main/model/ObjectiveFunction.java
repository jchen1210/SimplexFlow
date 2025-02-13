package model;

// represents a linear function of arbitrarily valued variables each multiplied
// by a coefficient with a constant term added
// ie, f(x_1,x_2,...) = ax_1 + bx_2 + ... + C
public class ObjectiveFunction {
    private int numVariables;
    private double[] coefficients;
    private double constantTerm;

    // REQUIRES: n >= 1
    // EFFECTS: creates an objective function with n variables (and thus n coefficients)
    // where all coefficients and the constantTerm are zeroes
    public ObjectiveFunction(int n) {

    }

    // MODIFIES: this
    // EFFECTS: sets the constant term to c
    public void setConstantTerm(double c) {

    }

    // REQUIRES: coeffs.length = numVariables
    // MODIFIES: this
    // EFFECTS: sets the coefficients to coeffs
    public void setCoefficients(double[] coeffs) {
        
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
