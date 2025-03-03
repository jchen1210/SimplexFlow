package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Constraint;
import model.LinearProgram;
import model.SolutionState;
import model.ObjectiveFunction;

// The structure of the test methods can be attributed to the provided JsonSerializationDemo
public class TestJsonWriter {
    private LinearProgram lp = new LinearProgram(3);
    private SolutionState ss;
    private Constraint c1 = new Constraint(3);
    private Constraint c2 = new Constraint(3);
    private Constraint c3 = new Constraint(3);
    private ObjectiveFunction objF = new ObjectiveFunction(3);
    private double[] coeffs1 = { -1, 4, 2 };
    private double[] coeffs2 = { 1, 0, 1 };
    private double[] coeffs3 = { 3, 2, 1 };
    private double[] objCoeffs = { -3, 4, 0.5 };
    private double delta = 0.0001;

    @BeforeEach
    public void runBefore() {

        c1.setCoefficients(coeffs1);
        c1.setConstantTerm(2);

        c2.setCoefficients(coeffs2);
        c2.setConstantTerm(8);

        c3.setCoefficients(coeffs3);
        c3.setConstantTerm(9);

        objF.setCoefficients(objCoeffs);
        objF.setConstantTerm(3);

        lp.addConstraint(c1);
        lp.addConstraint(c2);
        lp.addConstraint(c3);
        lp.setObjF(objF);

        ss = new SolutionState(lp);

    }

    @Test
    public void testWriteInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriteSSNoPrevs() {
        try {

            JsonWriter writer = new JsonWriter("./data/testWriteSSNoPrevs.json");
            writer.open();
            writer.write(ss);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteSSNoPrevs.json");
            SolutionState testOutput = reader.readSS();

            double[][] expectedTableau = {
                    { -1, 4, 2, 1, 0, 0, 2 },
                    { 1, 0, 1, 0, 1, 0, 8 },
                    { 3, 2, 1, 0, 0, 1, 9 },
                    { -3, 4, 0.5, 0, 0, 0, -3 }
            };

            assertEquals(3, testOutput.getNumVariables());
            assertEquals(3, testOutput.getNumConstraints());
            assertTrue(Arrays.deepEquals(expectedTableau, testOutput.getTableau()));
            assertTrue(testOutput.getPrevPivots().isEmpty());
            assertTrue(testOutput.getPrevTableaus().isEmpty());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @SuppressWarnings("methodlength")
    @Test
    public void testWriteSSWithPrevs() {
        try {

            ss.pivot(1, 2);
            JsonWriter writer = new JsonWriter("./data/testWriteSSWithPrevs.json");
            writer.open();
            writer.write(ss);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteSSWithPrevs.json");
            SolutionState testOutput = reader.readSS();

            double[][] expectedPrevTableau = {
                    { -1, 4, 2, 1, 0, 0, 2 },
                    { 1, 0, 1, 0, 1, 0, 8 },
                    { 3, 2, 1, 0, 0, 1, 9 },
                    { -3, 4, 0.5, 0, 0, 0, -3 }
            }; 

            double[][] expectedTableau = {
                {-0.25, 1, 0.5, 0.25, 0, 0, 0.5},
                {1, 0, 1, 0, 1, 0, 8},
                {3.5, 0, 0, -0.5, 0, 1, 8},
                {-2, 0, -1.5, -1, 0, 0, -5}
            };
            int[] expectedPrevPivot = {1,2};

            assertEquals(3, testOutput.getNumVariables());
            assertEquals(3, testOutput.getNumConstraints());

            assertEquals(1, testOutput.getPrevPivots().size());
            assertArrayEquals(expectedPrevPivot, testOutput.getPrevPivots().get(0));

            assertEquals(1, testOutput.getPrevTableaus().size());
            assertTrue(Arrays.deepEquals(expectedPrevTableau, testOutput.getPrevTableaus().get(0)));

            assertTrue(Arrays.deepEquals(expectedTableau, testOutput.getTableau()));

            


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testWriteLP() {
        try {

            JsonWriter writer = new JsonWriter("./data/testWriteLP.json");
            writer.open();
            writer.write(lp);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriteLP.json");

            LinearProgram outputLP = reader.readLP();

            assertEquals(3, outputLP.getNumVariables());
            assertEquals(3, outputLP.getConstraints().size());
            assertArrayEquals(coeffs1, outputLP.getConstraints().get(0).getCoefficients(),
                    delta);
            assertEquals(2, outputLP.getConstraints().get(0).getConstantTerm(), delta);

            assertArrayEquals(coeffs2, outputLP.getConstraints().get(1).getCoefficients(),
                    delta);
            assertEquals(8, outputLP.getConstraints().get(1).getConstantTerm(), delta);

            assertArrayEquals(coeffs3, outputLP.getConstraints().get(2).getCoefficients(),
                    delta);
            assertEquals(9, outputLP.getConstraints().get(2).getConstantTerm(), delta);

            assertArrayEquals(objCoeffs, outputLP.getObjF().getCoefficients(), delta);
            assertEquals(3, outputLP.getObjF().getConstantTerm());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}
