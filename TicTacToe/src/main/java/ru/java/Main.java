package ru.java;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ru.java.logic.Board;
import ru.java.logic.Logic;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main extends Application {

    static Label res = new Label("Game not start");
    static Label stat = new Label("");
    static GridPane gridpane = new GridPane();
    Logic logic = new Logic();

    private static void writeStat() {
        int cross = 0, zero = 0 , draw = 0;
        try (FileReader fr = new FileReader("src/statistic")) {
            Scanner scan = new Scanner(fr);
            cross = scan.nextInt();
            zero = scan.nextInt();
            draw = scan.nextInt();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        stat.setText("CROSS " + cross + "\nZERO " + zero + "\nDRAW " + draw);
    }

    /** write resualt of game
     * @param st string for write
     * @param who winner
     */
    public static void writeRes(String st, Board.Field who) {
        res.setText(st);
        int cross = 0, zero = 0 , draw = 0;
        try (FileReader fr = new FileReader("src/statistic")) {
            Scanner scan = new Scanner(fr);
            cross = scan.nextInt();
            zero = scan.nextInt();
            draw = scan.nextInt();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if (who == Board.Field.Empty)
            draw++;
        if (who == Board.Field.Cross)
            cross++;
        if (who == Board.Field.Zero)
            zero++;
        try (FileWriter fw = new FileWriter("src/statistic")) {
            fw.write(cross + '0');
            fw.write('\n');
            fw.write(zero + '0');
            fw.write('\n');
            fw.write(draw + '0');
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /** draw turn from logic
     * @param num field where draw
     * @param who sign of image
     */
    public static void drawTurn(int num, Board.Field who) {
        if (num == 0) return;
        ObservableList<Node> fields = gridpane.getChildren();
        ((Label) fields.get(num)).setText(who == Board.Field.Cross ? "X" : "O");
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void redraw() {
        ObservableList<Node> fields = gridpane.getChildren();
        for (int i = 1; i < fields.size(); i++) {
            ((Label) fields.get(i)).setText("");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        gridpane.setGridLinesVisible(true);
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Label l = new Label("");
                l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
                l.setAlignment(Pos.CENTER);
                gridpane.add(l, i, j);
            }
        Button restart = new Button("Start Single Game");
        Button bot1X = new Button("Start Simple Bot X");
        Button bot1O = new Button("Start Simple Bot O");
        Button bot2X = new Button("Start Hard Bot X");
        Button bot2O = new Button("Start Hard Bot O");
        Button showStatistic = new Button("Show statistic");
        showStatistic.setOnAction(event -> Main.writeStat());
        restart.setOnAction(event -> {
            logic.setSingleGame(true);
            logic.restart();
            logic.changePlayer(Board.Field.Cross);
            res.setText("");
            redraw();
        });
        bot1X.setOnAction(event -> {
            logic.restart();
            logic.changePlayer(Board.Field.Cross);
            logic.setBotGame(1);
            res.setText("");
            redraw();
        });
        bot1O.setOnAction(event -> {
            logic.restart();
            logic.changePlayer(Board.Field.Zero);
            logic.setBotGame(1);
            res.setText("");
            redraw();
            logic.BotTurn();
        });
        bot2X.setOnAction(event -> {
            logic.restart();
            logic.changePlayer(Board.Field.Cross);
            logic.setBotGame(2);
            res.setText("");
            redraw();
        });
        bot2O.setOnAction(event -> {
            logic.restart();
            logic.changePlayer(Board.Field.Zero);
            logic.setBotGame(2);
            res.setText("");
            redraw();
            logic.BotTurn();
        });
        HBox bot1 = new HBox();
        bot1.getChildren().addAll(bot1O, bot1X);
        HBox bot2 = new HBox();
        bot2.getChildren().addAll(bot2O, bot2X);
        vBox.getChildren().addAll(restart, bot1, bot2, showStatistic, res, stat);
        ObservableList<Node> fields = gridpane.getChildren();
        for (int i = 0; i < fields.size(); i++) {
            int unmodifiedI = i;
            fields.get(i).setOnMouseClicked(event -> logic.makeTurn(((unmodifiedI - 1) % 3),
                    (unmodifiedI - 1) / 3, (Label) fields.get(unmodifiedI)));
        }
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(33);
        rowConstraints.setPercentHeight(33);
        gridpane.getColumnConstraints().addAll(columnConstraints1, columnConstraints1, columnConstraints1);
        gridpane.getRowConstraints().addAll(rowConstraints, rowConstraints, rowConstraints);
        gridpane.setPrefSize(300, 300);
        hBox.getChildren().addAll(gridpane, vBox);
        primaryStage.setScene(new Scene(hBox, 600, 300));
        primaryStage.show();
    }
}
