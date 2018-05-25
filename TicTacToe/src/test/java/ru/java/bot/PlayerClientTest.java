package ru.java.bot;

import org.junit.Test;
import ru.java.logic.Board;
import ru.java.logic.Turn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class PlayerClientTest {
    @Test
    public void getNextTurn() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8080);
                Socket socket = serverSocket.accept();
                DataInputStream st = new DataInputStream(socket.getInputStream());
                assertEquals(1, st.readInt());
                assertEquals(2, st.readInt());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(100);
        PlayerClient client = new PlayerClient("localhost", 8080, Board.Field.Zero);
        client.setTurn(new Turn(Board.Field.Zero, 1, 2));
    }

    @Test
    public void setNextTurn() throws Exception {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8081);
                Socket socket = serverSocket.accept();
                DataOutputStream st = new DataOutputStream(socket.getOutputStream());
                st.writeInt(1);
                st.writeInt(2);
                st.writeInt(0);
                st.writeInt(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        Thread.sleep(100);
        PlayerClient client = new PlayerClient("localhost", 8081, Board.Field.Zero);
        Turn t1 = client.getNextTurn();
        Turn t2 = client.getNextTurn();
        assertEquals(1, t1.getCol());
        assertEquals(2, t1.getRow());
        assertEquals(0, t2.getCol());
        assertEquals(1, t2.getRow());
    }
}