package chess;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ModeMenu extends JFrame {
    private JButton pvpButton;
    private JButton pveButton;
    private ImageIcon pvpIcon;
    private ImageIcon pveIcon;

    public ModeMenu() {
        setTitle("Mode Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);

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

        // Preload button icons
        try {
            Image pvpImage = ImageIO.read(new File("project/src/main/java/chess/img/other/pvp-icon.png"));
            Image pveImage = ImageIO.read(new File("project/src/main/java/chess/img/other/pve-icon.png"));
            pvpIcon = new ImageIcon(pvpImage);
            pveIcon = new ImageIcon(pveImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create buttons using preloaded icons and set their text
        pvpButton = new JButton(pvpIcon);
        pvpButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pvpButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pvpButton.setText("PLAY VS HUMAN");
        customizeButton(pvpButton);

        pveButton = new JButton(pveIcon);
        pveButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        pveButton.setHorizontalTextPosition(SwingConstants.CENTER);
        pveButton.setText("PLAY VS COMPUTER");
        customizeButton(pveButton);

        // Add buttons to the content panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 50, 10, 50); // Padding
        contentPanel.add(pvpButton, gbc);

        gbc.gridx++;
        contentPanel.add(pveButton, gbc);

        // Set button hover effect
        addHoverEffect(pvpButton);
        addHoverEffect(pveButton);

        // Set the content panel to be the main content of the frame
        setContentPane(contentPanel);
        pvpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVP));
                dispose();
            }
        });

        pveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new DifficultyMenu());
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
                pvpButton.setIcon(new ImageIcon(
                        pvpIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
                pveButton.setIcon(new ImageIcon(
                        pveIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

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
