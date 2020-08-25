package Chess;

import java.util.ArrayList;

class ChessGameAINode {
    private int data;
    private ChessGameAINode parent;
    private ArrayList<ChessGameAINode> children;
    private int depth;
    Piece[][] board;

    ChessGameAINode(Piece[][] board) {
        this.board = board;
        children = new ArrayList<>();
    }

    int getData() {
        return data;
    }

    void setData(int data) {
        this.data = data;
    }

    ChessGameAINode getParent() {
        return parent;
    }

    void setParent(ChessGameAINode parent) {
        this.parent = parent;
    }

    ArrayList<ChessGameAINode> getChildren() {
        return children;
    }

    int getDepth() {
        return depth;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }
}
