import javafx.scene.control.Label;

public class Logic {
    private boolean isGameEnd = false;
    private Bot playingBot;
    private Board board = new Board();
    private boolean singleGame;
    private Board.Field who = Board.Field.Empty;

    public Logic(boolean singleGame){
        this.singleGame = singleGame;
    }

    public void changePlayer(Board.Field who) {
        this.who = who;
    }

    public void setBotGame(int level) {
        singleGame = false;
        if (level == 1)
            playingBot = new RandomBot(who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross);
        board = new Board();
    }

    public boolean makeTurn(int x, int y, Label l) {
        boolean isSuccess = board.set(x, y, who);
        System.out.println(isSuccess);
        if (isSuccess) {
            if (who == Board.Field.Zero)
                l.setText("O");
            else
                l.setText("X");
            if (singleGame) {
                who = who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross;

            } else {
                playingBot.setTurn(new Turn(who, y, x));
                Turn t = playingBot.getNextTurn();
                System.out.println(t.getWho());
                board.set(t.getRow(), t.getCol(), t.getWho());
                Main.drawTurn(t.getCol()*3+(t.getRow() + 1)% 3, t.getWho());
            }
        }
        if (board.isWin() != Board.Field.Empty)
            if (board.isWin() == Board.Field.Cross)
                Main.writeRes("Cross wins!");
            else
                Main.writeRes("Zero wins!");
        //board.write();
        return isSuccess;
    }

    public void restart() {
        isGameEnd = false;
        board = new Board();
    }
}
