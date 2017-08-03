package ProjectResources.PlayTiles;

// BlankTile

import java.io.Serializable;

// 
// Programmer: Easy Group
// Last Modified: 10/6/16
public class BlankTile extends PlayTile implements Serializable {
    public BlankTile() {
        setImgUrl("Resource/blankPlay.png");
        //setTileImage("Resource/blankPlay.png");
        setScore(0);
    }
}
