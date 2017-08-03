package ProjectResources.PlayTiles;

// NumberTile
// Programmer: Easy Group
// Last Modified: 9/20/16

import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;

import static ProjectResources.AMathConstants.allNumberNumber;

public class NumberTile extends PlayTile implements Serializable{
    private int number;  // to hold a number on a number tile

    // Constructors
    public NumberTile() {
        this.number = -1;
    }

    public NumberTile(int score, boolean isUsed, String tileImageUrl, int number) {
        super(score, isUsed, tileImageUrl);
        this.number = number;
    }

    // Getters
    public int getNumber() {
        return number;
    }

    // Setters
    public void setNumber(int number) {
        this.number = number;
    }

    // Methods
    @Override
    public String toString() {
        return "NumberTile{" +
                "number=" + number + ", " +
                "score=" + score + ", " +
                "isUsed=" + isUsed +
                '}' + "\n";
    }

    public void copy(NumberTile numberTile) {
        this.score = numberTile.getScore();
        this.number = numberTile.getNumber();
        this.isUsed = numberTile.isUsed();
        this.imgUrl = numberTile.getImgUrl();
        setTileImage(imgUrl);
        //String imgURL = "Resource/" + String.valueOf(allNumberNumber[this.number]) + ".png";
        //setTileImage(imgURL);
    }
}
