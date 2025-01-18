package com.sda.tictactoe;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeController {

    String playerValue = "";
    String opponentValue = "";
    boolean activeGame;
    Random random = new Random();
    List<Boolean> playedTiles = new ArrayList<>();  // this keeps track of which boxes are in use
    List<Label> boxes;
    Has_Won hasWon;

    // Array representing winning combinations on a 3x3 board
    private final int[][] winCombinations = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // Rows
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // Columns
            {0, 4, 8}, {2, 4, 6}              // Diagonals
    };

    @FXML
    public AnchorPane mainAnchorPane;

    @FXML
    private Pane matchPane, blockerPane;

    @FXML
    Label box1, box2, box3, box4, box5, box6, box7, box8, box9, chooseSide;

    @FXML
    Button asX, asO, startBtn, restartBtn, resetBtn;

    @FXML
    ToggleButton darkModeBtn;

    @FXML
    VBox playAgain;

    @FXML
    void setTile(MouseEvent event) {
        Label source = (Label) event.getSource();
        if(source.getText().isBlank()){
            source.setText(playerValue);
            int playedTile = boxes.indexOf(source);
            setPlayedTiles(playedTile);
            winCheck(playerValue);

            if(hasWon != Has_Won.Y && hasWon != Has_Won.T) {
                battleBot();
            }
        }
    }

    @FXML
    private void setPlayerValue(ActionEvent event) {
        if (event.getSource() == asO && !activeGame && !playerValue.equals("O")) {
            playerValue = "O";
            opponentValue = "X";
            chooseSide.setText(playerValue);
        } else if (event.getSource() == asX && !activeGame && !playerValue.equals("X")) {
            playerValue = "X";
            opponentValue = "O";
            chooseSide.setText(playerValue);
        } else {
            playerValue = random.nextInt() > 0.5 ? "X" : "O";
            if(playerValue.equals("X")) {
                opponentValue = "O";
            } else opponentValue = "X";
        }
    }

    public void activeMatch(ActionEvent event) {
        if(event.getSource() == startBtn && !activeGame) {
            if(playerValue.isBlank()|| playerValue.isEmpty()) {
                setPlayerValue(event);
            }
            activeGame = true;
            setMatchVisibility();
            blockerPane.setVisible(false);
        } else reset();
    }

    public void reset() {
        activeGame = false;
        startBtn.setDisable(false);
        setMatchVisibility();
        playerValue = "";
        opponentValue = "";
        hasWon = Has_Won.N;
        chooseSide.setText("Choose");
        playAgain.setVisible(false);
        blockerPane.setVisible(false);

        if(asX.isDisabled() || asO.isDisabled()) {
            asX.setDisable(false);
            asO.setDisable(false);
        }

        for(int i = 0; i < 9; i++) {
            boxes.get(i).setText("");
            playedTiles.set(i, false);
        }
    }

    public void setMatchVisibility() {
        matchPane.setVisible(activeGame);
        chooseSide.setText(playerValue);
    }

    public void winCheck(String identifier) {

        hasWon = Has_Won.N;

        for (int[] winCombo : winCombinations) {
            if (boxes.get(winCombo[0]).getText().equals(identifier) &&
                    boxes.get(winCombo[1]).getText().equals(identifier) &&
                    boxes.get(winCombo[2]).getText().equals(identifier)) {
                hasWon = Has_Won.Y;
            }
        }

        if(hasWon == Has_Won.Y) {
            chooseSide.setText(identifier.equals(playerValue) ? "You\n Win!" : "You\n Lose!");
            playAgain.setVisible(true);
            blockerPane.setVisible(true);
        }
        if(!playedTiles.contains(false) && hasWon != Has_Won.Y) {
            chooseSide.setText("Tie!");
            playAgain.setVisible(true);
            hasWon = Has_Won.T;
            blockerPane.setVisible(true);
        }
    }

    private void battleBot() {

        // looks for a winning move for the robot
        int botMove = findWinningMove(opponentValue);

        // looks for if the player has a winning move and blocks it
        if(botMove == -1) {
            botMove = findWinningMove(playerValue);
        }

        // takes the middle if no winning/blocking move is found
        if(botMove == -1 && boxes.get(4).getText().isBlank()) {
            botMove = 4;
        }

        // chooses a "random" free corner to go into
        if(botMove == -1) {
            int[] corners = {0, 2, 6, 8};
            botMove = findRandomMove(corners);
        }

        if(botMove == -1) {
            int[] sides = {1, 3, 5, 7};
            botMove = findRandomMove(sides);
        }

        if(botMove != -1) {
            boxes.get(botMove).setText(opponentValue);
            setPlayedTiles(botMove);
            winCheck(opponentValue);
        }

    }

    private int findWinningMove(String identifier) {
        for(int[] winCombo : winCombinations) {

            int playerSpots = 0;
            int emptySpot = -1;

            for(int i : winCombo) {
                if(boxes.get(i).getText().equals(identifier)) {
                    playerSpots++;
                } else if(boxes.get(i).getText().isBlank()) {
                    emptySpot = i;
                }
            }

            if(playerSpots == 2 && emptySpot != -1) {
                return emptySpot;
            }
        }
        return -1;
    }

    private int findRandomMove(int[] positions) {
        for(int pos : positions) {
            if(boxes.get(pos).getText().isBlank()) {
                return pos;
            }
        }
        return -1;
    }



    public void setPlayedTiles(int playedBox) {
        playedTiles.set(playedBox, true);
    }

    public void darkMode(MouseEvent event) {
        if(!darkModeBtn.isSelected()) {
            for(Label box : boxes) {
                box.setStyle("-fx-text-fill: #fff");
            }
        } else{
            for(Label box : boxes) {
                box.setStyle("-fx-text-fill: black");
            }
        }

    }


    protected void initList() { // I ran into issues, so I made the main application file initialize the List :D
        playAgain.setVisible(false);
        chooseSide.setAlignment(Pos.CENTER);
        boxes = List.of(box1, box2, box3, box4, box5, box6, box7, box8, box9);

        for(int i = 0; i < 9; i++) {
            playedTiles.add(i, false);
        }

    }

}