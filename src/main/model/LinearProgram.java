package model;

import java.util.ArrayList;

// Represents a linear program with an objective function and a list of constraints

// INVARIANT: numVariables = objectiveFunction.numVariables = constraint.numVariables for each
// constraint in constraints
public class LinearProgram {
    private int numVariables;
    private ArrayList<Constraint> constraints;
    private ObjectiveFunction objectiveFunction;

    // REQUIRES: n >= 1
    // EFFECTS: creates a linear program with n variables,
    // empty list of constraints, and objective function of zeroes
    public LinearProgram(int n) {
        numVariables = n;
        constraints = new ArrayList<Constraint>();
        objectiveFunction = new ObjectiveFunction(n);
    }

    // REQUIRES: objF.getNumVariables() = getNumVariables()
    // MODIFIES: this
    // EFFECTS: sets the objective function to objF
    public void setObjF(ObjectiveFunction objF) {
        objectiveFunction = objF;
    }

    // REQUIRES: constraint.getNumVariables() = getNumVariables()
    // MODIFIES: this
    // EFFECTS: adds a given constraint to the list of constraints
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
    }

    // REQUIRES: 1 <= i <= constraints.size()
    // MODIFIES: this
    // EFFECTS: deletes a constraint at index i in constraints in 1-based indexing
    public void deleteConstraint(int i) {
        constraints.remove(i - 1);
    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns whether the given solution satisfies all constraints
    public boolean checkFeasible(double[] solution) {

        for (double num : solution) {
            if (num < 0) {
                return false;
            }
        }

        for (Constraint c : constraints) {
            if (!c.isFeasible(solution)) {
                return false;
            }
        }

        return true;
    }

    // REQUIRES: solution.length = numVariables
    // EFFECTS: returns the value of the solution when inputted into the
    // objective function
    public double getObjValue(double[] solution) {
        return objectiveFunction.computeValue(solution);
    }

    // EFFECTS: returns a list of strings, one for each added constraint, and one
    // additional string that
    // represents the non-negativity constraints on each variable as the final item
    // in the output list
    public ArrayList<String> constraintsToStrings() {
        ArrayList<String> output = new ArrayList<String>();
        String impliedConstraintsString = "";

        for (int i = 1; i <= constraints.size(); i++) {
            String thisConstraintString = String.format("(%d) ", i);
            thisConstraintString += constraints.get(i - 1).toString();
            output.add(thisConstraintString);
        }

        for (int i = 1; i <= numVariables; i++) {
            if (i != numVariables) {
                impliedConstraintsString += String.format("x_%d, ", i);
            } else {
                impliedConstraintsString += String.format("x_%d", i);
            }

        }

        impliedConstraintsString += " >= 0";
        output.add(impliedConstraintsString);

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
