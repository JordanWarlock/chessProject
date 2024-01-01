package chess;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class DifficultyMenu extends JFrame {
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    private ImageIcon backgroundImage;

    public DifficultyMenu() {
        setTitle("Difficulty Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 720);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width / 5);
        int height = (int) (screenSize.height / 5);
        backgroundImage = createImageIcon("img/other/chess-background.png");

        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPanel.setLayout(new GridBagLayout()); // Use Y_AXIS for vertical arrangement

        easyButton.setBounds(50, 50, 200, 30);
        mediumButton.setBounds(50, 100, 200, 30);
        hardButton.setBounds(50, 150, 200, 30);

        easyButton.setPreferredSize(new Dimension((int) (width * 0.8), height / 3));
        easyButton.setBackground(new Color(75, 0, 130));
        easyButton.setForeground(Color.WHITE);
        easyButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));
        easyButton.setFocusPainted(false);
        easyButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));

        mediumButton.setPreferredSize(new Dimension((int) (width * 0.8), height / 3));
        mediumButton.setBackground(new Color(75, 0, 130));
        mediumButton.setForeground(Color.WHITE);
        mediumButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));
        mediumButton.setFocusPainted(false);
        mediumButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));

        hardButton.setPreferredSize(new Dimension((int) (width * 0.8), height / 3));
        hardButton.setBackground(new Color(75, 0, 130));
        hardButton.setForeground(Color.WHITE);
        hardButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));
        hardButton.setFocusPainted(false);
        hardButton.setBorder(BorderFactory.createLineBorder(Color.white, 3, false));

        ImageIcon logoImage = createImageIcon("img/other/chess-logo.png");
        Image resizedLogo = logoImage.getImage().getScaledInstance((int) (width * 1.3), -1, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedLogo);
        JLabel logoLabel = new JLabel(resizedIcon);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPanel.add(logoLabel, gbc);
        gbc.gridy++;
        contentPanel.add(easyButton, gbc);
        gbc.insets = new Insets(50, 5, 5, 5);
        gbc.gridy++;
        contentPanel.add(mediumButton, gbc);
        gbc.gridy++;
        contentPanel.add(hardButton, gbc);
        add(contentPanel);
        addActionListeners();
        addMouseListeners();
        pack();
        setVisible(true);
    }

    private void addActionListeners() {
        easyButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ChooseSideMenu("EASY"));
            dispose();
        });

        mediumButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ChooseSideMenu("MEDIUM"));
            dispose();
        });

        hardButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> new ChooseSideMenu("HARD"));
            dispose();
        });
    }

    private void addMouseListeners() {
        easyButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                easyButton.setBackground(new Color(90, 0, 200)); // Change background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                easyButton.setBackground(new Color(75, 0, 130)); // Restore the original background color
            }
        });

        mediumButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                mediumButton.setBackground(new Color(90, 0, 200)); // Change background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                mediumButton.setBackground(new Color(75, 0, 130)); // Restore the original background color
            }
        });

        hardButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hardButton.setBackground(new Color(90, 0, 200)); // Change background color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                hardButton.setBackground(new Color(75, 0, 130)); // Restore the original background color
            }
        });
    }

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