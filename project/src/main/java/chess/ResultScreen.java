package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResultScreen extends JFrame {
    private JLabel label;
    private JButton mainMenuButton;
    private JButton quitButton;
    private JLabel imageLabel; // To display images

    public ResultScreen(JFrame c, String inputString) {
        setTitle("Result Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        c.setEnabled(false);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label to display the string
        if (inputString.equals("Black") || inputString.equals("White")) {
            label = new JLabel(inputString + " WON!!!", SwingConstants.CENTER);
        } else {
            label = new JLabel("Draw!!!", SwingConstants.CENTER);
        }
        Font labelFont = new Font("Arial", Font.BOLD, 24);
        Color labelColor = Color.BLUE; // Choose the desired color for the label
        label.setFont(labelFont);
        label.setForeground(labelColor);
        panel.add(label, BorderLayout.NORTH);

        // Create image labels
        ImageIcon blackImageIcon = new ImageIcon("project/src/main/java/chess/img/pieces/BK.png");
        ImageIcon whiteImageIcon = new ImageIcon("project/src/main/java/chess/img/pieces/WK.png");
        ImageIcon drawImageIcon = new ImageIcon("project/src/main/java/chess/img/other/draw-icon.png");
        Image blackImage = blackImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image whiteImage = whiteImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        Image drawImage = drawImageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);

        ImageIcon scaledBlackImageIcon = new ImageIcon(blackImage);
        ImageIcon scaledWhiteImageIcon = new ImageIcon(whiteImage);
        ImageIcon scaledDrawImageIcon = new ImageIcon(drawImage);
        if (inputString.equals("Black")) {
            imageLabel = new JLabel(scaledBlackImageIcon, SwingConstants.CENTER);
        } else if (inputString.equals("White")) {
            imageLabel = new JLabel(scaledWhiteImageIcon, SwingConstants.CENTER);
        } else {
            imageLabel = new JLabel(scaledDrawImageIcon, SwingConstants.CENTER);
        }

        // Border for image label
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panel.add(imageLabel, BorderLayout.CENTER);

        // Create buttons
        mainMenuButton = new JButton("Main Menu");
        quitButton = new JButton("Quit");
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        Color buttonColor = new Color(75, 0, 130);
        mainMenuButton.setFont(buttonFont);
        mainMenuButton.setForeground(Color.WHITE);
        mainMenuButton.setBackground(buttonColor); // Change button background color if needed
        quitButton.setFont(buttonFont);
        quitButton.setForeground(Color.WHITE);
        quitButton.setBackground(buttonColor);
        mainMenuButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);
        mainMenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.dispose();
                dispose();
                SwingUtilities.invokeLater(() -> new MainMenu());
            }
        });

        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(mainMenuButton);
        buttonPanel.add(quitButton);

        // Add button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add panel to the frame
        add(panel);

        setVisible(true);
    }
}
