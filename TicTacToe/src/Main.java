import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    Logic logic = new Logic(true);
    static Label res = new Label("Game not start");
    static GridPane gridpane = new GridPane();

    private void redraw() {
        ObservableList<Node> fields = gridpane.getChildren();
        for (int i = 1 ; i < fields.size(); i++) {
            ((Label)fields.get(i)).setText("");
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        gridpane.setGridLinesVisible(true);
        for (int i = 0 ; i < 3; i++)
            for (int j = 0 ; j < 3; j++) {
                Label l = new Label("");
                l.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE );
                l.setAlignment(Pos.CENTER);
                gridpane.add(l, i, j);
            }
        Button restart = new Button("Start Single Game");
        Button bot1 = new Button("Start Simple Bot");
        Button bot2 = new Button("Start Hard Bot");
        restart.setOnAction(event -> { logic.restart(); logic.changePlayer(Board.Field.Cross); res.setText(""); redraw(); });
        bot1.setOnAction(event -> { logic.changePlayer(Board.Field.Cross); logic.setBotGame(1); res.setText(""); redraw();});
        bot2.setOnAction(event -> { logic.changePlayer(Board.Field.Cross);  logic.setBotGame(2); res.setText(""); redraw();});
        vBox.getChildren().addAll(restart, bot1, bot2, res);
        ObservableList<Node> fields = gridpane.getChildren();
        for (int i = 0 ; i < fields.size(); i++) {
            int unmodifiedI = i;
            fields.get(i).setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    logic.makeTurn(((unmodifiedI - 1)%3) , (unmodifiedI - 1)/3 , (Label)fields.get(unmodifiedI));
                }
            });
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

    public static void writeRes(String st){
        res.setText(st);
    }

    public static void drawTurn(int num, Board.Field who) {
        System.out.println(num);
        ObservableList<Node> fields = gridpane.getChildren();
        ((Label)fields.get(num)).setText(who == Board.Field.Cross ? "X" : "O");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
