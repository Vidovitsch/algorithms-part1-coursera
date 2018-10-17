import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCE_95 = 1.95;
    private final double[] fractions;

    private double mean;
    private double stddev;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int rndRow = StdRandom.uniform(1, n + 1);
                int rndCol = StdRandom.uniform(1, n + 1);
                percolation.open(rndRow, rndCol);
            }
            fractions[i] = (double) percolation.numberOfOpenSites() / (n * n);
        }
    }

    public double mean() {
        mean = StdStats.mean(fractions);
        return mean;
    }

    public double stddev()  {
        stddev = StdStats.stddev(fractions);
        return stddev;
    }

    public double confidenceLo() {
        return mean - Math.sqrt(stddev) * CONFIDENCE_95 / Math.sqrt(fractions.length);
    }

    public double confidenceHi() {
        return mean + Math.sqrt(stddev) * CONFIDENCE_95 / Math.sqrt(fractions.length);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        StdOut.println("% java-algs4 PercolationStats " + n + " " + trials);
        StdOut.println("mean = " + percolationStats.mean());
        StdOut.println("stddev = " + percolationStats.stddev());
        StdOut.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
    }
}
