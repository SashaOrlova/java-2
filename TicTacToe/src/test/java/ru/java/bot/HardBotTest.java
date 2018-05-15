package ru.java.bot;

import org.junit.Test;
import ru.java.logic.Board;
import ru.java.logic.Turn;

import static org.junit.Assert.*;

public class HardBotTest {
    @Test
    public void horizontalWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Cross);
        board.set(0, 1, Board.Field.Cross);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(2, turn.getCol());
        assertEquals(0, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }

    @Test
    public void verticalWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Cross);
        board.set(1, 0, Board.Field.Cross);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(0, turn.getCol());
        assertEquals(2, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }

    @Test
    public void diagonalWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Cross);
        board.set(1, 1, Board.Field.Cross);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(2, turn.getCol());
        assertEquals(2, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }

    @Test
    public void horizontalBlockWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Zero);
        board.set(0, 1, Board.Field.Zero);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(2, turn.getCol());
        assertEquals(0, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }

    @Test
    public void verticalBlockWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Zero);
        board.set(1, 0, Board.Field.Zero);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(0, turn.getCol());
        assertEquals(2, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }

    @Test
    public void diagonalBlockWin() throws Exception {
        Board board = new Board();
        board.set(0, 0, Board.Field.Zero);
        board.set(1, 1, Board.Field.Zero);
        Bot bot = new HardBot(Board.Field.Cross, board);
        Turn turn = bot.getNextTurn();
        assertEquals(2, turn.getCol());
        assertEquals(2, turn.getRow());
        assertEquals(Board.Field.Cross, turn.getWho());
    }
}