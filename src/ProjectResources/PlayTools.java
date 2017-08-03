package ProjectResources;

import ProjectResources.BoardTiles.*;
import ProjectResources.PlayTiles.NumberTile;
import ProjectResources.PlayTiles.OperatorTile;
import javafx.scene.control.Alert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// PlayTools
// Programmer: Easy Group
// Last Modified: 10/26/16
public class PlayTools {
    private ArrayList<BoardTile> usedBoardTiles = new ArrayList<>();
    private ArrayList<BoardTile> connectedVerticalTiles = new ArrayList<>();
    private ArrayList<BoardTile> connectedHorizontalTiles = new ArrayList<>();

    private String alertText = "";

    public String getAlertText() {
        return alertText;
    }

    public ArrayList<BoardTile> getUsedBoardTiles() {
        return usedBoardTiles;
    }

    public boolean checkEquation(ArrayList<BoardTile> boardTiles) {
        alertText = "";
        ResultFinder resultFinder;
        String equation = toString(boardTiles);
        System.out.println(equation);

        if (!equation.contains("=")) {
            alertText = "Invalid Input: An equation must contain at least one equal sign";
            return false;
        }
        
        String[] equations = equation.split("=");

        if (equations.length < 2) {
            alertText = "Invalid Input: An equation must contain at least 2 statements";
            return false;
        }

        int[] results = new int[equations.length];

        for (int j = 0; j < equations.length; j++) {
            String eq = equations[j];

            try {
                resultFinder = new ResultFinder(eq);

                if (resultFinder.isNotEvenDivision()) {
                    alertText = "Invalid Division";
                    return false;
                }

                results[j] = resultFinder.getResult();
            }
            catch (Exception ex) {
                alertText = "Invalid input";
                return false;
            }

            alertText += "Statement " + "#" + (j+1) + ": " + eq + "\n" +
                    "Result: " + String.valueOf(results[j]) + "\n";
        }

        int firstResult = results[0];
        for (int k = 1; k < results.length; k++) {
            if (results[k] != firstResult) {
                alertText += "Equation is false";
                return false;
            }
        }

        //alertText += "Equation is true";
        alertText = "Equation is true";
        return true;
    }

    public boolean checkWholeBoard(ArrayList<BoardTile> boardTiles) {
        ArrayList<BoardTile> temp = copyBoardTiles(boardTiles);
        ArrayList<BoardTile> temp1 = copyBoardTiles(usedBoardTiles);
        ArrayList<BoardTile> calculatedTiles = new ArrayList<>();
        temp.addAll(temp1);

        /*
        for (BoardTile boardTile: temp) {
            System.out.println("x1: " + boardTile.getX() + " y1: " + boardTile.getY());
        }
        */

        //////////////// CHECK VERTICALLY /////////////////
        sortXY(temp);

        calculatedTiles.add(temp.get(0));

        for (int i = 1; i < temp.size(); i++) {
            if (temp.get(i-1).getX() == temp.get(i).getX()) {
                if (temp.get(i - 1).getY() + 1 == temp.get(i).getY()) {
                    //System.out.println("1..");
                    calculatedTiles.add(temp.get(i));
                    //System.out.println(temp.get(i).getPlayTile().getImgUrl());
                }
                else if (temp.get(i-1).getY() + 1 != temp.get(i).getY() && calculatedTiles.size() > 1) {
                    //System.out.println("2..");

                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }

                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
                else {
                    //System.out.println("3..");
                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
            }
            else {
                //System.out.println("4..");
                if (calculatedTiles.size() > 1) {
                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }
                }

                calculatedTiles.clear();
                calculatedTiles.add(temp.get(i));
            }
        }

        if (calculatedTiles.size() > 1) {
            System.out.println(calculatedTiles.size());
            //System.out.println("5..");
            for (BoardTile b: calculatedTiles) {
                System.out.println(b.getPlayTile().getScore());
            }

            if (!checkEquation(calculatedTiles)) {
                return false;
            }
        }


        //////////////// CHECK HORIZONTALLY /////////////////
        calculatedTiles.clear();
        sortYX(temp);
        calculatedTiles.add(temp.get(0));

        for (int i = 1; i < temp.size(); i++) {
            if (temp.get(i-1).getY() == temp.get(i).getY()) {
                if (temp.get(i - 1).getX() + 1 == temp.get(i).getX()) {
                    //System.out.println("1..");
                    calculatedTiles.add(temp.get(i));
                    //System.out.println(temp.get(i).getPlayTile().getImgUrl());
                }
                else if (temp.get(i-1).getX() + 1 != temp.get(i).getX() && calculatedTiles.size() > 1) {
                    //System.out.println("2..");

                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }

                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
                else {
                    //System.out.println("3..");
                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
            }
            else {
                //System.out.println("4..");
                if (calculatedTiles.size() > 1) {
                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }
                }

                calculatedTiles.clear();
                calculatedTiles.add(temp.get(i));
            }
        }

        if (calculatedTiles.size() > 1) {
            System.out.println(calculatedTiles.size());
            //System.out.println("5..");
            for (BoardTile b: calculatedTiles) {
                System.out.println(b.getPlayTile().getScore());
            }

            if (!checkEquation(calculatedTiles)) {
                return false;
            }
        }

        return true;
    }

