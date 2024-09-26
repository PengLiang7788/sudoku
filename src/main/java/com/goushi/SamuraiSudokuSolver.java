package com.goushi;


import java.util.Arrays;

public class SamuraiSudokuSolver {

    // 每个数独网格的大小
    private static final int SIZE = 9;
    // 每个数独网格中子网格大小
    private static final int SUBGRID_SIZE = 3;
    // 空值表示
    private static final int EMPTY = 0;

    // 定义网格
    private static int[][][] boards = new int[5][SIZE][SIZE];

    public static void solveSamuraiSudoku() {
        if (solve(boards)) {
            printBoards(boards);
        } else {
            System.out.println("No solution exists.");
        }
    }

    /**
     * 打印整个武士数独
     *
     * @param boards 整个武士数独
     */
    private static void printBoards(int[][][] boards) {
        for (int i = 0; i < boards.length; i++) {
            System.out.println("Board " + (i + 1) + ": ");
            printBoard(boards[i]);
            System.out.println();
        }
    }

    /**
     * 打印单个数独
     *
     * @param board 二维数组 单个数独
     */
    private static void printBoard(int[][] board) {
        for (int[] rows : board) {
            System.out.println(Arrays.toString(rows));
        }
    }

    /**
     * 求解武士数独
     *
     * @param boards 存储武士数独的三维数组
     * @return 有解返回true
     */
    private static boolean solve(int[][][] boards) {
        // 迭代所有的数独
        for (int boardIndex = 0; boardIndex < boards.length; boardIndex++) {
            // 迭代当前数独的所有行和所有列
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    // 如果当前位置为空，添加元素
                    if (boards[boardIndex][row][col] == EMPTY) {
                        // 从1-9尝试
                        for (int num = 1; num <= SIZE; num++) {
                            // 判断在该位置添加num是否合法
                            if (isValid(boards, boardIndex, row, col, num)) {
                                // 合法则替换
                                boards[boardIndex][row][col] = num;

                                // 如果更新的是中间区域的数独，则更新武士数独的重叠部分
                                if (boardIndex == 0) {
                                    if (!updateOverlappingAreas(row, col, num)) {
                                        // 如果重叠部分冲突, 换下一个bun
                                        boards[boardIndex][row][col] = EMPTY;
                                        continue;
                                    }
                                }

                                if (solve(boards)) {
                                    return true;
                                } else {
                                    boards[boardIndex][row][col] = EMPTY;
                                    if (boardIndex == 0) {
                                        updateOverlappingAreas(row, col, EMPTY);
                                    }
                                }
                            }
                        }
                        // 当前位置1-9都不合法
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 更新重叠部分的数独
     *
     * @param row 更新所在的行
     * @param col 更新所在的列
     * @param num 更新的值
     */
    private static boolean updateOverlappingAreas(int row, int col, int num) {
        // 计算中央数独的哪个角在更新
        if (row < SUBGRID_SIZE && col < SUBGRID_SIZE) {
            // 左上角, 判断是否合法
            if (num == EMPTY) {
                boards[1][row + 2 * SUBGRID_SIZE][col + 2 * SUBGRID_SIZE] = num;
                return true;
            } else if (isValid(boards, 1, row + 2 * SUBGRID_SIZE, col + 2 * SUBGRID_SIZE, num)) {
                boards[1][row + 2 * SUBGRID_SIZE][col + 2 * SUBGRID_SIZE] = num;
                return true;
            } else {
                return false;
            }
        } else if (row < SUBGRID_SIZE && col >= 2 * SUBGRID_SIZE) {
            // 右上角
            if (num == EMPTY) {
                boards[2][row + 2 * SUBGRID_SIZE][col - 2 * SUBGRID_SIZE] = num;
                return true;
            } else if (isValid(boards, 2, row + 2 * SUBGRID_SIZE, col - 2 * SUBGRID_SIZE, num)) {
                boards[2][row + 2 * SUBGRID_SIZE][col - 2 * SUBGRID_SIZE] = num;
                return true;
            } else {
                return false;
            }
        } else if (row >= 2 * SUBGRID_SIZE && col < SUBGRID_SIZE) {
            // 左下角
            if (num == EMPTY) {
                boards[3][row - 2 * SUBGRID_SIZE][col + 2 * SUBGRID_SIZE] = num;
                return true;
            } else if (isValid(boards, 3, row - 2 * SUBGRID_SIZE, col + 2 * SUBGRID_SIZE, num)) {
                boards[3][row - 2 * SUBGRID_SIZE][col + 2 * SUBGRID_SIZE] = num;
                return true;
            } else {
                return false;
            }
        } else if (row >= 2 * SUBGRID_SIZE && col >= 2 * SUBGRID_SIZE) {
            // 右下
            if (num == EMPTY) {
                boards[4][row - 2 * SUBGRID_SIZE][col - 2 * SUBGRID_SIZE] = num;
                return true;
            } else if (isValid(boards, 4, row - 2 * SUBGRID_SIZE, col - 2 * SUBGRID_SIZE, num)) {
                boards[4][row - 2 * SUBGRID_SIZE][col - 2 * SUBGRID_SIZE] = num;
                return true;
            } else {
                return false;
            }
        }
        // 未重叠
        return true;

    }

    /**
     * 判断当前位置添加的元素是否合法
     *
     * @param boards     武士数独
     * @param boardIndex 当前普通数独索引
     * @param row        所在行
     * @param col        所在列
     * @param num        添加的数字
     * @return
     */
    private static boolean isValid(int[][][] boards, int boardIndex, int row, int col, int num) {
        // 检查行和列
        for (int i = 0; i < SIZE; i++) {
            if (boards[boardIndex][row][i] == num || boards[boardIndex][i][col] == num) {
                return false;
            }
        }

        // 检查子网格
        // 计算当前所在位置的子网格开始行和开始列
        int startRow = row - row % SUBGRID_SIZE;
        int startCol = col - col % SUBGRID_SIZE;

        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                if (boards[boardIndex][i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }


    public static void main(String[] args) {
        // 初始化武士数独
        int[][] initialValue = {
                // 中心数独
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 9, 0, 0, 0, 0},
                {0, 0, 6, 0, 0, 0, 9, 0, 0},
                {0, 0, 0, 4, 0, 7, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 8, 0},
                {0, 0, 0, 3, 0, 6, 0, 0, 0},
                {0, 0, 7, 0, 0, 0, 4, 0, 0},
                {0, 0, 0, 0, 5, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0},

                // 左上数独
                {9, 0, 0, 0, 5, 0, 0, 0, 7},
                {0, 0, 0, 9, 0, 7, 0, 0, 0},
                {0, 0, 0, 6, 0, 4, 0, 0, 0},
                {0, 1, 3, 0, 2, 0, 8, 9, 0},
                {2, 0, 0, 7, 0, 1, 0, 0, 3},
                {0, 9, 6, 0, 4, 0, 7, 2, 0},
                {0, 0, 0, 3, 0, 5, 0, 0, 0},
                {0, 0, 0, 4, 0, 9, 0, 0, 0},
                {3, 0, 0, 0, 7, 0, 0, 0, 6},

                // 右上数独
                {2, 0, 0, 0, 9, 0, 0, 0, 6},
                {0, 0, 0, 6, 0, 3, 0, 0, 0},
                {0, 0, 0, 1, 0, 5, 0, 0, 0},
                {0, 9, 8, 0, 6, 0, 1, 5, 0},
                {6, 0, 0, 5, 0, 9, 0, 0, 8},
                {0, 7, 2, 0, 8, 0, 6, 3, 0},
                {0, 0, 0, 7, 0, 4, 0, 0, 0},
                {0, 0, 0, 9, 0, 8, 0, 0, 0},
                {9, 0, 0, 0, 5, 0, 0, 0, 4},

                // 左下数独
                {9, 0, 0, 0, 3, 0, 0, 0, 7},
                {0, 0, 0, 5, 0, 9, 0, 0, 0},
                {0, 0, 0, 7, 0, 4, 0, 0, 0},
                {0, 1, 3, 0, 2, 0, 7, 5, 0},
                {5, 0, 0, 8, 0, 7, 0, 0, 2},
                {0, 7, 4, 0, 1, 0, 9, 8, 0},
                {0, 0, 0, 6, 0, 8, 0, 0, 0},
                {0, 0, 0, 4, 0, 1, 0, 0, 0},
                {6, 0, 0, 0, 7, 0, 0, 0, 9},

                // 右下数独
                {4, 0, 0, 0, 9, 0, 0, 0, 3},
                {0, 0, 0, 8, 0, 2, 0, 0, 0},
                {0, 0, 0, 6, 0, 1, 0, 0, 0},
                {0, 3, 1, 0, 7, 0, 8, 5, 0},
                {9, 0, 0, 2, 0, 3, 0, 0, 1},
                {0, 7, 5, 0, 8, 0, 3, 2, 0},
                {0, 0, 0, 4, 0, 8, 0, 0, 0},
                {0, 0, 0, 5, 0, 7, 0, 0, 0},
                {5, 0, 0, 0, 2, 0, 0, 0, 8}

        };
//        int[][] initialValue = {
//                // 中心数独
//                {0, 0, 0, 0, 0, 0, 6, 0, 0},
//                {0, 0, 6, 0, 0, 0, 8, 0, 0},
//                {8, 4, 0, 0, 1, 0, 0, 5, 0},
//                {0, 0, 0, 3, 0, 6, 0, 0, 0},
//                {0, 0, 8, 0, 0, 0, 7, 0, 0},
//                {0, 0, 0, 5, 0, 9, 0, 0, 0},
//                {0, 8, 0, 0, 6, 0, 0, 1, 2},
//                {0, 0, 5, 0, 0, 0, 3, 0, 0},
//                {0, 0, 7, 0, 0, 0, 0, 0, 0},
//
//                // 左上数独
//                {0, 9, 5, 3, 0, 0, 0, 1, 0},
//                {7, 0, 0, 0, 1, 0, 0, 0, 3},
//                {0, 0, 0, 0, 0, 7, 0, 0, 5},
//                {0, 0, 7, 0, 0, 0, 0, 0, 1},
//                {0, 2, 0, 0, 0, 0, 0, 8, 0},
//                {1, 0, 0, 0, 0, 0, 6, 0, 0},
//                {4, 0, 0, 9, 0, 0, 0, 0, 0},
//                {2, 0, 0, 0, 4, 0, 0, 0, 6},
//                {0, 5, 0, 0, 0, 1, 8, 4, 0},
//
//                // 右上数独
//                {0, 2, 5, 3, 0, 0, 0, 6, 0},
//                {9, 0, 0, 0, 2, 0, 0, 0, 8},
//                {0, 0, 0, 0, 0, 6, 0, 0, 4},
//                {0, 0, 6, 0, 0, 0, 0, 0, 9},
//                {0, 4, 0, 0, 0, 0, 0, 5, 0},
//                {7, 0, 0, 0, 0, 0, 1, 0, 0},
//                {6, 0, 0, 7, 0, 0, 0, 0, 0},
//                {8, 0, 0, 0, 1, 0, 0, 0, 5},
//                {0, 5, 0, 0, 0, 4, 3, 9, 0},
//
//                // 左下数独
//                {0, 2, 4, 1, 0, 0, 0, 8, 0},
//                {6, 0, 0, 0, 9, 0, 0, 0, 5},
//                {0, 0, 0, 0, 0, 4, 0, 0, 7},
//                {0, 0, 1, 0, 0, 0, 0, 0, 9},
//                {0, 5, 0, 0, 0, 0, 0, 4, 0},
//                {7, 0, 0, 0, 0, 0, 2, 0, 0},
//                {1, 0, 0, 7, 0, 0, 0, 0, 0},
//                {3, 0, 0, 0, 8, 0, 0, 0, 6},
//                {0, 6, 0, 0, 0, 2, 7, 9, 0},
//
//                // 右下数独
//                {0, 1, 2, 3, 0, 0, 0, 7, 0},
//                {3, 0, 0, 0, 8, 0, 0, 0, 2},
//                {0, 0, 0, 0, 0, 6, 0, 0, 3},
//                {0, 0, 4, 0, 0, 0, 0, 0, 9},
//                {0, 9, 0, 0, 0, 0, 0, 8, 0},
//                {2, 0, 0, 0, 0, 0, 6, 0, 0},
//                {9, 0, 0, 1, 0, 0, 0, 0, 0},
//                {1, 0, 0, 0, 4, 0, 0, 0, 5},
//                {0, 5, 0, 0, 0, 7, 2, 1, 0}
//        };

        for (int boardIndex = 0; boardIndex < 5; boardIndex++) {
            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    boards[boardIndex][row][col] = initialValue[boardIndex * SIZE + row][col];
                }
            }
        }
        long startTime = System.currentTimeMillis();
        solveSamuraiSudoku();
        long endTime = System.currentTimeMillis();
        System.out.println("执行时长: " + (endTime - startTime) + " 毫秒.");
    }
}