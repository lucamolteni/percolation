public class Percolation {

    private boolean[][] grid;
    private int N;

    private WeightedQuickUnionUF qu;
    // Doesn't connect to bottom, doesn't backwash
    private WeightedQuickUnionUF qu2;

    private int VIRTUAL_TOP_KEY;
    private int VIRTUAL_BOTTOM_KEY;

    public Percolation(int N) {
        this.N = N;
        this.grid = new boolean[N][N];
        int maxIndex = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = false;
                maxIndex = xytoD(i + 1, j + 1);
            }
        }
        VIRTUAL_TOP_KEY = maxIndex + 1;
        VIRTUAL_BOTTOM_KEY = maxIndex + 2;
        qu = new WeightedQuickUnionUF(VIRTUAL_BOTTOM_KEY + 1);
        qu2 = new WeightedQuickUnionUF(VIRTUAL_BOTTOM_KEY + 1);
    }

    private int xytoD(int x, int y) {
        return x + (y * N);
    }

    public boolean isOpen(int i, int j) {
        validateIndex(i, j);
        return grid[j - 1][i - 1];
    }

    public boolean isFull(int i, int j) {
        validateIndex(i, j);
        return qu2.connected(VIRTUAL_TOP_KEY, xytoD(i, j));
    }

    public void open(int i, int j) {
        // Indexes are from 1 to N while array indexes from 0 to N - 1
        grid[j - 1][i - 1] = true;
        if (!isLeftEdge(j)) {
            union(i, j, 0, -1); // Connect to left square
        }
        if (!isRightEdge(j)) {
            union(i, j, 0, +1); // Connect to right square
        }
        if (!isTopEdge(i)) {
            union(i, j, -1, 0); // connect to top cell
        } else {
            unionVirtual(i, j, VIRTUAL_TOP_KEY, false); // Connect to top virtual
        }
        if (!isBottomEdge(i)) {
            union(i, j, 1, 0); // connect to bottom cell
        } else {
            // Connect to bottom virtual
            unionVirtual(i, j, VIRTUAL_BOTTOM_KEY, true);
        }
    }

    public boolean percolates() {
        return qu.connected(VIRTUAL_BOTTOM_KEY, VIRTUAL_TOP_KEY);
    }

    /**
     * connects to adjacent cell given coordinates and offset
     * @param i
     * @param j
     * @param rowOffset
     * @param columnOffset
     */
    private void union(int i, int j, int rowOffset, int columnOffset) {
        final int currentKey = xytoD(i, j);
        final int column2 = j + columnOffset;
        final int row2 = i + rowOffset;
        if (isOpen(row2, column2)) {
            qu.union(currentKey, xytoD(row2, column2));
            qu2.union(currentKey, xytoD(row2, column2));
        }
    }

    /**
     * Connect to virtual top and bottom in qu.
     * qu2 has not virtual bottom to avoid backwash
     * @param i
     * @param j
     * @param virtualKey
     * @param bottom
     */
    private void unionVirtual(int i, int j, int virtualKey, boolean bottom) {
        final int currentKey = xytoD(i, j);
        qu.union(currentKey, virtualKey);
        if (!bottom) {
            qu2.union(currentKey, virtualKey);
        }
    }

    /**
     * Valid index are only between 1 and N
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isValid(int x, int y) {
        return x >= 1 && x <= N && y >= 1 && y <= N;
    }

    private void validateIndex(int x, int y) {
        if (!isValid(x, y)) {
            throw new IndexOutOfBoundsException(
                    String.format("N:%d x:%d y:%d", N, x, y));
        }
    }

    private boolean isBottomEdge(int i) {
        return i == N;
    }

    private boolean isTopEdge(int i) {
        return i == 1;
    }

    private boolean isRightEdge(int j) {
        return j == N;
    }

    private boolean isLeftEdge(int j) {
        return j == 1;
    }

    public static void main(String[] args) {
        test2();
    }

    private static void test2() {
        final Percolation p = new Percolation(3);
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
        p.open(1, 2);
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));


        System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
        p.open(2, 2);
        System.out.println("p.isOpen(2,2) = " + p.isOpen(2, 2));
        System.out.println("p.isFull(2, 2) = " + p.isFull(2, 2));


        System.out.println("p.isOpen(3, 1) = " + p.isOpen(3, 2));
        p.open(3, 2);
        System.out.println("p.isOpen(3, 1) = " + p.isOpen(3, 2));
        p.isFull(3, 2);


        System.out.println("p.percolates() = " + p.percolates());
    }

    private static void test3() {
        final Percolation p = new Percolation(3);
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));
        p.open(1, 2);
        System.out.println("p.isOpen(1, 2) = " + p.isOpen(1, 2));

        System.out.println("p.isOpen(1,1) = " + p.isOpen(1, 1));
        p.open(1, 1);
        System.out.println("p.isOpen(1,1) = " + p.isOpen(1, 1));

        System.out.println("p.isFull(1, 2) = " + p.isFull(1, 2));

        System.out.println("p.isOpen(2, 2) = " + p.isOpen(2, 2));
        p.open(2, 2);
        System.out.println("p.isOpen(2, 2) = " + p.isOpen(2, 2));

        System.out.println("p.isOpen(2, 3) = " + p.isOpen(2, 3));
        p.open(2, 3);
        System.out.println("p.isOpen(2, 3) = " + p.isOpen(2, 3));

        p.isFull(2, 3);

        System.out.println("p.percolates() = " + p.percolates());

    }

    private static void test4() {
        final Percolation p = new Percolation(5);
        System.out.println("p.isOpen(1, 3) = " + p.isOpen(1, 3));
        p.open(1, 3);
        System.out.println("p.isOpen(1,3) = " + p.isOpen(1, 3));
        System.out.println("p.isFull(1, 3) = " + p.isFull(1, 3));
    }
}