package ProjectResources.BoardTiles;

// StarBoardTile
// Programmer: Easy Group
// Last Modified: 9/23/16

import ProjectResources.BoardTiles.BoardTile;
import java.io.Serializable;

public class StarBoardTile extends BoardTile implements Serializable{
    @Override
    public int getScore() {
        return playTile.getScore()*3;
    }


}
