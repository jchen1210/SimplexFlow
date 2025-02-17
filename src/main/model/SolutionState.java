package model;

import java.util.ArrayList;

// Represents a step in the solution of a linear program under the simplex algorithm.
// Note that the tableau used in this representation begins with an un-negated OBF row

// I warn that this class and its methods rely VERY heavily on theory from linear algebra
// and linear optimization, and it is very difficult to make all of it clear to an unfamiliar
// reader. However, I do my best to illustrate some more important points.

// INVARIANT: matrix will always have (numVariables + numConstraints + 1) columns
//            and (numConstraints + 1) rows.
//            Each entry in prevPivots has length exactly 2

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
    @SuppressWarnings("methodlength")
    public SolutionState(LinearProgram lp) {
        numVariables = lp.getNumVariables();
        numConstraints = lp.getConstraints().size();
        prevPivots = new ArrayList<int[]>();
        prevStates = new ArrayList<SolutionState>();
        tableau = new double[numConstraints + 1][numVariables + numConstraints + 1];
        for (int i = 0; i < numConstraints + 1; i++) {
            for (int j = 0; j < numVariables + numConstraints + 1; j++) {
                if (i < numConstraints) {
                    if (j < numVariables) {
                        tableau[i][j] = lp.getConstraints().get(i).getCoefficients()[j];
                    } else if (j == i + numVariables) {
                        tableau[i][j] = 1;
                    } else if (j == numVariables + numConstraints) {
                        tableau[i][j] = lp.getConstraints().get(i).getConstantTerm();
                    }

                } else {
                    if (j < numVariables) {
                        tableau[i][j] = lp.getObjF().getCoefficients()[j];
                    } else if (j == numVariables + numConstraints) {
                        tableau[i][j] = -lp.getObjF().getConstantTerm();
                    }
                }
            }
        }
    }

    // EFFECTS: produces a copy of given solution state that references new objects
    public SolutionState(SolutionState ss) {
        
    }

    // EFFECTS: returns the objective value of the current solution state
    // (this is the negation of the bottom right value in the tableau)
    public double getValue() {
        return -tableau[numConstraints][numVariables + numConstraints];
    }

    // REQUIRES: 1 <= i <= (numVariables + numConstraints)
    //           1 <= j <= numConstraints
    //           tableau[i][j] != 0
    // EFFECTS: performs a pivot operation on the a_ij entry of the simplex tableau
    // and returns a new SolutionState object corresponding to the new tableau with
    // the same number of variables, constraints, and with the pivot location and current state
    // recorded
    public SolutionState pivot(int i, int j) {
        SolutionState output = this;
        return output;
    }

    // REQUIRES: some entry of the last row of the tableau is >= 0 (this is true anyways by invariant)
    // EFFECTS: returns the column number of the highest c_i value in the tableau in 1-based indexing
    // if 2 columns have the same value, return the first one
    public int maximalCoefficientIndex() {
        int output = -1;
        double currentMax = 0;
        for (int i = 0; i < tableau[0].length - 1; i++) {
            double currentEntry = tableau[numConstraints][i];
            if (currentEntry > currentMax) {
                currentMax = currentEntry;
                output = i + 1;
            }
        }
        return output;
    }

    // REQUIRES: a_ik is non-negative for some 1 <= k <= m
    // EFFECTS: returns the row number k (in 1-based indexing) of the minimum ratio b_k / a_ik
    // for entering variable i (column number in 1-based indexing)
    // if 2 rows have the same value, return the first one
    public int minimumRatioIndex(int i) {
        int output = -1;
        double currentMin = tableau[0][numVariables + numConstraints];
        for (int k = 0; k < numConstraints; k++) {
            double currentEntry = tableau[k][i - 1];
            double currentConstant = tableau[k][numVariables + numConstraints];
            double quotient = currentConstant / currentEntry;
            if (quotient < currentMin) {
                currentMin = quotient;
                output = k + 1;
            }
        }
        return output;
    }

    // EFFECTS: returns true if optimality has been reached in the current step,
    // false otherwise
    public boolean checkOptimal() {
        for (int j = 0; j < numConstraints + numVariables; j++) {
            double currentCoeff = tableau[numConstraints][j];
            if (currentCoeff > 0) {
                return false;
            }
        }
        return true;
    }

    // EFFECTS: returns true if unboundedness can be detected in the current step,
    // false otherwise
    public boolean checkUnbounded() {
        for (int j = 0; j < numConstraints + numVariables; j++) {
            boolean result = true;
            for (int i = 0; i < numConstraints; i++) {
                double currentEntry = tableau[i][j];
                if (currentEntry > 0) {
                    result = false;
                }
            }
            if (result) {
                return true;
            }
        }
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
        int i = maximalCoefficientIndex();
        int j = minimumRatioIndex(i);
        int[] pivotLocation = {i, j};
        return pivotLocation;
    }


    // REQUIRES: t.length = m + 1, t[0].length = m + n + 1
    // MODIFIES: this
    // EFFECTS: sets the tableau of a solution state to t for testing purposes.
    public void setTableau(double[][] t) {
        tableau = t;
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

    public ArrayList<SolutionState> getPrevStates() {
        return prevStates;
    }

    public ArrayList<int[]> getPrevPivots() {
        return prevPivots;
    }
}
