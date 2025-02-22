package ui;

import model.ObjectiveFunction;
import model.Constraint;
import model.LinearProgram;
import model.SolutionState;

import java.util.Scanner;

// Simplex learner application
public class SimplexLearnerApp {
    private Scanner input;
    private LinearProgram lp;
    private SolutionState ss;

    // EFFECTS: runs the Simplex learner app
    public SimplexLearnerApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user inputs
    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        initApp();
        doLinearProgramSetup();

        while (keepGoing) {
            displaySetupMenu();
            command = input.next();
            input.nextLine();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processSetupCommand(command);
            }
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
        input.nextLine();                   // handles the new line from pressing enter
    }

    // EFFECTS: displays the main menu of options to console user
    private void displaySetupMenu() {
        System.out.println("Please choose an option:");
        System.out.println("\tc: add constraint");
        System.out.println("\to: set objective function");
        System.out.println("\td: delete constraint");
        System.out.println("\tv: view LP");
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
        double cTerm = input.nextDouble();
        objF.setConstantTerm(cTerm);
        input.nextLine();                   // handles the new line from pressing enter

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
        double cTerm = input.nextDouble();
        cons.setConstantTerm(cTerm);
        input.nextLine();                   // handles the new line from pressing enter

        lp.addConstraint(cons);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to delete a constraint from the LP
    private void doDeleteConstraint() {
        for (String cons : lp.constraintsToStrings()) {
            System.out.println(cons);
        }
        System.out.println("Which constraint would you like to delete?");
        int deleteIndex = input.nextInt();
        lp.getConstraints().remove(deleteIndex - 1);
        input.nextLine();
    }

    // EFFECTS: prints out information on the current LP including OBF and
    // constraints
    private void doViewLP() {
        String objFString = lp.getObjF().toString();
        System.out.println("\nMaximize:\n" + objFString + "\n\nsubject to:");

        for (String cons : lp.constraintsToStrings()) {
            System.out.println(cons);
        }
        System.out.println("\n");
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
}
