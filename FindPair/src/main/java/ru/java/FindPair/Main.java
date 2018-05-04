package ru.java.FindPair;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Main extends Application {

    private static Logic logic;
    private static int N;
    private static GridPane gridpane;
    private static Label win = new Label();

    /** draw next turn on screen
     * @param row index in row
     * @param col index in col
     */
    public static void drawTurn(int row, int col) {
        if (gridpane != null) {
            int num = col * N + (row + 1);
            ObservableList<Node> fields = gridpane.getChildren();
            ((Label) fields.get(num)).setText(Integer.toString(logic.getField(row, col)));
        }
    }

    /** delete turn from screen
     * @param row index in row
     * @param col index in col
     */
    public static void deleteTurn(int row, int col) {
        if (gridpane != null) {
            int num = col * N + (row + 1);
            ObservableList<Node> fields = gridpane.getChildren();
            ((Label) fields.get(num)).setText("");
        }
    }

    /**
     * write result of game
     */
    public static void writeWin() {
        win.setText("You win!");
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        primaryStage.setTitle("Find Pair");
        gridpane = new GridPane();
        gridpane.setGridLinesVisible(true);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++) {
                Label l = new Label("");
                l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                l.setAlignment(Pos.CENTER);
                gridpane.add(l, i, j);
            }
        double per = 100 / N;
        ColumnConstraints cc = new ColumnConstraints();
        RowConstraints rc = new RowConstraints();
        cc.setPercentWidth(per);
        rc.setPercentHeight(per);
        for (int i = 0 ; i < N; i++) {
            gridpane.getColumnConstraints().add(cc);
            gridpane.getRowConstraints().add(rc);
        }
        ObservableList<Node> fields = gridpane.getChildren();
        for (int i = 0; i < fields.size(); i++) {
            int unmodifiedI = i;
            fields.get(i).setOnMouseClicked(event -> logic.makeTurn(((unmodifiedI - 1) % N),
                    (unmodifiedI - 1) / N));
        }

        gridpane.setPrefSize(400, 400);
        Button restart = new Button("Restart Game");
        restart.setOnAction(event -> {
            win.setText("");
            try {
                logic = new Logic(N);
            } catch (Logic.LogicException ignored) {}
            for (int i = 0 ; i < N; i++)
                for (int j = 0 ; j < N ; j ++)
                    deleteTurn(i, j);
        });
        vBox.getChildren().addAll(restart, win);
        hBox.getChildren().addAll(gridpane, vBox);
        primaryStage.setScene(new Scene(hBox, 600, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length > 0)
            try {
                N = Integer.parseInt(args[0]);
                logic = new Logic(N);
            } catch (Logic.LogicException e) {
                System.out.println(e.getMessage());
                return;
            }
        else
            System.out.println("Expected board size");
        launch(args);
    }
}
