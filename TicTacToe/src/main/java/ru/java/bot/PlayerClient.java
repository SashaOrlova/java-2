package ru.java.bot;

import ru.java.logic.Board;
import ru.java.logic.Turn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerClient implements Bot {
    /**
     * Ask bot about his next turn
     *
     * @return turn witch bot make
     */
    public boolean connected = true;
    private DataInputStream ios;
    private DataOutputStream oos;
    private Board.Field who;

    public PlayerClient(String host, int port, Board.Field who) throws ConnectException {
        this.who = who;
        try {
            Socket socket = new Socket(host, port);
            ios = new DataInputStream(socket.getInputStream());
            oos = new DataOutputStream(socket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            connected = false;
            throw new ConnectException();
        }
    }

    @Override
    public Turn getNextTurn() throws ConnectException {
        if (connected) {
            try {
                int x = ios.readInt();
                int y = ios.readInt();
                return new Turn(who, x, y);
            } catch (IOException e) {
                connected = false;
                throw new ConnectException();
            }
        }
        return new Turn(Board.Field.Empty, -1, -1);
    }

    /**
     * Inform bot about opponent turn
     *
     * @param turn opponent turn
     */
    @Override
    public void setTurn(Turn turn) throws ConnectException {
        if (connected) {
            try {
                oos.writeInt(turn.getCol());
                oos.writeInt(turn.getRow());
            } catch (IOException e) {
                connected = false;
                throw new ConnectException();
            }
        }
    }

    public static class ConnectException extends Exception {}
}