    public ArrayList<BoardTile> copyBoardTiles(ArrayList<BoardTile> boardTiles) {
        ArrayList<BoardTile> tempBoardTiles = new ArrayList<>();

        for (BoardTile boardTile: boardTiles) {
            BoardTile temp;

            if (boardTile instanceof DoubleAllBoardTile) {
                temp = new DoubleAllBoardTile();
            }
            else if (boardTile instanceof DoubleBoardTile) {
                temp = new DoubleBoardTile();
            }
            else if (boardTile instanceof NormalBoardTile) {
                temp = new NormalBoardTile();
            }
            else if (boardTile instanceof StarBoardTile) {
                temp = new StarBoardTile();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                temp = new TripleAllBoardTile();
            }
            else {
                temp = new TripleBoardTile();
            }

            temp.copy(boardTile);
            tempBoardTiles.add(temp);
        }

        return tempBoardTiles;
    }

    public void addUsedBoardTiles(ArrayList<BoardTile> boardTiles) {
        //usedBoardTiles.addAll(boardTiles);
        for (BoardTile boardTile: boardTiles) {
            BoardTile temp;

            if (boardTile instanceof DoubleAllBoardTile) {
                temp = new DoubleAllBoardTile();
            }
            else if (boardTile instanceof DoubleBoardTile) {
                temp = new DoubleBoardTile();
            }
            else if (boardTile instanceof NormalBoardTile) {
                temp = new NormalBoardTile();
            }
            else if (boardTile instanceof StarBoardTile) {
                temp = new StarBoardTile();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                temp = new TripleAllBoardTile();
            }
            else {
                temp = new TripleBoardTile();
            }

            temp.copy(boardTile);
            usedBoardTiles.add(temp);
        }
    }

    /*
    public void addUsedBoardTiles(BoardTile[] boardTiles) {
        for (BoardTile boardTile: boardTiles) {
            usedBoardTiles.add(boardTile);
        }
    }
    */

    public boolean isUsedTilesContainX(int x) {
        for (BoardTile boardTile : usedBoardTiles) {
            if (boardTile.getX() == x) {
                return true;
            }
        }
        return false;
    }

    public boolean isUsedTilesContainY(int y) {
        for (BoardTile boardTile : usedBoardTiles) {
            if (boardTile.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public boolean hasMissingSpotVertical(ArrayList<BoardTile> boardTiles) {
        int previousY = boardTiles.get(0).getY();
        System.out.println(boardTiles.size());
        for (int j = 1; j < boardTiles.size(); j++) {
            System.out.println(previousY + ", " + boardTiles.get(j).getY());
            if (previousY + 1 != boardTiles.get(j).getY()) {
                System.out.println("return true");
                return true;
            }
            previousY = boardTiles.get(j).getY();
        }
        System.out.println("return false");
        return false;
    }

    public boolean hasMissingSpotHorizontal(ArrayList<BoardTile> boardTiles) {
        int previousX = boardTiles.get(0).getX();
        System.out.println(boardTiles.size());
        for (int j = 1; j < boardTiles.size(); j++) {
            System.out.println(previousX + ", " + boardTiles.get(j).getX());
            if (previousX + 1 != boardTiles.get(j).getX()) {
                System.out.println("return true");
                return true;
            }
            previousX = boardTiles.get(j).getX();
        }
        System.out.println("return false");
        return false;
    }

    public boolean isConnectedWithOldTilesVertical(ArrayList<BoardTile> boardTiles) {
        boolean isConnected = false;

        for (BoardTile boardTile: boardTiles) {
            int x = boardTile.getX();
            int y = boardTile.getY();

            System.out.println("==" + x + ", " + y);

            for (BoardTile boardTile1: getUsedBoardTiles()) {
                if (boardTile1.getX() == x && (boardTile1.getY() + 1 == y || boardTile1.getY() - 1 == y)) {
                    connectedVerticalTiles.add(boardTile1);
                    System.out.println("--" + boardTile1.getX() + ", " + boardTile1.getY());
                    isConnected = true;
                }
            }
        }

        System.out.println("**" + isConnected);
        System.out.println(connectedVerticalTiles.size());
        return isConnected;
    }

    public boolean isConnectedWithOldTilesHorizontal(ArrayList<BoardTile> boardTiles) {
        boolean isConnected = false;

        for (BoardTile boardTile: boardTiles) {
            int x = boardTile.getX();
            int y = boardTile.getY();

            System.out.println("==" + x + ", " + y);

            for (BoardTile boardTile1: getUsedBoardTiles()) {
                if (boardTile1.getY() == y && (boardTile1.getX() + 1 == x || boardTile1.getX() - 1 == x)) {
                    connectedHorizontalTiles.add(boardTile1);
                    System.out.println("--" + boardTile1.getX() + ", " + boardTile1.getY());
                    isConnected = true;
                }
            }
        }

        System.out.println("**" + isConnected);
        System.out.println(connectedHorizontalTiles.size());
        return isConnected;
    }

    //public ArrayList<Integer> findMissingSpotVertical(ArrayList<BoardTile> boardTiles)

    public boolean isHorizontal(ArrayList<BoardTile> boardTiles, int y) {
        boolean isHorizontal = false;

        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getY() != y) {
                isHorizontal = false;
                break;
            }
            else {
                isHorizontal = true;
            }
        }

        return isHorizontal;
    }

    public boolean isVertical(ArrayList<BoardTile> boardTiles, int x) {
        boolean isVertical = false;

        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getX() != x) {
                isVertical = false;
                break;
            }
            else {
                isVertical = true;
            }
        }

