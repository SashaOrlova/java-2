public class Board {
    public enum Field { Cross, Zero, Empty}
    private Field[][] board  = new Field[3][3];

    public Board() {
        for (int i = 0 ;i < 3; i++)
            for (int j = 0 ; j < 3; j++)
                board[i][j] = Field.Empty;
    }

    public boolean set(int row, int col, Field sign) {
        if (row < 3 && col < 3 && row >= 0 && col >= 0 && board[row][col] == Field.Empty) {
            board[row][col] = sign;
            return true;
        }
        return false;
    }

    public Field get(int row, int col) throws BoardOutOfIndexException {
        if (row < 9 && col < 9 && row >= 0 && col >= 0)
            return board[row][col];
        else
            throw new BoardOutOfIndexException();
    }

    public Field isWin() {
        int crossL = 0;
        int crossR = 0;
        int zeroL = 0;
        int zeroR = 0;
        for (int i = 0 ; i < 3; i++) {
            int crossH = 0;
            int zeroH = 0;
            for (int j = 0; j < 3; j++) {
                crossH += board[j][i] == Field.Cross ? 1 : 0;
                zeroH += board[j][i] == Field.Zero ? 1 : 0;
            }
            if (crossH == 3)
                return Field.Cross;
            if (zeroH == 3)
                return Field.Zero;
        }
        for (int i = 0 ; i < 3; i++) {
            int crossV = 0;
            int zeroV = 0;
            for (int j = 0 ; j < 3; j++) {
                crossV += board[i][j] == Field.Cross ? 1 : 0;
                zeroV += board[i][j] == Field.Zero ? 1 : 0;
                crossL += board[i][j] == Field.Cross && i == j ? 1 : 0;
                crossR += board[i][j] == Field.Cross && i == (2 - j) ? 1 : 0;
                zeroL += board[i][j] == Field.Zero && i == j ? 1 : 0;
                zeroR += board[i][j] == Field.Zero && i == (2 - j) ? 1 : 0;
            }
            if (crossV == 3)
                return Field.Cross;
            if (zeroV == 3)
                return Field.Zero;
        }
        if (crossL == 3 || crossR == 3)
            return Field.Cross;
        if (zeroL == 3 || zeroR == 3)
            return Field.Zero;
        return Field.Empty;
    }

    public void write() {
        for (int i = 0 ;i < 3; i++) {
            for (int j = 0; j < 3; j++)
                if (board[i][j] == Field.Empty)
                    System.out.print(' ');
                else if (board[i][j] == Field.Cross)
                    System.out.print('X');
                else
                    System.out.print('O');
            System.out.print('\n');
        }
    }

    public class BoardOutOfIndexException extends Exception {}
}
