package ru.java.Client;

import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    @Test
    public void firstRequestTest() throws Exception {
        ServerTask1 severTask = new ServerTask1("3 a.txt false dir true b.exe false", 8080);
        Thread server = new Thread(severTask);
        server.start();
        Thread.sleep(100);
        Client client = new Client();
        client.start(8080);
        Client.Response resp = client.makeRequest("1 dir");
        List<Client.Pair<String, Boolean>> listOfFiles = resp.getList();
        assertEquals(false, listOfFiles.get(0).second);
        assertEquals(true, listOfFiles.get(1).second);
        assertEquals(false, listOfFiles.get(2).second);
        assertEquals("a.txt", listOfFiles.get(0).first);
        assertEquals("dir", listOfFiles.get(1).first);
        assertEquals("b.exe", listOfFiles.get(2).first);
        server.join();
    }

    @Test
    public void secondRequestTest() throws Exception {
        ServerTask2 severTask = new ServerTask2("test".getBytes(), 8080);
        Thread server = new Thread(severTask);
        server.start();
        Thread.sleep(100);
        Client client = new Client();
        client.start(8080);
        Client.Response resp = client.makeRequest("2 dir");
        File f = resp.getFile();
        try (FileReader reader = new FileReader(f.getCanonicalFile())) {
            char[] buf = new char[256];
            int read = reader.read(buf);
            assertEquals("test", (new String(buf)).substring(0, read));
        }
        server.join();
    }

    class ServerTask1 implements Runnable {
        String[] result;
        int port;

        ServerTask1(String st, int port) {
            result = st.split(" ");
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket client = server.accept();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                in.readInt();
                int c = Integer.parseInt(result[0].substring(0, 1));
                out.writeInt(c);
                for (int i = 1; i < result.length; i += 2) {
                    out.writeInt(result[i].length());
                    out.writeChars(result[i]);
                    out.writeBoolean(Boolean.parseBoolean(result[i + 1]));
                }
            } catch (IOException ignored) {
            }
        }
    }

    class ServerTask2 implements Runnable {
        byte[] result;
        int port;

        ServerTask2(byte[] st, int port) {
            result = st;
            this.port = port;
        }

        @Override
        public void run() {
            try (ServerSocket server = new ServerSocket(port)) {
                Socket client = server.accept();
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                DataInputStream in = new DataInputStream(client.getInputStream());
                in.readInt();
                out.writeLong(result.length);
                out.writeLong(result.length);
                out.write(result);
            } catch (IOException ignored) {
            }
        }
    }
}