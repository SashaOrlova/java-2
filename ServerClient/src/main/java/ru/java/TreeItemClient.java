package ru.java;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import ru.java.Client.Client;

import java.io.IOException;


public class TreeItemClient extends TreeItem<Label> {
    private String path;
    private boolean isDir;
    private boolean emptyDir = false;

    public class ClientHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            if (!isDir) {
                try {
                    Main.client.makeRequest("2 " + path);
                } catch (Client.ClientException | IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public TreeItemClient(String val, String path, boolean isDir) {
        super(new Label(val));
        super.getValue().setOnMouseClicked(new ClientHandler());
        System.out.println(path);
        this.path = path;
        this.isDir = isDir;
    }

    @Override
    public ObservableList<TreeItem<Label>> getChildren() {
        ObservableList<TreeItem<Label>> children = super.getChildren();
        if (children.size() == 0 && !emptyDir) {
            Client.Response resp = null;
            try {
                resp = Main.client.makeRequest("1 " + path);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if (resp != null)
                if (resp.getSize() == 0) {
                    emptyDir = true;
                }
                else {
                    for (Client.Pair p : resp.getList()) {
                        TreeItemClient item = new TreeItemClient(p.first.toString(),
                                path.equals("/") ? path + "/" + p.first.toString() : "/" + p.first.toString(),
                                (boolean) p.second);
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
