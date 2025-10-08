src/main/
├── Game.java                    # Console-based game
├── GameGUI.java                 # GUI-based game with game mode selection
├── ai/
│   ├── AI.java                  # Minimax algorithm implementation
│   └── Heuristic.java           # H1 and H2 evaluation functions
├── gameobjects/
│   ├── GameBoard.java           # Board state and move generation
│   ├── Cell.java                # Individual cell representation
│   ├── Player.java              # Player state
│   ├── Move.java                # Move representation
│   └── GameState.java           # State saving/restoration for AI
└── experiment/
    └── gameTrials.java          # Automated AI matchup experiments

    GUI Version
src/main/GameGUI.java
main.GameGUI
Select your preferred game mode from the dialog:

Human vs Human: Two players take turns
Human vs AI: Play against the computer (depth 1 search)
AI vs AI: Watch two AIs compete (choose heuristic matchup)

Run Experiments
java main.experiment.gameTrials
Generates experiment_results_final.csv with match data from 200 games.

AI Configuration
The AI depth and heuristic can be modified in GameGUI.java:
singleAI = new AI(board, false, 1);  // H1 heuristic, depth 1
singleAI = new AI(board, true, 3);   // H2 heuristic, depth 3


Controls:
Move Phase: Click on an adjacent cell to move your pawn
Removal Phase: Click on any available cell to remove its token
Restart: Click "Restart Game" button
Exit: Click "Exit" button or close the window


Required: Java 8 or higher
