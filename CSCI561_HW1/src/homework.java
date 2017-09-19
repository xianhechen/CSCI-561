import sun.misc.Queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class homework {
    private static String method = "";
    private static int size = 0;
    private static int lizards = 0;
    private static int line_number = 1;
    private static boolean success = false;
    private static int available_space = 0;
    private static int trees = 0;
    private static int[][] result;
    private static int[][] lizards_locations;
    private static double currentSystemTemperature = 10.0;
    private static int nextConflicts = 0;
    private static int old_row, old_col, new_row, new_col, changingLizard;

    class Root {
        Stack<int[]> lizards_positions;
        private int depth;
        private int length;
        private int lizard_num;
        // point initialized from parameters
        Root(Stack<int[]> lizards_positions, int depth, int length, int lizard_num) {
            this.lizards_positions = lizards_positions;
            this.depth = depth;
            this.length = length;
            this.lizard_num = lizard_num;
        }
        // accessor methods
        public Stack<int[]> lizards_positions() { return lizards_positions; }
        public int depth() { return depth; }
        public int length() { return length; }
        public int lizard_num() { return lizard_num; }
    }

    int[][] deepCopy(int[][] A) {
        int[][] B = new int[A.length][A[0].length];
        for (int x = 0; x < A.length; x++) {
            for (int y = 0; y < A[0].length; y++) {
                if (A[x][y] != 0) { //write only when necessary
                    B[x][y] = A[x][y];
                }
            }
        }
        return B;
    }

    void changeBoard(Stack<int[]> p, int target) {
        //change board
        for (Iterator iterator = p.iterator(); iterator.hasNext();) {
            int[] loc = (int[]) iterator.next();
            result[loc[0]][loc[1]] = target;
        }
    }

    Stack dfs(int depth, int length, int lizard_num) {
        success = false;
        Stack<int[]> lizards_positions = new Stack<>();
        Stack stack = new Stack();
        Root root = new Root(lizards_positions, depth, length, lizard_num);
        stack.push(root);
        while (!stack.isEmpty()) {
            Root curr_root = (Root) stack.pop();
            Stack<int[]> p = curr_root.lizards_positions();
            int d = curr_root.depth();
            int w = curr_root.length();
            int l = curr_root.lizard_num();
            if (l == lizards) {
                success = true;
                return curr_root.lizards_positions();
            }
            if (d < result.length) {
                for (int i = 0; i < result[0].length; i++) {
                    Stack<int[]> new_p = new Stack<>();
                    for (Iterator iterator = p.iterator(); iterator.hasNext();) {
                        int[] loc = (int[]) iterator.next();
                        new_p.push(loc);
                    }
                    changeBoard(new_p, 1);
                    if (result[d][i] == 0 && isSafe(result, d, i)) {
                        new_p.push(new int[]{d, i});
                        Root temp_root = new Root(new_p, d + 1, w, l + 1);
                        stack.push(temp_root);

                    }
                    if (i == result[0].length-1 && p.size() != l+1) {
                        Root temp_root = new Root(new_p, d + 1, w, l);
                        stack.push(temp_root);
                    }
                    changeBoard(new_p, 0);
                }
            }
            if (w < result[0].length) {
                for (int i = 0; i < result.length; i++) {
                    Stack<int[]> new_p = new Stack<>();
                    for (Iterator iterator = p.iterator(); iterator.hasNext();) {
                        int[] loc = (int[]) iterator.next();
                        new_p.push(loc);
                    }
                    changeBoard(new_p, 1);
                    if (result[i][w] == 0 && isSafe(result, i, w)) {
                        new_p.push(new int[]{i, w});
                        Root temp_root = new Root(new_p, d, w+1, l + 1);
                        stack.push(temp_root);
                    }
                    if (i == result.length-1 && p.size() != l+1) {
                        Root temp_root = new Root(new_p, d , w+1, l);
                        stack.push(temp_root);
                    }
                    changeBoard(new_p, 0);
                }
            }
        }
        Stack<int[]> empty = new Stack<>();
        return empty;
    }

    Stack bfs(int depth, int length, int lizard_num) throws InterruptedException {
        success = false;
        Stack<int[]> lizards_positions = new Stack<>();
        Queue<Root> queue = new Queue();
        Root root = new Root(lizards_positions, depth, length, lizard_num);
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Root curr_root = queue.dequeue();
            Stack<int[]> p = curr_root.lizards_positions();
            int d = curr_root.depth();
            int w = curr_root.length();
            int l = curr_root.lizard_num();
            if (l == lizards) {
                success = true;
                return curr_root.lizards_positions();
            }
            if (d < result.length) {
                for (int i = 0; i < result[0].length; i++) {
                    Stack<int[]> new_p = new Stack<>();
                    for (Iterator iterator = p.iterator(); iterator.hasNext();) {
                        int[] loc = (int[]) iterator.next();
                        new_p.push(loc);
                    }
                    changeBoard(new_p, 1);
                    if (result[d][i] == 0 && isSafe(result, d, i)) {
                        new_p.push(new int[]{d, i});
                        Root temp_root = new Root(new_p, d + 1, w, l + 1);
                        queue.enqueue(temp_root);
                    }
                    if (i == result.length-1 && p.size() != l+1) {
                        Root temp_root = new Root(new_p, d + 1, w, l);
                        queue.enqueue(temp_root);
                    }
                    changeBoard(new_p, 0);
                }
            }
            if (w < result[0].length) {
                for (int i = 0; i < result.length; i++) {
                    Stack<int[]> new_p = new Stack<>();
                    for (Iterator iterator = p.iterator(); iterator.hasNext();) {
                        int[] loc = (int[]) iterator.next();
                        new_p.push(loc);
                    }
                    changeBoard(new_p, 1);
                    if (result[i][w] == 0 && isSafe(result, i, w)) {
                        new_p.push(new int[]{i, w});
                        Root temp_root = new Root(new_p, d, w+1, l + 1);
                        queue.enqueue(temp_root);
                    }
                    if (i == result[0].length-1 && p.size() != l+1) {
                        Root temp_root = new Root(new_p, d , w+1, l);
                        queue.enqueue(temp_root);
                    }
                    changeBoard(new_p, 0);
                }
            }
        }
        Stack<int[]> empty = new Stack<>();
        return empty;
    }

    boolean isSafe(int[][] board, int y, int x) {
        // up
        for (int i = y-1; i >= 0; i--) {
            if (board[i][x] == 1) {
                return false;
            }
            if (board[i][x] == 2) {
                break;
            }
        }
        // down
        for (int i = y+1; i != board.length; i++) {
            if (board[i][x] == 1) {
                return false;
            }
            if (board[i][x] == 2) {
                break;
            }
        }
        // left
        for (int i = x-1; i >= 0; i--) {
            if (board[y][i] == 1) {
                return false;
            }
            if (board[y][i] == 2) {
                break;
            }
        }
        // right
        for (int i = x+1; i != board.length; i++ ) {
            if (board[y][i] == 1) {
                return false;
            }
            if (board[y][i] == 2) {
                break;
            }
        }
        //left up
        for (int i = 1; i <= Math.min(y,x); i++) {
            if (board[y-i][x-i] == 1) {
                return false;
            }
            if (board[y-i][x-i] == 2) {
                break;
            }
        }
        //left down
        for (int i = 1; i!=Math.min(board.length-1-y,x)+1; i++) {
            if (board[y+i][x-i] == 1) {
                return false;
            }
            if (board[y+i][x-i] == 2) {
                break;
            }
        }
        //right up
        for (int i = 1; i != Math.min(y, board[0].length -1 -x)+1; i++) {
            if (board[y-i][x+i] == 1) {
                return false;
            }
            if (board[y-i][x+i] == 2) {
                break;
            }
        }
        //right down
        for (int i = 1; i!= Math.min(board.length-1-y, board[0].length-1-x)+1; i++) {
            if (board[y+i][x+i] == 1) {
                return false;
            }
            if (board[y+i][x+i] == 2) {
                break;
            }
        }
        return true;
    }

    // flip the coin function
    boolean probability(double temperature, double delta) {
        double E = Math.exp((-1)*delta / temperature);
        double R = Math.random();
        if (R < E) {
            return true;
        }
        return false;
    }

    // number of conflicts needs to be as small as possible
    int countConflicts(int [][] board) {
        int conflicts = 0;
        for (int row = 0; row != board.length; row++) {
            for (int col = 0; col != board[row].length; col++) {
                if (board[row][col] == 1) {
                    if (!isSafe(board, row, col)) {
                        conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    // generates random lizard locations
    void randomPositionsGenerator() {
        if (size == 4) {
            for (int i = 0; i < lizards; i++) {
                int row = 0;
                int col = 0;
                int[] random_position;
                boolean repetition = true;
                while (repetition) {
                    random_position = randomPositionGenerator();
                    row = random_position[0];
                    col = random_position[1];
                    if (result[row][col] == 0) {
                        repetition = false;
                    }
                }
                lizards_locations[i][0] = row;
                lizards_locations[i][1] = col;
                result[row][col] = 1;
            }
        }
        else {
            for (int i = 0; i < lizards; i++) {
                int row = 0;
                int col = 0;
                int[] random_position;
                boolean repetition = true;
                while (repetition) {
                    random_position = randomPositionGenerator();
                    row = random_position[0];
                    col = random_position[1];
                    if (result[row][col] == 0) {
                        repetition = false;
                    }
                }
                lizards_locations[i][0] = row;
                lizards_locations[i][1] = col;
                result[row][col] = 1;
            }
        }
    }

    // generates random lizard location
    int[] randomPositionGenerator() {
        // generate random numbers between 1 and size number
        int row = (int)(Math.random() * (size));
        int col = (int)(Math.random() * (size));
        return new int[] {row, col};
    }

    void generateNeighbor() {
        // take our current board, take a random queen, make a single step.
        // need a list of lizards to track all lizards' locations
        // take a random lizard
        changingLizard = (int)(Math.random() * (lizards));
        old_row = lizards_locations[changingLizard][0];
        old_col = lizards_locations[changingLizard][1];
        // current lizards
        int[][] new_lizards_locations = deepCopy(lizards_locations);
        boolean repetitions = true;
        while (repetitions) {
            new_row = (int)(Math.random() * (size));
            new_col = (int)(Math.random() * (size));
            if (result[new_row][new_col] == 0) {
                repetitions = false;
            }
        }
        new_lizards_locations[changingLizard][0] = new_row;
        new_lizards_locations[changingLizard][1] = new_col;
        lizards_locations = deepCopy(new_lizards_locations);
        result[new_row][new_col] = 1;
        result[old_row][old_col] = 0;
    }

    boolean simulatedAnnealing() {
        randomPositionsGenerator();
        success = false;
        while (currentSystemTemperature > 0.1) {
            int currConflicts = countConflicts(result);
            generateNeighbor();
            nextConflicts = countConflicts(result);
            if (nextConflicts == 0) {
                success = true;
                return true;
            }
            if (nextConflicts < currConflicts) {
                currentSystemTemperature = currentSystemTemperature * 0.99;
            } else {
                if (probability(currentSystemTemperature, (nextConflicts- currConflicts))) {
                    currentSystemTemperature = currentSystemTemperature * 0.999999;
                } else {
                    lizards_locations[changingLizard][0] = old_row;
                    lizards_locations[changingLizard][1] = old_col;
                    result[old_row][old_col] = 1;
                    result[new_row][new_col] = 0;
                }
            }
        }
        currentSystemTemperature = 0;
        return true;
    }

    public static void main(String args[]) throws IOException, InterruptedException {
        int[][] initial_map = new int[0][];
        try {
            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
            String str;
            while ((str = in.readLine()) != null) {
                if (line_number == 1) {
                    method = str;
                }
                else if (line_number == 2) {
                    size = Integer.valueOf(str);
                    initial_map = new int[size][size];
                }
                else if (line_number == 3) {
                    lizards = Integer.valueOf(str);
                }
                else {
                    // store the nursery as a matrix
                    for (int i = 0; i != size; i++) {
                        initial_map[line_number-4][i] = Integer.parseInt(str.valueOf(str.charAt(i)));
                        if (Integer.parseInt(str.valueOf(str.charAt(i))) == 0) {
                            available_space+=1;
                        }
                        else if (Integer.parseInt(str.valueOf(str.charAt(i))) == 2) {
                            trees+=1;
                        }
                    }
                }
                line_number ++;
            }
            System.out.println(method);
            System.out.println(size);
            System.out.println(lizards);
            in.close();
        } catch (IOException e) {
        }
        result = initial_map;
        for (int[] row : initial_map)
        {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("____________________________");
        homework hw = new homework();
        if (method.equals("BFS")) {
            if ( trees == 0 && size < lizards) {
            } else if (available_space > lizards) { // run for 5 min
                Stack<int[]> locations = hw.bfs(0,0,0);
                for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
                    int[] loc = (int[]) iterator.next();
                    result[loc[0]][loc[1]] = 1;
                }
                for (int[] row : result)
                {
                    System.out.println(Arrays.toString(row));
                }
            } else {
            }
        }
        else if (method.equals("DFS")) {
            if ( trees == 0 && size < lizards) {
            } else if (available_space > lizards) { // run for 5 min
                Stack<int[]> locations = hw.dfs(0,0,0);
                for (Iterator iterator = locations.iterator(); iterator.hasNext();) {
                    int[] loc = (int[]) iterator.next();
                    result[loc[0]][loc[1]] = 1;
                }
                for (int[] row : result)
                {
                    System.out.println(Arrays.toString(row));
                }
            } else {
            }
        }
        else if (method.equals("SA")) {
            if ( trees == 0 && size < lizards) {
            } else if (available_space > lizards) {
                lizards_locations = new int[lizards][2];
                hw.simulatedAnnealing();
                System.out.println("conflicts: "+nextConflicts);
                for (int[] row : result)
                {
                    System.out.println(Arrays.toString(row));
                }
            } else {
            }
        }
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            if ( trees == 0 && size < lizards) {
            } else if (success == false) {
                writer.println("FAIL");
            } else if (success == true && available_space > lizards) {
                writer.println("OK");
                for (int[] row : result)
                {
                    String result_row = "";
                    for (int i = 0; i != row.length; i++) {
                        result_row += row[i];
                    }
                    writer.println(result_row);
                }
            } else {
                writer.println("FAIL");
            }
            writer.close();
        } catch (IOException e) {
        }
    }
}

