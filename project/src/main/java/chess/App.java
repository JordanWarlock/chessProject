package chess;

import javax.swing.SwingUtilities;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.BoardEvent;
import com.github.bhlangonijr.chesslib.BoardEventListener;
import com.github.bhlangonijr.chesslib.BoardEventType;
import com.github.bhlangonijr.chesslib.move.Move;

public class App {
    public static void main(String[] args) {
        Board board = new Board();
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.setBoardState(board); // Assuming 'board' is an instance of the Board class
        SwingUtilities.invokeLater(() -> chessBoard.setVisible(true));
        class MyBoardListener implements BoardEventListener {
            @Override
            public void onEvent(BoardEvent event) {

                if (event.getType() == BoardEventType.ON_MOVE) {
                    Move move = (Move) event;
                    System.out.println("Move " + move + " was played");
                }
            }

        }
        board.addEventListener(BoardEventType.ON_MOVE, new MyBoardListener());

    }

}
