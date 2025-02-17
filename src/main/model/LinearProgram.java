package model;

import java.util.ArrayList;

import model.Constraint;
import model.ObjectiveFunction;

// Represents a linear program with an objective function and a list of constraints

// INVARIANT: numVariables = objectiveFunction.numVariables = constraint.numVariables for each
// constraint in constraints
public class LinearProgram {
    private int numVariables;
    private ArrayList<Constraint> constraints;
    private ObjectiveFunction objectiveFunction;

    // EFFECTS: creates a linear program with n variables,
    // empty list of constraints, and objective function of zeroes
    public LinearProgram(int n) {
        
    }

    // REQUIRES: objF.getNumVariables() = getNumVariables()
    // MODIFIES: this
    // EFFECTS: sets the objective function to objF
    public void setObjF(ObjectiveFunction objF) {

    }

    // REQUIRES: constraint.getNumVariables() = getNumVariables()
    // MODIFIES: this
    // EFFECTS: adds a given constraint to the list of constraints
    public void addConstraint(Constraint constraint) {

    }

    // REQUIRES: 1 <= i <= constraints.size()
    // MODIFIES: this
    // EFFECTS: deletes a constraint at index i in constraints in 1-based indexing
    public void deleteConstraint(int i) {

    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns whether the given solution satisfies all constraints
    public boolean checkFeasible(double[] solution) {
        return false;
    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns the value of the solution when inputted into the
    // objective function
    public double getObjValue(double[] solution) {
        return 0;
    }

    // EFFECTS: returns a list of strings, one for each added constraint, and one additional string that
    // represents the non-negativity constraints on each variable as the final item in the output list
    public ArrayList<String> constraintsToStrings() {
        ArrayList<String> output = new ArrayList<String>();
        return output;
    }


    public ArrayList<Constraint> getConstraints() {
        return constraints;
    }

    public int getNumVariables() {
        return numVariables;
    }

    public ObjectiveFunction getObjF() {
        return objectiveFunction;
    }
}
