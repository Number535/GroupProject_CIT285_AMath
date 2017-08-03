package ProjectResources;
// Main_GUI
// Programmer: Easy Group
// Last Modified: 9/23/16

import ProjectResources.BoardTiles.BoardTile;
import ProjectResources.BoardTiles.DoubleAllBoardTile;
import ProjectResources.BoardTiles.TripleAllBoardTile;
import ProjectResources.PlayTiles.PlayTile;
import ProjectResources.PlayTiles.PlusOrMinusTile;
import ProjectResources.PlayTiles.PlusTile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Main_GUI extends Application implements AMathConstants {

    public static void main(String[] args) {
        launch(args);
    }

    private Socket socket;

    private ObjectInputStream fromServerObject;
    private ObjectOutputStream toServerObject;
    private DataInputStream fromServerData;
    private DataOutputStream toServerData;
    private int numPlayer;
    private Bag bag;
    private OnHandTiles onHandTiles;
    private PlayTools playTools;
    private Board board;


    private int totalScore = 0, opponentTotalScore = 0;

    private boolean isMyTurn;
    private static int turnCounter = 1;

    @Override
    public void start(Stage primaryStage) {

        // Tiles Container
        bag = new Bag();
        playTools = new PlayTools();
        
        // Tiles on hand
        onHandTiles = new OnHandTiles();
        
        // Draw tiles from bag to on hand
        for (int i = 0; i < 8; i++) {
            PlayTile temp = bag.draw();
            onHandTiles.add(temp);
        }

        // Board
        board = new Board(onHandTiles);
        

        TextArea chatRoom = new TextArea();
        chatRoom.setWrapText(true);
        chatRoom.setEditable(false);

        HBox typingArea = new HBox();
        TextField tfTyping = new TextField();
        tfTyping.setPromptText("Type here..");
        tfTyping.setPrefWidth(300);
        Button btTyping = new Button("Send");
        btTyping.setPrefWidth(90);
        typingArea.getChildren().addAll(tfTyping, btTyping);


        //Label lbCurrentScore = new Label("Current Score: ");
        Label lbTotalScore = new Label("Total Score: ");
        Label lbTotalOpponentScore = new Label("Opponent Total Score: ");
        Label lbTilesLeft = new Label();

        HBox processButtonArea = new HBox();
        processButtonArea.setAlignment(Pos.CENTER);
        processButtonArea.setSpacing(20);
        Button btSubmit = new Button("Submit");
        btSubmit.setPrefWidth(150);
        Button btChangeTiles = new Button("Change PlayTiles");
        btChangeTiles.setPrefWidth(150);
        processButtonArea.getChildren().addAll(btSubmit, btChangeTiles);


        VBox rightPane = new VBox();
        rightPane.setSpacing(10);
        rightPane.setPadding(new Insets(20, 10, 10, 10));
        rightPane.setPrefHeight(613);
        rightPane.setPrefWidth(400);
        rightPane.setId("rightPane");
        rightPane.getChildren().addAll(chatRoom, typingArea, /*lbCurrentScore,*/
                lbTotalScore, lbTotalOpponentScore, lbTilesLeft, processButtonArea, onHandTiles.getPane());

        VBox leftPane = new VBox();
        leftPane.getChildren().add(board.getBoardPane());

        HBox mainPane = new HBox();
        mainPane.getChildren().addAll(leftPane, rightPane);

        double leftPaneWidth = new Board(new OnHandTiles()).getBoardPane().getPrefWidth();
        //double leftPaneHeight = new Board(new OnHandTiles()).getBoardPane().getPrefHeight();

        Scene scene = new Scene(mainPane, leftPaneWidth + rightPane.getPrefWidth(), rightPane.getPrefHeight());
        scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
        primaryStage.setTitle("A - math");
        primaryStage.setScene(scene);
        primaryStage.show();

        //==================================[ Action Listener ]=======================================
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        tfTyping.setOnAction(event -> {
            if (!tfTyping.getText().equals("")) {
                try {
                    toServerData.writeInt(CHAT);
                    String message = tfTyping.getText();
                    toServerObject.writeObject(message);
                    toServerObject.flush();
                    Platform.runLater(() -> {
                        tfTyping.setText("");
                        chatRoom.appendText("Me: " + message + "\n");
                    });
                }
                catch (Exception ex) {}
            }
        });

        btTyping.setOnAction(event -> {
            if (!tfTyping.getText().equals("")) {
                try {
                    toServerData.writeInt(CHAT);
                    String message = tfTyping.getText();
                    toServerObject.writeObject(message);
                    toServerObject.flush();
                    Platform.runLater(() -> {
                        tfTyping.setText("");
                        chatRoom.appendText("Me: " + message + "\n");
                    });
                }
                catch (Exception ex) {}
            }
        });

        btSubmit.setOnAction(event -> {
          
            ArrayList<BoardTile> onUsedBoardTile = board.getUsedBoardTile();

            int x = onUsedBoardTile.get(0).getX(), y = onUsedBoardTile.get(0).getY();

            if (!playTools.isOnStar(onUsedBoardTile) && turnCounter == 1) {
                showAlert("Invalid Input: One Tile must be placed on the star", Alert.AlertType.ERROR);
                return;
            }

            if (playTools.isHorizontal(onUsedBoardTile, y)) {

                playTools.sortByX(onUsedBoardTile);

                if (playTools.hasMissingSpotHorizontal(onUsedBoardTile) && turnCounter == 1) {
                    showAlert("Invalid Input", Alert.AlertType.ERROR);
                    return;
                }

                if (!playTools.isConnectedWithOldTilesVertical(onUsedBoardTile) &&
                        !playTools.isConnectedWithOldTilesHorizontal(onUsedBoardTile) &&
                        turnCounter != 1) {
                    showAlert("Invalid Input: no connection", Alert.AlertType.ERROR);
                    return;
                }
            }
            else if (playTools.isVertical(onUsedBoardTile, x)) {

                playTools.sortByY(onUsedBoardTile);

                if (playTools.hasMissingSpotVertical(onUsedBoardTile) && turnCounter == 1) {
                    showAlert("Invalid Input", Alert.AlertType.ERROR);
                    return;
                }

                if (!playTools.isConnectedWithOldTilesVertical(onUsedBoardTile) &&
                        !playTools.isConnectedWithOldTilesHorizontal(onUsedBoardTile) &&
                        turnCounter != 1) {
                    showAlert("Invalid Input: no connection", Alert.AlertType.ERROR);
                    return;
                }
            }
            else {
                showAlert("Invalid Input", Alert.AlertType.ERROR);
                return;
            }

            boolean isEquationTrue = false;
            if (playTools.checkWholeBoard(onUsedBoardTile)) {
                showAlert(playTools.getAlertText(), Alert.AlertType.INFORMATION);
                //showAlert("Correct!!", Alert.AlertType.INFORMATION);
                isEquationTrue = true;
            }
            else {
                showAlert(playTools.getAlertText(), Alert.AlertType.ERROR);
                //showAlert("Incorrect!!", Alert.AlertType.ERROR);
                isEquationTrue = false;
            }


            if (isEquationTrue == true) {
                try {
                    BoardTile[] newBoard = board.getBoardUsedArray();

                    toServerData.writeInt(PLAY);
                    toServerObject.writeObject(newBoard);

                    for (int i = onHandTiles.getOnHandPlayTileArray().size(); i < MAX_AMOUNT_ON_HAND; i++) {
                        PlayTile temp = bag.draw();
                        onHandTiles.add(temp);
                    }

                    PlayTile[] newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);

                    totalScore += getTotalScore();
                    toServerData.writeInt(totalScore);
                    updateTotalScore(lbTotalScore, totalScore);

                    isMyTurn = false;
                    updateTileLeft(lbTilesLeft);
                    onHandTiles.updatePane();

                    btSubmit.setDisable(true);
                    btChangeTiles.setDisable(true);

                    playTools.addUsedBoardTiles(board.getUsedBoardTile());
                    board.takeOutUsedTiles();
                }
                catch (Exception ex) {}
            }
            else {
                return;
            }
        });

        btChangeTiles.setOnAction(event -> {
            onHandTiles.clearUsed();
            ChangeTile changeTile = new ChangeTile();
            changeTile.display(onHandTiles, bag);
        });


        //==================================[ CONNECTION ]=======================================

        try {
            socket = new Socket("24.147.20.98", 2705);
            fromServerObject = new ObjectInputStream(socket.getInputStream());
            toServerObject = new ObjectOutputStream(socket.getOutputStream());
            fromServerData = new DataInputStream(socket.getInputStream());
            toServerData = new DataOutputStream(socket.getOutputStream());
            chatRoom.appendText("You are connected\n");

            numPlayer = fromServerData.readInt();
            System.out.println(numPlayer);


        }
        catch (Exception ex) {}

        new Thread(() -> {
            try {
                String notifyPlayerNum = fromServerObject.readObject().toString();
                Platform.runLater(() -> {
                    chatRoom.appendText(notifyPlayerNum + "\n");
                });

                if (numPlayer == PLAYER1) {
                    String notifyWaitForPlayer2 = fromServerObject.readObject().toString();
                    Platform.runLater(() -> {
                        chatRoom.appendText(notifyWaitForPlayer2 + "\n");
                        btSubmit.setDisable(true);
                        btChangeTiles.setDisable(true);
                        lbTilesLeft.setText("Tiles Left: " + bag.getTilesLeft());
                    });


                    PlayTile[] newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);

                    newBag = (PlayTile[]) fromServerObject.readObject();
                    bag.newPlayTiles(newBag);
                    Platform.runLater(() -> {
                        updateTileLeft(lbTilesLeft);
                        //lbTilesLeft.setText("Tiles Left: " + bag.getTilesLeft());
                    });

                    isMyTurn = true;
                }
                else if (numPlayer == PLAYER2) {
                    PlayTile[] newBag = (PlayTile[]) fromServerObject.readObject();

                    bag.newPlayTiles(newBag);

                    for (int i = 0; i < 8; i++) {
                        PlayTile temp = bag.draw();
                        onHandTiles.replace(i, temp);
                    }

                    Platform.runLater(() -> {
                        onHandTiles.updatePane();
                        updateTileLeft(lbTilesLeft);
                        //lbTilesLeft.setText("Tiles Left: " + bag.getTilesLeft());
                        btSubmit.setDisable(true);
                        btChangeTiles.setDisable(true);
                    });

                    System.out.println(bag.getTilesLeft());

                    newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);

                    isMyTurn = false;
                }

                String notifyGameStarted = fromServerObject.readObject().toString();
                Platform.runLater(() -> {
                    chatRoom.appendText(notifyGameStarted + "\n");

                    if (numPlayer == PLAYER1) {
                        btSubmit.setDisable(false);
                        btChangeTiles.setDisable(false);
                    }
                });

                // Playing Part
                new Thread(() -> {
                    while (true) {
                        try {
                            int code = fromServerData.readInt();
                            if (code == CHAT) {
                                String message = fromServerObject.readObject().toString();
                                System.out.println("Receive: " +  message);
                                Platform.runLater(() -> {
                                    chatRoom.appendText(message + "\n");
                                });
                            }
                            else if (code == PLAY) {
                                BoardTile[] boardTiles = (BoardTile[]) fromServerObject.readObject();
                                System.out.println("Receive: Board");
                                PlayTile[] playTiles = (PlayTile[]) fromServerObject.readObject();
                                System.out.println("Receive: Bag");
                                opponentTotalScore = fromServerData.readInt();
                                System.out.println("Receive: Score");
                                isMyTurn = true;
                                turnCounter++;

                                Platform.runLater(() -> {
                                    btSubmit.setDisable(false);
                                    btChangeTiles.setDisable(false);
                                    chatRoom.appendText("Your Turn!!\n");
                                    board.updateBoardTile(boardTiles);
                                    bag.newPlayTiles(playTiles);
                                    updateTileLeft(lbTilesLeft);
                                    updateTotalScore(lbTotalOpponentScore, opponentTotalScore);

                                    playTools.addUsedBoardTiles(board.getUsedBoardTile());
                                    board.takeOutUsedTiles();
                                });
                            }
                        }
                        catch (Exception ex) {}
                    }
                }).start();

            }
            catch (Exception ex) {

            }
        }).start();

    }

    
    //==================================[ PRIVATE METHODS ]=======================================
   
    private void showAlert(String string, Alert.AlertType AlertType) {
        Alert alert = new Alert(AlertType, string);
        alert.setResizable(false);
        alert.show();
    }

    private void updateTileLeft(Label label) {
        label.setText("Tiles Left: " + bag.getTilesLeft());
    }

    private void updateTotalScore(Label label, int score) {
        String text = label.getText();
        text = text.substring(0, text.indexOf(':') + 1);
        label.setText(text + " " + score);
    }

    private int getTotalScore() {
        int totalScore = 0;
        int tempTotal = 0;

        for (BoardTile boardTile: board.getUsedBoardTile()) {
            tempTotal += boardTile.getPlayTile().getScore();
        }

        for (BoardTile boardTile: board.getUsedBoardTile()) {
            if (boardTile instanceof DoubleAllBoardTile) {
                ((DoubleAllBoardTile) boardTile).setTotalScore(tempTotal);
                totalScore += boardTile.getScore();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                ((TripleAllBoardTile) boardTile).setTotalScore(tempTotal);
                totalScore += boardTile.getScore();
            }
            else {
                totalScore += boardTile.getScore();
            }
        }

        return totalScore;
    }
}





