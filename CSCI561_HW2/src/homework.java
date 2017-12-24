import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class homework {
    // state class is each node in minimax tree
    static class State {
        private String[][] grid;
        private int total;
        private int depth;

        private int row;
        private int col;

        State(String[][] grid, int total, int depth, int row, int col) {
            this.grid = grid;
            this.total = total;
            this.depth = depth;
            this.row = row;
            this.col = col;
        }
        // getters
        public String[][] getGrid() { return grid; }
        public int getTotal() { return total; }
        public int getDepth() { return depth; }
        public int getRow() { return row; }
        public int getCol() { return col; }
    }

    private static String[][] gameBoard;
    private static int line_number = 1;
    private static int size = 0;
    private static int fruits_types = 0;
    private static float remaining_time = 0;

    private static int currScore = 0;
    private static int totalScore = 0;

    private static int startingRow = 0;
    private static int startingCol = 0;

    private static int maxDepth = 1;
    private static int statesNum = 0;

    private static int alpha = Integer.MIN_VALUE;
    private static int beta = Integer.MAX_VALUE;

    private static boolean isCurrPlayer = true;

    private static float msPassed = 0.0f;

    private static String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

//    Timer myTimer = new Timer();
//    TimerTask task = new TimerTask() {
//        public void run() {
//            msPassed++;
//        }
//    };
//
//    public void start() {
//        myTimer.scheduleAtFixedRate(task,0,1);
//    }


    // create a board to record big chunks of fruits of current board
    // draw next steps base on the board of chunks of current board
    public boolean isGameOver(String[][] grid) {
        int count = 0;
        String[][] temp_0 = deepCopy(grid);
        String[][] temp = groupFruits(temp_0);
        for (int col = 0; col != temp.length; col++) {
            for (int row = 0; row != temp[0].length; row++) {
                if (!temp[row][col].equals("*")) {
                    count++;
                }
            }
        }
        return count == 1;
    }

    // create a board that has only the fruits that shows up for the first time in its chunk
    public String[][] groupFruits(String[][] grid) {
        String[][] blocks = deepCopy(grid);
        for (int col = 0; col != blocks.length; col++) {
            for (int row = 0; row != blocks[0].length; row++) {
                if (!blocks[row][col].equals("*")) {
                    String initialVal = blocks[row][col];
                    blockHelper(blocks,row,col,blocks[row][col], true);
                    blocks[row][col] = initialVal;
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
        // reset currentScore
        currScore = 0;
        DFS(grid,row,col,val);
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

    public int[] miniMax(State state) {

        State result = maxValue(state,alpha, beta);
//        for (String[] row : result.getGrid()) {
//            System.out.println(Arrays.toString(row));
//        }

        int row = result.getRow();
        int col = result.getCol();
        int final_score = result.getTotal();

        return new int[] {row, col, final_score};

    }

    public State maxValue(State state, int alpha, int beta) {
        if (cutoffTest(state)) {
            if(isGameOver(state.getGrid()) && state.depth == maxDepth) {
                state.total += currScore;
            }
            if(!isGameOver(state.getGrid()) && state.depth == maxDepth && state.depth > 1) {
                state.total += currScore;
            }
            return state; // EVAL(state)
        }
        isCurrPlayer = true;
        State result_1 = new State(state.getGrid(), state.getTotal(), state.getDepth(), state.getRow(), state.getCol());

        Stack<State> successors = successors(state);

        for (State successor : successors) {
            int tempTotal = minValue(successor, alpha, beta).getTotal();
            if (alpha < tempTotal) {
                alpha = tempTotal;
                result_1.col = successor.getCol();
                result_1.row = successor.getRow();
                result_1.total = alpha;
            }
            if (alpha >= beta) {
                result_1.total=beta;
                return result_1;
            }
        }
        return result_1;
    }

    public State minValue(State state, int alpha, int beta) {
        if (cutoffTest(state)) {
            if(isGameOver(state.getGrid()) && state.depth == maxDepth) {
                state.total -= currScore;
            }
            if(!isGameOver(state.getGrid()) && state.depth == maxDepth && state.depth > 1) {
                state.total -= currScore;
            }

            return state; // EVAL(state)
        }
        isCurrPlayer = false;
        State result_1 = new State(state.getGrid(), state.getTotal(), state.getDepth(), state.getRow(), state.getCol());
        Stack<State> successors = successors(state);
        for (State successor : successors) {
            int tempTotal = maxValue(successor, alpha, beta).getTotal();
            if (beta > tempTotal) {
                beta = tempTotal;
                result_1.col = successor.getCol();
                result_1.row = successor.getRow();
                result_1.total = beta;
            }
            if (beta <= alpha) {
                result_1.total=alpha;
                return result_1;
            }
        }
        return result_1;
    }


    public boolean cutoffTest(State state) {
        if (isGameOver(state.getGrid()) || state.getDepth() == maxDepth) {




            if (isGameOver(state.getGrid())) {
                String[][] group = groupFruits(state.getGrid());

                for (int col = 0; col != group.length; col++) {
                    for (int row = 0; row != group[0].length; row++) {
                        if (!group[row][col].equals("*")) {
                            state.col = col;
                            state.row = row;
                            String[][] temp = deepCopy(state.getGrid());
                            claimFruit(temp, row, col);

                            state.total += currScore;
                        }
                    }
                }
            }
            return true;
        }

        return false;
    }

    public Stack<State> successors(State state) {

        Stack<State> successors = new Stack<>();
        String[][] group = groupFruits(state.getGrid());
//        for (String[] s : group) {
//            System.out.println(Arrays.toString(s));
//        }
        for (int col = 0; col != group.length; col++) {
            for (int row = 0; row != group[0].length; row++) {
                if (!group[row][col].equals("*")) {
                    // make deepcopy of original board, claim this point on that board
                    // put new board into node, put node into stack

                    String[][] successor = deepCopy(state.getGrid());
                    claimFruit(successor,row,col);
                    // claim Fruit returns a score, push to end of successor
                    int total = state.getTotal();
                    if (isCurrPlayer) {
                        total += currScore;
                    } else {
                        total -= currScore;
                    }
                    statesNum++;
                    State newState = new State(successor, total, state.getDepth()+1, row, col);
                    successors.push(newState);
                }
            }
        }
        // returns a stack of successors
        return successors;
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        homework hw = new homework();
        gameBoard = new String[0][];
        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (line_number == 1) {
                    size = Integer.valueOf(str);
                    gameBoard = new String[size][size];
                }
                else if (line_number == 2) {
                    fruits_types = Integer.valueOf(str);
                }
                else if (line_number == 3) {
                    remaining_time = Float.valueOf(str);
                }
                else {
                    for (int i = 0; i != size; i++) {
                        gameBoard[line_number-4][i] = String.valueOf(str.charAt(i));
                    }
                }
                line_number ++;
            }
            if (remaining_time < 120.0f || size > 16 || fruits_types > 6) {
                maxDepth = 2;
            } else if (size == 26 && fruits_types > 2) {
                maxDepth = 2;
            } else {
                maxDepth = 3;
            }
//            System.out.println(size);
//            System.out.println(fruits_types);
//            System.out.println(remaining_time);
            in.close();
        } catch (IOException e) {
        }


//        System.out.println("gameover: "+hw.isGameOver(gameBoard));
//        System.out.println("_____________MINIMAX____________");
        // this is the initial state
        State tempState = new State(gameBoard, 0, 0, 0, 0);

//        hw.start();



        int[] result = hw.miniMax(tempState);

//        System.out.println(statesNum);
//        System.out.println(msPassed);

//        System.out.println("ms passed: " + msPassed);
//        System.out.println("result");

        hw.claimFruit(gameBoard,result[0],result[1]);
//        System.out.println("row: "+result[0]+" col: "+result[1]+" score: "+result[2]);
//        for (String[] row : gameBoard) {
//            System.out.println(Arrays.toString(row));
//        }
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            String loc = getCharForNumber(result[1]+1);
            loc += result[0]+1;
            writer.println(loc);
            for (String[] row : gameBoard) {
                String result_row = "";
                for (int i = 0; i != row.length; i++) {
                    result_row += row[i];
                }
                writer.println(result_row);
            }
            writer.close();
        } catch (IOException e) {
        }
    }
}

// things to do:
// check if input is out of border, less than 1, or greater than 26
// only 1 fruit
//