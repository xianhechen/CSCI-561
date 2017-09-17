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
    public static int[][] backup;
    public static int[][] new_lizards_locations;
    public static int[][] lizards_locations;
    public static double currentSystemTemperature = 10.0;
    public static int currConflicts = 0;
    public static int nextConflicts = 0;


    public static int old_row, old_col, new_row, new_col, changingLizard;


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
//        System.out.println("6");
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
//        System.out.println("5");
        int conflicts = 0;
        for (int row = 0; row != board.length; row++) {
            for (int col = 0; col != board[row].length; col++) {
                if (board[row][col] == 1) {
                    if (!conflict(board, row, col)) {
                        conflicts++;
                    }
                }
            }
        }
        return conflicts;
    }

    // generates random lizard locations
    void randomPositionsGenerator() {
//        System.out.println("4");
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

    void generateNeighbor() {
        // take our current board, take a random queen, make a single step.
        // need a list of lizards to track all lizards' locations
        // take a random lizard
        changingLizard = (int)(Math.random() * (lizards-1));
        old_row = lizards_locations[changingLizard][0];
        old_col = lizards_locations[changingLizard][1];
//        System.out.println("old_row: "+old_row+" old_col: "+old_col);
        // current lizards
        new_lizards_locations = deepCopy(lizards_locations);
//        System.out.println("99");
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
        while (currentSystemTemperature > 0.1) {
            currConflicts = countConflicts(result);
            generateNeighbor();
            nextConflicts = countConflicts(result);
//            System.out.println("next_conf: " + nextConflicts);
//            System.out.println("curr_conf: " + currConflicts);
//            System.out.println(currentSystemTemperature);

            if (nextConflicts == 0) {
                return true;
            }
            if (nextConflicts < currConflicts) {
                currentSystemTemperature = currentSystemTemperature * 0.999;
            } else {
                if (probability(currentSystemTemperature, (nextConflicts-currConflicts))) {
//                    System.out.println("Head");
                } else {
                    System.out.println("Tail");
//                    System.out.println("old_row: "+old_row+" old_col: "+old_col);
//                    System.out.println("new_row: "+new_row+" new_col: "+new_col);
                    lizards_locations[changingLizard][0] = old_row;
                    lizards_locations[changingLizard][1] = old_col;
                    result[old_row][old_col] = 1;
                    result[new_row][new_col] = 0;
                }
            }
//            for (int[] row : result)
//            {
//                System.out.println(Arrays.toString(row));
//            }
        }
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
            lizards_locations = new int[lizards][2];
            hw.simulatedAnnealing();
            System.out.println("______________final______________");
            for (int[] row : result)
            {
                System.out.println(Arrays.toString(row));
            }
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

