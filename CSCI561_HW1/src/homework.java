import sun.misc.Queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Stack;

public class homework {

    public static String method = "";
    public static int size = 0;
    public static int lizards = 0;

    public static int line_number = 1;
    public static int[][] result;
    public static int[][] new_lizards_locations;

    public static int[][] lizards_locations;
    public static double currentSystemTemperature = 30.0;
    public static double freezingTemperature = 0.0;
    public static double currentStabilizer = 5.0;
    public static double currentSystemEnergy = 0.0;
    public static double coolingFactor = 0.05;
    public static double stabilizingFactor = 1.08;


    class Root {
        int[][] board;
        private int depth;
        private int length;
        private int lizard_num;


        // point initialized from parameters
        Root(int[][] board, int depth, int length, int lizard_num) {
            this.board = board;
            this.depth = depth;
            this.length = length;
            this.lizard_num = lizard_num;
        }

        // accessor methods
        public int[][] board() { return board; }
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


    int[][] dfs(int[][] board, int depth, int length, int lizard_num) {
        Stack stack = new Stack();
        Root root = new Root(board, depth, length, lizard_num);
        stack.push(root);
        while (!stack.isEmpty()) {
            Root curr_root = (Root) stack.pop();
            int [][] b = curr_root.board();
            int d = curr_root.depth();
            int w = curr_root.length();
            int l = curr_root.lizard_num();
//            System.out.println("Lizards in:"+l);
            if (l == lizards) {
//                System.out.println("asdf:"+l);
                //result = curr_root.board();
                return curr_root.board();
            }

            if (d < b.length) {
                for (int i = 0; i < b[0].length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[d][i] == 0 && isSafe(t, d, i)) {
                        t[d][i] = 1;
                        Root temp_root = new Root(t, d + 1, w, l + 1);
                        stack.push(temp_root);
                    }
                }

                for (int i = 0; i < b[0].length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[d][b.length-1-i] == 0 && isSafe(t, d, b.length-1-i)) {
                        t[d][b.length-1-i] = 1;
                        Root temp_root = new Root(t, d + 1, w, l + 1);
                        stack.push(temp_root);
                    }
                }
            }

            // left to right
            if (w < b[0].length) {
                for (int i = 0; i < b.length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[i][w] == 0 && isSafe(t, i, w)) {
                        t[i][w] = 1;
                        Root temp_root = new Root(t, d, w+1, l + 1);
                        stack.push(temp_root);
                    }
                }

