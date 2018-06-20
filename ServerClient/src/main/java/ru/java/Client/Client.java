package ru.java.Client;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for presentation instance of client
 */
public class Client {
    private Socket socket;
    private DataOutputStream oos;
    private DataInputStream ois;
    private static int count = 0;

    /** Start new client associate with port
     * @param port number of port for communication with server
     */
    public void start(int port) throws IOException {
            socket = new Socket("localhost", port);
            oos = new DataOutputStream(socket.getOutputStream());
            ois = new DataInputStream(socket.getInputStream());
    }

    /** make request to server from client
     * @param request string describes request to server
     * @return answer from server
     * @throws ClientException
     * @throws IOException
     */
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
            File f = new File("src/main/resources/ans/res" + (++count));
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

    /**
     * Class for represented server answer
     */
    public static class Response {
        private long size;
        private List<Pair<String, Boolean>> list;
        private File content;

        private void setSize(long s) {
            size = s;
        }

        private void addInList(String name, boolean isDir) {
            if (list == null)
                list = new ArrayList<>();
            list.add(new Pair<>(name, isDir));
        }

        private void setFile(File file) {
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

    /** Class for keep pair of classes
     * @param <T> type of first object
     * @param <R> type of second object
     */
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