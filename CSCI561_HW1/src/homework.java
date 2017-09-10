import sun.misc.Queue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Stack;

public class homework {

    public static String method = "";
    public static int size = 0;
    public static int lizards = 0;

    public static int line_number = 0;
    public static int[][] result;

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


    public static void main(String args[]) throws IOException, InterruptedException {

//        try {
//            BufferedReader in = new BufferedReader(new FileReader("input.txt"));
//            String str;
//            while ((str = in.readLine()) != null) {
//                line_number ++;
//                if (line_number == 1) {
//                    method = str;
//                }
//                else if (line_number == 2) {
//                    size = Integer.valueOf(str);
//                }
//                else if (line_number == 3) {
//                    lizards = Integer.valueOf(str);
//                }
//                else {
//                    // System.out.println(str);
//                    // store the nursery as a matrix?
//                }
//            }
//            System.out.println(method);
//            System.out.println(size);
//            System.out.println(lizards);
//            in.close();
//        } catch (IOException e) {
//        }


        int row_len = 8;
        int col_len = 8;
        int n = 9;
        lizards = n;
        int[][] initial_map = new int[row_len][col_len];
        initial_map[3][4] = 2;
        initial_map[5][5] = 2;

        result = initial_map;
        for (int[] row : initial_map)
        {
            System.out.println(Arrays.toString(row));
        }

        System.out.println("____________________________");

        homework hw = new homework();
        int[][] temp = hw.bfs(initial_map,0,0,0);
//        System.out.println(temp);
        for (int[] row : temp)
        {
            System.out.println(Arrays.toString(row));
        }


    }

}

