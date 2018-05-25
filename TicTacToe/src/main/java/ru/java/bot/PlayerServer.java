package ru.java.bot;

import ru.java.logic.Board;
import ru.java.logic.Turn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PlayerServer implements Bot {
    /**
     * Ask bot about his next turn
     *
     * @return turn witch bot make
     */
    public boolean connected = true;
    private DataInputStream ios;
    private DataOutputStream oos;
    private Board.Field who;

    public PlayerServer(int port, Board.Field who) throws PlayerClient.ConnectException {
        this.who = who;
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            ios = new DataInputStream(socket.getInputStream());
            oos = new DataOutputStream(socket.getOutputStream());
            connected = true;
        } catch (IOException e) {
            connected = false;
            throw new PlayerClient.ConnectException();
        }
    }

    @Override
    public Turn getNextTurn() throws PlayerClient.ConnectException {
        if (connected) {
            try {
                int x = ios.readInt();
                int y = ios.readInt();
                return new Turn(who, x, y);
            } catch (IOException e) {
                connected = false;
                throw new PlayerClient.ConnectException();
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
    public void setTurn(Turn turn) throws PlayerClient.ConnectException {
        if (connected) {
            try {
                oos.writeInt(turn.getCol());
                oos.writeInt(turn.getRow());
            } catch (IOException e) {
                connected = false;
                throw new PlayerClient.ConnectException();
            }
        }
    }
}
