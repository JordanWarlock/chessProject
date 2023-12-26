package chess;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    private JButton playButton;
    private JButton quitButton;
    private ImageIcon backgroundImage;

    public MainMenu() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1920, 1080));

        // Set frame size to 80% of the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 5);
        int height = (int) (screenSize.height / 5);
        setSize(width, height);
        setLocationRelativeTo(null);
        backgroundImage = createImageIcon("img/other/chess-background.png");

        if (backgroundImage != null) {
            setLayout(new BorderLayout());

            JPanel contentPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            };
            contentPanel.setLayout(new GridBagLayout());
            add(contentPanel);

            playButton = new JButton("Play");
            quitButton = new JButton("Quit");

            playButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> new ModeMenu());
                    dispose();
                }
            });
            quitButton.addActionListener(e -> System.exit(0));

            playButton.setPreferredSize(new Dimension((int) (width * 0.8), height / 3));
            quitButton.setPreferredSize(new Dimension((int) (width * 0.8), height / 3));

            playButton.setBackground(new Color(75, 0, 130));
            playButton.setForeground(Color.WHITE);

            quitButton.setBackground(new Color(75, 0, 130));
            quitButton.setForeground(Color.WHITE);

            playButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));
            quitButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));

            playButton.setFocusPainted(false);
            quitButton.setFocusPainted(false);

            playButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    playButton.setBackground(new Color(90, 0, 200)); // Change background color on hover
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    playButton.setBackground(new Color(75, 0, 130)); // Restore the original background color
                }
            });

            quitButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    quitButton.setBackground(new Color(90, 0, 200)); // Change background color on hover
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    quitButton.setBackground(new Color(75, 0, 130)); // Restore the original background color
                }
            });

            ImageIcon logoImage = createImageIcon("img/other/chess-logo.png");
            Image resizedLogo = logoImage.getImage().getScaledInstance((int) (width * 1.3), -1, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedLogo);
            JLabel logoLabel = new JLabel(resizedIcon);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(0, 0, 0, 0);
            gbc.gridx = 0;
            gbc.gridy = 0;
            contentPanel.add(logoLabel, gbc);
            gbc.insets = new Insets(0, 0, height / 4, 0);
            gbc.gridy++;
            contentPanel.add(playButton, gbc);
            gbc.gridy++;
            contentPanel.add(quitButton, gbc);
        }

        pack();
        setVisible(true);
    }

    // Method to load an image icon from the file path
    protected ImageIcon createImageIcon(String path) {
        URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
