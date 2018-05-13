package ru.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import ru.java.Client.Client;


public class Main extends Application {

    static public Client client = new Client();

    @Override
    public void start(Stage primaryStage) throws Exception {
        client.start(8080);
        TreeItemClient rootItem = new TreeItemClient("/",
                "/", true);
        TreeView<Label> tree = new TreeView<>(rootItem);
        primaryStage.setScene(new Scene(tree, 800, 500));
        primaryStage.show();
    }
}
