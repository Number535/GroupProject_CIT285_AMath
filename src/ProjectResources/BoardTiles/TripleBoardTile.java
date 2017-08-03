package ProjectResources.BoardTiles;

import ProjectResources.BoardTiles.BoardTile;
import java.io.Serializable;

// TripleBoardTile
// 
// Programmer: Easy Group
// Last Modified: 9/23/16
public class TripleBoardTile extends BoardTile implements Serializable{
    @Override
    public int getScore() {
        return playTile.getScore()*3;
    }
}
