package me.tangni.sudoku.game;

/**
 * Created by gaojian on 2017/11/3.
 */

public class SudokuSolver {

    /**
     * 解数独，并打印结果
     */
    public static void solve(int[][] grids) {
//        printArray(grids);
        System.out.println();

        if (solve(grids, 0, 0)) {
//            printArray(grids);
        } else {
            System.out.println("没有找到相应的解法");
        }
    }

    /**
     * 解数独
     */
    private static boolean solve(int[][] grids, int row, int column) {

        /**
         * 结束条件
         */
        if (row == 8 && column == 9) {
            return true;
        }

        /**
         * 如果column == 9, 那么需要换行，也就需要更新column和row的值.
         */
        if (column == 9) {
            column = 0;
            row++;
        }
        /**
         * 如果当前位置上grids[row][column]的值不为0，则处理下一个
         */
        if (grids[row][column] != 0) {
            return solve(grids, row, column + 1);
        }

        /**
         * 如果当前位置上grids[row][column]的值为0, 尝试在1~9的数字中选择合适的数字
         */
        for (int num = 1; num <= 9; num++) {
            if (isSafe(grids, row, column, num)) {
                grids[row][column] = num;
                if (solve(grids, row, column + 1)) {
                    return true;
                }

            }
        }
        /**
         * 回溯重置
         */
        grids[row][column] = 0;
        return false;
    }

    /**
     * 某一行放置数据是否有冲突
     */
    public static boolean isRowSafe(int[][] grids, int row, int value) {
        for (int i = 0; i < 9; i++) {
            if (grids[row][i] == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 某一行放置数据是否有冲突
     */
    public static boolean isRowSafe(Cell[][] cells, int row, int column, int value) {
        for (int i = 0; i < 9; i++) {
            if (i != column && cells[row][i].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 某一列放置数据是否有冲突
     */
    public static boolean isColumnSafe(int[][] grids, int column, int value) {
        for (int i = 0; i < 9; i++) {
            if (grids[i][column] == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 某一列放置数据是否有冲突
     */
    public static boolean isColumnSafe(Cell[][] cells, int row, int column, int value) {
        for (int i = 0; i < 9; i++) {
            if (i != row && cells[i][column].getValue() == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * 每个区域是 3 X 3 的子块，是否可以可以放置数据
     */
    public static boolean isSmallBoxSafe(int[][] grids, int row, int column, int value) {
        int rowOffset = (row / 3) * 3;
        int columnOffset = (column / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grids[rowOffset + i][columnOffset + j] == value) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 每个区域是 3 X 3 的子块，是否可以可以放置数据
     */
    public static boolean isSmallBoxSafe(Cell[][] cells, int row, int column, int value) {
        int rowOffset = (row / 3) * 3;
        int columnOffset = (column / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int r = rowOffset + i, c = columnOffset + j;
                if ((row != r || column != c) && cells[r][c].getValue() == value) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 在指定位置是否可以放置数据
     */
    public static boolean isSafe(int[][] grids, int row, int column, int value) {
        return isColumnSafe(grids, column, value)
                && isRowSafe(grids, row, value)
                && isSmallBoxSafe(grids, row, column, value);
    }

    /**
     * 在指定位置是否可以放置数据
     */
    public static boolean isSafe(Cell[][] cells, int row, int column, int value) {
        return isColumnSafe(cells, row, column, value)
                && isRowSafe(cells, row, column, value)
                && isSmallBoxSafe(cells, row, column, value);
    }

}
