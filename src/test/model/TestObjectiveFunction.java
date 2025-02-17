package model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestObjectiveFunction {
    private ObjectiveFunction twoVarObjF;
    private ObjectiveFunction fiveVarObjF;
    private double delta;
    
    @BeforeEach
    public void runBefore() {
        twoVarObjF = new ObjectiveFunction(2);
        fiveVarObjF = new ObjectiveFunction(5);
        delta = 0.0001; // allowable difference for floating point comparisons
    }

    @Test
    public void testConstructor() {
        assertEquals(2, twoVarObjF.getNumVariables());
        assertEquals(0, twoVarObjF.getConstantTerm(), delta);
        double[] twoZeroes = {0,0};
        assertArrayEquals(twoZeroes, twoVarObjF.getCoefficients(), delta);
    }


    @Test
    public void testSetConstantTerm() {
        twoVarObjF.setConstantTerm(3.14);
        assertEquals(3.14, twoVarObjF.getConstantTerm(), delta);
    }

    @Test
    public void testSetCoefficients() {
        double[] testCoeffs = {0.123, -59};
        twoVarObjF.setCoefficients(testCoeffs);
        assertArrayEquals(testCoeffs, twoVarObjF.getCoefficients(), delta);
    }

    @Test
    public void testComputeValue() {
        double[] testSolution = {0, 1.5, 2, 3, 1};
        double[] testCoeffs = {100, 0, 3, -1, 0.1};
        fiveVarObjF.setCoefficients(testCoeffs);
        fiveVarObjF.setConstantTerm(1);
        double correctValue = 0*100 + 1.5*0 + 2*3 + 3*-1 + 1*0.1 + 1;

        assertEquals(correctValue, fiveVarObjF.computeValue(testSolution), delta);
    }

    @Test
    public void testToStringNonZeroCoeffsPosConstant() {
        double[] testCoeffs = {100, 0, 3, -1, 0.1};
        fiveVarObjF.setCoefficients(testCoeffs);
        fiveVarObjF.setConstantTerm(1);

        assertEquals("f = 100.00*x_1 + 0.00*x_2 + 3.00*x_3 - 1.00*x_4 + 0.10*x_5 + 1.00", fiveVarObjF.toString());
    }

    @Test
    public void testToStringNonZeroCoeffsNegConstant() {
        double[] testCoeffs = {100, 0, 3, -1, 0.1};
        fiveVarObjF.setCoefficients(testCoeffs);
        fiveVarObjF.setConstantTerm(-1);

        assertEquals("f = 100.00*x_1 + 0.00*x_2 + 3.00*x_3 - 1.00*x_4 + 0.10*x_5 - 1.00", fiveVarObjF.toString());
    }

    @Test
    public void testToStringAllZeroCoeffsPosConstant() {
        double[] testCoeffs = {0, 0, 0, 0, 0};
        fiveVarObjF.setCoefficients(testCoeffs);
        fiveVarObjF.setConstantTerm(1);

        assertEquals("f = 1.00", fiveVarObjF.toString());
    }

    @Test
    public void testToStringAllZeroCoeffsNegConstant() {
        double[] testCoeffs = {0, 0, 0, 0, 0};
        fiveVarObjF.setCoefficients(testCoeffs);
        fiveVarObjF.setConstantTerm(-1);

        assertEquals("f = -1.00", fiveVarObjF.toString());
    }
}

