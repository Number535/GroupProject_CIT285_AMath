package ProjectResources;

// CommunicatePackage
// 
// Programmer: Easy Group
// Last Modified: 10/27/16
public class CommunicatePackage implements java.io.Serializable {
    private transient Board board;
    private transient Bag bag;

    CommunicatePackage(Board board, Bag bag) {
        this.board = board;
        this.bag = bag;
    }

    public Board getBoard() {
        return board;
    }

    public Bag getBag() {
        return bag;
    }
}
