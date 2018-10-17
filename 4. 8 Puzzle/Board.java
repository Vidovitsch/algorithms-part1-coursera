import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

public final class Board {

    private final int[][] blocks;

    public Board(int[][] blocks) {
        this.blocks = copyBlocks(blocks);
    }

    private int[][] copyBlocks(int[][] board) {
        int[][] copyBlocks = new int[board.length][board.length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board.length; col++) {
                copyBlocks[row][col] = board[row][col];
            }
        }
        return copyBlocks;
    }

    // board dimension N
    public int dimension() {
        return blocks.length;
    }

    // number of blocks out of place
    public int hamming() {
        int hamming = 0;
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++) {
                if (blocks[row][col] != row * blocks.length + indxTo1ArBegin(col) && blocks[row][col] != 0) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // correct array index to first index = 1 and back
    private int indxTo1ArBegin(int index) {
        return index + 1;
    }

    private int indxTo0ArBegin(int index) {
        return (index - 1);
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int manhattan = 0;
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++) {
                int value = blocks[row][col];
                if (value != row * blocks.length + indxTo1ArBegin(col) && value != 0) {
                    int goalRow = value / blocks.length;
                    int goalCol =  indxTo0ArBegin(value - (goalRow * blocks.length) );
                    boolean lastColBlock = (goalCol == -1);
                    if (lastColBlock) {
                        goalRow--;
                        goalCol = blocks.length - 1;
                    }
                    manhattan += Math.abs(goalRow - row);
                    manhattan += Math.abs(goalCol - col);
                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return (hamming() == 0);
    }

    // a board obtained by exchanging two adjacent blocks in the same row
    public Board twin() {
        int[][] twinBlocks = copyBlocks(blocks);
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length - 1; col++) {
                if (twinBlocks[row][col] != 0 && twinBlocks[row][col + 1] != 0) {
                    int swap = twinBlocks[row][col];
                    twinBlocks[row][col] =  twinBlocks[row][col + 1];
                    twinBlocks[row][col + 1] = swap;
                    return new Board(twinBlocks);
                }
            }
        }
        return null;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (this.getClass() != y.getClass()) return false;
        return Arrays.deepEquals(blocks, ((Board) y).blocks);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Iterable<Board> neighbors = null;
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++) {
                if (blocks[row][col] == 0) {
                    neighbors = getNeighborsQueue(row, col);
                }
            }
        }
        return neighbors;
    }

    private	Iterable<Board> getNeighborsQueue(int emptyBlockRow, int emptyBlockCol) {
        Queue<Board> neighborsQueue = new Queue<Board>();
        final int[][] SHIFTS_TO_NEIGHBORS = {
                {0, -1},
                {-1, 0},
                {0, 1},
                {1, 0}
        };

        for (int shiftIndex = 0; shiftIndex < SHIFTS_TO_NEIGHBORS.length; shiftIndex++) {
            int row = emptyBlockRow + SHIFTS_TO_NEIGHBORS[shiftIndex][0];
            int col = emptyBlockCol + SHIFTS_TO_NEIGHBORS[shiftIndex][1];
            boolean isNeighborBlockInDim = (row >= 0) && (row < blocks.length) && (col >= 0) && (col < blocks.length);
            if (isNeighborBlockInDim) {
                int[][] neighborBlocks = copyBlocks(blocks);
                // change values between 2 blocks: empty and neighbor
                neighborBlocks[row][col] = blocks[emptyBlockRow][emptyBlockCol];
                neighborBlocks[emptyBlockRow][emptyBlockCol] = blocks[row][col];
                // add new neighbor board
                neighborsQueue.enqueue(new Board(neighborBlocks));
            }
        }
        return neighborsQueue;
    }

    // string representation of the board (in the output format specified below)
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(blocks.length + "\n");
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks.length; col++) {
                sb.append(String.format("%2d ", blocks[row][col]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}