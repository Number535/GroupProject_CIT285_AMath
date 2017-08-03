package ProjectResources;

import ProjectResources.PlayTiles.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

// Bag
// Programmer: Easy Group
// Last Modified: 9/27/16


public class Bag implements AMathConstants {
    private ArrayList<PlayTile> playTilesArray;

    Bag() {
        playTilesArray = new ArrayList<>();
        setupBag();
    }

    public ArrayList<PlayTile> getPlayTilesArray() {
        return playTilesArray;
    }

    public PlayTile[] getBagArray() {
        PlayTile[] newBag = new PlayTile[getTilesLeft()];
        for (int i = 0; i < newBag.length; i++) {
            newBag[i] = playTilesArray.get(i);
        }
        return newBag;
    }

    private void setupBag() {
        for (int i = 0; i < allNumberAmount.length; i++) {
            for (int j = 0; j < allNumberAmount[i]; j++) {
                String tempImageUrl = "Resource/" + String.valueOf(allNumberNumber[i]) + ".png";
                int tempNumber = allNumberNumber[i];
                int tempScore = allNumberScore[i];
                NumberTile tempNumberTile = new NumberTile(tempScore, false, tempImageUrl, tempNumber);
                playTilesArray.add(tempNumberTile);
            }
        }

        for (int i = 0; i < infoBlank[AMOUNT]; i++) {
            BlankTile blankTile = new BlankTile();
            playTilesArray.add(blankTile);
        }

        for (int i = 0; i < infoPlus[AMOUNT]; i++) {
            PlusTile plusTile = new PlusTile();
            playTilesArray.add(plusTile);
        }

        for (int i = 0; i < infoMinus[AMOUNT]; i++) {
            MinusTile minusTile = new MinusTile();
            playTilesArray.add(minusTile);
        }

        for (int i = 0; i < infoPlusOrMinus[AMOUNT]; i++) {
            PlusOrMinusTile plusOrMinusTile = new PlusOrMinusTile();
            playTilesArray.add(plusOrMinusTile);
        }

        for (int i = 0; i < infoMultiply[AMOUNT]; i++) {
            MultiplyTile multiplyTile = new MultiplyTile();
            playTilesArray.add(multiplyTile);
        }

        for (int i = 0; i < infoDivide[AMOUNT]; i++) {
            DivideTile divideTile = new DivideTile();
            playTilesArray.add(divideTile);
        }

        for (int i = 0; i < infoMultipleOrDivide[AMOUNT]; i++) {
            MultipleOrDivideTile multipleOrDivideTile = new MultipleOrDivideTile();
            playTilesArray.add(multipleOrDivideTile);
        }

        for (int i = 0; i < infoEqualSign[AMOUNT]; i++) {
            EqualTile equalTile = new EqualTile();
            playTilesArray.add(equalTile);
        }

        Collections.shuffle(playTilesArray);

        /*
        for (int i = 0; i < playTilesArray.size(); i++) {
            System.out.print(playTilesArray.get(i).toString());
        }
        */
    }


    public PlayTile draw() {
        PlayTile temp = playTilesArray.get(0);
        playTilesArray.remove(0);

        return temp;
    }

    public int getTilesLeft() {
        return playTilesArray.size();
    }

    public void returnTiles(PlayTile tile) {
        playTilesArray.add(tile);
        Collections.shuffle(playTilesArray);
    }

    public void newPlayTiles(PlayTile[] playTiles) {
        playTilesArray.clear();

        for (PlayTile playTile : playTiles) {
            playTile.setTileImage(playTile.getImgUrl());
            playTilesArray.add(playTile);
            /*
            if (playTile instanceof NumberTile) {
                NumberTile numberTile = new NumberTile();
                numberTile.copy((NumberTile)playTile);
                playTilesArray.add(numberTile);
            }
            else {
                playTile.setTileImage(playTile.getImgUrl());
                playTilesArray.add(playTile);
            }

            else if (playTile instanceof BlankTile) {
                playTilesArray.add(new BlankTile());
            }
            else if (playTile instanceof DivideTile) {
                playTilesArray.add(new DivideTile());
            }
            else if (playTile instanceof EqualTile) {
                playTilesArray.add(new EqualTile());
            }
            else if (playTile instanceof MinusTile) {
                playTilesArray.add(new MinusTile());
            }
            else if (playTile instanceof MultipleOrDivideTile) {
                playTilesArray.add(new MultipleOrDivideTile());
            }
            else if (playTile instanceof MultiplyTile) {
                playTilesArray.add(new MultiplyTile());
            }
            else if (playTile instanceof PlusOrMinusTile) {
                playTilesArray.add(new PlusOrMinusTile());
            }
            else if (playTile instanceof PlusTile) {
                playTilesArray.add(new PlusTile());
            }
            */
        }
    }
}
