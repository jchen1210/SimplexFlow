package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.util.ArrayList;

// Represents a step in the solution of a linear program under the simplex algorithm.
// Note that the tableau used in this representation begins with an un-negated OBF row

// I warn that this class and its methods rely VERY heavily on theory from linear algebra
// and linear optimization, and it is very difficult to make all of it clear to an unfamiliar
// reader. However, I do my best to illustrate some more important points.

// INVARIANT: tableau will always have (numVariables + numConstraints + 1) columns
//            and (numConstraints + 1) rows.
//            Each entry in prevPivots has length exactly 2
//            All entries in prevTableaus obey the tableau invariant

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

public class SolutionState implements Writeable{
    private int numVariables;
    private int numConstraints;
    private double[][] tableau;
    private ArrayList<double[][]> prevTableaus;
    private ArrayList<int[]> prevPivots;

    // MODIFIES: this
    // EFFECTS: produces a solution state corresponding to the inputted lp with
    // no prior tableaus or pivots and:
    // numVariables = lp.getNumVariables() &
    // numConstraints = lp.constraints.size()

    // INVARIANT: the tableau is constructed from the constraints & objective
    // function of the inputted LP
    // according to the class specification, with a m * m identity matrix attached
    // (this is necessary for the algorithm)

    public SolutionState(LinearProgram lp) {
        numVariables = lp.getNumVariables();
        numConstraints = lp.getConstraints().size();
        prevPivots = new ArrayList<int[]>();
        prevTableaus = new ArrayList<double[][]>();
        tableau = convertToTableau(lp);
    }

    // MODIFIES: this
    // EFFECTS: produces a solution state with n variables and m constraints, zero tableau
    // of appropriate dimensions, with no previous tableaus or pivots
    public SolutionState(int n, int m) {
        numVariables = n;
        numConstraints = m;
        prevPivots = new ArrayList<int[]>();
        prevTableaus = new ArrayList<double[][]>();
        tableau = new double[m + 1][n + m + 1];
    }

    // EFFECTS: produces a simplex tableau from a given Linear Program with
    // numConstraints + 1 rows, numVariables + numConstraints + 1 columns
    // according to the class invariant
    public static double[][] convertToTableau(LinearProgram lp) {
        int n = lp.getNumVariables();
        int m = lp.getConstraints().size();
        double[][] output = new double[m + 1][n + m + 1];
        for (int i = 0; i < m + 1; i++) {
            for (int j = 0; j < n + m + 1; j++) {
                if (i < m) {
                    if (j < n) {
                        output[i][j] = lp.getConstraints().get(i).getCoefficients()[j];
                    } else if (j == i + n) {
                        output[i][j] = 1;
                    } else if (j == n + m) {
                        output[i][j] = lp.getConstraints().get(i).getConstantTerm();
                    }

                } else {
                    if (j < n) {
                        output[i][j] = lp.getObjF().getCoefficients()[j];
                    } else if (j == n + m) {
                        output[i][j] = -lp.getObjF().getConstantTerm();
                    }
                }
            }
        }
        return output;
    }

    // EFFECTS: returns the objective value of the current solution state
    // (this is the negation of the bottom right value in the tableau)
    public double getValue() {
        return -tableau[numConstraints][numVariables + numConstraints];
    }

    // REQUIRES: 1 <= l <= (numVariables + numConstraints + 1)
    // 1 <= k <= numConstraints + 1
    // tableau[k-1][l-1] != 0
    // MODIFIES: this
    // EFFECTS: performs a pivot operation on the a_kl entry of the simplex tableau
    // (1-based index, entering column l, exiting row k)
    // and returns a new SolutionState object corresponding to the new tableau with
    // the same
    // number of variables, constraints, and with the pivot location and current
    // tableau recorded
    public void pivot(int l, int k) {
        int[] newPivot = { l, k };
        double[][] oldTableau = copyTableau(tableau);
        prevPivots.add(newPivot);
        prevTableaus.add(oldTableau);
        double pivotEntry = oldTableau[l - 1][k - 1];
        double[][] newTableau = new double[numConstraints + 1][numVariables + numConstraints + 1];
        for (int i = 1; i <= numConstraints + 1; i++) {
            double r = oldTableau[i - 1][k - 1];
            for (int j = 1; j <= numVariables + numConstraints + 1; j++) {
                double q = oldTableau[l - 1][j - 1];
                if (i == l) {
                    newTableau[i - 1][j - 1] = q / pivotEntry;
                } else {
                    newTableau[i - 1][j - 1] = oldTableau[i - 1][j - 1] - (r * q / pivotEntry);
                }
            }
        }
        tableau = newTableau;
    }

