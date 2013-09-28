public class PercolationStats {
    private double[] opened;
    private int T;
    private int N;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException("N and T should be > 0");

        this.T = T;
        this.N = N;

        opened = new double[T];
        for (int i = 0; i < T; i++) {
            doTest(i);
        }
    }

    private void doTest(int indexTest) {
        final Percolation p = new Percolation(N);

        double count = 0;
        while (!p.percolates()) {
            int row = StdRandom.uniform(1, N + 1);
            int column = StdRandom.uniform(1, N + 1);
            if (!p.isOpen(row, column)) {
                p.open(row, column);
                count++;
            }
        }
        double maxSize = N * N;
        opened[indexTest] = count / maxSize;
    }

    public double mean() {
        return StdStats.mean(opened);
    }

    public double stddev() {
        return StdStats.stddev(opened);
    }

    public double confidenceLo() {
        return mean() - ((1.96 * Math.sqrt(stddev())) / Math.sqrt(T));
    }

    public double confidenceHi() {
        return mean() + ((1.96 * Math.sqrt(stddev())) / Math.sqrt(T));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: PercolationStats N T");
            return;
        }

        final int N = Integer.valueOf(args[0]);
        final int T = Integer.valueOf(args[1]);
        final PercolationStats ps = new PercolationStats(N, T);
        System.out.printf("mean                     = %f\n", ps.mean());
        System.out.printf("stddev                   = %f\n", ps.stddev());
        System.out.printf("95%% confidence interval  = %f, %f\n"
                , ps.confidenceHi(), ps.confidenceLo());

    }
}