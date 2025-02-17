package model;

import java.util.ArrayList;

// Represents a step in the solution of a linear program under the simplex algorithm.
// Note that the tableau used in this representation begins with an un-negated OBF row

// I warn that this class and its methods rely VERY heavily on theory from linear algebra
// and linear optimization, and it is very difficult to make all of it clear to an unfamiliar
// reader. However, I do my best to illustrate some more important points.

// INVARIANT: matrix will always have (numVariables + numConstraints + 1) columns
//            and (numConstraints + 1) rows

// As shorthand, let n = numVariables and m = numConstraints
// A tableau takes the following form:
// a_11, a_12, ..., a_1(m+n), b_1
// a_21, a_22, ..., a_2(m+n), b_2
// ...                        ...
// a_n1, a_n2, ..., a_n(m+n), b_n
// c_1,  c_2,  ..., c_n,      f

// the a_ij entries come from the coefficients of the constraints in the associated LP,
// the b_j entries come from the constant term in each constraint
// the c_i entries come from the coefficients of the objective function
// and the f entry comes from the NEGATION of the constant term in the objective function

public class SolutionState {
    private int numVariables;
    private int numConstraints;
    private double[][] tableau;
    private ArrayList<SolutionState> prevStates;
    private ArrayList<int[]> prevPivots;

    // EFFECTS: produces a solution state corresponding to the inputted lp with
    // no prior steps or pivots and:
    // numVariables = lp.getNumVariables() &
    // numConstraints = lp.constraints.size()
    
    // INVARIANT: the tableau is constructed from the constraints & objective function of the inputted LP
    // according to the class specification, with a m * m identity matrix attached
    // (this is necessary for the algorithm)
    public SolutionState(LinearProgram lp) {

    }

    // REQUIRES: 1 <= i <= (numVariables + numConstraints)
    //           1 <= j <= numConstraints
    //           matrix[i][j] != 0
    // MODIFIES: this
    // EFFECTS: performs a pivot operation on the a_ij entry of the simplex tableau
    // and records the current state and pivot done
    public SolutionState pivot(int i, int j) {
        return null;
    }

    // EFFECTS: returns true if optimality has been reached in the current step,
    // false otherwise
    public boolean checkOptimal() {
        return false;
    }

    // EFFECTS: returns true if unboundedness can be detected in the current step,
    // false otherwise
    public boolean checkUnbounded() {
        return false;
    }

    // REQUIRES: tableau[i][numConstraints + 1] >= 0 for some i = 1, ..., (numVariables + numConstraints)
    //           ie, some entry in the last row of the tableau (other than the final one) is non-negative
    // EFFECTS: suggests indices {i, j} for the next feasible pivot operation in the 
    // simplex algorithm according to Dantzig's rule (maximal coefficient)

    // NOTE: (this method relies VERY heavily on theory from linear optimization)
    // This rule relies on picking a column l to pivot on based on the maximum value amongst the
    // non-negative entries described in the "REQUIRES" section
    // Feasibility is ensured by picking the row k leaving from the basis by computing the minimum quotient
    // b_k / a_lk over all values of k.
    public int[] suggestDantzigPivot() {
        int[] pivotLocation = {1,1};
        return pivotLocation;
    }

    public double[][] getTableau() {
        return tableau;
    }

    public int getNumVariables() {
        return numVariables;
    }

    public int getNumConstraints() {
        return numConstraints;
    }
}
