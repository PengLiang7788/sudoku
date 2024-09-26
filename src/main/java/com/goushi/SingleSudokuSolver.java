package com.goushi;


import java.util.Arrays;

public class SingleSudokuSolver {

    private static int SIZE = 9;
    private static int SUBGRID_SIZE = 3;

    private static int EMPTY = 0;

    private static int[][] boards = new int[SIZE][SIZE];

    public static void solveSingleSudoku() {
        if (solve(boards)) {
            printBoards(boards);
        } else {
            System.out.println("No solution exists.");
        }
    }

    /**
     * 求解数独
     *
     * @param boards
     * @return
     */
    private static boolean solve(int[][] boards) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                // 如果为空, 填充值
                if (boards[row][col] == EMPTY) {
                    for (int num = 1; num <= SIZE; num++) {
                        if (isValid(row, col, num)) {
                            boards[row][col] = num;

                            if (solve(boards)) {
                                return true;
                            } else {
                                boards[row][col] = EMPTY;
                            }
                        }
                    }
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 验证在当前位置插入该值是否合法
     *
     * @param row
     * @param col
     * @param num
     * @return
     */
    private static boolean isValid(int row, int col, int num) {
        // 检查行、列
        for (int i = 0; i < SIZE; i++) {
            if (boards[row][i] == num || boards[i][col] == num) {
                return false;
            }
        }

        // 检查小3x3网格
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;

        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (boards[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 输出数独
     *
     * @param boards
     */
    private static void printBoards(int[][] boards) {
        for (int[] board : boards) {
            System.out.println(Arrays.toString(board));
        }
    }


    public static void main(String[] args) {
        int[][] initialBoards = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0},
                {0, 0, 6, 0, 0, 0, 9, 0, 0},
                {0, 0, 0, 4, 0, 7, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 8, 0},
                {0, 0, 0, 3, 0, 6, 0, 0, 0},
                {0, 0, 7, 0, 0, 0, 4, 0, 0},
                {0, 0, 0, 0, 5, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boards[i][j] = initialBoards[i][j];
            }
        }
        solveSingleSudoku();
    }
}
