package ru.java.bot;

import ru.java.logic.Board;
import ru.java.logic.Turn;

import java.util.Random;

/**
 * Realization of bot with strategy
 */
public class HardBot implements Bot {
    private Board.Field who;
    private Board board = new Board();

    final Random random = new Random();

    public HardBot(Board.Field who) {
        this.who = who;
    }

    private Turn canWin(Board.Field who) {
        for (int i = 0; i < 3 ; i++) {
            int cross = 0;
            int zero = 0;
            int positionX = 0, positionY = 0;
            for (int j = 0; j < 3; j++) {
                if (board.get(i, j) == Board.Field.Empty){
                    positionX = i;
                    positionY = j;
                }
                if (board.get(i, j) == Board.Field.Cross) {
                    cross++;

                }
                if (board.get(i, j) == Board.Field.Zero) {
                    zero++;

                }
            }
            if (cross == 2 && zero == 0 && who == Board.Field.Cross)
                return new Turn(Board.Field.Cross, positionX, positionY);
            if (cross == 0 && zero == 2 && who == Board.Field.Zero)
                return new Turn(Board.Field.Zero, positionX, positionY);
        }
        for (int i = 0; i < 3 ; i++) {
            int cross = 0;
            int zero = 0;
            int positionX = 0;
            int positionY = 0;
            for (int j = 0; j < 3; j++) {
                if (board.get(j, i) == Board.Field.Cross) {
                    cross++;
                }
                if (board.get(j, i) == Board.Field.Empty){
                    positionX = j;
                    positionY = i;
                }
                if (board.get(j, i) == Board.Field.Zero) {
                    zero++;
                }
            }
            if (cross == 2 && zero == 0 && who == Board.Field.Cross)
                return new Turn(Board.Field.Cross, positionX, positionY);
            if (cross == 0 && zero == 2 && who == Board.Field.Zero)
                return new Turn(Board.Field.Zero, positionX, positionY);
        }
        int cross = 0, zero = 0, positionX = 0, positionY = 0;
        for (int i = 0; i < 3; i++) {
            if (board.get(i, i) == Board.Field.Empty) {
                positionX = i;
                positionY = i;
            }
            cross += board.get(i, i) == Board.Field.Cross ? 1 : 0;
            zero += board.get(i, i) == Board.Field.Zero ? 1 : 0;
        }
        if (cross == 2 && zero == 0 && who == Board.Field.Cross)
            return new Turn(Board.Field.Cross, positionX, positionY);
        if (cross == 0 && zero == 2 && who == Board.Field.Zero)
            return new Turn(Board.Field.Zero, positionX, positionY);
        cross = 0;
        zero = 0;
        for (int i = 0; i < 3; i++) {
            if (board.get(i, 2-i) == Board.Field.Empty) {
                positionX = i;
                positionY = 2-i;
            }
            cross += board.get(i, 2-i) == Board.Field.Cross ? 1 : 0;
            zero += board.get(i, 2-i) == Board.Field.Zero ? 1 : 0;
        }
        if (cross == 2 && zero == 0 && who == Board.Field.Cross)
            return new Turn(Board.Field.Cross, positionX, positionY);;
        if (cross == 0 && zero == 2 && who == Board.Field.Zero)
            return new Turn(Board.Field.Zero, positionX, positionY);
        return new Turn(Board.Field.Empty, -1, -1);
    }

    @Override
    public Turn getNextTurn() {
        Turn canBotWin = canWin(who);
        if (canBotWin.getWho() == who) {
            board.set(canBotWin.getCol(), canBotWin.getRow(), who);
            return new Turn(who,canBotWin.getRow(), canBotWin.getCol());
        }
        Turn canOpponentWin = canWin(who == Board.Field.Zero ? Board.Field.Cross : Board.Field.Zero);
        if (canOpponentWin.getWho() != Board.Field.Empty) {
            board.set( canOpponentWin.getCol(), canOpponentWin.getRow(), who);
            return new Turn(who, canOpponentWin.getRow(), canOpponentWin.getCol());
        }
        while (true) {
            int y = random.nextInt() % 3;
            int x = random.nextInt() % 3;
            if (board.set(y, x, who)) {
                return new Turn(who, x, y);
            }
        }
    }

    @Override
    public void setTurn(Turn turn) {
        board.set(turn.getRow(), turn.getCol(), turn.getWho());
    }
}
