package ProjectResources;
// Board
// Programmer: Easy Group
// Last Modified: 9/23/16

import ProjectResources.BoardTiles.*;
import ProjectResources.PlayTiles.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;


public class Board implements AMathConstants {
    // to hold all board tiles
    private Map<String, BoardTile> boardTileMap;
    private Map<String, ImageView> imageViewMap;

    private OnHandTiles onHandTiles;

    private static final int PLUS_SIGN = 0;
    private static final int MINUS_SIGN = 1;
    private static final int MULTIPLY_SIGN = 2;
    private static final int DIVIDE_SIGN = 3;

    //private ArrayList<BoardTile> boardTiles = new ArrayList<>();

    /**
     * @param onHandTiles
     */
    // constructors
    public Board(OnHandTiles onHandTiles) {
        this.onHandTiles = onHandTiles;
        boardTileMap = new HashMap<>();
        imageViewMap = new HashMap<>();
        setupBoardTileArray();
    }

    // getters
    public Map<String, BoardTile> getBoardTileMap() {
        return boardTileMap;
    }

    public BoardTile[] getBoardUsedArray() {
        BoardTile[] newBoard = new BoardTile[getUsedBoardTile().size()];
        for (int i = 0; i < newBoard.length; i++) {
            newBoard[i] = getUsedBoardTile().get(i);
        }
        return newBoard;
    }

    public String getKey(BoardTile boardTile) {
        return String.valueOf(boardTile.getX()) + " " + String.valueOf(boardTile.getY());
    }

