package ProjectResources.PlayTiles;

import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;

// OperatorTile
// Programmer: Easy Group
// Last Modified: 9/29/16

public class PlusTile extends OperatorTile implements Serializable {

    public PlusTile() {
        sign = '+';
        setImgUrl("Resource/+.png");
        setScore(2);
        //setTileImage("Resource/+.png");
    }

    public char toChar() {
        return sign;
    }
}
