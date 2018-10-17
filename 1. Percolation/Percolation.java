import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int edgeSize;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    private final WeightedQuickUnionUF antiBackwash;
    private boolean[][] grid;
    private int numberOfOpenSites = 0;

    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        edgeSize = n;
        grid = createBlockedGrid(n);
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        antiBackwash = new WeightedQuickUnionUF(n * n + 1);
        unionWithUpperGrid();
        unionWithLowerGrid();
    }

    public void open(int row, int col) {
        if (row < 1 || row > edgeSize || col < 1 || col > edgeSize) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            unionWithUpperSite(row, col);
            unionWithLowerSite(row, col);
            unionWithLeftSite(row, col);
            unionWithRightSite(row, col);
            numberOfOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > edgeSize || col < 1 || col > edgeSize) {
            throw new IllegalArgumentException();
        }
        return grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > edgeSize || col < 1 || col > edgeSize) {
            throw new IllegalArgumentException();
        }
        int siteIndex = getIndex(row, col);
        System.out.println(antiBackwash.count());
        return isOpen(row, col) && antiBackwash.connected(0, siteIndex + 1);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        if (edgeSize == 1) {
            return isOpen(1, 1);
        } else if (edgeSize == 2) {
            return (isOpen(1, 1) && isOpen(2, 1)) || (isOpen(1, 2) && isOpen(2, 2));
        }
        return weightedQuickUnionUF.connected(0, edgeSize * edgeSize - 1);
    }

    private void unionWithUpperSite(int row, int col) {
        if (row > 1 && isOpen(row - 1, col)) {
            int siteIndex = getIndex(row, col);
            int upperSiteIndex = getIndex(row - 1, col);
            weightedQuickUnionUF.union(siteIndex + 1, upperSiteIndex + 1);
            antiBackwash.union(siteIndex + 1, upperSiteIndex + 1);
        }
    }

    private void unionWithLowerSite(int row, int col) {
        if (row < edgeSize && isOpen(row + 1, col)) {
            int siteIndex = getIndex(row, col);
            int lowerSiteIndex = getIndex(row + 1, col);
            weightedQuickUnionUF.union(siteIndex + 1, lowerSiteIndex + 1);
            antiBackwash.union(siteIndex + 1, lowerSiteIndex + 1);
        }
    }

    private void unionWithLeftSite(int row, int col) {
        if (col > 1 && isOpen(row, col - 1)) {
            int siteIndex = getIndex(row, col);
            int leftSiteIndex = getIndex(row, col - 1);
            weightedQuickUnionUF.union(siteIndex + 1, leftSiteIndex + 1);
            antiBackwash.union(siteIndex + 1, leftSiteIndex + 1);
        }
    }

    private void unionWithRightSite(int row, int col) {
        if (col < edgeSize && isOpen(row, col + 1)) {
            int siteIndex = getIndex(row, col);
            int rightSiteIndex = getIndex(row, col + 1);
            weightedQuickUnionUF.union(siteIndex + 1, rightSiteIndex + 1);
            antiBackwash.union(siteIndex + 1, rightSiteIndex + 1);
        }
    }

    private void unionWithUpperGrid() {
        for (int i = 0; i < edgeSize; i++) {
            weightedQuickUnionUF.union(i + 1, 0);
            antiBackwash.union(i + 1, 0);
        }
    }

    private void unionWithLowerGrid() {
        for (int i = 1; i < edgeSize + 1; i++) {
            int siteIndex = getIndex(edgeSize, i);
            weightedQuickUnionUF.union(siteIndex + 1, edgeSize * edgeSize - 1);
        }
    }

    private boolean[][] createBlockedGrid(int n) {
        boolean[][] blockedGrid = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blockedGrid[i][j] = false;
            }
        }
        return blockedGrid;
    }

    private int getIndex(int row, int col) {
        return col + (row - 1) * edgeSize - 1;
    }
}