    // private methods
    private void setupBoardTileArray() {
        String mapID;

        {
            StarBoardTile temp = new StarBoardTile();
            temp.setImgURL("Resource/Star.png");
            //temp.setTileImage("Resource/Star.png");
            temp.setX(7);
            temp.setY(7);
            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // DoubleBoardTile
        for (int count = 0; count < locationsDoubleTile.length; count++) {
            DoubleBoardTile temp = new DoubleBoardTile();
            temp.setImgURL("Resource/x2piece.png");
            //temp.setTileImage("Resource/x2piece.png");

            temp.setX(locationsDoubleTile[count][0]);
            temp.setY(locationsDoubleTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);

        }

        // DoubleAllBoardTile
        for (int count = 0; count < locationsDoubleAllTile.length; count++) {
            DoubleAllBoardTile temp = new DoubleAllBoardTile();
            temp.setImgURL("Resource/x2equation.png");
            //temp.setTileImage("Resource/x2equation.png");

            temp.setX(locationsDoubleAllTile[count][0]);
            temp.setY(locationsDoubleAllTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // TripleBoardTile
        for (int count = 0; count < locationsTripleTile.length; count++) {
            TripleBoardTile temp = new TripleBoardTile();
            temp.setImgURL("Resource/x3piece.png");
            //temp.setTileImage("Resource/x3piece.png");

            temp.setX(locationsTripleTile[count][0]);
            temp.setY(locationsTripleTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // TripleAllBoardTile
        for (int count = 0; count < locationsTripleAllTile.length; count++) {
            TripleAllBoardTile temp = new TripleAllBoardTile();
            temp.setImgURL("Resource/x3equation.png");
            //temp.setTileImage("Resource/x3equation.png");

            temp.setX(locationsTripleAllTile[count][0]);
            temp.setY(locationsTripleAllTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);

        }

        // NormalBoardTile
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                NormalBoardTile temp = new NormalBoardTile();
                temp.setImgURL("Resource/blank.png");
                //temp.setTileImage("Resource/blank.png");

                temp.setX(i);
                temp.setY(j);

                mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
                if (!boardTileMap.containsKey(mapID)) {
                    boardTileMap.put(mapID, temp);
                }
            }
        }
    }

    // public methods
    public GridPane getBoardPane() {
        GridPane boardPane = new GridPane();
        boardPane.setPrefHeight(600);
        boardPane.setPrefWidth(600);
        boardPane.setVgap(1);
        boardPane.setHgap(1);

        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            String key = entry.getKey();
            BoardTile temp = entry.getValue();
            ImageView imageView = new ImageView(temp.getTileImage());
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            imageViewMap.put(key, imageView);
        }

        for ( Map.Entry<String, ImageView> entry : imageViewMap.entrySet()) {
            entry.getValue().setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    if(event.getClickCount() == 2){
                        System.out.println("Double clicked");

                        if (boardTileMap.get(entry.getKey()).isPlaced() == true) {
                            entry.getValue().setImage(boardTileMap.get(entry.getKey()).getTileImage());

                            boardTileMap.get(entry.getKey()).getPlayTile().setUsed(false);
                            onHandTiles.add(boardTileMap.get(entry.getKey()).getPlayTile());

                            boardTileMap.get(entry.getKey()).setPlayTile(null);
                            boardTileMap.get(entry.getKey()).setPlaced(false);
                        }
                    }
                    else {
                        if (onHandTiles.getTileSelected() != -1 && !boardTileMap.get(entry.getKey()).isPlaced()) {

                            int indexTileSelected = onHandTiles.getTileSelected();

                            if (onHandTiles.getTile(indexTileSelected) instanceof PlusOrMinusTile) {
                                System.out.println("Plus or minus");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                PLUS_SIGN, MINUS_SIGN);
                                choosePlayTiles.display();
                            }
                            else if (onHandTiles.getTile(indexTileSelected) instanceof MultipleOrDivideTile) {
                                System.out.println("Multiply or divide");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                MULTIPLY_SIGN, DIVIDE_SIGN);
                                choosePlayTiles.display();
                            }
                            else if (onHandTiles.getTile(indexTileSelected) instanceof BlankTile) {
                                System.out.println("Blank");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                0);
                                choosePlayTiles.displayBlankTile();
                            }
                            else {
                                entry.getValue().setImage(onHandTiles.getTile(indexTileSelected).getTileImage());

                                boardTileMap.get(entry.getKey()).setPlaced(true);
                                boardTileMap.get(entry.getKey()).setPlayTile(onHandTiles.getTile(indexTileSelected));

                                onHandTiles.remove(indexTileSelected);

                                onHandTiles.updatePane();
                            }
                        }
                    }
                }
            });


            ImageView tempImageView = entry.getValue();
            BoardTile tempTile = boardTileMap.get(entry.getKey());
            boardPane.add(tempImageView, tempTile.getX(), tempTile.getY());
        }

        return boardPane;
    }

    public ArrayList<BoardTile> getUsedBoardTile() {
        ArrayList<BoardTile> onUsedBoardTile = new ArrayList<BoardTile>();

        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            if (entry.getValue().isPlaced() == true) {
                onUsedBoardTile.add(entry.getValue());
            }
        }

        return onUsedBoardTile;
    }

    public void updateBoardTile(BoardTile[] boardTiles) {
        for (BoardTile boardTile : boardTiles) {
            boardTile.setTileImage(boardTile.getImgURL());
            boardTile.getPlayTile().setTileImage(boardTile.getPlayTile().getImgUrl());
            String key = String.valueOf(boardTile.getX()) + " " + String.valueOf(boardTile.getY());
            System.out.println(key);
            boardTileMap.get(key).copy(boardTile);
            imageViewMap.get(key).setImage(boardTile.getPlayTile().getTileImage());
        }
    }

    public void takeOutUsedTiles() {
        for (BoardTile boardTile: getUsedBoardTile()) {
            boardTileMap.remove(getKey(boardTile));
            imageViewMap.get(getKey(boardTile)).setDisable(true);
        }
    }

    //========================== Choose Play Tiles Function ==============================
    class ChoosePlayTiles {
        private int tileSelected = 0;
        private Stage stage = new Stage();
        private ArrayList<Integer> choices = new ArrayList<>();
        private PlayTile playTile;
        private ImageView imageView;
        private String key;

        ChoosePlayTiles(ImageView boardTile, String key, PlayTile playTile, int index, int... playTiles) {
            stage.initModality(Modality.APPLICATION_MODAL);
            this.imageView = boardTile;
            this.playTile = playTile;
            this.key = key;
            this.tileSelected = index;

            for (int i : playTiles) {
                choices.add(i);
            }
        }

        public int getTileSelected() {
            return tileSelected;
        }

        public void display() {
            HBox tilesPane = new HBox();
            tilesPane.setSpacing(10);

            ToggleGroup toggleGroupChoices = new ToggleGroup();

            for (int i : choices) {
                RadioButton radioButton = new RadioButton();
                switch (i) {
                    case PLUS_SIGN: radioButton = new RadioButton("Plus"); break;
                    case MINUS_SIGN: radioButton = new RadioButton("Minus"); break;
                    case MULTIPLY_SIGN: radioButton = new RadioButton("Multiply"); break;
                    case DIVIDE_SIGN: radioButton = new RadioButton("Divide"); break;
                }

                radioButton.setToggleGroup(toggleGroupChoices);
                tilesPane.getChildren().add(radioButton);
            }

            VBox mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER);
            mainPane.setSpacing(10);
            mainPane.setPadding(new Insets(10,10,10,10));
            Button btSubmit = new Button("Submit");
            btSubmit.setPrefWidth(200);
            btSubmit.setOnAction(event -> {
                String choice = ((RadioButton)toggleGroupChoices.getSelectedToggle()).getText();

                switch (choice) {
                    case "Plus":
                        playTile = new PlusTile();
                        System.out.println("Plus"); break;
                    case "Minus":
                        playTile = new MinusTile();
                        System.out.println("Minus"); break;
                    case "Multiply":
                        playTile = new MultiplyTile();
                        System.out.println("Multiply"); break;
                    case "Divide":
                        playTile = new DivideTile();
                        System.out.println("Divide"); break;
                }

                System.out.println(tileSelected);
                stage.close();
                imageView.setImage(playTile.getTileImage());

                boardTileMap.get(key).setPlaced(true);
                boardTileMap.get(key).setPlayTile(playTile);

                onHandTiles.remove(tileSelected);

                onHandTiles.updatePane();
            });

            mainPane.getChildren().addAll(tilesPane, btSubmit);
            stage.setOnCloseRequest(event -> {
                onHandTiles.clearUsed();
                onHandTiles.updatePane();
            });


            Scene scene = new Scene(mainPane);
            scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
            stage.setTitle("Choose Tiles");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }


        public void displayBlankTile() {
            HBox tilesPane = new HBox();
            tilesPane.setSpacing(10);

            Label lbChooseNumberTile = new Label("Choose the number tile: ");
            ComboBox<Integer> cbNumberTile = new ComboBox<>();
            cbNumberTile.setPrefWidth(100);

            for (int i = 0; i <= 20; i++) {
                cbNumberTile.getItems().add(i);
            }

            cbNumberTile.getSelectionModel().selectFirst();

            tilesPane.getChildren().addAll(lbChooseNumberTile, cbNumberTile);

            VBox mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER);
            mainPane.setSpacing(10);
            mainPane.setPadding(new Insets(10,10,10,10));
            Button btSubmit = new Button("Submit");
            btSubmit.setPrefWidth(200);
            btSubmit.setOnAction(event -> {
                int number = cbNumberTile.getSelectionModel().getSelectedItem();
                String tempImageUrl = "Resource/" + String.valueOf(allNumberNumber[number]) + ".png";
                int tempNumber = allNumberNumber[number];
                int tempScore = allNumberScore[number];
                playTile = new NumberTile(tempScore, false, tempImageUrl, tempNumber);
                System.out.println("Selected number: " + number);

                System.out.println(tileSelected);
                stage.close();

                imageView.setImage(playTile.getTileImage());
                boardTileMap.get(key).setPlaced(true);
                boardTileMap.get(key).setPlayTile(playTile);

                onHandTiles.remove(tileSelected);

                onHandTiles.updatePane();
            });

            mainPane.getChildren().addAll(tilesPane, btSubmit);
            stage.setOnCloseRequest(event -> {
                onHandTiles.clearUsed();
                onHandTiles.updatePane();
            });

            Scene scene = new Scene(mainPane, 400, 100);
            scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
            stage.setTitle("Choose Tiles");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }
}
