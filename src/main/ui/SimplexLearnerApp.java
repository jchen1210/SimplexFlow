package ui;

import model.ObjectiveFunction;
import model.Constraint;
import model.LinearProgram;
import model.SolutionState;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

// Simplex learner application
// The methods relating to data persistence are attributeable to the JsonSerializationDemo provided
public class SimplexLearnerApp {
    private static final String JSON_STORE = "./data/save.json";
    private Scanner input;
    private LinearProgram lp;
    private SolutionState ss;
    private boolean solutionStage;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the Simplex learner app
    public SimplexLearnerApp() {
        solutionStage = false;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user inputs
    private void runApp() {
        boolean keepGoing = true;

        initApp();
        if (!doPromptLoad()) {
            doLinearProgramSetup();
        }

        while (keepGoing) {
            if (solutionStage) {
                displaySolutionMenu();
            } else {
                displaySetupMenu();
            }

            String command = input.next();
            input.nextLine();
            System.out.println();

            if (command.equals("q")) {
                keepGoing = false;
                doOfferSave();
            } else if (solutionStage) {
                processSolutionCommand(command);
            } else {
                processSetupCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts the user to load from saved file
    private boolean doPromptLoad() {
        System.out.println("Would you like to load from save? (y/n)");
        String command = input.nextLine();
        if (command.equals("y")) {
            try {
                loadSS();
                solutionStage = true;
                return true;
            } catch (Exception e) {
                System.out.println("No solution state saved, trying LP!");
                try {
                    loadLP();
                    return true;
                } catch (Exception x) {
                    System.out.println("Unable to read from file: " + JSON_STORE);
                    return false;
                }
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: loads SS from file
    private void loadSS() throws IOException {
        ss = jsonReader.readSS();
        System.out.println("Loaded ss from " + JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: loads LP from file
    private void loadLP() throws IOException {
        lp = jsonReader.readLP();
        System.out.println("Loaded lp from " + JSON_STORE);
    }

    // EFFECTS: prompts the user to save their current state
    private void doOfferSave() {
        System.out.println("Would you like to save? (y/n)");
        String command = input.nextLine();
        if (command.equals("y")) {
            if (solutionStage) {
                saveSS();
            } else {
                saveLP();
            }
        }
    }

    // EFFECTS: saves the LP to file
    private void saveLP() {
        try {
            jsonWriter.open();
            jsonWriter.write(lp);
            jsonWriter.close();
            System.out.println("Saved LP to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the SS to file   
    private void saveSS() {
        try {
            jsonWriter.open();
            jsonWriter.write(ss);
            jsonWriter.close();
            System.out.println("Saved solution state to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes app for user input
    private void initApp() {
        input = new Scanner(System.in);
        input.useDelimiter("\r?\n|\r");
    }

    // MODIFIES: this
    // EFFECTS: sets up the number of variables in the LP under consideration
    private void doLinearProgramSetup() {
        System.out.println("Enter the number of variables in your LP: ");
        int command = input.nextInt();
        lp = new LinearProgram(command);
        input.nextLine(); // handles the new line from pressing enter
    }

    // EFFECTS: displays the menu of setup options to console user
    private void displaySetupMenu() {
        System.out.println("\nPlease choose an option:");
        System.out.println("\tc: add constraint");
        System.out.println("\to: set objective function");
        System.out.println("\td: delete constraint");
        System.out.println("\tv: view LP");
        System.out.println("\tt: try custom solution");
        System.out.println("\ts: begin solving");
        System.out.println("\tq: quit app");

    }

    // EFFECTS: displays the menu of solution options to console user
    private void displaySolutionMenu() {
        System.out.println("\nPlease choose an option:");
        System.out.println("\tt: view tableau");
        System.out.println("\tp: pivot");
        System.out.println("\ts: suggest pivot");
        System.out.println("\tc: check current solution");
        System.out.println("\tv: view LP");
        System.out.println("\th: view solution history");
        System.out.println("\tt: try custom solution");
        System.out.println("\tq: quit app");

    }

    // MODIFIES: this
    // EFFECTS: processes user input for the LP setup menu
    private void processSetupCommand(String command) {
        if (command.equals("c")) {
            doAddConstraint();
        } else if (command.equals("o")) {
            doSetObjF();
        } else if (command.equals("d")) {
            doDeleteConstraint();
        } else if (command.equals("v")) {
            doViewLP();
        } else if (command.equals("t")) {
            doTrySolution();
        } else if (command.equals("s")) {
            doBeginSolve();
        } else {
            System.out.println("Thats not a valid command! Try again...");
        }
    }

    // MODIFIES: this
    // EFFECTS: processes user input for the LP solution menu
    private void processSolutionCommand(String command) {
        if (command.equals("t")) {
            printTableau(ss);
        } else if (command.equals("p")) {
            doPivot();
        } else if (command.equals("s")) {
            doSuggestPivot();
        } else if (command.equals("c")) {
            doCheckStatus();
        } else if (command.equals("v")) {
            doViewLP();
        } else if (command.equals("h")) {
            doShowHistory();
        } else if (command.equals("t")) {
            doTrySolution();
        } else {
            System.out.println("Thats not a valid command! Try again...");
        }
    }

    // MODIFIES: this
    // EFFECTS: prompts user to set a new objective function
    private void doSetObjF() {
        System.out.println("Enter a comma separated list for the coefficients of your objective function:");
        String coeffString = input.nextLine();
        ObjectiveFunction objF = new ObjectiveFunction(lp.getNumVariables());

        objF.setCoefficients(csvToArray(coeffString));

        System.out.println("What is the constant term of your objective function?");
        double constantTerm = input.nextDouble();
        objF.setConstantTerm(constantTerm);
        input.nextLine(); // handles the new line from pressing enter

        lp.setObjF(objF);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to add a constraint to the LP
    private void doAddConstraint() {
        System.out.println("Enter a comma separated list for the coefficients of your constraint:");
        String coeffString = input.nextLine();
        Constraint cons = new Constraint(lp.getNumVariables());

        cons.setCoefficients(csvToArray(coeffString));

        System.out.println("What is the constant term of your constraint?");
        double constantTerm = input.nextDouble();
        cons.setConstantTerm(constantTerm);
        input.nextLine(); // handles the new line from pressing enter

        lp.addConstraint(cons);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to delete a constraint from the LP
    private void doDeleteConstraint() {
        for (String cons : lp.constraintsToStrings()) {
            System.out.println(cons);
        }
        System.out.println("\nWhich constraint would you like to delete?");
        int deleteIndex = input.nextInt();
        lp.getConstraints().remove(deleteIndex - 1);
        input.nextLine();
    }

    // EFFECTS: prints out information on the current LP including OBF and
    // constraints
    private void doViewLP() {
        String objFString = lp.getObjF().toString();
        System.out.println("Maximize:\n" + objFString + "\n\nsubject to:");

        for (String cons : lp.constraintsToStrings()) {
            System.out.println(cons);
        }
    }

    // REQUIRES: lp.getConstraints().size() >= 1
    // MODIFIES: this
    // EFFECTS: converts the lp into a solution state and begins the solving stage
    private void doBeginSolve() {
        doViewLP();
        System.out.println("\nWould you like to start solving this LP? (y/n)");
        String response = input.next();
        if (response.equals("y")) {
            System.out.println("Beginning solution stage...");
            solutionStage = true;
            ss = new SolutionState(lp);
            printTableau(ss);
        } else {
            System.out.println("Returning to setup menu...");
        }
    }

    // REQUIRES: ss is not null
    // MODIFIES: this
    // EFFECTS: prompts user to perform a pivot operation on the current solution
    // state
    private void doPivot() {
        System.out.println("Which column would you like to enter into the basis?");
        int j = input.nextInt();
        input.nextLine();
        System.out.println("Which row would you like to exit into the basis?");
        int i = input.nextInt();
        input.nextLine();

        ss.pivot(i, j);
        printTableau(ss);
    }

    // REQUIRES: ss is not null
    // EFFECTS: suggests the indices for a pivot based on maximal objective
    // coefficient
    private void doSuggestPivot() {
        int[] indices = ss.suggestDantzigPivot();
        int[] invalidOutput = { -1, -1 };

        if (!Arrays.equals(indices, invalidOutput)) {
            System.out.println("Try pivoting at column " + indices[1] + ", row " + indices[0]);
        } else {
            System.out.println("No valid/nondegenerate pivots found, try checking status!");
        }
    }

    // REQUIRES: ss is not null
    // EFFECTS: prints status information on the current solution state including
    // objective value,
    // optimality, or unboundedness
    private void doCheckStatus() {
        System.out.println("Checking LP status...");
        boolean optimality = ss.checkOptimal();
        if (!optimality) {
            System.out.println("Current tableau is NOT optimal");
            System.out.println("Unboundedness detected in current tableau? " + ss.checkUnbounded());
        } else {
            System.out.printf("Current tableau is optimal with value %.4f\n", ss.getValue());
        }
    }

    // EFFECTS: prompts user to input a potential solution to check the value and
    // feasibility of
    private void doTrySolution() {
        System.out.println("Enter a comma separated list of values for your custom solution:");
        String csvString = input.nextLine();
        double[] soln = csvToArray(csvString);
        boolean feasible = lp.checkFeasible(soln);
        double value = lp.getObjValue(soln);
        String solnString = Arrays.toString(soln).replace("[", "(").replace("]", ")");
        if (feasible) {
            System.out.println("Inputted solution " + solnString + " is feasible with value " + value);
        } else {
            System.out.println("Inputted solution " + solnString + " is NOT feasible with value " + value);
        }
    }

    // EFFECTS: prints out the previous pivoting steps and tableau history from
    // first to last
    private void doShowHistory() {
        int len = ss.getPrevTableaus().size();
        if (len == 0) {
            System.out.println("No previous tableaus or pivots found, printing current tableau...");
        } else {
            System.err.println("Starting tableau:");
            for (int i = 0; i < len; i++) {
                printTableau(ss.getPrevTableaus().get(i));
                System.out.printf("\nEntered column %d, exited row %d\n",
                        ss.getPrevPivots().get(i)[1],
                        ss.getPrevPivots().get(i)[0]);
            }
        }
        printTableau(ss);

    }

    // EFFECTS: converts a comma separated list of numbers to an array of doubles
    private static double[] csvToArray(String csv) {
        String[] nums = csv.split(", ");
        double[] result = new double[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = Double.parseDouble(nums[i]);
        }

        return result;
    }

    // EFFECTS: prints the tableau of a given solution state
    private static void printTableau(SolutionState ss) {
        double[][] tab = ss.getTableau();

        System.out.print("\nBV  |");
        for (int i = 0; i < ss.getNumVariables(); i++) {
            System.out.printf("   x_%-2d", i + 1);
        }

        for (int j = 0; j < ss.getNumConstraints(); j++) {
            System.out.printf("   s_%-2d", j + 1);
        }
        System.out.print("|  RHS\n");

        for (int i = 0; i < tab.length; i++) {
            if (i < tab.length - 1) {
                System.out.printf("C%-2d |", i + 1);
            } else {
                System.out.print("f   |");
            }
            for (int j = 0; j < tab[0].length; j++) {
                if (j < tab[0].length - 1) {
                    System.out.printf("%7.2f", tab[i][j]);
                } else {
                    System.out.printf("|%6.2f\n", tab[i][j]);
                }
            }
        }
    }

    @SuppressWarnings("methodlength")
    // EFFECTS: prints a given tableau and infers the number of variables and
    // constraints
    private static void printTableau(double[][] tab) {
        int m = tab.length - 1;
        int n = tab[0].length - m - 1;

        System.out.print("\nBV  |");
        for (int i = 0; i < n; i++) {
            System.out.printf("   x_%-2d", i + 1);
        }

        for (int j = 0; j < m; j++) {
            System.out.printf("   s_%-2d", j + 1);
        }
        System.out.print("|  RHS\n");

        for (int i = 0; i < tab.length; i++) {
            if (i < tab.length - 1) {
                System.out.printf("C%-2d |", i + 1);
            } else {
                System.out.print("f   |");
            }
            for (int j = 0; j < tab[0].length; j++) {
                if (j < tab[0].length - 1) {
                    System.out.printf("%7.2f", tab[i][j]);
                } else {
                    System.out.printf("|%6.2f\n", tab[i][j]);
                }
            }
        }
    }
}
