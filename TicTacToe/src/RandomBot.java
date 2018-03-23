import java.util.Random;

public class RandomBot implements Bot{
    private Board.Field who;
    private Board board = new Board();

    final Random random = new Random();

    public RandomBot(Board.Field who) {
        this.who = who;
    }

    @Override
    public Turn getNextTurn() {
        while (true) {
            int x = random.nextInt() % 3;
            int y = random.nextInt() % 3;
            if (board.set(y, x, who)) {
                board.write();
                return new Turn(who, x, y);
            }
        }
    }

    @Override
    public void setTurn(Turn turn) {
        board.set(turn.getRow(), turn.getCol(), turn.getWho());
    }
}
