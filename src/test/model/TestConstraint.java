package model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestConstraint {
    private Constraint c1;
    private Constraint c2;
    private double delta;

    @BeforeEach
    public void runBefore() {
        c1 = new Constraint(2);
        c2 = new Constraint(5);
        delta = 0.0001; // allowable difference for floating point comparisons
    }

    @Test
    public void testConstructor() {
        assertEquals(2, c1.getNumVariables());
        assertEquals(0, c1.getConstantTerm(), delta);
        double[] twoZeroes = { 0, 0 };
        assertArrayEquals(twoZeroes, c1.getCoefficients(), delta);
    }

    @Test
    public void testSetConstantTerm() {
        c1.setConstantTerm(3.14);
        assertEquals(3.14, c1.getConstantTerm(), delta);
    }

    @Test
    public void testSetCoefficients() {
        double[] testCoeffs = { 0.123, -59 };
        c1.setCoefficients(testCoeffs);
        assertArrayEquals(testCoeffs, c1.getCoefficients(), delta);
    }

    @Test
    public void testIsFeasibleFalse() {
        double[] testCoeffs = { 3, 6 };
        double[] testSoln = { 30, 40 };
        c1.setCoefficients(testCoeffs);
        c1.setConstantTerm(100);
        assertFalse(c1.isFeasible(testSoln));
    }

    @Test
    public void testIsFeasibleTrue() {
        double[] testCoeffs = { 3, 6 };
        double[] testSoln = { 0, 0 };
        c1.setCoefficients(testCoeffs);
        c1.setConstantTerm(100);
        assertTrue(c1.isFeasible(testSoln));
    }

    @Test
    public void testIsFeasibleBoundaryTrue() {
        double[] testCoeffs = { 3, 6 };
        double[] testSoln = { 20, 10 };
        c1.setCoefficients(testCoeffs);
        c1.setConstantTerm(120);
        assertTrue(c1.isFeasible(testSoln));
    }

    @Test
    public void testToString() {
        double[] testCoeffs = { -3, 3, -1, 0, 0.14 };
        c2.setCoefficients(testCoeffs);
        c2.setConstantTerm(5);
        assertEquals("-3.00*x_1 + 3.00*x_2 - 1.00*x_3 + 0.00*x_4 + 0.14*x_5 <= 5.00", c2.toString());
    }
}