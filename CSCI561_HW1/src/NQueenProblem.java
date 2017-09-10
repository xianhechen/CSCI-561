//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
///* Java program to solve N Queen Problem using
//   backtracking */
//public class NQueenProblem
//{
//    final int N = 2;
//    int queens_placed = 0;
//
//    /* A utility function to print solution */
//    void printSolution(int board[][])
//    {
//        for (int i = 0; i < 8; i++)
//        {
//            for (int j = 0; j < 8; j++)
//                System.out.print(" " + board[i][j]
//                        + " ");
//            System.out.println();
//        }
//    }
//
//    /* A utility function to check if a queen can
//       be placed on board[row][col]. Note that this
//       function is called when "col" queens are already
//       placeed in columns from 0 to col -1. So we need
//       to check only left side for attacking queens */
//    boolean isSafe(int board[][], int row, int col)
//    {
//        int i, j;
//        boolean row_left_tree = false;
//        boolean col_upper_tree = false;
//        boolean upper_left_diag = false;
//        boolean lower_left_diag = false;
//
//        /* Check this row on left side */
//        for (i = col; i >= 0; i--)
//            if (board[row][i] == 2)
//                row_left_tree = true;
//            else if (board[row][i] == 1)
//                if (row_left_tree == false)
//                    return false;
//
//        /* Check this column on upper side */
//        for (i = row; i >= 0; i--)
//            if (board[i][col] == 2)
//                col_upper_tree = true;
//            else if (board[i][col] == 1)
//                if (col_upper_tree == false)
//                    return false;
//
//
//        /* Check upper diagonal on left side */
//        for (i=row, j=col; i>=0 && j>=0; i--, j--)
//            if (board[i][j] == 2)
//                upper_left_diag = true;
//            else if (board[i][j] == 1)
//                if (upper_left_diag == false)
//                    return false;
//
//        /* Check lower diagonal on left side */
//        for (i=row, j=col; j>=0 && i<8; i++, j--)
//            if (board[i][j] == 2)
//                lower_left_diag = true;
//            else if (board[i][j] == 1)
//                if (lower_left_diag == false)
//                    return false;
//
//        return true;
//    }
//
//
//    /*
//
//
//        boolean isSafe(int board[][], int row, int col)
//        {
//            int i, j;
//            int row_left_tree, upper_left_diag, lower_left_diag;
//
//            for (i = 0; i < col; i++)
//            if (board[row][i] == 1)
//            return false;
//
//
//            for (i=row, j=col; i>=0 && j>=0; i--, j--)
//            if (board[i][j] == 1)
//            return false;
//
//
//            for (i=row, j=col; j>=0 && i<6; i++, j--)
//            if (board[i][j] == 1)
//            return false;
//
//            return true;
//        } */
//
//
//
//
//
//
//
////
////    /* A recursive utility function to solve N
////       Queen problem */
////    boolean solveNQUtil(int board[][], int col)
////    {
////        /* base case: If all queens are placed
//////           then return true */
////        if (queens_placed >= N)
////            return true;
////        if (col >= 8)
////            return true;
////
////        /* Consider this column and try placing
////           this queen in all rows one by one */
////        for (int i = 0; i < 8; i++)
////        {
////            /* Check if queen can be placed on
////               board[i][col] */
////            if (board[i][col] == 2) {
////                continue;
////            }
////            if (board[i][col] != 2 && isSafe(board, i, col))
////            {
////                /* Place this queen in board[i][col] */
////                board[i][col] = 1;
////                queens_placed += 1;
////
//////                /* recur to place rest of the queens */
////
////
////
////                if (solveNQUtil(board, col + 1) == true) {
////                    return true;
////                }
////                board[i][col] = 0; // BACKTRACK
////                queens_placed -= 1;
////
////
////
//////
//////
//////                /* If placing queen in board[i][col]
//////                   doesn't lead to a solution then
//////                   remove queen from board[i][col] */
//////
////
////            }
////        }
////
////
////        /* If queen can not be place in any row in
////           this column col, then return false */
////        return false;
////    }
//
//    /* This function solves the N Queen problem using
//       Backtracking.  It mainly uses  solveNQUtil() to
//       solve the problem. It returns false if queens
//       cannot be placed, otherwise return true and
//       prints placement of queens in the form of 1s.
//       Please note that there may be more than one
//       solutions, this function prints one of the
//       feasible solutions.*/
//    boolean solveNQ(String method, int size, int lizards, int nursery[][])
//    {
//        int board[][] = nursery;
//
//        if (solveNQUtil(board, 0, 0) == false)
//        {
//            System.out.print("Solution does not exist");
//
//            return false;
//        }
//
//        printSolution(board);
//        return true;
//    }
//
//    // driver program to test above function
//    public static void main(String args[])
//    {
//
//        String method = "";
//        int size = 0;
//        int lizards = 0;
//
//
//
//        int nursery[][] = {{0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 2, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 2, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0},
//                {0, 0, 0, 0, 0, 0, 0, 0}};
//
//        int line_number = 0;
//
//
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
//
//                }
//            }
//            System.out.println(method);
//            System.out.println(size);
//            System.out.println(lizards);
//
//            in.close();
//        } catch (IOException e) {
//        }
//
//
//
//
//        NQueenProblem Queen = new NQueenProblem();
//        Queen.solveNQ(method, size, lizards, nursery);
//
//
//
//    }
//}