                for (int i = 0; i < b.length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[b[0].length-1-i][w] == 0 && isSafe(t, b[0].length-1-i, w)) {
                        t[b[0].length-1-i][w] = 1;
                        Root temp_root = new Root(t, d, w+1, l + 1);
                        stack.push(temp_root);
                    }
                }
            }
        }
        int[][] empty = new int[0][];
        return empty;
    }

    int[][] bfs(int[][] board, int depth, int length, int lizard_num) throws InterruptedException {
        Root root = new Root(board, depth, length, lizard_num);
        Queue<Root> queue = new Queue<Root>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Root curr_root = queue.dequeue();
            int [][] b = curr_root.board();
            int d = curr_root.depth();
            int w = curr_root.length();
            int l = curr_root.lizard_num();
            System.out.println("Lizards in:"+l);
            if (l == lizards) {
                System.out.println("asdf:"+l);
                //result = curr_root.board();
                return curr_root.board();
            }

            if (d < b.length) {
                for (int i = 0; i < b[0].length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[d][i] == 0 && isSafe(t, d, i)) {
                        t[d][i] = 1;
                        Root temp_root = new Root(t, d + 1, w, l + 1);
                        queue.enqueue(temp_root);
                    }
                }

                for (int i = 0; i < b[0].length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[d][b.length-1-i] == 0 && isSafe(t, d, b.length-1-i)) {
                        t[d][b.length-1-i] = 1;
                        Root temp_root = new Root(t, d + 1, w, l + 1);
                        queue.enqueue(temp_root);
                    }
                }
            }

            // left to right
            if (w < b[0].length) {
                for (int i = 0; i < b.length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[i][w] == 0 && isSafe(t, i, w)) {
                        t[i][w] = 1;
                        Root temp_root = new Root(t, d, w+1, l + 1);
                        queue.enqueue(temp_root);
                    }
                }

                for (int i = 0; i < b.length; i++) {
                    int[][] t = deepCopy(b);
                    if (t[b[0].length-1-i][w] == 0 && isSafe(t, b[0].length-1-i, w)) {
                        t[b[0].length-1-i][w] = 1;
                        Root temp_root = new Root(t, d, w+1, l + 1);
                        queue.enqueue(temp_root);
                    }
                }
            }
        }
        int[][] empty = new int[0][];
        return empty;
    }

    boolean isSafe(int[][] board, int y, int x) {
        // up
        for (int i = y; i >= 0; i--) {
            if (board[i][x] == 1) {
                return false;
            }
            if (board[i][x] == 2) {
                break;
            }
        }

        // down
        for (int i = y; i != board.length; i++) {
            if (board[i][x] == 1) {
                return false;
            }
            if (board[i][x] == 2) {
                break;
            }
        }

        // left
        for (int i = x; i >= 0; i--) {
            if (board[y][i] == 1) {
                return false;
            }
            if (board[y][i] == 2) {
                break;
            }
        }

        // right
        for (int i = x; i != board.length; i++ ) {
            if (board[y][i] == 1) {
                return false;
            }
            if (board[y][i] == 2) {
                break;
            }

        }

        //left up
        for (int i = 0; i <= Math.min(y,x); i++) {
            if (board[y-i][x-i] == 1) {
                return false;
            }
            if (board[y-i][x-i] == 2) {
                break;
            }
        }

        //left down
        for (int i = 0; i!=Math.min(board.length-1-y,x)+1; i++) {
            if (board[y+i][x-i] == 1) {
                return false;
            }
            if (board[y+i][x-i] == 2) {
                break;
            }
        }

        //right up
        for (int i = 0; i != Math.min(y, board[0].length -1 -x)+1; i++) {
            if (board[y-i][x+i] == 1) {
                return false;
            }
            if (board[y-i][x+i] == 2) {
                break;
            }
        }

        //right down
        for (int i = 0; i!= Math.min(board.length-1-y, board[0].length-1-x)+1; i++) {
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
        System.out.println("6");
        if (delta < 0) {
            return true;
        }
        double E = Math.exp(-delta / temperature);
        double R = Math.random();
        if (R < E) {
            return true;
        }
        return false;
    }

    // number of conflicts needs to be as small as possible
    int countConflicts(int [][] board) {
        System.out.println("5");
        int conflicts = 0;
        for (int row = 0; row != board.length; row++) {
            for (int col = 0; col != board[row].length; col++) {
                if (board[row][col] == 1) {
                    board[row][col] = 0;
                    if (!isSafe(board, row, col)) {
//                        System.out.println("conflicting row: "+row+" col: "+col+" safe: "+isSafe(board, row, col));
                        conflicts++;
                        board[row][col] = 1;
                    }
                    board[row][col] = 1;

                }
            }
        }
        return conflicts;
    }

    // generates random lizard locations
    void randomPositionsGenerator() {
        System.out.println("4");
        for (int i = 0; i < lizards; i++) {
            int row = 0;
            int col = 0;
            int[] random_position = randomPositionGenerator();
            row = random_position[0];
            col = random_position[1];

            while (result[row][col] != 0) {
                //generate again
                random_position = randomPositionGenerator();
                row = random_position[0];
                col = random_position[1];
            }

            lizards_locations[i][0] = row;
            lizards_locations[i][1] = col;
            result[row][col] = 1;
        }
    }

    // generates random lizard location
    int[] randomPositionGenerator() {
        // generate random numbers between 1 and size number
        int row = (int )(Math.random() * (size-1) + 0);
        int col = (int )(Math.random() * (size-1) + 0);
        return new int[] {row, col};
    }





    int[][] generateNeighbor(int[][] board) {
        // take our current board, take a random queen, make a single step.
        // need a list of lizards to track all lizards' locations
        // take a random lizard
        int changingLizard = (int)(Math.random() * (lizards-1) + 0);

        // current lizards
        new_lizards_locations = deepCopy(lizards_locations);

        boolean repetitions = true;
        while (repetitions) {
            int old_row = new_lizards_locations[changingLizard][0];
            int old_col = new_lizards_locations[changingLizard][1];
            System.out.println("2");
            new_lizards_locations[changingLizard][0] = (new_lizards_locations[changingLizard][0] + ((int)(Math.random() * (size - 1))) )% size;
            new_lizards_locations[changingLizard][1] = (new_lizards_locations[changingLizard][1] + ((int)(Math.random() * (size - 1))) )% size;
//            System.out.println("row: "+new_lizards_locations[changingLizard][0]+"col: "+new_lizards_locations[changingLizard][1]);
            int new_row = new_lizards_locations[changingLizard][0];
            int new_col = new_lizards_locations[changingLizard][1];
            if (result[new_row][new_col] == 0) {
                result[old_row][old_col] = 0;
                result[new_row][new_col] = 1;
                repetitions = false;

            } else {
                new_lizards_locations[changingLizard][0] = old_row;
                new_lizards_locations[changingLizard][1] = old_col;
            }

        }
        for (int[] row : lizards_locations)
        {
            System.out.println(Arrays.toString(row));
        }
        System.out.println("___________________________");
        for (int[] row : new_lizards_locations)
        {
            System.out.println(Arrays.toString(row));
        }
        //return countConflicts(result); //return number of conflicts
        return board;
    }

    void acceptNext() {
        System.out.println("3");
        for (int row = 0; row != result.length; row++) {
            for (int col = 0; col != result[row].length; col++) {
                if (result[row][col] == 1) {
                    result[row][col] = 0;
                }
            }
        }
//        System.out.println("size: "+new_lizards_locations.length);
        for (int i = 0; i < lizards; i++) {
            //lizards_locations[i] = new int[]{new_lizards_locations[i][0], new_lizards_locations[i][1]};
//            result[lizards_locations[i][0]][lizards_locations[i][1]] = 0;
            result[new_lizards_locations[i][0]][new_lizards_locations[i][1]] = 1;
        }
    }


    boolean simulatedAnnealing() {
        //while (currentSystemTemperature > freezingTemperature) {
//            double newEnergy = generateNext(),
//                    energyDelta = newEnergy - currentSystemEnergy;
//            if (probability(currentSystemTemperature, energyDelta)) {
//                acceptNext();
//                currentSystemEnergy = newEnergy;
//            }
//            currentSystemTemperature = currentSystemTemperature - coolingFactor;
//            currentStabilizer = currentStabilizer * stabilizingFactor;
//        int curr_cost = countConflicts(result);
//        int[][] temp_board = deepCopy(result);
//        int new_cost = generateNext();
        return false;
    }

//
//    simulated-annealing(initial solution)
//    let solution be initial
//    let t be an initial temperature
//    until t is almost zero
//          let neighbor be a random neighbor of solution
//          if the cost of neighbor is less than the cost of solution
//              let solution be neighbor
//              stop if the cost is now 0
//          otherwise
//              let c be the cost increase
//              compute p = e^(-c/t)
//              with probability p, let solution be neighbor
//          multiply t by a decay rate
//     return solution
























    public static void main(String args[]) throws IOException, InterruptedException {

//        int[][] initial_map = new int[][];
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
                    // store the nursery as a matrix?
                    for (int i = 0; i != size; i++) {
                        initial_map[line_number-4][i] = Integer.parseInt(str.valueOf(str.charAt(i)));
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

//        initial_map[3][4] = 2;
//        initial_map[5][5] = 2;

        result = initial_map;
        for (int[] row : initial_map)
        {
            System.out.println(Arrays.toString(row));
        }

        System.out.println("____________________________");

        homework hw = new homework();


        if (method.equals("DFS")) {
            result = hw.dfs(initial_map,0,0,0);

        }

        else if (method.equals("BFS")) {
            result = hw.bfs(initial_map,0,0,0);
        }

        else if (method.equals("SA")) {
//            result = hw.simulatedAnnealing();
            lizards_locations = new int[lizards][2];
            hw.randomPositionsGenerator();
            for (int[] row : result)
            {
                System.out.println(Arrays.toString(row));
            }

            boolean i = hw.simulatedAnnealing();
            System.out.println(i);



            for (int[] row : result)
            {
                System.out.println(Arrays.toString(row));
            }

//            boolean done = false;
//
//            while (!done) {
//                System.out.println("1");
//                done = hw.simulatedAnnealing();
//            }




//            result[0][0] = 1;
//            result[1][2] = 1;
//            result[1][4] = 1;
//            result[1][6] = 1;
//            result[2][4] = 1;
//            result[4][5] = 1;


        }

        try{
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            writer.println(method);
            writer.println(size);
            writer.println(lizards);
            for (int[] row : result)
            {
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

