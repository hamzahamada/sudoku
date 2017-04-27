package sudokusolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hamza
 */
public class SudokuSolver {

    static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();

        File input = new File("input.txt");
        File output = new File("output.txt");
        Scanner scan = new Scanner(input);
        PrintWriter outputToFile = new PrintWriter(output);
        int n = scan.nextInt();
        for (int i = 0; i < n; i++) {
            executor.execute(() ->{
            int[][] grid = readAPuzzle(scan);

            if (!isValid(grid)) {
                outputToFile.println("The input is not valid");
            } else if (search(grid)) {
                try {
                    printGrid(grid, outputToFile);
                } catch (FileNotFoundException ex) {
                    System.err.println(ex);
                }
            } else {
                outputToFile.println("There is No solutions");
            }

            outputToFile.println();
            });
        }
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);
    }

    private static int[][] readAPuzzle(Scanner scan) {

        int[][] grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = scan.nextInt();
            }
        }

        return grid;
    }

    private static int[][] getFreeCellList(int[][] grid) {

        int numberOfFreeCells = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    numberOfFreeCells++;
                }
            }
        }
        int[][] freeCellList = new int[numberOfFreeCells][2];
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    freeCellList[count][0] = i;
                    freeCellList[count++][1] = j;
                }
            }
        }
        return freeCellList;
    }

    public static boolean search(int[][] grid) {
        int[][] freeCellList = getFreeCellList(grid);
        return search(0, freeCellList, grid);
    }

    private static boolean search(int k, int[][] freeCellList, int[][] grid) {
        if (k < freeCellList.length) {
            int row = freeCellList[k][0];
            int column = freeCellList[k][1];
            for (int i = 1; i < 10; i++) {
                grid[row][column] = i;
                if (isValid(row, column, grid) && search(k + 1, freeCellList, grid)) {
                    return true;
                }
            }
            grid[row][column] = 0;
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValid(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] != 0 && !isValid(i, j, grid)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static boolean isValid(int i, int j, int[][] grid) {

        for (int column = 0; column < 9; column++) {
            if (column != j && grid[i][column] == grid[i][j]) {
                return false;
            }
        }

        for (int row = 0; row < 9; row++) {
            if (row != i && grid[row][j] == grid[i][j]) {
                return false;
            }
        }

        for (int row = (i / 3) * 3; row < (i / 3) * 3 + 3; row++) {
            for (int col = (j / 3) * 3; col < (j / 3) * 3 + 3; col++) {
                if (row != i && col != j && grid[row][col] == grid[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void printGrid(int[][] grid, PrintWriter pw) throws FileNotFoundException {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                pw.print(grid[i][j] + " ");
            }

            pw.println();
        }
        pw.flush();
    }

}