        return isVertical;
    }

    public void sortByY(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                return ((Integer)o1.getY()).compareTo(o2.getY());
            }
        });
    }


    public void sortByX(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                return ((Integer)o1.getX()).compareTo(o2.getX());
            }
        });
    }

    /*
    public int compareXY(BoardTile b1, BoardTile b2) {

        int x1 = b1.getX();
        int x2 = b2.getX();

        int y1 = b1.getY();
        int y2 = b2.getY();

        if (x1 < x2) return -1;
        if (x1 > x2) return 1;
        if (y1 < y2) return -1;
        if (y1 > y2) return 1;
        return 0;
    }
    */

    public void sortXY(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                int x1 = o1.getX();
                int x2 = o2.getX();

                int y1 = o1.getY();
                int y2 = o2.getY();

                if (x1 < x2) return -1;
                if (x1 > x2) return 1;
                if (y1 < y2) return -1;
                if (y1 > y2) return 1;
                return 0;
            }
        });
    }

    public void sortYX(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                int x1 = o1.getX();
                int x2 = o2.getX();

                int y1 = o1.getY();
                int y2 = o2.getY();

                if (y1 < y2) return -1;
                if (y1 > y2) return 1;
                if (x1 < x2) return -1;
                if (x1 > x2) return 1;
                return 0;
            }
        });
    }


    public String toString(ArrayList<BoardTile> boardTiles) {
        String equation = "";
        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getPlayTile() instanceof NumberTile) {
                String temp;
                temp = String.valueOf(((NumberTile) boardTile.getPlayTile()).getNumber());
                equation += temp;
            }
            else if (boardTile.getPlayTile() instanceof OperatorTile) {
                Character temp;
                temp = ((OperatorTile) boardTile.getPlayTile()).getSign();
                equation += temp;
            }
        }
        return equation;
    }

    public boolean isOnStar(ArrayList<BoardTile> boardTiles) {
        for (BoardTile boardTile : boardTiles) {
            if (boardTile instanceof StarBoardTile)
                return true;
        }
        return false;
    }
}


/*
                else if (temp.get(i-1).getY() + 1 != temp.get(i).getY() && calculatedTiles.size() > 1) {
                    System.out.println("2..");
                    ResultFinder resultFinder;
                    String equation = toString(calculatedTiles);
                    String[] equations = equation.split("=");

                    int[] results = new int[equations.length];

                    for (int j = 0; j < equations.length; j++) {
                        String eq = equations[j];

                        System.out.println(eq);

                        try {
                            resultFinder = new ResultFinder(eq);
                            results[j] = resultFinder.getResult();
                        }
                        catch (Exception ex) {
                            return false;
                        }
                    }

                    int firstResult = results[0];
                    for (int k = 1; k < results.length; k++) {
                        if (results[k] != firstResult) {
                            return false;
                        }
                    }

                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
                else {
                    System.out.println("3..");
                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
            }
            else {
                if (calculatedTiles.size() > 1) {
                    ResultFinder resultFinder;
                    String equation = toString(calculatedTiles);
                    String[] equations = equation.split("=");

                    int[] results = new int[equations.length];

                    for (int j = 0; j < equations.length; j++) {
                        String eq = equations[j];

                        System.out.println(eq);

                        try {
                            resultFinder = new ResultFinder(eq);
                            results[j] = resultFinder.getResult();
                        }
                        catch (Exception ex) {
                            return false;
                        }
                    }

                    int firstResult = results[0];
                    for (int k = 1; k < results.length; k++) {
                        if (results[k] != firstResult) {
                            return false;
                        }
                    }
                }

                System.out.println("4..");
                calculatedTiles.clear();
                calculatedTiles.add(temp.get(i));
            } */