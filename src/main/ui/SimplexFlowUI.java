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

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



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

    private JButton loadButton;
    private JButton skipLoadButton;
    private JButton saveButton;
    private JButton submitButton;
    
    private JTextField numVarTextField;
    


    // EFFECTS: runs the UI of the SimplexFlow app
    public SimplexFlowUI() {
        initWindow();
        loadMenu();
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
    // EFFECTS: shows the save menu and prompts user to choose whether to load from previous
    // save or not
    public void loadMenu() {
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

    public void failToLoad() {
        JLabel failLabel = new JLabel("Failed to load from save");
        loadPanel.add(failLabel);
    }

    // MODIFIES: this
    // EFFECTS: initializes the data persistence features of the application
    private void initPersistence() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
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

        submitButton = new JButton("Submit");
        submitButton.setBounds(150, 100, 100, 40);
        submitButton.addActionListener(this);

        loadPanel.add(submitButton);

    }

    // MODIFIES: this
    // EFFECTS: performs load operation from save
    private void doLoad() {
        try {
            loadLP();
            loadPanel.setVisible(false);
        } catch (IOException ioe) {
            failToLoad();
        }
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
        } else if (source == submitButton) {
            createLP();
        }
    }

    // MODIFIES: this
    // EFFECTS: creates an LP with the number of variables in the numVarTextField
    private void createLP() {
        try {
            int userInput = Integer.parseInt(numVarTextField.getText());
            lp = new LinearProgram(userInput);
            loadPanel.setVisible(false);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(loadPanel, "Please enter a valid integer!");
        }
        
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
