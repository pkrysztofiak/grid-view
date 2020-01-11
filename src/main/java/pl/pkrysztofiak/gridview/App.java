package pl.pkrysztofiak.gridview;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.pkrysztofiak.gridview.model.Model;
import pl.pkrysztofiak.gridview.view.View;

public class App extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Model model = new Model();
        View view = new View(model);
        
        Scene scene = new Scene(view, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}