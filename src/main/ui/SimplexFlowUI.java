package ui;

import model.ObjectiveFunction;
import model.Constraint;
import model.LinearProgram;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the ui for the SimplexFlow app
// The methods relating to data persistence are attributeable to the JsonSerializationDemo provided
public class SimplexFlowUI extends JFrame implements ActionListener {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final String JSON_STORE = "./data/save.json";
    private LinearProgram lp;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel loadPanel;
    private JPanel objPanel;
    private JPanel constraintPanel;

    private JButton loadButton;
    private JButton skipLoadButton;
    private JButton saveButton;
    private JButton submitNumVarButton;
    private JButton quitButton;
    private JButton overwriteButton;
    private JButton clearConstraintsButton;
    private JButton addConstraintButton;

    private JTextField numVarTextField;
    private JTextField objCoeffsField;
    private JTextField objConstantField;

    // EFFECTS: runs the UI of the SimplexFlow app
    public SimplexFlowUI() {
        initWindow();
        addLoadMenu();

        addQuitButton();
    }

    // MODIFIES: this
    // EFFECTS: finishes the UI setup after the LP has been initialized
    private void finishMenuSetup() {
        addObjPanel();
        addConstraintPanel();
        addSaveButton();

        revalidate();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: adds a panel that houses the objective function settings
    private void addObjPanel() {
        objPanel = new JPanel();
        objPanel.setBounds(30, 10, 600, 140);
        objPanel.setLayout(null);
        objPanel.setVisible(true);
        objPanel.setBackground(Color.LIGHT_GRAY);

        JLabel title = new JLabel("Objective Function:");
        title.setBounds(5, 5, 200, 20);
        objPanel.add(title);

        JLabel objFLabel = new JLabel(lp.getObjF().toString());
        objFLabel.setBounds(5, 25, 400, 20);
        objPanel.add(objFLabel);

        addOverwriteMenu();
        this.add(objPanel);
    }

    // MODIFIES: this
    // EFFECTS: creates the overwrite menu for the objective function
    private void addOverwriteMenu() {
        JPanel overwriteMenu = new JPanel();
        overwriteMenu.setLayout(null);
        overwriteMenu.setVisible(true);
        overwriteMenu.setBackground(Color.LIGHT_GRAY);
        overwriteMenu.setBounds(5, 45, 400, 100);

        JPanel subPanel = new JPanel();
        subPanel.setVisible(true);
        subPanel.setLayout(new GridLayout(2, 2, 5, 0));
        subPanel.setBackground(Color.LIGHT_GRAY);
        subPanel.setBounds(0, 0, 400, 60);

        subPanel.add(new JLabel("OBF coefficients as a csv: "));
        subPanel.add(new JLabel("Constant term: "));
        objCoeffsField = makeSmallTextField(subPanel);
        objConstantField = makeSmallTextField(subPanel);

        overwriteButton = new JButton("Overwrite OBF");
        overwriteButton.addActionListener(this);
        overwriteButton.setFocusable(false);
        overwriteButton.setBounds(0, 65, 150, 25);

        overwriteMenu.add(subPanel);
        overwriteMenu.add(overwriteButton);

        objPanel.add(overwriteMenu);
        objPanel.revalidate();
        objPanel.repaint();
    }

    // EFFECTS: creates a small text field and adds it to destination
    private JTextField makeSmallTextField(Container destination) {
        JTextField output = new JTextField(20);
        output.setMaximumSize(new Dimension(200, 20));
        destination.add(output);
        return output;
    }

    // MODIFIES: this
    // EFFECTS: adds a panel that houses the constraint list and settings
    private void addConstraintPanel() {
        constraintPanel = new JPanel();
        constraintPanel.setBounds(30, 160, 600, 350);
        constraintPanel.setLayout(null);
        constraintPanel.setVisible(true);
        constraintPanel.setBackground(Color.LIGHT_GRAY);
        
        JLabel title = new JLabel("Constraints:");
        title.setBounds(5, 5, 200, 20);
        constraintPanel.add(title);

        addConstraintsList();

        addAddConstraintButton();
        addClearConstraintsButton();

        this.add(constraintPanel);
    }

    // MODIFIES: this
    // EFFECTS: adds a panel with each individual constraint inside of it to the constraint panel
    private void addConstraintsList() {
        JPanel constraintsList = new JPanel();
        constraintsList.setBounds(15, 25, 600, 200);
        constraintsList.setLayout(null);
        constraintsList.setVisible(true);
        constraintsList.setBackground(Color.LIGHT_GRAY);

        int step = 15;
        for (int i = 0; i < lp.constraintsToStrings().size(); i++) {
            String s = lp.constraintsToStrings().get(i);
            JLabel constraintLabel = new JLabel(s);
            constraintLabel.setBounds(0, i * step, 300, step);
            constraintsList.add(constraintLabel);
        }

        constraintPanel.add(constraintsList);
    }

    // EFFECTS: initializes the window of the UI to house other components
    public void initWindow() {
        setTitle("SimplexFlow");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        setLayout(null);
    }

    // MODIFIES: this
    // EFFECTS: shows the save menu and prompts user to choose whether to load from
    // previous save or not
    public void addLoadMenu() {
        initPersistence();

        loadButton = new JButton();
        loadButton.setBounds(70, 125, 130, 50);
        loadButton.addActionListener(this);
        loadButton.setText("Load from save");
        loadButton.setFocusable(false);

        skipLoadButton = new JButton();
        skipLoadButton.setBounds(220, 125, 100, 50);
        skipLoadButton.addActionListener(this);
        skipLoadButton.setText("New LP");
        skipLoadButton.setFocusable(false);

        loadPanel = new JPanel();
        loadPanel.setBounds(200, 150, 400, 300);
        loadPanel.setLayout(null);
        loadPanel.setBackground(new Color(200, 200, 200));

        loadPanel.add(loadButton);
        loadPanel.add(skipLoadButton);

        add(loadPanel);
    }

    // MODIFIES: this
    // EFFECTS: initializes the data persistence features of the application
    private void initPersistence() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: creates a quit button and adds to frame
    private void addQuitButton() {
        quitButton = new JButton("Quit");
        quitButton.setBounds(WIDTH - 120, HEIGHT - 80, 100, 40);
        quitButton.addActionListener(this);
        quitButton.setFocusable(false);

        add(quitButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a save button and adds to frame
    private void addSaveButton() {
        saveButton = new JButton("Save");
        saveButton.setBounds(WIDTH - 120, HEIGHT - 120, 100, 40);
        saveButton.addActionListener(this);
        saveButton.setFocusable(false);

        add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to the constraints panel that adds a constraint
    private void addAddConstraintButton() {
        addConstraintButton = new JButton();
        addConstraintButton.setBounds(5, 290, 200, 25);
        addConstraintButton.addActionListener(this);
        addConstraintButton.setText("Add constraint");
        addConstraintButton.setFocusable(false);
        constraintPanel.add(addConstraintButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a button to the constraints panel that clears all constraints
    private void addClearConstraintsButton() {
        clearConstraintsButton = new JButton();
        clearConstraintsButton.setBounds(5, 320, 200, 25);
        clearConstraintsButton.addActionListener(this);
        clearConstraintsButton.setText("Clear ALL Constraints");
        clearConstraintsButton.setFocusable(false);
        constraintPanel.add(clearConstraintsButton);
    }

    // MODIFIES: this
    // EFFECTS: handles user actions
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == loadButton) {
            doLoad();
        } else if (source == skipLoadButton) {
            doLinearProgramSetup();
        } else if (source == submitNumVarButton) {
            createLP();
        } else if (source == quitButton) {
            System.exit(0);
        } else if (source == saveButton) {
            doSave();
        } else if (source == overwriteButton) {
            overwriteObjF();
        }
    }

    // MODIFIES: this
    // EFFECTS: overwrites the OBF with the inputs in the relevant text fields
    private void overwriteObjF() {
        ObjectiveFunction objF = lp.getObjF();
        objF.setConstantTerm(Integer.parseInt(objConstantField.getText()));
        objF.setCoefficients(csvToArray(objCoeffsField.getText()));

        JLabel objFunctionLabel = (JLabel) objPanel.getComponent(1);
        objFunctionLabel.setText(lp.getObjF().toString());

        objPanel.revalidate();
        objPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: prompts user to set the number of variables in their LP
    private void doLinearProgramSetup() {
        loadButton.setVisible(false);
        skipLoadButton.setVisible(false);

        numVarTextField = new JTextField();
        numVarTextField.setBounds(100, 50, 200, 30);
        loadPanel.add(numVarTextField);

        loadPanel.revalidate();
        loadPanel.repaint();

        submitNumVarButton = new JButton("Submit");
        submitNumVarButton.setBounds(150, 100, 100, 40);
        submitNumVarButton.addActionListener(this);

        loadPanel.add(submitNumVarButton);
    }

    // MODIFIES: this
    // EFFECTS: creates an LP with the number of variables in the numVarTextField
    private void createLP() {
        try {
            int userInput = Integer.parseInt(numVarTextField.getText());
            lp = new LinearProgram(userInput);
            loadPanel.setVisible(false);
            finishMenuSetup();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(loadPanel, "Please enter a valid integer!");
        }
    }

    // MODIFIES: save.json
    // EFFECTS: prompts user to write current LP to json
    private void doSave() {
        try {
            jsonWriter.open();
            jsonWriter.write(lp);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null, "Saved LP to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: performs load operation from save
    private void doLoad() {
        try {
            loadLP();
            loadPanel.setVisible(false);
            finishMenuSetup();
        } catch (IOException ioe) {
            failToLoad();
        }
    }

    // EFFECTS: shows a label on the load panel that tells user that load failed
    private void failToLoad() {
        JLabel failLabel = new JLabel("Failed to load from save");
        loadPanel.add(failLabel);
    }

    // MODIFIES: this
    // EFFECTS: loads LP from file
    private void loadLP() throws IOException {
        lp = jsonReader.readLP();
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
