package ru.java.bot;

import org.junit.Test;
import ru.java.logic.Board;
import ru.java.logic.Turn;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class PlayerServerTest {
    @Test
    public void getNextTurn() throws Exception {
        Thread serverThread = new Thread(() -> {
        try {
            PlayerServer playerServer = new PlayerServer(8181, Board.Field.Zero);
            Turn t = playerServer.getNextTurn();
            assertEquals(1, t.getCol());
            assertEquals(2, t.getRow());
        } catch (PlayerClient.ConnectException e) {
            e.printStackTrace();
        }
        });
        serverThread.start();
        Thread.sleep(100);
        Socket socket = new Socket("localhost", 8181);
        DataOutputStream os = new DataOutputStream(socket.getOutputStream());
        os.writeInt(1);
        os.writeInt(2);
    }

    @Test
    public void setTurn() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                PlayerServer playerServer = new PlayerServer(8183, Board.Field.Zero);
                playerServer.setTurn(new Turn(Board.Field.Zero, 1,1));
                playerServer.setTurn(new Turn(Board.Field.Zero, 2,3));
            } catch (PlayerClient.ConnectException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(100);
        Socket socket = new Socket("localhost", 8183);
        DataInputStream os = new DataInputStream(socket.getInputStream());
        assertEquals(1, os.readInt());
        assertEquals(1, os.readInt());
        assertEquals(2, os.readInt());
        assertEquals(3, os.readInt());
    }
}