    // EFFECTS: returns a deep copy of the input tableau with new references
    public static double[][] copyTableau(double[][] oldTableau) {
        double[][] deepTableauCopy = new double[oldTableau.length][oldTableau[0].length];
        for (int i = 0; i < oldTableau.length; i++) {
            System.arraycopy(oldTableau[i], 0, deepTableauCopy[i], 0, oldTableau[i].length);
        }
        return deepTableauCopy;
    }

    // EFFECTS: returns the column number of the highest non-negative c_i value in
    // the tableau in 1-based indexing
    // if 2 columns have the same value, return the first one
    // if all entries in the final row are <= 0, return -1
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

    // EFFECTS: returns the row number k (in 1-based indexing) of the minimum ratio
    // b_k / a_ik
    // for entering variable i (column number in 1-based indexing)
    // if 2 rows have the same value, return the first one
    // if no ratio b_k / a_ik exists as a real number or if index i is invalid for
    // the tableau, return -1
    public int minimumRatioIndex(int i) {
        int output = -1;
        double currentMin = Double.POSITIVE_INFINITY;
        for (int k = 0; k < numConstraints; k++) {
            double currentEntry = tableau[k][i - 1];
            if (currentEntry > 0) {
                double currentConstant = tableau[k][numVariables + numConstraints];
                double quotient = currentConstant / currentEntry;
                if (quotient < currentMin) {
                    currentMin = quotient;
                    output = k + 1;
                }
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

    // REQUIRES: tableau[i][numConstraints + 1] >= 0 for some i = 1, ...,
    // (numVariables + numConstraints)
    // ie, some entry in the last row of the tableau (other than the final one) is
    // non-negative
    // EFFECTS: suggests indices {i, j} (1-based, matrix indexing) for the next
    // feasible pivot operation in the simplex algorithm according to Dantzig's rule
    // (maximal coefficient)
    // if no valid pivots are found, returns {-1, -1}

    // NOTE: (this method relies VERY heavily on theory from linear optimization)
    // This rule relies on picking a column l to pivot on based on the maximum value
    // amongst the non-negative entries described in the "REQUIRES" section
    // Feasibility is ensured by picking the row k leaving from the basis by
    // computing the minimum quotient
    // b_k / a_lk over all values of k.
    public int[] suggestDantzigPivot() {
        int[] pivotLocation = { -1, -1 };
        int j = maximalCoefficientIndex();
        if (j == -1) {
            return pivotLocation;
        }
        int i = minimumRatioIndex(j);
        if (i == -1) {
            return pivotLocation;
        }
        pivotLocation[0] = i;
        pivotLocation[1] = j;
        return pivotLocation;
    }

    // REQUIRES: t.length = m + 1, t[0].length = m + n + 1
    // MODIFIES: this
    // EFFECTS: sets the tableau of a solution state to t for testing purposes.
    public void setTableau(double[][] t) {
        tableau = t;
    }

    @Override
    // EFFECTS: returns this as a JSON Object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("numVariables", numVariables);
        json.put("numConstraints", numConstraints);
        json.put("tableau", tableauToJson(tableau));
        json.put("prevTableaus", prevTableausToJson());
        json.put("prevPivots", prevPivotsToJson());

        return json;
    }

    // EFFECTS: returns the prev tableaus of this SolutionState as a JSON array
    private JSONArray prevTableausToJson() {
        JSONArray jsonArray = new JSONArray();
        for (double[][] t : prevTableaus) {
            jsonArray.put(tableauToJson(t));
        }
        return jsonArray;
    }

    // EFFECTS: returns a given tableau as a JSON array
    private JSONArray tableauToJson(double[][] t) {
        JSONArray jsonArray = new JSONArray();
        for (double[] row : t) {
            jsonArray.put(row);
        }
        return jsonArray;
    }

    // EFFECTS: returns the prevPivots of this SolutionState as a JSON array
    private JSONArray prevPivotsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (int[] p : prevPivots) {
            jsonArray.put(p);
        }
        return jsonArray;
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

    public ArrayList<double[][]> getPrevTableaus() {
        return prevTableaus;
    }

    public ArrayList<int[]> getPrevPivots() {
        return prevPivots;
    }
}
