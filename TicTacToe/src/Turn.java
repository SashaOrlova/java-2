public class Turn {
    private int row, col;
    private Board.Field who;
    public Turn(Board.Field who, int x, int y) {
        this.who = who;
        row = y;
        col = x;
    }
    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    }
    public Board.Field getWho() {
        return who;
    }
}
