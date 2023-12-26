package chess;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ModeMenu extends JFrame {
    private JButton pvpButton;
    private JButton pveButton;

    public ModeMenu() {
        setTitle("Mode Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(null);

        pvpButton = new JButton("Player vs Player");
        pveButton = new JButton("Player vs Computer");

        pvpButton.setBounds(50, 50, 200, 30);
        pveButton.setBounds(50, 100, 200, 30);

        add(pvpButton);
        add(pveButton);

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

        setVisible(true);
    }
}