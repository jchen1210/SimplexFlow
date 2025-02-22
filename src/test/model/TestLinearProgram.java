package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestLinearProgram {
    private LinearProgram lp2;
    private Constraint testC1;
    private Constraint testC2;
    private Constraint testC3;
    private double delta;
    
    @BeforeEach
    public void runBefore() {
        lp2 = new LinearProgram(2);
        testC1 = new Constraint(2);
        testC2 = new Constraint(2);
        testC3 = new Constraint(2);
        delta = 0.0001; // allowable difference for floating point comparisons
    }

    @Test
    public void testConstructor() {
        assertEquals(2, lp2.getNumVariables());

        ObjectiveFunction testObjF = lp2.getObjF();
        double[] twoZeroes = new double[2];
        assertEquals(2, testObjF.getNumVariables());
        assertArrayEquals(twoZeroes, testObjF.getCoefficients(), delta);
        assertEquals(0, testObjF.getConstantTerm(), delta);

        assertEquals(0, lp2.getConstraints().size());
    }

    @Test
    public void setObjF() {
        ObjectiveFunction testObjF = new ObjectiveFunction(2);
        double[] testCoeffs = {1.14, -0.5};
        testObjF.setCoefficients(testCoeffs);
        testObjF.setConstantTerm(1);
        lp2.setObjF(testObjF);

        assertEquals(testObjF, lp2.getObjF());
    }

    @Test
    public void addOneConstraint() {
        double[] testCoeffs1 = {5, -5};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        lp2.addConstraint(testC1);

        assertEquals(1, lp2.getConstraints().size());
        assertEquals(testC1, lp2.getConstraints().get(0));
    }

    @Test
    public void addTwoConstraint() {
        double[] testCoeffs1 = {5, -5};
        double[] testCoeffs2 = {3, 6};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        testC2.setCoefficients(testCoeffs2);
        testC2.setConstantTerm(20);
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);

        assertEquals(2, lp2.getConstraints().size());
        assertEquals(testC1, lp2.getConstraints().get(0));
        assertEquals(testC2, lp2.getConstraints().get(1));
    }

    @Test
    public void deleteConstraint() {
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);
        lp2.addConstraint(testC3);

        assertEquals(3, lp2.getConstraints().size());
        assertEquals(testC1, lp2.getConstraints().get(0));
        assertEquals(testC2, lp2.getConstraints().get(1));
        assertEquals(testC3, lp2.getConstraints().get(2));

        lp2.deleteConstraint(2);

        assertEquals(2, lp2.getConstraints().size());
        assertEquals(testC1, lp2.getConstraints().get(0));
        assertEquals(testC3, lp2.getConstraints().get(1));
    }

    @Test
    public void getObjValue() {
        ObjectiveFunction testObjF = new ObjectiveFunction(2);
        double[] testCoeffs = {2, -0.5};
        testObjF.setCoefficients(testCoeffs);
        testObjF.setConstantTerm(10);
        lp2.setObjF(testObjF);

        double[] testSoln = {10, 10};
        assertEquals(testObjF.computeValue(testSoln), lp2.getObjValue(testSoln), delta);
    }

    @Test
    public void checkFeasibleTrue() {
        double[] testCoeffs1 = {5, -5};
        double[] testCoeffs2 = {3, 6};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        testC2.setCoefficients(testCoeffs2);
        testC2.setConstantTerm(20);
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);

        double[] testSoln = {0,0};
        assertTrue(lp2.checkFeasible(testSoln));
    }

    @Test
    public void checkFeasibleFalseConstraints() {
        double[] testCoeffs1 = {5, -5};
        double[] testCoeffs2 = {3, 6};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        testC2.setCoefficients(testCoeffs2);
        testC2.setConstantTerm(20);
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);

        double[] testSoln = {10,5};
        assertFalse(lp2.checkFeasible(testSoln));
    }

    @Test
    public void checkFeasibleFalseNonNegativity() {
        double[] testCoeffs1 = {5, -5};
        double[] testCoeffs2 = {3, 6};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        testC2.setCoefficients(testCoeffs2);
        testC2.setConstantTerm(20);
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);

        double[] testSoln = {1, -1};
        assertFalse(lp2.checkFeasible(testSoln));
    }

    @Test
    public void constraintsToStrings() {
        double[] testCoeffs1 = {5, -5};
        double[] testCoeffs2 = {3, 6};
        testC1.setCoefficients(testCoeffs1);
        testC1.setConstantTerm(1);
        testC2.setCoefficients(testCoeffs2);
        testC2.setConstantTerm(20);
        lp2.addConstraint(testC1);
        lp2.addConstraint(testC2);

        ArrayList<String> testOutput = lp2.constraintsToStrings();
        assertEquals(3, testOutput.size());
        assertEquals("(1) 5.00*x_1 - 5.00*x_2 <= 1.00", testOutput.get(0));
        assertEquals("(2) 3.00*x_1 + 6.00*x_2 <= 20.00", testOutput.get(1));
        assertEquals("x_1, x_2 >= 0", testOutput.get(2));
    }
}