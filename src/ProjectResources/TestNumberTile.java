package ProjectResources;

// TestNumberTile
// Programmer: Easy Group
// Last Modified: 9/20/16

import ProjectResources.PlayTiles.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class TestNumberTile extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /*// create a NumberTile obj with no argument extends BoardTile
        NumberTile four = new NumberTile();
        // create a NumberTile obj with arguments
        NumberTile twenty = new NumberTile(5, false, "Resource/NumberTile_20.png", 20);

        // set up the properties for "four"
        four.setNumber(20);
        four.setScore(2);
        four.setTileImage("Resource/NumberTile_4.png");
        four.setUsed(false);

        // create a new pane
        FlowPane main = new FlowPane();

        // create a new ImageView to display the image of "four"
        ImageView fourTile = new ImageView(four.getTileImage());
        fourTile.setFitHeight(100);
        fourTile.setFitWidth(100);

        // create a new ImageView to display the image of "twenty"
        ImageView twentyTile = new ImageView(twenty.getTileImage());
        twentyTile.setFitHeight(100);
        twentyTile.setFitWidth(100);*/

        PlusTile plusTile = new PlusTile();
        ImageView plus = new ImageView(plusTile.getTileImage());
        plus.setFitHeight(100);
        plus.setFitWidth(100);
        MinusTile minusTile = new MinusTile();
        ImageView minus = new ImageView(minusTile.getTileImage());
        minus.setFitHeight(100);
        minus.setFitWidth(100);
        PlusOrMinusTile plusOrMinusTile = new PlusOrMinusTile();
        ImageView plusOrMinus = new ImageView(plusOrMinusTile.getTileImage());
        plusOrMinus.setFitHeight(100);
        plusOrMinus.setFitWidth(100);
        EqualTile equalTile = new EqualTile();
        ImageView equal = new ImageView(equalTile.getTileImage());
        equal.setFitHeight(100);
        equal.setFitWidth(100);
        MultiplyTile multiplyTile = new MultiplyTile();
        ImageView multiple = new ImageView(multiplyTile.getTileImage());
        multiple.setFitHeight(100);
        multiple.setFitWidth(100);
        DivideTile divideTile = new DivideTile();
        ImageView divide = new ImageView(divideTile.getTileImage());
        divide.setFitHeight(100);
        divide.setFitWidth(100);
        MultipleOrDivideTile multipleOrDivideTile = new MultipleOrDivideTile();
        ImageView multipleOrDivide = new ImageView(multipleOrDivideTile.getTileImage());
        multipleOrDivide.setFitHeight(100);
        multipleOrDivide.setFitWidth(100);

        FlowPane main = new FlowPane();
        // add the ImageViews to the pane
        main.getChildren().addAll(plus, minus, plusOrMinus, equal, multiple, divide, multipleOrDivide);

        Scene scene = new Scene(main, 100, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test");
        primaryStage.show();
    }
}