/*
                    System.out.println(bag.getTilesLeft());


                    for (int i = 0; i < 8; i++) {
                        System.out.println(bag.getPlayTilesArray().get(i).getScore());
                    }
                    */



/*
                // Playing
                new Thread(() -> {
                    while (true) {
                        try {
                            BoardTile[] boardTiles = (BoardTile[]) fromServerObject.readObject();
                            System.out.println("Receive: Board");

                            isMyTurn = true;

                            Platform.runLater(() -> {
                                btSubmit.setDisable(false);
                                btChangeTiles.setDisable(false);
                                chatRoom.appendText("Your Turn!!\n");
                            });
                        }
                        catch (Exception ex) {}
                    }
                }).start();
                */


/*
                    for (int i = 0; i < 8; i++) {
                        System.out.println(bag.getPlayTilesArray().get(i).getScore());
                    }
                    */

/*
                    PlayTile[] newBag = new PlayTile[bag.getTilesLeft()];
                    for (int i = 0; i < newBag.length; i++) {
                        newBag[i] = bag.getPlayTilesArray().get(i);
                    }

                    toServerObject.writeObject(newBag);
                    */

 /*
                    newBag = new PlayTile[bag.getTilesLeft()];
                    for (int i = 0; i < newBag.length; i++) {
                        newBag[i] = bag.getPlayTilesArray().get(i);
                    }
                    */

