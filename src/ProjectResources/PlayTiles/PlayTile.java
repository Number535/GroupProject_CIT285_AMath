package ProjectResources.PlayTiles;

// PlayTile
// Programmer: Easy Group
// Last Modified: 9/20/16

import java.io.Serializable;
import javafx.scene.image.Image;

public class PlayTile implements Serializable {
    protected int score;        // to hold a score on a play tile
    protected boolean isUsed;   // to tell if a tile is used
    protected String imgUrl;
    protected transient Image tileImage;  // to hold an image object of a tile

    // constructor
    public PlayTile() {
        this.score = -1;
        this.isUsed = false;
        this.imgUrl = null;
        this.tileImage = null;
    }

    public PlayTile(int score, boolean isUsed, String tileImageUrl) {
        this.score = score;
        this.isUsed = isUsed;
        this.imgUrl = tileImageUrl;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
        //this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    // Getters
    public int getScore() {
        return score;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Image getTileImage() {
        return tileImage;
    }

    // Setters
    public void setScore(int score) {
        this.score = score;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
    }

    public void setTileImage(String tileImageUrl) {
        this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    // Methods
    public void copy(PlayTile playTile) {
        this.score = playTile.getScore();
        this.isUsed = playTile.isUsed();
        this.imgUrl = playTile.getImgUrl();
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
    }
}
