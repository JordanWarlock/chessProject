package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ChooseSideMenu extends JFrame {
    private JButton whiteButton;
    private JButton blackButton;
    private ImageIcon whiteIcon;
    private ImageIcon blackIcon;
    private String diff;

    public ChooseSideMenu(String diff) {
        setTitle("Side Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        this.diff = diff;
        // Create a content panel to hold the background image and buttons
        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Image backgroundImage;
                try {
                    backgroundImage = ImageIO
                            .read(new File("project/src/main/java/chess/img/other/chess-background.png"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        contentPanel.setLayout(new GridBagLayout());

        try {
            Image pvpImage = ImageIO.read(new File("project/src/main/java/chess/img/pieces/WK.png"));
            Image pveImage = ImageIO.read(new File("project/src/main/java/chess/img/pieces/BK.png"));
            whiteIcon = new ImageIcon(pvpImage);
            blackIcon = new ImageIcon(pveImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create buttons using preloaded icons and set their text
        whiteButton = new JButton(whiteIcon);
        whiteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        whiteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        whiteButton.setText("WHITE");
        customizeButton(whiteButton);

        blackButton = new JButton(blackIcon);
        blackButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        blackButton.setHorizontalTextPosition(SwingConstants.CENTER);
        blackButton.setText("BLACK");
        customizeButton(blackButton);

        // Add buttons to the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 50, 10, 50); // Padding
        contentPanel.add(whiteButton, gbc);

        gbc.gridx++;
        contentPanel.add(blackButton, gbc);

        // Set button hover effect
        addHoverEffect(whiteButton);
        addHoverEffect(blackButton);

        // Set the content panel to be the main content of the frame
        setContentPane(contentPanel);
        whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (diff == "EASY")
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_EASY_WHITE));
                else if (diff == "MEDIUM")
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_MEDIUM_WHITE));
                else
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_HARD_WHITE));

                dispose();
            }
        });

        blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (diff == "EASY")
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_EASY_BLACK));
                else if (diff == "MEDIUM")
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_MEDIUM_BLACK));
                else
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_HARD_BLACK));

                dispose();
                dispose();
            }
        });

        // Add a ComponentListener to the frame to handle resizing
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Get the new size of the frame
                int buttonWidth = getWidth() / 5; // Adjust this factor as needed
                int buttonHeight = getHeight() / 3; // Adjust this factor as needed

                // Resize the button icons
                whiteButton.setIcon(new ImageIcon(
                        whiteIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                blackButton.setIcon(new ImageIcon(
                        blackIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

                // Update the content panel size to fit the buttons properly
                contentPanel.setPreferredSize(new Dimension(getWidth(), getHeight()));
                revalidate();
            }
        });

        setVisible(true);
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void customizeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Custom font and size
        button.setForeground(Color.BLACK); // Text color
        button.setBackground(new Color(249, 210, 218));
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        button.setFocusPainted(false); // Remove focus border
    }

    private void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(211, 105, 250)); // Change background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(249, 210, 218)); // Reset background color
            }
        });
    }

}
