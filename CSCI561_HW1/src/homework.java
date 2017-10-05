import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class homework {
    // node class is each node in minimax tree
    // each node has a grid, alpha, beta, and v
    class Node {
        private String[][] grid;
        private int alpha;
        private int beta;
        private int currentV;
        Node(String[][] grid, int alpha, int beta, int v) {
            this.grid = grid;
            this.alpha = alpha;
            this.beta = beta;
            this.currentV = v;
        }
        // getters
        public String[][] getGrid() { return grid; }
        public int getAlpha() { return alpha; }
        public int getBeta() { return beta; }
        public int getCurrentV() { return currentV; }
        // setters
        public void setGrid(String[][] grid) { this.grid = grid; }
        public void setAlpha(int alpha) { this.alpha = alpha; }
        public void setBeta(int beta) { this.beta = beta; }
        public void setCurrentV(int v) { this.currentV = v; }
    }

    private static int line_number = 1;
    private static int size = 0;
    private static int fruits_types = 0;
    private static float remaining_time = 0;

    private static int currScore = 0;
    private static int totalScore = 0;

    private static int startingRow = 0;
    private static int startingCol = 0;

    // functions needed:
    // 0. check if game is over
    // 1. player choose a fruit
    // 2. increment score
    // 3. sink
    // 4. check if game is over
    // 5. AI run alpha beta pruning
    // 6. AI make a choice in given time



    // create a board to record big chunks of fruits of current board
    // draw next steps base on the board of chunks of current board
    public boolean isGameOver(String[][] grid) {
        for (int col = 0; col != grid.length; col++) {
            for (int row = 0; row != grid[0].length; row++) {
                if (!grid[row][col].equals("*")) {
                    return false;
                }
            }
        }
        return true;
    }

    // create a board that has only the fruits that shows up for the first time in its chunk
    public String[][] groupFruits(String[][] grid) {
        String[][] blocks = deepCopy(grid);
        for (int col = 0; col != grid.length; col++) {
            for (int row = 0; row != grid[0].length; row++) {
                if (!grid[row][col].equals("*")) {
                    String initialVal = grid[row][col];
                    blockHelper(grid,row,col,grid[row][col], true);
                    grid[row][col] = initialVal;
                }
            }
        }
        return blocks;
    }

    public String[][] deepCopy(String[][] A) {
        String[][] B = new String[A.length][A[0].length];
        for (int x = 0; x < A.length; x++) {
            for (int y = 0; y < A[0].length; y++) {
                B[x][y] = A[x][y];
            }
        }
        return B;
    }

    public void blockHelper(String[][] grid, int row, int col, String val, boolean first) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || (!grid[row][col].equals(val))) return;
        if (grid[row][col].equals(val)) {
            if (!first) {
                grid[row][col] = "*";
            }
        }
        blockHelper(grid, row + 1, col, val, false);
        blockHelper(grid, row - 1, col, val, false);
        blockHelper(grid, row, col + 1, val, false);
        blockHelper(grid, row, col - 1, val, false);
    }








    public void claimFruit(String[][] grid, int row, int col) {
        // should use DFS or BFS to clear selected fruit
        String val = grid[row][col];
        DFS(grid,row,col,val);
//        for (String[] r : grid) {
//            System.out.println(Arrays.toString(r));
//        }
//        System.out.println("___________________________________");
        sink(grid);
        currScore *= currScore;
    }

    // this function claims fruit and turns them into *s
    public void DFS(String[][] grid, int row, int col, String val) {
        if (row < 0 || col < 0 || row >= grid.length || col >= grid[0].length || (!grid[row][col].equals(val))) return;
        currScore++;
        grid[row][col] = "*";
        DFS(grid, row + 1, col, val);
        DFS(grid, row - 1, col, val);
        DFS(grid, row, col + 1, val);
        DFS(grid, row, col - 1, val);
    }

    // this function sinks all the fruits after they are claimed
    public void sink(String[][] grid) {
        // use for loops to sink all fruits
        for (int row = grid.length-1; row != 0; row--) {
            for (int col = 0; col != grid[0].length; col++) {
                if (grid[row][col].equals("*")) {
                    for (int i = row-1; i!=-1; i--) {
                        if (!grid[i][col].equals("*")) {
                            grid[row][col] = grid[i][col];
                            grid[i][col] = "*";
                            break;
                        }
                    }
                }
            }
        }
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        homework hw = new homework();
        String[][] initial_board = new String[0][];
        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (line_number == 1) {
                    size = Integer.valueOf(str);
                    initial_board = new String[size][size];
                }
                else if (line_number == 2) {
                    fruits_types = Integer.valueOf(str);
                }
                else if (line_number == 3) {
                    remaining_time = Float.valueOf(str);
                }
                else {
                    for (int i = 0; i != size; i++) {
                        initial_board[line_number-4][i] = String.valueOf(str.charAt(i));
                    }
                }
                line_number ++;
            }
            System.out.println(size);
            System.out.println(fruits_types);
            System.out.println(remaining_time);
            in.close();
        } catch (IOException e) {
        }
        for (String[] row : initial_board) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("___________________________________");
//        hw.claimFruit(initial_board,8,3);
//        for (String[] row : initial_board) {
//            System.out.println(Arrays.toString(row));
//        }

//        System.out.println("gameover: "+hw.isGameOver(initial_board));

        hw.groupFruits(initial_board);
        for (String[] row : initial_board) {
            System.out.println(Arrays.toString(row));
        }
    }
}
