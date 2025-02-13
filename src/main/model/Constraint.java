package model;

// Represents a inequality constraint consisting of a linear combination
// of arbitrarily valued variables each multiplied by a coefficient <= a constant term
// ie, ax_1 + bx_2 + ... <= C

// INVARIANT: coefficients.length = numVariables
public class Constraint {
    private double constantTerm;
    private double[] coefficients;
    private int numVariables;

    // REQUIRES: n >= 1
    // EFFECTS: creates a constraint with n variables (and thus n coefficients)
    // where all coefficients and the constantTerm are zeroes
    public Constraint(int n) {

    }

    // REQUIRES: c >= 0
    // MODIFIES: this
    // EFFECTS: sets the constant term to c
    public void setConstantTerm(double c) {

    }

    // REQUIRES: coeffs.length = numVariables
    // MODIFIES: this
    // EFFECTS: sets the coefficients to coeffs
    public void setCoefficients(double[] coeffs) {

    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns true if a given solution satisfies the constraint,
    // returns false otherwise
    public boolean isFeasible(double[] solution) {
        return false;
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
