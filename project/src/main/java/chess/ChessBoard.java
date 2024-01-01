package chess;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.BoardEvent;
import com.github.bhlangonijr.chesslib.BoardEventListener;
import com.github.bhlangonijr.chesslib.BoardEventType;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Rank;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.List;

public class ChessBoard extends JFrame {
    class SquareLabel extends JLabel {
        private boolean isHighlighted = false;
        private BufferedImage pieceImage;

        public void setHighlighted(boolean highlighted) {
            isHighlighted = highlighted;
            repaint();
        }

        public void setPieceImage(BufferedImage image) {
            this.pieceImage = image;
            repaint();
        }

        public BufferedImage getPieceImage() {
            return pieceImage;
        }

        public boolean isHighlighted() {
            return isHighlighted;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (pieceImage != null) {
                int width = currentTileSize;
                int height = currentTileSize;
                int x = (getWidth() - width) / 2;
                int y = (getHeight() - height) / 2;
                g.drawImage(pieceImage, x, y, width, height, this);
            }
            if (isHighlighted) {
                if (pieceImage == null) {
                    int diameter = currentTileSize / 3;
                    int x = (getWidth() - diameter) / 2;
                    int y = (getHeight() - diameter) / 2;

                    // Set the color with transparency
                    Color semiTransparentGray = new Color(128, 128, 128, 145); // Change alpha value as needed

                    // Draw the semi-transparent filled oval
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(semiTransparentGray);
                    g2d.fillOval(x, y, diameter, diameter);
                    g2d.dispose();
                } else {
                    int diameter = currentTileSize / 3;
                    int x = (getWidth() - diameter) / 2;
                    int y = (getHeight() - diameter) / 2;

                    // Set the color with transparency
                    Color semiTransparentGray = new Color(255, 0, 0, 120); // Change alpha value as needed

                    // Draw the semi-transparent filled oval
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(semiTransparentGray);
                    g2d.fillOval(x, y, diameter, diameter);
                    g2d.dispose();
                }

            }

        }
    }

    class MyBoardListener implements BoardEventListener {

        @Override
        public void onEvent(BoardEvent event) {

            if (event.getType() == BoardEventType.ON_MOVE) {
                if (boardState.isMated()) {
                    if (boardState.getSideToMove() == Side.BLACK) {
                        SwingUtilities.invokeLater(() -> new ResultScreen(c, "White"));
                    } else {
                        SwingUtilities.invokeLater(() -> new ResultScreen(c, "Black"));
                    }
                } else if (boardState.isDraw()) {
                    SwingUtilities.invokeLater(() -> new ResultScreen(c, "Draw"));
                }
            }
        }
    }

    private JFrame c = this;
    private JPanel chessBoard;
    private Board boardState;
    private final int initialTileSize = 8;
    private final Color lightSquareColor = new Color(235, 236, 208);
    private final Color darkSquareColor = new Color(119, 148, 85);
    private final int minWindowSize = 512;
    private final Color selectionColor = Color.YELLOW;
    private int currentTileSize = initialTileSize;
    private SquareLabel[][] allLabels;
    private String highlightedPiecePosition = "";
    private Map<Character, BufferedImage> pieceImageMap = new HashMap<>();
    Piece piece = null;
    private Piece selectedPiece = null;
    private Mode mode = null;
    private String lastHumanMove = null;
    private String moveString = "";
    private String stockFishMove = "";

