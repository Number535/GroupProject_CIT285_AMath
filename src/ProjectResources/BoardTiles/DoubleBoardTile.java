package ProjectResources.BoardTiles;

import ProjectResources.BoardTiles.BoardTile;
import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;

// DoubleBoardTile
// 
// Programmer: Easy Group
// Last Modified: 9/23/16
public class DoubleBoardTile extends BoardTile implements Serializable {
    @Override
    public int getScore() {
        return playTile.getScore()*2;
    }
}

