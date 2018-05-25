package ru.java.logic;

import javafx.scene.control.Label;
import ru.java.Main;
import ru.java.bot.*;

/**
 * Class control all logic in game
 */

public class Logic {
    private boolean isGameEnd = true;
    private Bot playingBot;
    private Board board = new Board();
    private boolean singleGame;
    private Board.Field who = Board.Field.Empty;
    private int type;

    /** change mode game to single
     * @param singleGame true if game mode single
     */
    public void setSingleGame(boolean singleGame) {
        this.singleGame = singleGame;
        type = 0;
    }

    /** choose player sign
     * @param who new sing
     */
    public void changePlayer(Board.Field who) {
        this.who = who;
    }

    /** create game with bot
     * @param level 1 for simple bot, 2 for hard bot
     */
    public void setBotGame(int level) {
        singleGame = false;
        if (level == 1) {
            playingBot = new RandomBot(who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross);
            type = 1;
        }
        if (level == 2) {
            playingBot = new HardBot(who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross, new Board());
            type = 2;
        }
        if (level == 3) {
            try {
                playingBot = new PlayerServer(Main.port, who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross);
            } catch (PlayerClient.ConnectException e) {
                Main.writeError("Exception in connection with other player");
            }
            type = 3;
        }
        if (level == 4) {
            try {
                playingBot = new PlayerClient("localhost", Main.port, who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross);
            } catch (PlayerClient.ConnectException e) {
                Main.writeError("Exception in connection with other player");
            }
            type = 4;
        }
        board = new Board();
    }

    /** check correctness and draw turn
     * @param x - row of turn
     * @param y - col of turn
     * @param l - label to draw turn
     * @return true if turn is success
     */
    public boolean makeTurn(int x, int y, Label l) {
        if (!isGameEnd) {
            boolean isSuccess = board.set(x, y, who);
            if (isSuccess && !isGameEnd) {
                if (who == Board.Field.Zero)
                    l.setText("O");
                else
                    l.setText("X");
                if (board.isWin() != Board.Field.Empty) {
                    if (board.isWin() == Board.Field.Cross)
                        Main.writeRes("Cross wins!", Board.Field.Cross, type);
                    else
                        Main.writeRes("Zero wins!", Board.Field.Zero, type);
                    isGameEnd = true;
                    return true;
                }
                if (board.isDraw()) {
                    isGameEnd = true;
                    Main.writeRes("Draw!", Board.Field.Empty, type);
                    return true;
                }
                if (singleGame) {
                    who = who == Board.Field.Cross ? Board.Field.Zero : Board.Field.Cross;
                } else {
                    try {
                        playingBot.setTurn(new Turn(who, y, x));
                        Turn t = playingBot.getNextTurn();
                        board.set(t.getRow(), t.getCol(), t.getWho());
                        Main.drawTurn(t.getCol() * 3 + (t.getRow() + 1), t.getWho());
                    } catch (PlayerClient.ConnectException e) {
                        Main.writeError("Exception in connection with other player");
                    }

                }
            }
            if (board.isWin() != Board.Field.Empty) {
                if (board.isWin() == Board.Field.Cross)
                    Main.writeRes("Cross wins!", Board.Field.Cross, type);
                else
                    Main.writeRes("Zero wins!", Board.Field.Zero, type);
                isGameEnd = true;
            }
            if (board.isDraw()) {
                isGameEnd = true;
                Main.writeRes("Draw!", Board.Field.Empty, type);
            }
            return isSuccess;
        }
        return false;
    }

    /**
     * make bot turn
     */
    public void BotTurn() {
        try {
            Turn t = playingBot.getNextTurn();
            board.set(t.getRow(), t.getCol(), t.getWho());
            Main.drawTurn(t.getCol()*3+(t.getRow() + 1), t.getWho());
        } catch (PlayerClient.ConnectException e) {
            Main.writeError("Exception in connection with other player");
        }
    }

    /**
     * start new game
     */
    public void restart() {
        isGameEnd = false;
        board = new Board();
    }
}
