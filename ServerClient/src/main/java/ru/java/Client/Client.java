package ru.java.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private Socket socket;
    private DataOutputStream oos;
    private DataInputStream ois;
    private static int count = 0;

    public void start(int port) {
        try {
            socket = new Socket("localhost", port);
            oos = new DataOutputStream(socket.getOutputStream());
            ois = new DataInputStream(socket.getInputStream());
        } catch (Exception e) {
            System.out.println("Exception in starting client socket" + e.getMessage());
        }
    }

    public Response makeRequest(String request) throws ClientException, IOException {
        if (socket == null)
            throw new ClientException("No open socket");
        else {
            oos.writeInt(request.charAt(0) - '0');
            oos.writeInt(request.length() - 2);
            for (int i = 2 ; i < request.length(); i++) {
                oos.writeChar(request.charAt(i));
            }
        }
        if (request.charAt(0) == '1') {
            Response response = new Response();
            int size = ois.readInt();
            response.setSize(size);
            for (int i = 0; i < size; i++) {
                int stringSize = ois.readInt();
                StringBuilder s = new StringBuilder();
                for (int j = 0 ; j < stringSize; j++) {
                    s.append(ois.readChar());
                }
                boolean isDir = ois.readBoolean();
                response.addInList(s.toString(), isDir);
            }
            return response;
        }
        if (request.charAt(0) == '2') {
            Response response = new Response();
            long size = ois.readLong();
            response.setSize(size);
            byte[] content = new byte[1000];
            File f = new File("src/main/resources/ans/res" + (count++));
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStream os = new FileOutputStream(f);
            for (int read = 0; size - read > 0;) {
                int len = ois.readInt();
                ois.read(content, 0, len);
                os.write(content, 0, len);
                read += len;
            }
            os.close();
            response.setFile(f);
            return response;
        }
        throw new ClientException("Illegal request");
    }

    public static class Response {
        private long size;
        private List<Pair<String, Boolean>> list;
        private File content;

        public void setSize(long s) {
            size = s;
        }

        public void addInList(String name, boolean isDir) {
            if (list == null)
                list = new ArrayList<>();
            list.add(new Pair<>(name, isDir));
        }

        public void setFile(File file) {
            content = file;
        }

        public List<Pair<String, Boolean>> getList() {
            return list;
        }

        public File getFile() {
            return content;
        }

        public long getSize() {
            return size;
        }
    }

    public static class Pair<T, R> {
        public T first;
        public R second;
        public Pair(T f, R s) {
            first = f;
            second = s;
        }
    }
    public static class ClientException extends Exception {
        ClientException(String message) {
            super(message);
        }
    }
}