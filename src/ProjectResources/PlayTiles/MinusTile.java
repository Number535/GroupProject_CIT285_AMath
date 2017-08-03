package ProjectResources.PlayTiles;

// MinusTile

import java.io.Serializable;

// 
// Programmer: Easy Group
// Last Modified: 10/6/16
public class MinusTile extends OperatorTile implements Serializable {

    public MinusTile() {
        sign = '-';
        setImgUrl("Resource/-.png");
        setScore(2);
        //setTileImage("Resource/-.png");
    }

}
