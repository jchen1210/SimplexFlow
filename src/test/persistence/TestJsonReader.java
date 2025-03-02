package persistence;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import model.LinearProgram;
import model.SolutionState;

public class TestJsonReader {
    private double delta = 0.0001;

    @Test
    public void testReadSSNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            SolutionState ss = reader.readSS();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadLPNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            LinearProgram lp = reader.readLP();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadLP() {
        JsonReader reader = new JsonReader("./data/testJsonLP.json");
        try {
            LinearProgram outputLP = reader.readLP();

            double[][] expectedConstraintCoeffMatrix = {
                    { -1, 4, 2 },
                    { 1, 0, 1 },
                    { 3, 2, 1 }
            };
            double[] expectedObjFCoeffs = { -3, 4, 0.5 };

            assertEquals(3, outputLP.getNumVariables());
            assertEquals(3, outputLP.getConstraints().size());
            assertArrayEquals(expectedConstraintCoeffMatrix[0], outputLP.getConstraints().get(0).getCoefficients(),
                    delta);
            assertEquals(2, outputLP.getConstraints().get(0).getConstantTerm(), delta);

            assertArrayEquals(expectedConstraintCoeffMatrix[1], outputLP.getConstraints().get(1).getCoefficients(),
                    delta);
            assertEquals(8, outputLP.getConstraints().get(1).getConstantTerm(), delta);

            assertArrayEquals(expectedConstraintCoeffMatrix[2], outputLP.getConstraints().get(2).getCoefficients(),
                    delta);
            assertEquals(9, outputLP.getConstraints().get(2).getConstantTerm(), delta);

            assertArrayEquals(expectedObjFCoeffs, outputLP.getObjF().getCoefficients(), delta);
            assertEquals(3, outputLP.getObjF().getConstantTerm());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    public void testReadSS() {
        JsonReader reader = new JsonReader("./data/testJsonSS.json");
        try {
            SolutionState ssOutput = reader.readSS();

            double[][] expectedPrevTableau = {
                    { 0, 3.0 / 2, 1, -1.0 / 2, 2 },
                    { 1, 1.0 / 2, 0, 1.0 / 2, 4 },
                    { 0, 1.0 / 2, 0, -3.0 / 2, -12 }
            };
            double[][] expectedTableau = {
                    { 0, 1, 2.0 / 3, -1.0 / 3, 4.0 / 3 },
                    { 1, 0, -1.0 / 3, 2.0 / 3, 10.0 / 3 },
                    { 0, 0, -1.0 / 3, -4.0 / 3, -38.0 / 3 }
            };
            int[] expectedPrevPivot = { 2, 1 };

            assertEquals(2, ssOutput.getNumVariables());
            assertEquals(2, ssOutput.getNumConstraints());
            assertTrue(Arrays.deepEquals(expectedTableau, ssOutput.getTableau()));

            assertEquals(1, ssOutput.getPrevPivots().size());
            assertArrayEquals(expectedPrevPivot, ssOutput.getPrevPivots().get(0));

            assertEquals(1, ssOutput.getPrevTableaus().size());
            assertTrue(Arrays.deepEquals(expectedPrevTableau, ssOutput.getPrevTableaus().get(0)));

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
