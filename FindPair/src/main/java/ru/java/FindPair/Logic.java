package ru.java.FindPair;

import java.util.Random;

/**
 * Class represented logic of game
 */
public class Logic {
    private Random random = new Random();
    private int numOfTurn = 0;
    private int lastRow = -1;
    private int lastCol = -1;
    private int saveRow = -1;
    private int saveCol = -1;
    private int countForWin = 0;
    private int[][] board;
    private boolean[][] active;

    /** Create new Logic
     * @param n number of cells
     * @throws LogicException if n is big or ood
     */
    public Logic(int n) throws LogicException {
        if (n % 2 == 1)
            throw new LogicException("Ood number");
        if (n > 16)
            throw new LogicException("Big number");
        board = new int[n][n];
        active = new boolean[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                active[i][j] = true;
        for (int i = (n * n / 2) - 1; i > 0; ) {
            int row1 = ((random.nextInt() % n) + n) % n;
            int col1 = ((random.nextInt() % n) + n) % n;
            int row2 = ((random.nextInt() % n) + n) % n;
            int col2 = ((random.nextInt() % n) + n) % n;
            if ((board[row1][col1] == 0) && (board[row2][col2] == 0) && !(row1 == row2 && col1 == col2)) {
                board[row1][col1] = i;
                board[row2][col2] = i;
                i--;
            }
        }
    }

    /** get value of game board
     * @param row index in row
     * @param col index in col
     * @return value of board
     */
    public int getField(int row, int col) {
        return board[row][col];
    }

    /** make new turn
     * @param row index in row
     * @param col index in col
     */
    public void makeTurn(int row, int col) {
        if (numOfTurn == 0) {
            if (active[row][col]) {
                Main.drawTurn(row, col);
                active[row][col] = false;
                numOfTurn = 1;
                if (saveCol != -1) {
                    active[lastRow][lastCol] = true;
                    active[saveRow][saveCol] = true;
                    Main.deleteTurn(saveRow, saveCol);
                    Main.deleteTurn(lastRow, lastCol);
                }
                lastCol = col;
                lastRow = row;
            }
        }
        if (numOfTurn == 1) {
            if (active[row][col]) {
                active[row][col] = false;
                Main.drawTurn(row, col);
                numOfTurn = 0;
                if (board[lastRow][lastCol] == board[row][col]) {
                    saveCol = -1;
                    saveRow = -1;
                    countForWin += 2;
                } else {
                    saveCol = col;
                    saveRow = row;
                }
            }
        }
        if (countForWin == board.length * board.length) {
            Main.writeWin();
        }
    }

    static public class LogicException extends Exception {
        public LogicException(String st) {
            super(st);
        }
    }
}
