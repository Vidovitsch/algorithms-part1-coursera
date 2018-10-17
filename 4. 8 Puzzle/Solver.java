import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private SearchNode bestNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        if (initial.isGoal()) {
            bestNode = new SearchNode(initial, null);
        } else {
            MinPQ<SearchNode> normal = new MinPQ<SearchNode>();
            MinPQ<SearchNode> twin = new MinPQ<SearchNode>();
            normal.insert(new SearchNode(initial, null));
            twin.insert(new SearchNode(initial.twin(), null));
            while (true) {
                SearchNode bestTwinNode = nextStep(twin);
                if (bestTwinNode.board.isGoal()) {
                    bestNode = null;
                    return;
                }
                bestNode = nextStep(normal);
                if (bestNode.board.isGoal()) {
                    return;
                }
            }
        }
    }

    private SearchNode nextStep(MinPQ<SearchNode> minPQ) {
        SearchNode best = minPQ.delMin();
        for (Board neighbor: best.board.neighbors()) {
            if (best.previous == null || !neighbor.equals(best.previous.board)) {
                minPQ.insert(new SearchNode(neighbor, best));
            }
        }
        return best;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return bestNode != null;
    }

    // min number of moves to solve initial board; -1 if no solution
    public int moves() {
        return isSolvable() ? bestNode.moves : -1;
    }

    // sequence of boards in a shortest solution; null if no solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> stack = new Stack<Board>();
        SearchNode node = bestNode;
        while (node != null) {
            stack.push(node.board);
            node = node.previous;
        }
        return stack;
    }

    private final class SearchNode implements Comparable<SearchNode> {

        private final int priority;
        private final int moves;
        private final int manhattan;
        private final Board board;
        private final SearchNode previous;

        public SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.manhattan = board.manhattan();
            this.moves = this.previous != null ? this.previous.moves + 1 : 0;
            this.priority = this.manhattan + this.moves;
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            return 0;
        }
    }
}