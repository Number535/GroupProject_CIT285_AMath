package ProjectResources.BoardTiles;

// NormalBoardTile
// Programmer: Easy Group
// Last Modified: 9/23/16

import ProjectResources.BoardTiles.BoardTile;
import java.io.Serializable;

public class NormalBoardTile extends BoardTile implements Serializable{
    @Override
    public int getScore() {
        return playTile.getScore();
    }
}
