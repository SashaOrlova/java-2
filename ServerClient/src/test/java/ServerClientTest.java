import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.java.Client.Client;
import ru.java.Server.Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Random;

import static org.junit.Assert.*;

public class ServerClientTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder(new File("src/main/resources"));

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
        File tempFolder = folder.newFolder("TempFolder");
        folder.newFile("TempFolder/a.txt");
        folder.newFile("TempFolder/b.txt");
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3354);
        Client.Response response = client.makeRequest("1 " + tempFolder.getCanonicalPath());
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
        File tempFolder = folder.newFolder("TestFolder");
        folder.newFolder("TestFolder", "Directory", "oneMore");
        folder.newFile("TestFolder/c.txt");
        folder.newFile("TestFolder/file.java");
        folder.newFile("TestFolder/Directory/smth.txt");
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3353);
        Client.Response response = client.makeRequest("1 " + tempFolder.getCanonicalPath());
        boolean wasDir = false, wasC = false, wasFile = false;
        for (int i = 0; i < 3; i++) {
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
    public void getBigFile() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3351);
        });
        File f = folder.newFile("test");
        FileOutputStream out = new FileOutputStream(f);
        Random random = new Random();
        byte[] buffer = new byte[1000];
        for (int i = 0; i < 5; i++) {
            random.nextBytes(buffer);
            out.write(buffer);
        }
        out.close();
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3351);
        Client.Response response = client.makeRequest("2 " + f.getCanonicalPath());
        File res = response.getFile();
        assertEquals(f.length(), res.length());
        byte[] newBuff = new byte[1000];
        FileInputStream oldOutput = new FileInputStream(f);
        FileInputStream newOutput = new FileInputStream(res);
        for (int i = 0; i < 5; i++) {
            oldOutput.read(buffer);
            newOutput.read(newBuff);
            assertArrayEquals(newBuff, buffer);
        }
        oldOutput.close();
        newOutput.close();
        serverThread.interrupt();
        serverThread.join();
    }

    @Test
    public void getNotExistedFile() throws Exception {
        Thread serverThread = new Thread(() -> {
            Server server = new Server();
            server.run(3350);
        });
        serverThread.start();
        Client client = new Client();
        Thread.sleep(100);
        client.start(3350);
        Client.Response response = client.makeRequest("2 src/main/resources/TestExamples/abc.txt");
        assertEquals(0, response.getSize());
        serverThread.interrupt();
        serverThread.join();
    }
}