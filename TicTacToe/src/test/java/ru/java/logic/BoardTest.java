package ru.java.logic;

import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {
    @Test
    public void set() throws Exception {
        Board board = new Board();
        assertTrue(board.set(0, 0,Board.Field.Cross));
        assertTrue(board.set(0, 1,Board.Field.Zero));
        assertFalse(board.set(0, 0,Board.Field.Cross));
        assertTrue(board.set(0, 2,Board.Field.Cross));
        assertFalse(board.set(0, 2,Board.Field.Zero));
    }

    @Test(expected = Board.BoardOutOfIndexException.class)
    public void get() throws Exception {
        Board board = new Board();
        assertTrue(board.set(0, 0,Board.Field.Cross));
        assertTrue(board.set(0, 1,Board.Field.Zero));
        assertTrue(board.get(0,0) == Board.Field.Cross);
        assertTrue(board.get(0,1) == Board.Field.Zero);
        assertTrue(board.get(1,1) == Board.Field.Empty);
        board.get(4,4);
    }

    @Test
    public void isWin() throws Exception {
        Board board = new Board();
        assertTrue(board.set(0, 0,Board.Field.Cross));
        assertTrue(board.set(0, 1,Board.Field.Cross));
        assertTrue(board.set(0, 2,Board.Field.Cross));
        assertTrue(board.isWin() == Board.Field.Cross);
    }

    @Test
    public void isDraw() throws Exception {
        Board board = new Board();
        assertTrue(board.set(0, 0,Board.Field.Cross));
        assertTrue(board.set(0, 1,Board.Field.Cross));
        assertTrue(board.set(0, 2,Board.Field.Zero));
        assertTrue(board.set(1, 0,Board.Field.Zero));
        assertTrue(board.set(1, 1,Board.Field.Zero));
        assertTrue(board.set(1, 2,Board.Field.Cross));
        assertTrue(board.set(2, 0,Board.Field.Cross));
        assertTrue(board.set(2, 1,Board.Field.Zero));
        assertTrue(board.set(2, 2,Board.Field.Cross));
        assertTrue(board.isDraw());
    }
}