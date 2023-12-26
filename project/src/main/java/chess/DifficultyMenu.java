package chess;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultyMenu extends JFrame {
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;

    public DifficultyMenu() {
        setTitle("Difficulty Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(null);

        easyButton = new JButton("Easy");
        mediumButton = new JButton("Medium");
        hardButton = new JButton("Hard");

        easyButton.setBounds(50, 50, 200, 30);
        mediumButton.setBounds(50, 100, 200, 30);
        hardButton.setBounds(50, 150, 200, 30);

        add(easyButton);
        add(mediumButton);
        add(hardButton);

        easyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new ChooseSideMenu("EASY"));
                dispose();
            }
        });
        mediumButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new ChooseSideMenu("MEDIUM"));
                dispose();
            }
        });
        hardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> new ChooseSideMenu("HARD"));
                dispose();
            }
        });

        setVisible(true);
    }
}