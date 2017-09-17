import sun.misc.Queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class NQueenProblem {

    public static String method = "";
    public static int size = 0;
    public static int lizards = 0;

    public static int line_number = 1;
    public static int[][] result;
    public static int[][] neighbor;

    public static int[][] new_lizards_locations;

    public static int[][] lizards_locations;
    public static double currentSystemTemperature = 10.0;
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






    boolean conflict(int[][] board, int y, int x) {
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
//
        //right up
        for (int i = 1; i != Math.min(y, board[0].length -1 -x)+1; i++) {
            if (board[y-i][x+i] == 1) {
                return false;
            }
            if (board[y-i][x+i] == 2) {
                break;
            }
        }
//
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
//                    board[row][col] = 0;
                    if (!conflict(board, row, col)) {
//                        System.out.println("conflicting row: "+row+" col: "+col+" safe: "+isSafe(board, row, col));
                        conflicts++;
//                        board[row][col] = 1;
                    }
//                    board[row][col] = 1;
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
                random_position = randomPositionGenerator();
                row = random_position[0];
                col = random_position[1];
            }
            lizards_locations[i][0] = row;
            lizards_locations[i][1] = col;
            result[row][col] = 1;
        }
        for (int[] row : result)
        {
            System.out.println(Arrays.toString(row));
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
        int changingLizard = (int)(Math.random() * (lizards-1));
        int old_row = lizards_locations[changingLizard][0];
        int old_col = lizards_locations[changingLizard][1];
        System.out.println("old_row: "+old_row+" old_col "+old_col);
        // current lizards
        new_lizards_locations = deepCopy(lizards_locations);
        int[][] nextBoard = deepCopy(board);
        System.out.println("99");
        boolean repetitions = true;
        while (repetitions) {
            int new_row = (int)(Math.random() * (size));
            int new_col = (int)(Math.random() * (size));
            if (board[new_row][new_col] == 0) {
                new_lizards_locations[changingLizard][0] = new_row;
                new_lizards_locations[changingLizard][1] = new_col;
                lizards_locations = deepCopy(new_lizards_locations);
                nextBoard[old_row][old_col] = 0;
                nextBoard[new_row][new_col] = 1;
                repetitions = false;
            }
        }
        for (int[] row : lizards_locations)
        {
            System.out.println(Arrays.toString(row));
        }
        return nextBoard;
    }
//
//    void acceptNext() {
//        System.out.println("3");
//        for (int row = 0; row != result.length; row++) {
//            for (int col = 0; col != result[row].length; col++) {
//                if (result[row][col] == 1) {
//                    result[row][col] = 0;
//                }
//            }
//        }
////        System.out.println("size: "+new_lizards_locations.length);
//        for (int i = 0; i < lizards; i++) {
//            //lizards_locations[i] = new int[]{new_lizards_locations[i][0], new_lizards_locations[i][1]};
////            result[lizards_locations[i][0]][lizards_locations[i][1]] = 0;
//            result[new_lizards_locations[i][0]][new_lizards_locations[i][1]] = 1;
//        }
//    }


    boolean simulatedAnnealing() {
        randomPositionsGenerator();
        while (currentSystemTemperature > 1) {
            System.out.println(currentSystemTemperature);
            int[][] neighbor = generateNeighbor(result);
            int currConflicts = countConflicts(result);
            int nextConflicts = countConflicts(neighbor);

            System.out.println("next_conf: "+nextConflicts);
            System.out.println("curr_conf: "+currConflicts);
            if (nextConflicts < currConflicts) {
                System.out.println("____________result1____________");
                result = deepCopy(neighbor);
                for (int[] row : result)
                {
                    System.out.println(Arrays.toString(row));
                }


            } else {
                if (probability(currentSystemTemperature, nextConflicts-currConflicts)) {
                    result = deepCopy(neighbor);
                    System.out.println("____________result2____________");
                    for (int[] row : result)
                    {
                        System.out.println(Arrays.toString(row));
                    }

                }
            }

            currentSystemTemperature -= 0.01;
        }
        return true;
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

        result = initial_map;
        for (int[] row : initial_map)
        {
            System.out.println(Arrays.toString(row));
        }

        System.out.println("____________________________");

        NQueenProblem hw = new NQueenProblem();



        if (method.equals("SA")) {
//            result = hw.simulatedAnnealing();
            lizards_locations = new int[lizards][2];

//            hw.randomPositionsGenerator();
//            System.out.println("__________result_________");
//            for (int[] row : result)
//            {
//                System.out.println(Arrays.toString(row));
//            }
//            System.out.println("Conf: "+hw.countConflicts(result));



            hw.simulatedAnnealing();
            System.out.println("______________final______________");
            for (int[] row : result)
            {
                System.out.println(Arrays.toString(row));
            }
//
//            nextBoard = hw.generateNeighbor(result);




//            int conf = hw.countConflicts(result);
//            System.out.println("conf: "+conf);
//            System.out.println("____________result___________");
//            for (int[] row : result)
//            {
//                System.out.println(Arrays.toString(row));
//            }







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

