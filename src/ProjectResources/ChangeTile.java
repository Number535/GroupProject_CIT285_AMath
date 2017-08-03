package ProjectResources;

import ProjectResources.PlayTiles.PlayTile;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

// ChangeTile
// Programmer: Easy Group
// Last Modified: 10/27/16

public class ChangeTile {

    public void display(OnHandTiles onHandTiles, Bag bag) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        
        HBox tilesPane = new HBox();
        for (PlayTile playTile : onHandTiles.getOnHandPlayTileArray()) {
            ImageView imageView = new ImageView(playTile.getTileImage());
            imageView.setOnMouseClicked(event -> {
                //imageView.getStyleClass().add("playTileBorderClicked");
                if (playTile.isUsed()) {
                    imageView.setOpacity(1);
                    playTile.setUsed(false);
                }
                else {
                    imageView.setOpacity(0.5);
                    playTile.setUsed(true);
                }
            });
            tilesPane.getChildren().add(imageView);
        }

        VBox mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(10);
        mainPane.setPadding(new Insets(10,10,10,10));
        Button btSubmit = new Button("Submit");
        btSubmit.setPrefWidth(200);

        mainPane.getChildren().addAll(tilesPane, btSubmit);

        btSubmit.setOnAction(event -> {
            ArrayList<Integer> selectedIndex = new ArrayList<Integer>();
            for (int i = 0; i < onHandTiles.getOnHandPlayTileArray().size(); i++) {
                if (onHandTiles.getTile(i).isUsed())
                    selectedIndex.add(i);
            }

            for (int i = 0; i < selectedIndex.size(); i++) {
               
                PlayTile playTile = onHandTiles.getTile(i);
                bag.returnTiles(playTile);              
                
                playTile = bag.draw();
                onHandTiles.replace(selectedIndex.get(i), playTile);
            }

            onHandTiles.clearUsed();
            onHandTiles.updatePane();
            stage.close();
        });


        stage.setOnCloseRequest(event -> {
            onHandTiles.clearUsed();
        });


        Scene scene = new Scene(mainPane);
        scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
        stage.setTitle("Change Tiles");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
