package ru.java;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import ru.java.Client.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class TreeItemClient extends TreeItem<Label> {
    private String path;
    private boolean isDir;
    private boolean emptyDir = false;
    private boolean isRoot = false;

    public class ClientHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (!isDir) {
                try {
                    Main.client.makeRequest("2 " + path);
                } catch (Client.ClientException | IOException ex) {
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
    }

    public TreeItemClient(String val, String path, boolean isDir, boolean isRoot) {
        super(new Label(val));
        super.getValue().setOnMouseClicked(new ClientHandler());
        //System.out.println(path);
        this.path = path;
        this.isDir = isDir;
        this.isRoot = isRoot;
    }

    @Override
    public ObservableList<TreeItem<Label>> getChildren() {
        ObservableList<TreeItem<Label>> children = super.getChildren();
        if (children.size() == 0 && !emptyDir) {
            Client.Response resp = null;
            try {
                resp = Main.client.makeRequest("1 " + path);
            } catch (Exception ex) {
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
            if (resp != null)
                if (resp.getSize() == 0) {
                    emptyDir = true;
                }
                else {
                    for (Client.Pair p : resp.getList()) {
                        TreeItemClient item = new TreeItemClient(p.first.toString(),
                                path + "/" + p.first.toString(),
                                (boolean) p.second, false);
                        children.add(item);
                    }
                }
        }
        return children;
    }

    @Override
    public boolean isLeaf() {
        return !isDir;
    }
}
