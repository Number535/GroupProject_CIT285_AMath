package ProjectResources.PlayTiles;

// EqualTile

import java.io.Serializable;

// 
// Programmer: Easy Group
// Last Modified: 10/6/16
public class EqualTile extends OperatorTile implements Serializable{

    public EqualTile() {
        sign = '=';
        setImgUrl("Resource/=.png");
        setScore(1);
        //setTileImage("Resource/=.png");
    }
}
