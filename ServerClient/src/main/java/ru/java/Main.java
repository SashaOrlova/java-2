package ru.java;

import ru.java.Client.Client;
import ru.java.Server.Server;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Client client = new Client();
        while (true) {
            String st = in.nextLine();
            if (st.equals("start server")) {
                Thread serverThread = new Thread(() -> {
                    Server server = new Server();
                    server.run(7353);
                });
                serverThread.start();
                client.start(7353);
                System.out.println("Server start");
            } else {
                String[] strings = st.split(" ");
                if (strings.length != 2) {
                    System.out.println("unexpected number of words");
                }
                if (strings[0].equals("get")) {
                    try {
                        Client.Response resp = client.makeRequest("1 " + strings[1]);
                        if (resp.getList() == null) {
                            System.out.println("Not exist directory");
                            continue;
                        }
                        for (Client.Pair<String, Boolean> p : resp.getList()) {
                            System.out.println(p.first + " " + p.second.toString());
                        }
                    } catch (Client.ClientException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
                if (strings[0].equals("download")) {
                    try {
                        Client.Response resp = client.makeRequest("2 " + strings[1]);
                        System.out.println("file save as " + resp.getFile().getName());
                    } catch (Client.ClientException | IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
    }
}
