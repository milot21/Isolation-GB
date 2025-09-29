package main;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameGUI extends JFrame {
  private GameBoard board;
  private Player p1, p2;
  private Player currentPlayer;
  private JButton[][] buttons;
  private boolean isMovingPhase = true; // move phase = true, removal phase = false
  private int selectedMoveRow = -1;
  private int selectedMoveCol = -1;
  private JLabel statusLabel;
  private AI ai;
  private boolean isAIGame = false;

  public GameGUI() {
    this(false);
  }

  public GameGUI(boolean withAI) {
    this.isAIGame = withAI;

    // setup game board
    board = new GameBoard();
    p1 = board.getP1();
    p2 = board.getP2();
    currentPlayer = board.getP1();

    if (isAIGame) {
      ai = new AI(board);
    }

    setupGUI();
  }

  private void setupGUI() {
    setTitle(isAIGame ? "Isolation Game - Human vs AI" : "Isolation Game - Human vs Human");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // board panel
    JPanel boardPanel = new JPanel(new GridLayout(board.getRows(), board.getCols(), 2, 2));
    boardPanel.setBackground(Color.BLACK);
    boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    buttons = new JButton[board.getRows()][board.getCols()];

    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(70, 70));
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);

        final int row = i, col = j;
        btn.addActionListener(e -> handleClick(row, col));
        buttons[i][j] = btn;
        boardPanel.add(btn);
      }
    }

    // control panel
    JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
    controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    statusLabel = new JLabel("Player 1's turn - Move your pawn", SwingConstants.CENTER);
    statusLabel.setFont(new Font("Arial", Font.BOLD, 16));

    JButton restartButton = new JButton("Restart Game");
    restartButton.setFont(new Font("Arial", Font.PLAIN, 14));
    restartButton.addActionListener(e -> restartGame());

    JButton exitButton = new JButton("Exit");
    exitButton.setFont(new Font("Arial", Font.PLAIN, 14));
    exitButton.addActionListener(e -> System.exit(0));

    controlPanel.add(statusLabel);
    controlPanel.add(restartButton);
    controlPanel.add(exitButton);

    add(boardPanel, BorderLayout.CENTER);
    add(controlPanel, BorderLayout.SOUTH);

    updateBoard();
    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }

  private void handleClick(int row, int col) {
    // no clicks during AI turn
    if (isAIGame && currentPlayer == p2) {
      return;
    }

    if (isMovingPhase) {
      handleMovePhase(row, col);
    } else {
      handleRemovalPhase(row, col);
    }
  }

  private void handleMovePhase(int row, int col) {
    // Get all legal moves for current player
    List<Move> legalMoves = board.getLegalMoves(currentPlayer);

    // Check if clicked cell is a valid move destination
    Move nextMove = null;
    for (Move move : legalMoves) {
      if (move.getNewRow() == row && move.getNewCol() == col) {
        nextMove = move;
        break;
      }
    }

    if (nextMove != null) {
      // Clear old position
      board.getCell(currentPlayer.getRow(), currentPlayer.getCol()).setPlayer(null);

      // Move player to new position
      currentPlayer.setRow(row);
      currentPlayer.setCol(col);
      board.getCell(row, col).setPlayer(currentPlayer);

      // Store move location for removal phase
      selectedMoveRow = row;
      selectedMoveCol = col;

      // Switch to removal phase
      isMovingPhase = false;
      updateStatus("Remove a token from any available cell");
      updateBoard();
    } else {
      updateStatus("Invalid move! Select an adjacent cell with a token.");
    }
  }

  private void handleRemovalPhase(int row, int col) {
    Cell cell = board.getCell(row, col);

    // Check if the cell is valid for removal
    if (!cell.isRemoved() && cell.getPlayer() == null &&
        !(row == selectedMoveRow && col == selectedMoveCol)) {

      // Remove the token
      cell.removeToken();

      // Switch players
      currentPlayer = (currentPlayer == p1) ? p2 : p1;
      isMovingPhase = true;

      // Check for game over
      if (board.getLegalMoves(currentPlayer).isEmpty()) {
        Player winner = (currentPlayer == p1) ? p2 : p1;
        handleGameOver(winner);
        return;
      }

      // Update UI
      String playerName = (currentPlayer == p1) ? "Player 1" : (isAIGame ? "AI" : "Player 2");
      updateStatus(playerName + "'s turn - Move your pawn");
      updateBoard();

      // If it's AI's turn, make AI move
      if (isAIGame && currentPlayer == p2) {
        SwingUtilities.invokeLater(() -> makeAIMove());
      }
    } else {
      updateStatus("Invalid selection! Choose a different cell to remove.");
    }
  }

  private void makeAIMove() {
    setButtonsEnabled(false);
    updateStatus("AI is thinking...");

    // delay for visual effect
    Timer thinkingTimer = new Timer(800, e -> {
      Move aiMove = ai.chooseMove(p2, p1);

      if (aiMove != null) {
        board.applyMove(p2, aiMove);

        // Move AI
        p2.setRow(aiMove.getNewRow());
        p2.setCol(aiMove.getNewCol());

        // Switch back to human player
        currentPlayer = p1;

        // Check for game over
        if (board.getLegalMoves(currentPlayer).isEmpty()) {
          handleGameOver(p2);
          return;
        }

        updateStatus("Player 1's turn - Move your pawn");
        setButtonsEnabled(true);
        updateBoard();
      } else {
        // AI has no moves - human wins
        handleGameOver(p1);
      }
    });
    thinkingTimer.setRepeats(false);
    thinkingTimer.start();
  }

  private void handleGameOver(Player winner) {
    String winnerName = (winner == p1) ? "Player 1" : (isAIGame ? "AI" : "Player 2");
    updateStatus("Game Over! " + winnerName + " wins!");

    // Ask if they want to exit
    int exitChoice = JOptionPane.showConfirmDialog(
        this,
        "Game Over! " + winnerName + " wins!\nWould you like to exit?",
        "Game Over",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE
    );

    if (exitChoice == JOptionPane.YES_OPTION) {
      System.exit(0);
    } else {
      // Ask if they want to restart
      int restartChoice = JOptionPane.showConfirmDialog(
          this,
          "Restart Game?",
          "Restart",
          JOptionPane.YES_NO_OPTION,
          JOptionPane.QUESTION_MESSAGE
      );

      if (restartChoice == JOptionPane.YES_OPTION) {
        restartGame();
      }
    }
  }

  private void updateBoard() {
    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        JButton btn = buttons[i][j];
        Cell cell = board.getCell(i, j);

        if (cell.getPlayer() != null) {
          if (cell.getPlayer() == p1) {
            btn.setText("P1");
            btn.setBackground(new Color(0, 200, 0));
            btn.setForeground(Color.WHITE);
          } else {
            btn.setText("P2");
            btn.setBackground(new Color(0, 100, 200));
            btn.setForeground(Color.WHITE);
          }
        } else if (cell.isRemoved()) {
          btn.setText("");
          btn.setBackground(new Color(100, 100, 100));
        } else {
          btn.setText("●");
          btn.setBackground(Color.WHITE);
          btn.setForeground(Color.BLACK);
        }
        btn.setOpaque(true);
        btn.setBorderPainted(true);
      }
    }
  }

  private void updateStatus(String message) {
    statusLabel.setText(message);
  }

  private void setButtonsEnabled(boolean enabled) {
    for (int i = 0; i < board.getRows(); i++) {
      for (int j = 0; j < board.getCols(); j++) {
        buttons[i][j].setEnabled(enabled);
      }
    }
  }

  private void restartGame() {
    dispose();
    new GameGUI(isAIGame);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      String[] options = {"Human vs Human", "Human vs AI"};
      int choice = JOptionPane.showOptionDialog(
          null,
          "Select Game Mode:",
          "Isolation Game",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]
      );

      if (choice >= 0) {
        new GameGUI(choice == 1);
      } else {
        System.exit(0);
      }
    });
  }
}