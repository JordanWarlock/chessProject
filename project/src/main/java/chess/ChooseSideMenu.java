package chess;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChooseSideMenu extends JFrame {
    private JButton blackButton;
    private JButton whiteButton;

    public ChooseSideMenu(String difficulty) {
        setTitle("Mode Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(null);

        blackButton = new JButton("BLACK");
        whiteButton = new JButton("WHITE");

        whiteButton.setBounds(50, 50, 200, 30);
        blackButton.setBounds(50, 100, 200, 30);

        add(whiteButton);
        add(blackButton);

        whiteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (difficulty == "EASY") {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_EASY_WHITE));
                    dispose();
                } else if (difficulty == "MEDIUM") {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_MEDIUM_WHITE));
                    dispose();
                } else {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_HARD_WHITE));
                    dispose();
                }
            }
        });

        blackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (difficulty == "EASY") {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_EASY_BLACK));
                    dispose();
                } else if (difficulty == "MEDIUM") {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_MEDIUM_BLACK));
                    dispose();
                } else {
                    SwingUtilities.invokeLater(() -> new ChessBoard(Mode.PVE_HARD_BLACK));
                    dispose();
                }
            }
        });

        setVisible(true);
    }
}
