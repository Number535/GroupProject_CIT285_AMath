package ProjectResources.PlayTiles;

// DivideTile

import java.io.Serializable;

// 
// Programmer: Easy Group
// Last Modified: 10/6/16
public class DivideTile extends OperatorTile implements Serializable {
    public DivideTile() {
        sign = '/';
        setImgUrl("Resource/divide.png");
        setScore(2);
        //setTileImage("Resource/divide.png");
    }
}
