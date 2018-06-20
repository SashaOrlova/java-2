package ru.java;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import ru.java.Client.Client;
import ru.java.Server.Server;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Main extends Application {

    static public Client client = new Client();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Thread serverThread = new Thread(() -> {
                       Server server = new Server();
                       server.run(8080);
        });
        try {
            serverThread.start();
            Thread.sleep(1000);
            client.start(8080);
            TreeItemClient rootItem = new TreeItemClient(".",
                    ".", true, true);
            TreeView<Label> tree = new TreeView<>(rootItem);
            primaryStage.setScene(new Scene(tree, 800, 500));
            primaryStage.show();
        }   catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Exception Dialog");
            alert.setContentText(ex.getMessage());

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);

            alert.showAndWait();
        }
    }
}
