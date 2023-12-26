package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResultScreen extends JFrame {
    private JLabel label;
    private JButton mainMenuButton;
    private JButton quitButton;

    public ResultScreen(JFrame c, String inputString) {
        setTitle("Result Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        c.setEnabled(false);

        // Create a panel to hold components
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Create a label to display the string
        if (inputString.equals("Black") || inputString.equals("Black")) {
            label = new JLabel(inputString + "WON!!!", SwingConstants.CENTER);
        } else {
            label = new JLabel("Draw!!!", SwingConstants.CENTER);
        }
        panel.add(label, BorderLayout.CENTER);

        // Create buttons
        mainMenuButton = new JButton("Main Menu");
        quitButton = new JButton("Quit");

        // Add action listeners to the buttons
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