    public ChessBoard(Mode mode) {
        setTitle("Chess Board");
        setMinimumSize(new Dimension(minWindowSize, minWindowSize));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.mode = mode;
        StockFishHandler.startStockfish("project/src/stockfish/stockfish-windows-x86-64-avx2.exe");

        chessBoard = new JPanel(new GridLayout(initialTileSize, initialTileSize));
        add(chessBoard);

        boardState = new Board();
        boardState.addEventListener(BoardEventType.ON_MOVE, new MyBoardListener());

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                onBoardResize();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_B) {
                    String fen = boardState.getFen(false);
                    if (!fen.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -")) {
                        boardState.undoMove();
                        updateBoard(boardState.toString());
                        clearHighlightedSquares();
                        onBoardResize();
                    }
                }
            }
        });
        setFocusable(true);
        requestFocus();
        initBoard();
        setVisible(true);
        if (mode == Mode.PVE_EASY_BLACK || mode == Mode.PVE_MEDIUM_BLACK || mode == Mode.PVE_HARD_BLACK) {
            if (mode == Mode.PVE_EASY_BLACK || mode == Mode.PVE_MEDIUM_BLACK) {
                makeRandomMove();
            } else {
                String StockFishMove = StockFishHandler.getStockfishMove(lastHumanMove);
                stockFishMove = StockFishMove;
                Move move = parseStockFishMove(StockFishMove);
                boardState.doMove(move);
                moveString += stockFishMove + " ";
                updateBoard(boardState.toString());
            }
        }
    }

    private void loadPieceImages() {
        String imagePath = "project/src/main/java/chess/img/pieces/";
        pieceImageMap.put('K', loadImage(imagePath + "WK.png"));
        pieceImageMap.put('Q', loadImage(imagePath + "WQ.png"));
        pieceImageMap.put('R', loadImage(imagePath + "WR.png"));
        pieceImageMap.put('N', loadImage(imagePath + "WN.png"));
        pieceImageMap.put('B', loadImage(imagePath + "WB.png"));
        pieceImageMap.put('P', loadImage(imagePath + "WP.png"));
        pieceImageMap.put('k', loadImage(imagePath + "BK.png"));
        pieceImageMap.put('q', loadImage(imagePath + "BQ.png"));
        pieceImageMap.put('r', loadImage(imagePath + "BR.png"));
        pieceImageMap.put('n', loadImage(imagePath + "BN.png"));
        pieceImageMap.put('b', loadImage(imagePath + "BB.png"));
        pieceImageMap.put('p', loadImage(imagePath + "BP.png"));

    }

    private void onBoardResize() {
        SwingUtilities.invokeLater(() -> {
            int width = getContentPane().getWidth();
            int height = getContentPane().getHeight();
            int minDimension = Math.min(width, height);
            currentTileSize = minDimension / initialTileSize;
            int xInset = (width - currentTileSize * initialTileSize) / 2;
            int yInset = (height - currentTileSize * initialTileSize) / 2;

            for (int i = 0; i < initialTileSize; i++) {
                for (int j = 0; j < initialTileSize; j++) {
                    JLabel square = allLabels[i][j];
                    square.setPreferredSize(new Dimension(currentTileSize, currentTileSize));
                }
            }

            chessBoard.setBorder(BorderFactory.createEmptyBorder(yInset, xInset, yInset, xInset));
            chessBoard.revalidate();
            chessBoard.repaint();
        });
    }

    private void initBoard() {
        allLabels = new SquareLabel[initialTileSize][initialTileSize];
        loadPieceImages();
        for (int i = 0; i < initialTileSize; i++) {
            for (int j = 0; j < initialTileSize; j++) {
                SquareLabel square = new SquareLabel();
                square.setPreferredSize(new Dimension(64, 64));
                square.setOpaque(true);
                square.setBackground((i + j) % 2 == 0 ? lightSquareColor : darkSquareColor);

                char pieceChar = determinePieceChar(i, j);
                if (pieceChar != '.') {
                    BufferedImage pieceImage = pieceImageMap.get(pieceChar);
                    square.setPieceImage(pieceImage);
                }

                square.addMouseListener(new SquareClickListener());
                allLabels[i][j] = square;
                chessBoard.add(square);
            }
        }
    }

    private BufferedImage loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBoard(String boardString) {
        int index = 0;
        for (int i = 0; i < initialTileSize; i++) {
            for (int j = 0; j < initialTileSize; j++) {
                SquareLabel square = allLabels[i][j];
                char pieceChar = boardString.charAt(index++);

                if (pieceChar != '\n') {
                    if (pieceChar != '.') {
                        BufferedImage pieceImage = pieceImageMap.get(pieceChar);
                        square.setPieceImage(pieceImage);
                    } else {
                        square.setPieceImage(null);
                    }
                } else {
                    j--; // Go back one column to handle the new line character
                }
            }
        }
    }

    private char determinePieceChar(int row, int col) {
        if (row == 0 || row == 1 || row == 6 || row == 7) {
            boolean isWhitePiece = (row == 6 || row == 7);
            char pieceChar = getPiecePrefix(row, col);
            if (!isWhitePiece) {
                return Character.toLowerCase(pieceChar);
            } else
                return pieceChar;
        }
        return '.';
    }

    private char getPiecePrefix(int row, int col) {
        if (row == 0 || row == 7) {
            switch (col) {
                case 0:
                case 7:
                    return 'R';
                case 1:
                    return 'N';
                case 2:
                case 5:
                    return 'B';
                case 3:
                    return 'Q';
                case 4:
                    return 'K';
                case 6:
                    return 'N';
                default:
                    return 'P';
            }
        }
        return 'P';
    }

    class SquareClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            SquareLabel clickedSquare = (SquareLabel) e.getSource();
            int index = findLabelIndex(clickedSquare);
            int row = index / initialTileSize;
            int col = index % initialTileSize;
            String chessPosition = getChessPosition(row, col);
            piece = boardState.getPiece(Square.valueOf(chessPosition));

            if (mode == Mode.PVP) {
                if (clickedSquare.isHighlighted) {
                    makeMove(highlightedPiecePosition, chessPosition);
                    lastHumanMove = null;
                    lastHumanMove += highlightedPiecePosition + chessPosition;
                    clearHighlightedSquares();
                } else if (clickedSquare.getPieceImage() != null && clickedSquare.getBackground() != selectionColor
                        && piece.getPieceSide() == boardState.getSideToMove()) {
                    clearHighlightedSquares();
                    lastHumanMove = null;
                    lastHumanMove += highlightedPiecePosition + chessPosition;
                    clickedSquare.setBackground(selectionColor);
                    highlightLegalMovesForPiece(chessPosition);
                    highlightedPiecePosition = chessPosition;
                } else {
                    clickedSquare.setBackground((row + col) % 2 == 0 ? lightSquareColor : darkSquareColor);
                    clearHighlightedSquares();
                }
            } else {
                if (mode == Mode.PVE_EASY_WHITE || mode == Mode.PVE_MEDIUM_WHITE || mode == Mode.PVE_HARD_WHITE) {
                    if (boardState.getSideToMove() == Side.WHITE) {
                        if (mode == Mode.PVE_HARD_WHITE) {
                            if (clickedSquare.isHighlighted) {
                                makeMove(highlightedPiecePosition, chessPosition);
                                clearHighlightedSquares();
                                makeAIMove(highlightedPiecePosition, chessPosition);

                            } else if (clickedSquare.getPieceImage() != null
                                    && clickedSquare.getBackground() != selectionColor) {
                                clearHighlightedSquares();
                                clickedSquare.setBackground(selectionColor);
                                highlightLegalMovesForPiece(chessPosition);
                                highlightedPiecePosition = chessPosition;
                            } else {
                                clickedSquare.setBackground((row + col) % 2 == 0 ? lightSquareColor : darkSquareColor);
                                clearHighlightedSquares();
                            }
                        } else {
                            if (clickedSquare.isHighlighted) {
                                makeMove(highlightedPiecePosition, chessPosition);
                                clearHighlightedSquares();
                                makeRandomMove();

                            } else if (clickedSquare.getPieceImage() != null
                                    && clickedSquare.getBackground() != selectionColor) {
                                clearHighlightedSquares();
                                clickedSquare.setBackground(selectionColor);
                                highlightLegalMovesForPiece(chessPosition);
                                highlightedPiecePosition = chessPosition;
                            } else {
                                clickedSquare.setBackground((row + col) % 2 == 0 ? lightSquareColor : darkSquareColor);
                                clearHighlightedSquares();
                            }
                        }
                    }
                } else {
                    if (boardState.getSideToMove() == Side.BLACK) {
                        if (mode == Mode.PVE_HARD_BLACK) {
                            if (clickedSquare.isHighlighted) {
                                makeMove(highlightedPiecePosition, chessPosition);
                                clearHighlightedSquares();
                                makeAIMove(highlightedPiecePosition, chessPosition);
                            } else if (clickedSquare.getPieceImage() != null
                                    && clickedSquare.getBackground() != selectionColor
                                    && piece.getPieceSide() == boardState.getSideToMove()) {
                                clearHighlightedSquares();
                                clickedSquare.setBackground(selectionColor);
                                highlightLegalMovesForPiece(chessPosition);
                                highlightedPiecePosition = chessPosition;
                            } else {
                                clickedSquare.setBackground((row + col) % 2 == 0 ? lightSquareColor : darkSquareColor);
                                clearHighlightedSquares();
                            }
                        } else {
                            if (clickedSquare.isHighlighted) {
                                makeMove(highlightedPiecePosition, chessPosition);
                                clearHighlightedSquares();
                                makeRandomMove();
                            } else if (clickedSquare.getPieceImage() != null
                                    && clickedSquare.getBackground() != selectionColor
                                    && piece.getPieceSide() == boardState.getSideToMove()) {
                                clearHighlightedSquares();
                                clickedSquare.setBackground(selectionColor);
                                highlightLegalMovesForPiece(chessPosition);
                                highlightedPiecePosition = chessPosition;
                            } else {
                                clickedSquare.setBackground((row + col) % 2 == 0 ? lightSquareColor : darkSquareColor);
                                clearHighlightedSquares();
                            }
                        }
                    }
                }
            }

        }
    }

    private void makeAIMove(String highlightedPiecePosition, String chessPosition) {
        lastHumanMove = "";
        lastHumanMove += highlightedPiecePosition.toLowerCase() + chessPosition.toLowerCase();
        moveString += lastHumanMove + " ";
        stockFishMove = StockFishHandler.getStockfishMove(moveString);
        moveString += stockFishMove + " ";
        Move move = parseStockFishMove(stockFishMove);
        boardState.doMove(move);
        updateBoard(boardState.toString());
    }

    private int findLabelIndex(JLabel label) {
        for (int i = 0; i < initialTileSize; i++) {
            for (int j = 0; j < initialTileSize; j++) {
                if (allLabels[i][j] == label) {
                    return i * initialTileSize + j;
                }
            }
        }
        return -1;
    }

    private boolean isPromoRank(Side side, Move move) {
        if (side.equals(Side.WHITE) &&
                move.getTo().getRank().equals(Rank.RANK_8)) {
            return true;
        } else
            return side.equals(Side.BLACK) &&
                    move.getTo().getRank().equals(Rank.RANK_1);

    }

    private void makeMove(String fromPosition, String toPosition) {

        Move move = new Move(Square.valueOf(fromPosition), Square.valueOf(toPosition));
        Side currentSide = boardState.getSideToMove();
        if (isPromoRank(currentSide, move)) {
            Piece piece = showPromotionDialog(currentSide);
            move = new Move(Square.valueOf(fromPosition), Square.valueOf(toPosition), piece);
        }
        boardState.doMove(move);
        updateBoard(boardState.toString());
        System.out.println(boardState.toString());
    }

    private Move parseStockFishMove(String moveString) {
        if (moveString.length() == 4) {
            String fromPosition = moveString.substring(0, 2).toUpperCase();
            String toPosition = moveString.substring(2, 4).toUpperCase();
            return new Move(Square.valueOf(fromPosition), Square.valueOf(toPosition));
        } else if (moveString.length() == 5) {
            String fromPosition = moveString.substring(0, 2).toUpperCase();
            String toPosition = moveString.substring(2, 4).toUpperCase();
            String promotion = moveString.substring(4, 5).toUpperCase();
            Piece promotionPiece = null;
            switch (promotion) {
                case "Q":
                    promotionPiece = Piece.WHITE_QUEEN;
                    break;
                case "N":
                    promotionPiece = Piece.WHITE_KNIGHT;
                    break;
                case "B":
                    promotionPiece = Piece.WHITE_BISHOP;
                    break;
                case "R":
                    promotionPiece = Piece.WHITE_ROOK;
                    break;
            }
            return new Move(Square.valueOf(fromPosition), Square.valueOf(toPosition), promotionPiece);
        } else {
            throw new IllegalArgumentException("Invalid move string length");
        }
    }

    private Piece showPromotionDialog(Side currentSide) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Promotion");
        dialog.setModal(true);
        dialog.setLayout(new BorderLayout());

        JLabel headingLabel = new JLabel("Select Promotion Piece");
        headingLabel.setHorizontalAlignment(JLabel.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(headingLabel, BorderLayout.NORTH);
        dialog.add(topPanel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        dialog.add(buttonsPanel, BorderLayout.CENTER);

        selectedPiece = (currentSide == Side.BLACK) ? Piece.BLACK_QUEEN : Piece.WHITE_QUEEN;
        Piece[] pieces = {
                (currentSide == Side.BLACK) ? Piece.BLACK_QUEEN : Piece.WHITE_QUEEN,
                (currentSide == Side.BLACK) ? Piece.BLACK_ROOK : Piece.WHITE_ROOK,
                (currentSide == Side.BLACK) ? Piece.BLACK_BISHOP : Piece.WHITE_BISHOP,
                (currentSide == Side.BLACK) ? Piece.BLACK_KNIGHT : Piece.WHITE_KNIGHT
        };

        String imagePath = "project/src/main/java/chess/img/pieces/";

        for (int i = 0; i < 4; i++) {
            JButton button = new JButton();
            int buttonSize = currentTileSize * 2; // Adjust button size as needed
            button.setPreferredSize(new Dimension(buttonSize, buttonSize));

            ImageIcon icon = new ImageIcon(
                    imagePath + ((currentSide == Side.BLACK) ? "B" : "W") + getImageName(i) + ".png");
            System.out.println("Current directory: " + System.getProperty("user.dir"));
            System.out.println("Image path: " + imagePath);
            Image img = icon.getImage().getScaledInstance(buttonSize - 10, buttonSize - 10, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(img);

            Piece piece = pieces[i]; // Capture the associated piece
            button.setIcon(scaledIcon);
            button.addActionListener(e -> {
                selectedPiece = piece;
                dialog.dispose();
            });

            buttonsPanel.add(button);
        }

        int x = chessBoard.getLocationOnScreen().x + chessBoard.getWidth() / 4;
        int y = chessBoard.getLocationOnScreen().y;
        dialog.setLocation(x, y);
        dialog.pack();
        dialog.setVisible(true);
        return selectedPiece; // Return the selected piece
    }

    private String getImageName(int index) {
        return switch (index) {
            case 0 -> "Q";
            case 1 -> "R";
            case 2 -> "B";
            case 3 -> "N";
            default -> "";
        };
    }

    private void highlightSquare(int row, int col) {
        allLabels[row][col].setHighlighted(true);
    }

    private void unhighlightSquare(int row, int col) {
        allLabels[row][col].setHighlighted(false);
    }

    private void clearHighlightedSquares() {
        for (int i = 0; i < initialTileSize; i++) {
            for (int j = 0; j < initialTileSize; j++) {
                unhighlightSquare(i, j);
                if (allLabels[i][j].getBackground() == selectionColor) {
                    allLabels[i][j].setBackground((i + j) % 2 == 0 ? lightSquareColor : darkSquareColor);
                }
            }
        }
    }

    private void highlightLegalMovesForPiece(String chessPosition) {
        List<Move> legalMovesForPosition = boardState.legalMoves();
        for (Move move : legalMovesForPosition) {
            String fromPos = move.getFrom().value();
            if (fromPos.equals(chessPosition)) {
                String toPos = move.getTo().value();
                int[] to = getRowAndColFromChessPosition(toPos);
                int toRow = to[0];
                int toCol = to[1];
                highlightSquare(toRow, toCol);
            }
        }
    }

    private String getChessPosition(int row, int col) {
        char file = (char) ('a' + col);
        file = Character.toUpperCase(file);
        char rank = (char) ('8' - row);
        return String.valueOf(file) + rank;
    }

    private int[] getRowAndColFromChessPosition(String chessPosition) {
        int col = (chessPosition.charAt(0) - 'A'); // Assuming 'A' to 'H' for columns
        int row = (8 - Character.getNumericValue(chessPosition.charAt(1)));
        return new int[] { row, col };
    }

    private void makeRandomMove() {
        List<Move> legalMoves = boardState.legalMoves();
        while (true) {
            Random random = new Random();
            int randomIndex = random.nextInt(legalMoves.size());
            Move move = legalMoves.get(randomIndex);
            if (boardState.getPiece(move.getFrom()).getPieceSide() == Side.BLACK) {
                boardState.doMove(move);
                updateBoard(boardState.toString());
                break;
            } else if (boardState.getPiece(move.getFrom()).getPieceSide() == Side.WHITE) {
                boardState.doMove(move);
                updateBoard(boardState.toString());
                break;
            }
        }
    }
}
