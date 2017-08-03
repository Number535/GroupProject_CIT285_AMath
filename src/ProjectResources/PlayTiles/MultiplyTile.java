package ProjectResources.PlayTiles;

// MultiplyTile

import java.io.Serializable;

// 
// Programmer: Easy Group
// Last Modified: 10/6/16
public class MultiplyTile extends OperatorTile implements Serializable{

    public MultiplyTile() {
        sign = '*';
        setImgUrl("Resource/x.png");
        setScore(2);
        //setTileImage("Resource/x.png");
    }

}