/*
                BoardTile[] newBoard = new BoardTile[board.getUsedBoardTile().size()];
                for (int i = 0; i < newBoard.length; i++) {
                    newBoard[i] = board.getUsedBoardTile().get(i);
                }
                */

/*
            for (BoardTile boardTile : board.getUsedBoardTile()) {
                System.out.println(boardTile.getPlayTile().getScore() + " " + boardTile.getX() + " " + boardTile.getY());
            }
            */

/*
            String equation = playTools.toString(onUsedBoardTile);

            ResultFinder resultFinder;
            String equation = playTools.toString(onUsedBoardTile);

            if (!equation.contains("=")) {
                showAlert("Invalid Input: An equation must contain at least one equal sign"
                        , Alert.AlertType.ERROR);
                return;
            }

            String[] equations = equation.split("=");
            if (equations.length < 2) {
                showAlert("Invalid Input: An equation must contain at least 2 statements"
                        , Alert.AlertType.ERROR);
                return;
            }
            int[] results = new int[equations.length];
            String stringShowResult = "";

            for (int i = 0; i < equations.length; i++) {
                String eq = equations[i];
                try {
                    resultFinder = new ResultFinder(eq);
                    results[i] = resultFinder.getResult();
                }
                catch (Exception ex) {
                    showAlert("Invalid Input", Alert.AlertType.ERROR);
                    return;
                }

                stringShowResult += "Statement " + "#" + (i+1) + ": " + eq + "\n" +
                     "Result: " + String.valueOf(results[i]) + "\n";
            }

            boolean isEquationTrue = false;
            int firstResult = results[0];
            for (int i = 1; i < results.length; i++) {
                if (results[i] == firstResult) {
                    isEquationTrue = true;
                }
                else {
                    isEquationTrue = false;
                    break;
                }

            }

            stringShowResult += "Equation is " + isEquationTrue;

            showAlert(stringShowResult, Alert.AlertType.INFORMATION);
            */