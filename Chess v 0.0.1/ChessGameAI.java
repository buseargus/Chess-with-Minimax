package Chess;

class ChessGameAI {
    private ChessGameAINode root;

    ChessGameAI() {
        this.root = null;
    }

    ChessGameAINode minimax(ChessGameAINode node, int depth, boolean isMax) {
        if(node.getDepth() == depth) {
            return node;
        }

        if(isMax) {
            ChessGameAINode maxBest = new ChessGameAINode(null);
            maxBest.setData(Integer.MIN_VALUE);
            for(ChessGameAINode value : node.getChildren()) {
                ChessGameAINode bestNode = minimax(value, depth, false);
                int v = bestNode.getData();
                if(v == Math.max(maxBest.getData(), v)) {
                    maxBest = bestNode;
                }
            }
            return maxBest;

        } else {
            ChessGameAINode minBest = new ChessGameAINode(null);
            minBest.setData(Integer.MAX_VALUE);
            for(ChessGameAINode value : node.getChildren()) {
                ChessGameAINode bestNode = minimax(value, depth, true);
                int v = bestNode.getData();
                if(v == Math.min(minBest.getData(), v)) {
                    minBest = bestNode;
                }
            }
            return minBest;
        }
    }

    ChessGameAINode getRoot() {
        return root;
    }

    void setRoot(ChessGameAINode root) {
        this.root = root;
    }
}
