package pl.pkrysztofiak.gridview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MouseEventsApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        StackPane stackPane1 = new StackPane();
        stackPane1.setStyle("-fx-background-color: yellow;");
        stackPane1.setPrefSize(200, 200);
        
        StackPane stackPane2 = new StackPane();
        stackPane2.setStyle("-fx-background-color: red;");
        stackPane2.setPrefSize(200, 200);
        
        HBox hBox = new HBox(stackPane1, stackPane2);
        
        Scene scene = new Scene(hBox);
        stage.setScene(scene);
        stage.show();
        
        stackPane1.setOnMousePressed(event -> {
            System.out.println("stackPane1 MousePressed");
        });
        
        stackPane1.setOnMouseReleased(event -> {
            System.out.println("stackPane1 MouseReleased");
        });
        
        stackPane1.setOnMouseDragged(event -> {
            System.out.println("stackPane1 MouseDragged");
        });
        
        stackPane1.setOnMouseClicked(event -> {
            System.out.println("stackPane1 MouseClicked");
        });
        
        stackPane2.setOnMousePressed(event -> {
            System.out.println("stackPane2 MousePressed");
        });
        
        stackPane2.setOnMouseReleased(event -> {
            System.out.println("stackPane2 MouseReleased");
        });
        
        stackPane2.setOnMouseDragged(event -> {
            System.out.println("stackPane2 MouseDragged");
        });
        
        stackPane2.setOnMouseClicked(event -> {
            System.out.println("stackPane2 MouseClicked");
        });
        
    }
}