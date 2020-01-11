package pl.pkrysztofiak.gridview;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class GroupStackApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {

        Line line1 = new Line(200, 0, 200, 400);
        line1.setStroke(Color.RED);
        line1.setStrokeWidth(16);
        
        Line line2 = new Line(200, 0, 200, 400);
        line2.setStroke(Color.PURPLE);
        line2.setStrokeWidth(16);
        
        Group group = new Group(line1, line2);
        
        Pane pane = new Pane(group);
        
        Scene scene = new Scene(pane, 400, 400);
        stage.setScene(scene);
        stage.show();

        line1.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Line1 clicked filter");
        });
        
        line2.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("Line2 clicked filter");
        });
        
        line1.setOnMouseClicked(event -> {
            System.out.println("Line1 clicked");
        });
        
        line2.setOnMouseClicked(event -> {
            System.out.println("Line2 clicked");
        });
    }
}