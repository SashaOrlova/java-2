package ru.java.Server;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("src/main/resources"));

    @Test
    public void testSecondRequest() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(4353);
        });
        folder.newFolder("TempFolder");
        File f = folder.newFile("TempFolder/a.txt");
        FileOutputStream out = new FileOutputStream(f);
        out.write("AAAA!".getBytes());
        Thread.sleep(100);
        serverThread.start();
        Socket socket = new Socket("localhost", 4353);
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        String request = f.getCanonicalPath();
        oos.writeInt(2);
        oos.writeInt(request.length());
        oos.writeChars(request);
        byte[] buf = new byte[256];
        ois.readLong();
        ois.readInt();
        ois.read(buf);
        assertEquals("AAAA!", (new String(buf).substring(0, 5)));
    }

    @Test
    public void testFirstRequest() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(4354);
        });
        File tempFolder = folder.newFolder("TestFolder");
        folder.newFile("TestFolder/a.txt");
        folder.newFile("TestFolder/b.txt");
        serverThread.start();
        Thread.sleep(100);
        Socket socket = new Socket("localhost", 4354);
        DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
        DataInputStream ois = new DataInputStream(socket.getInputStream());
        String request = tempFolder.getCanonicalPath();
        oos.writeInt(1);
        oos.writeInt(request.length());
        oos.writeChars(request);
        int size = ois.readInt();
        assertEquals(2, size);
        for (int i = 0; i < size; i++) {
            int stringSize = ois.readInt();
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < stringSize; j++) {
                s.append(ois.readChar());
            }
            boolean isDir = ois.readBoolean();
            assertEquals(false, isDir);
            assertTrue(s.toString().equals("a.txt") || s.toString().equals("b.txt"));
        }
    }
}