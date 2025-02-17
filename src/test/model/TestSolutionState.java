package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestSolutionState {
    private LinearProgram lp1;
    private LinearProgram lp2;
    private Constraint c1;
    private Constraint c2;
    private Constraint c3;
    private Constraint c4;
    private ObjectiveFunction objF1;
    private SolutionState ss1;
    private SolutionState ssEmpty;
    private double delta;

    @BeforeEach
    public void runBefore() {
        delta = 0.0001;

        c1 = new Constraint(2);
        c2 = new Constraint(2);
        objF1 = new ObjectiveFunction(2);

        double[] coeffs1 = { 1, 2 };
        double[] coeffs2 = { 2, 1 };
        c1.setCoefficients(coeffs1);
        c1.setConstantTerm(6);
        c2.setCoefficients(coeffs2);
        c2.setConstantTerm(8);

        double[] coeffs3 = { 3, 2 };
        objF1.setCoefficients(coeffs3);
        objF1.setConstantTerm(1);

        lp1 = new LinearProgram(2);
        lp1.setObjF(objF1);
        lp1.addConstraint(c1);
        lp1.addConstraint(c2);

        ss1 = new SolutionState(lp1);

        lp2 = new LinearProgram(2);
        c3 = new Constraint(2);
        c4 = new Constraint(2);

        lp2.addConstraint(c3);
        lp2.addConstraint(c4);

        ssEmpty = new SolutionState(lp2);
    }

    @Test
    public void testConstructor() {
        double[][] expectedTableau = {
                { 1, 2, 1, 0, 6 },
                { 2, 1, 0, 1, 8 },
                { 3, 2, 0, 0, -1 }
        };
        assertEquals(2, ss1.getNumVariables());
        assertEquals(2, ss1.getNumConstraints());
        double[][] t = ss1.getTableau();
        assertTrue(Arrays.deepEquals(expectedTableau, t));
        assertTrue(ss1.getPrevStates().isEmpty());
        assertTrue(ss1.getPrevPivots().isEmpty());
    }

    @Test
    public void testGetValue() {
        assertEquals(1, ss1.getValue(), delta);
        assertEquals(0, ssEmpty.getValue(), delta);
    }

    @Test
    public void testMaximalCoefficientIndex() {
        assertEquals(1, ss1.maximalCoefficientIndex());
    }

    @Test
    public void testMinimumRatioIndex() {
        assertEquals(2, ss1.minimumRatioIndex(1));
        assertEquals(1, ss1.minimumRatioIndex(2));
    }

    @Test
    public void testCheckOptimalTrue() {
        double[][] optimalTableau = {
                { 0, 1, 2 / 3, -1 / 3, 4 / 3 },
                { 1, 0, -1 / 3, 2 / 3, 10 / 3 },
                { 0, 0, -1 / 3, -4 / 3, -38 / 3 }
        };
        ss1.setTableau(optimalTableau);

        assertTrue(ss1.checkOptimal());
    }

    @Test
    public void testCheckOptimalFalse() {
        assertFalse(ss1.checkOptimal());
    }

    @Test
    public void testCheckUnboundedTrue() {
        double[][] unboundedTableau = {
                { 1, -2, 1, 0, 6 },
                { 2, 0, 0, 1, 8 },
                { 3, 2, 0, 0, 0 }
        };
        ss1.setTableau(unboundedTableau);

        assertTrue(ss1.checkUnbounded());
    }

    @Test
    public void testCheckUnboundedFalse() {
        assertFalse(ss1.checkUnbounded());
    }

    @Test
    public void testSuggestDantzigPivot() {
        int[] correctPivot = { 1, 2 };
        assertArrayEquals(correctPivot, ss1.suggestDantzigPivot());
    }

    @SuppressWarnings("methodlength")
    @Test
    public void testPivot() {
        SolutionState ssP = ss1.pivot(1, 2);
        int[] pivotIndices = {1, 2};

        double[][] expectedTableau = {
                { 0, 3 / 2, 1, -1 / 2, 2 },
                { 1, 1 / 2, 0, 1 / 2, 4 },
                { 0, 1 / 2, 0, -3 / 2, -12 }
        };
        assertEquals(2, ssP.getNumVariables());
        assertEquals(2, ssP.getNumConstraints());
        assertTrue(Arrays.deepEquals(expectedTableau, ssP.getTableau()));
        assertEquals(1, ssP.getPrevPivots().size());
        assertArrayEquals(pivotIndices, ssP.getPrevPivots().get(0));
        assertEquals(1, ssP.getPrevStates().size());
        assertEquals(ss1, ssP.getPrevStates().get(0));

        double[][] oldTableau = {
                { 1, 2, 1, 0, 6 },
                { 2, 1, 0, 1, 8 },
                { 3, 2, 0, 0, -1 }
        };
        assertEquals(2, ss1.getNumVariables());
        assertEquals(2, ss1.getNumConstraints());
        assertTrue(Arrays.deepEquals(oldTableau, ss1.getTableau()));
        assertTrue(ss1.getPrevStates().isEmpty());
        assertTrue(ss1.getPrevPivots().isEmpty());
    }

    @Test
    public void testSetTableau() {
        double[][] expectedTableau = {
                { 0, 3 / 2, 1, -1 / 2, 2 },
                { 1, 1 / 2, 0, 1 / 2, 4 },
                { 0, 1 / 2, 0, -3 / 2, -12 }
        };
        ss1.setTableau(expectedTableau);

        assertTrue(Arrays.deepEquals(expectedTableau, ss1.getTableau()));
    }

}
