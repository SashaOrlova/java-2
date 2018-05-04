import org.junit.Test;
import ru.java.Server.Server;
import ru.java.Client.Client;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ServerClientTest {
    @Test
    public void smokeTest() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3355);
        });
        serverThread.start();
        Thread.sleep(100);
        Client client = new Client();
        client.start(3355);
        serverThread.interrupt();
        serverThread.join();
    }

    @Test
    public void getDirectoryContent() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3354);
        });
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3354);
        Client.Response response = client.makeRequest("1 src/main/resources/TestExamples");
        assertEquals(2, response.getSize());
        assertTrue((response.getList().get(0).first.equals("a.txt") && response.getList().get(1).first.equals("b.txt"))
                || (response.getList().get(1).first.equals("a.txt") && response.getList().get(0).first.equals("b.txt")));
        assertEquals(false, response.getList().get(0).second);
        assertEquals(false, response.getList().get(1).second);
        serverThread.interrupt();
        serverThread.join();
    }

    @Test
    public void getDirectoryWithDirectory() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3353);
        });
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3353);
        Client.Response response = client.makeRequest("1 src/main/resources/DirectoryWithDirectory");
        assertEquals(3, response.getSize());
        boolean wasDir = false, wasC = false, wasFile = false;
        for (int i = 0 ; i < 3; i++) {
            if (response.getList().get(i).first.equals("Directory")) {
                assertTrue(response.getList().get(i).second);
                wasDir = true;
            }
            if (response.getList().get(i).first.equals("c.txt")) {
                assertFalse(response.getList().get(i).second);
                wasC = true;
            }
            if (response.getList().get(i).first.equals("file.java")) {
                assertFalse(response.getList().get(i).second);
                wasFile = true;
            }
        }
        assertTrue(wasC && wasDir && wasFile);
        serverThread.interrupt();
        serverThread.join();
    }

    @Test
    public void getFile() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3352);
        });
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3352);
        Client.Response response = client.makeRequest("2 src/main/resources/TestExamples/a.txt");
        assertEquals(5, response.getSize());
        FileInputStream in = new FileInputStream(response.getFile());
        assertEquals('a', in.read());
        assertEquals('a', in.read());
        assertEquals('a', in.read());
        assertEquals('a', in.read());
        assertEquals('!', in.read());
        serverThread.interrupt();
        serverThread.join();
    }
}