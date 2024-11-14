import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GuessingGameGUI {
    private int randomNumber;
    private int attempts;
    private int range;
    private int highScore = Integer.MAX_VALUE;
    private JTextField guessField;
    private JLabel resultLabel;
    private JLabel attemptsLabel;
    private JLabel highScoreLabel;
    private JLabel timerLabel;
    private Timer gameTimer;
    private int secondsElapsed;
    private JButton guessButton;
    private JButton startButton;
    private JComboBox<String> difficultyComboBox;
    private Color backgroundColor = new Color(230, 230, 250); // Light purple
    private boolean gameInProgress = false;
    private JTextArea feedbackArea;
    private JTextField feedbackField;
    private int totalGames = 0;
    private int totalAttempts = 0;

    public GuessingGameGUI() {
        // Create the main frame
        JFrame frame = new JFrame(" Number Guessing Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(backgroundColor);
        frame.setLayout(new BorderLayout(10, 10));

        // Initialize timer
        gameTimer = new Timer(1000, e -> updateTimer());

        // Create the main panel
        JPanel mainPanel = createMainPanel();
        frame.add(mainPanel, BorderLayout.CENTER);

        // Create bottom panel
        JPanel bottomPanel = createBottomPanel();
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Set up the frame
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        // Title
        JLabel titleLabel = createTitleLabel();
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Difficulty selection
        JPanel difficultyPanel = createDifficultyPanel();
        mainPanel.add(difficultyPanel);

        // Game controls
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel);

        // Game information
        JPanel infoPanel = createInfoPanel();
        mainPanel.add(infoPanel);

        // Feedback area
        JPanel feedbackPanel = createFeedbackPanel();
        mainPanel.add(feedbackPanel);

        return mainPanel;
    }

    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel(" Number Guessing Game");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(75, 0, 130));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        return titleLabel;
    }

    private JPanel createDifficultyPanel() {
        JPanel panel = createStyledPanel();
        panel.add(new JLabel("Choose difficulty:"));
        String[] difficulties = {"Easy (1-10)", "Medium (1-50)", "Hard (1-100)"};
        difficultyComboBox = new JComboBox<>(difficulties);
        panel.add(difficultyComboBox);
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = createStyledPanel();
        guessField = new JTextField(10);
        guessField.setFont(new Font("Arial", Font.PLAIN, 14));
        guessField.setEnabled(false);
        panel.add(guessField);

        guessButton = new JButton("Guess");
        styleButton(guessButton);
        guessButton.addActionListener(e -> makeGuess());
        guessButton.setEnabled(false);
        panel.add(guessButton);
        return panel;
    }

    private JPanel createInfoPanel() {
        JPanel panel = createStyledPanel();
        resultLabel = createStyledLabel("");
        attemptsLabel = createStyledLabel("Attempts: 0");
        highScoreLabel = createStyledLabel("High Score: N/A");
        timerLabel = createStyledLabel("Time: 0s");

        panel.add(resultLabel);
        panel.add(attemptsLabel);
        panel.add(highScoreLabel);
        panel.add(timerLabel);
        return panel;
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(backgroundColor);
    
        feedbackArea = new JTextArea(5, 30);
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        JPanel inputPanel = new JPanel(); // Fixed the space between "input" and "Panel"
        inputPanel.setLayout(new FlowLayout());
        inputPanel.setBackground(backgroundColor);
        feedbackField = new JTextField(20);
        feedbackField.setFont(new Font("Arial", Font.PLAIN, 14));
        JButton submitButton = new JButton("Submit Feedback");
        styleButton(submitButton);
        submitButton.addActionListener(e -> submitFeedback());
        inputPanel.add(feedbackField);
        inputPanel.add(submitButton);
        panel.add(inputPanel, BorderLayout.SOUTH);
    
        return panel;
    }

    private JPanel createBottomPanel() {
        JPanel panel = createStyledPanel();
        startButton = new JButton("Start Game");
        styleButton(startButton);
        startButton.addActionListener(e -> startGame());
        panel.add(startButton);

        JButton hintButton = new JButton("Get Hint");
        styleButton(hintButton);
        hintButton.addActionListener(e -> provideHint());
        panel.add(hintButton);
        return panel;
    }

    private void startGame() {
        if (!gameInProgress) {
            gameInProgress = true;
            startButton.setEnabled(false);
            guessButton.setEnabled(true);
            guessField.setEnabled(true);
            secondsElapsed = 0;
            attempts = 0;
            attemptsLabel.setText("Attempts: 0");
            timerLabel.setText("Time: 0s");
            gameTimer.start();

            String difficulty = (String) difficultyComboBox.getSelectedItem();
            switch (difficulty) {
                case "Easy (1-10)":
                    range = 10;
                    break;
                case "Medium (1-50)":
                    range = 50;
                    break;
                case "Hard (1-100)":
                    range = 100;
                    break;
            }

            Random random = new Random();
            randomNumber = random.nextInt(range) + 1;
            resultLabel.setText("I have selected a number between 1 and " + range + ". Can you guess it?");

            // Add a feedback dialog when the game starts
            JOptionPane.showMessageDialog(null, "Game started! You have chosen " + difficulty + ". Good luck!");
        }
    }

    private void makeGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());

            if (guess < randomNumber) {
                resultLabel.setText("Too low! Try again.");
                addFeedback("Your guess (" + guess + ") is too low. Try again!");
            } else if (guess > randomNumber) {
                resultLabel.setText("Too high! Try again.");
                addFeedback("Your guess (" + guess + ") is too high. Try again!");
            } else {
                resultLabel.setText("Congratulations! You guessed the number in " + (attempts + 1) + " attempts.");
                guessField.setEnabled(false);
                guessButton.setEnabled(false);
                gameTimer.stop();
                gameInProgress = false;
                startButton.setEnabled(true);

                if (attempts + 1 < highScore) {
                    highScore = attempts + 1;
                    highScoreLabel.setText("High Score: " + highScore);
                }

                addFeedback("Congratulations! You won the game in " + (attempts + 1) + " attempts.");
                
                // Update game statistics
                totalGames++;
                totalAttempts += attempts + 1;
                
                // Generate and display game analysis
                String gameAnalysis = analyzeGame();
                addFeedback(gameAnalysis);
            }

            attempts++;
            attemptsLabel.setText("Attempts: " + attempts);
            guessField.setText("");
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input. Please enter a number.");
            addFeedback("Invalid input. Please enter a number.");
        }
    }

    private String analyzeGame() {
        StringBuilder analysis = new StringBuilder("Game Analysis:\n");

        // Analyze performance based on difficulty
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        int expectedAttempts = 0;
        switch (difficulty) {
            case "Easy (1-10)":
                expectedAttempts = 4;
                break;
            case "Medium (1-50)":
                expectedAttempts = 6;
                break;
            case "Hard (1-100)":
                expectedAttempts = 8;
                break;
        }

        if (attempts + 1 <= expectedAttempts) {
            analysis.append("Great job! You solved it faster than expected for this difficulty.\n");
        } else {
            analysis.append("You can do better! Try to solve it in fewer attempts next time.\n");
        }

        // Compare to average performance
        double averageAttempts = (double) totalAttempts / totalGames;
        if (attempts + 1 < averageAttempts) {
            analysis.append("You performed better than your average of " + String.format("%.2f", averageAttempts) + " attempts.\n");
        } else if (attempts + 1 > averageAttempts) {
            analysis.append("You usually perform better. Your average is " + String.format("%.2f", averageAttempts) + " attempts.\n");
        } else {
            analysis .append("This game was right on par with your average performance.\n");
        }

        // Provide a tip
        if (attempts + 1 > expectedAttempts) {
            analysis.append("Tip: Try using a binary search strategy to find the number more quickly.\n");
        }

        return analysis.toString();
    }

    private void addFeedback(String message) {
        feedbackArea.append(message + "\n");
        feedbackArea.setCaretPosition(feedbackArea.getDocument().getLength());
    }

    private void submitFeedback() {
        String feedback = feedbackField.getText();
        if (!feedback.isEmpty()) {
            addFeedback("User feedback: " + feedback);
            feedbackField.setText("");
            
            // Generate a response to the user's feedback
            String response = generateFeedbackResponse(feedback);
            addFeedback("Game response: " + response);
        }
    }

    private String generateFeedbackResponse(String feedback) {
        feedback = feedback.toLowerCase();
        if (feedback.contains("difficult") || feedback.contains("hard")) {
            return "Thanks for your feedback. If you find the game too challenging, try an easier difficulty level.";
        } else if (feedback.contains("easy") || feedback.contains("simple")) {
            return "Glad you're finding it manageable! Feel free to try a harder difficulty for more of a challenge.";
        } else if (feedback.contains("fun") || feedback.contains("enjoy")) {
            return "We're happy you're enjoying the game! Keep playing to improve your skills.";
        } else if (feedback.contains("bug") || feedback.contains("issue")) {
            return "We're sorry to hear you encountered a problem. Please provide more details so we can look into it.";
        } else {
            return "Thank you for your feedback! We appreciate your input and will use it to improve the game.";
        }
    }

    private JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(backgroundColor);
        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(0, 0, 0)); // Black
        return label;
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(new Color(255, 255, 255)); // White
        button.setBackground(new Color(0, 0, 255)); // Blue
    }

    private void updateTimer() {
        secondsElapsed++;
        timerLabel.setText("Time: " + secondsElapsed + "s");
    }

    private void provideHint() {
        if (gameInProgress) {
            int hint = randomNumber / 2;
            if (hint == 0) {
                hint = 1;
            }
            resultLabel.setText("Hint: The number is around " + hint + ".");
            JOptionPane.showMessageDialog(null, "Hint: The number is around " + hint + ".");
        }
    }

    public static void main(String[] args) {
        new GuessingGameGUI();
    }
}