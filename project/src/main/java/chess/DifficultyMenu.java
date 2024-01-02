package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DifficultyMenu extends JFrame {
    private JPanel easyPanel;
    private JPanel mediumPanel;
    private JPanel hardPanel;
    private JPanel hoveredPanel;
    private float alpha = 0.0f;

    public DifficultyMenu() {
        setTitle("Difficulty Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3)); // Divide the frame into 3 columns
        String easyDesc = "Tailored for beginners and casual players.\n" +
                "Offers a relaxed, forgiving gameplay experience.\n" +
                "Provides an accessible entry point to chess.\n" +
                "The AI makes forgiving moves, ideal for learning.\n" +
                "Allows ample time to contemplate and plan moves.\n" +
                "A slower pace fosters confidence and strategy exploration.\n" +
                "Perfect for those seeking an enjoyable yet gentle adventure\n";

        String mediumDesc = "Strikes a balance between challenge and accessibility.\n" +
                "Tests skills with a moderately competitive AI opponent.\n" +
                "Suited for players with a moderate grasp of tactics.\n" +
                "Encourages strategic thinking and careful planning.\n" +
                "Offers a mix of offensive and defensive AI moves.\n" +
                "Provides a fair challenge without overwhelming difficulty.\n" +
                "Ideal for players seeking a stimulating yet manageable game.\n";

        String hardDesc = "Tailored for seasoned chess enthusiasts.\n" +
                "Presents a formidable challenge with advanced AI.\n" +
                "Ramps up difficulty significantly for skilled players.\n" +
                "Demands astute planning and precise execution.\n" +
                "Provides an intense, high-stakes chess experience.\n" +
                "Challenges players to master intricate strategies.\n" +
                "An arena for honing expertise and pushing boundaries.\n";

        String imagePath = "project/src/main/java/chess/img/other/";
        easyPanel = createPanel(imagePath + "easy-back.jpg", "Easy", easyDesc,
                imagePath + "easy-icon.png");
        mediumPanel = createPanel(imagePath + "medium-back.jpg", "Medium", mediumDesc,
                imagePath + "medium-icon.png");
        hardPanel = createPanel(imagePath + "hard-back.jpg", "Hard", hardDesc,
                imagePath + "hard-icon.png");
        easyPanel.addMouseListener(createMouseListener(easyPanel));
        mediumPanel.addMouseListener(createMouseListener(mediumPanel));
        hardPanel.addMouseListener(createMouseListener(hardPanel));
        easyPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        mediumPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        hardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

        add(easyPanel);
        add(mediumPanel);
        add(hardPanel);

        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize the frame
        setVisible(true);
    }

    private JPanel createPanel(String backgroundPath, String title, String description, String iconPath) {
        ImageIcon backgroundImage = new ImageIcon(backgroundPath);
        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledIcon = icon.getImage().getScaledInstance(280, 280, Image.SCALE_SMOOTH);
        ImageIcon scaledIconImage = new ImageIcon(scaledIcon);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                g2d.drawImage(backgroundImage.getImage(), 0, 0, w, h, this);

                AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
                g2d.setComposite(composite);
                g2d.setColor(new Color(240, 240, 240, 150)); // A shade closer to white

                if (alpha > 0.0f && hoveredPanel == this) {
                    int borderSize = 10;
                    g2d.fillRect(0, 0, borderSize, h); // Left border
                    g2d.fillRect(w - borderSize, 0, borderSize, h); // Right border
                    g2d.fillRect(0, 0, w, borderSize); // Top border
                    g2d.fillRect(0, h - borderSize, w, borderSize); // Bottom border
                }

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 300); // Customize the panel size
            }

        };
        panel.setLayout(new BorderLayout());

        JLabel iconLabel = new JLabel(scaledIconImage);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(iconLabel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 70)));
        Font titleFont = new Font(Font.SANS_SERIF, Font.ITALIC, 70);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(titleFont);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align horizontally
        titleLabel.setForeground(Color.WHITE);
        contentPanel.add(titleLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        Font descriptionFont = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
        JLabel descriptionLabel = new JLabel("<html>" + description.replaceAll("\n", "<br>") + "</html>");
        descriptionLabel.setFont(descriptionFont);
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        descriptionLabel.setForeground(new Color(255, 255, 255)); // White color
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descriptionLabel.setVerticalAlignment(SwingConstants.TOP);
        contentPanel.add(descriptionLabel);

        contentPanel.setOpaque(false);

        panel.add(contentPanel, BorderLayout.CENTER);

        JButton button = new JButton(title);
        if (title.equals("Easy")) {
            button.setBackground(new Color(100, 200, 100)); // Green color for Easy
        } else if (title.equals("Medium")) {
            button.setBackground(new Color(200, 200, 100)); // Yellow color for Medium
        } else if (title.equals("Hard")) {
            button.setBackground(new Color(255, 100, 100)); // Red color for Hard
        }
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        panel.add(button, BorderLayout.SOUTH);

        return panel;
    }

    private MouseAdapter createMouseListener(JPanel panel) {
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (hoveredPanel == null) {
                    hoveredPanel = panel;
                    alpha = 1.0f;
                    panel.repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (hoveredPanel == panel) {
                    hoveredPanel = null;
                    alpha = 0.0f;
                    panel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point point = e.getPoint();
                if (!panel.contains(point) && hoveredPanel == panel) {
                    hoveredPanel = null;
                    alpha = 0.0f;
                    panel.repaint();
                }
            }
        };
    }
}
