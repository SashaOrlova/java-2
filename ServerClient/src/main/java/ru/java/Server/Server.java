package ru.java.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    public void run(int port) {
        try (ServerSocket server = new ServerSocket(port)){
            Socket client = server.accept();
            client.setSoTimeout(100);
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            DataInputStream in = new DataInputStream(client.getInputStream());
            while(!client.isClosed()){
                try {
                    int type = in.readInt();
                    if (type == 1) {
                        int size = in.readInt();
                        StringBuilder path = new StringBuilder();
                        for (int i = 0; i < size; i++) {
                            path.append(in.readChar());
                        }
                        File dir = new File(path.toString());
                        File[] files = dir.listFiles();
                        if (files == null)
                            out.writeInt(0);
                        else {
                            out.writeInt(files.length);
                            for (File f : files) {
                                out.writeInt(f.getName().length());
                                out.writeChars(f.getName());
                                out.writeBoolean(f.isDirectory());
                            }
                        }
                    }
                    if (type == 2) {
                        int size = in.readInt();
                        StringBuilder path = new StringBuilder();
                        for (int i = 0; i < size; i++) {
                            path.append(in.readChar());
                        }
                        File file = new File(path.toString());
                        InputStream os = new FileInputStream(file);
                        byte[] buf = new byte[1000];
                        out.writeLong(file.length());
                        int read = os.read(buf, 0, 1000);
                        while (read > 0) {
                            out.writeInt(read);
                            out.write(buf, 0, read);
                            read = os.read(buf, 0, 1000);
                        }
                    }
                }
                catch (SocketTimeoutException e) {
                    if (Thread.interrupted()) {
                        return;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server error " + e.getMessage());
        }
    }
}
