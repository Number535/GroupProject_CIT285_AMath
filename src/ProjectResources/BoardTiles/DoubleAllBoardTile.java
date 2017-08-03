package ProjectResources.BoardTiles;

import ProjectResources.BoardTiles.BoardTile;
import java.io.Serializable;

// DoubleAllBoardTile
// 
// Programmer: Easy Group
// Last Modified: 9/23/16
public class DoubleAllBoardTile extends BoardTile implements Serializable{
    int totalScore = -1;

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getScore() {
        return (2 * totalScore);
    }
}
