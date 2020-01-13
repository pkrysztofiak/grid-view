package pl.pkrysztofiak.gridview;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.stage.Stage;

public class IterateOverListApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        List<Integer> numbers = FXCollections.observableArrayList(1, 2, 3, 4, 5, 6);

        int index = numbers.indexOf(3);
        System.out.println("index=" + index);
        
        for (int i = index, j = i + 1; j < numbers.size(); i++, j++) {
            compare(numbers.get(i), numbers.get(j));
        }
        
//        for (int i = index, j = i - 1; j >= 0; i--, j--) {
//            compare(numbers.get(i), numbers.get(j));
//        }
    }
    
    private void compare(int number1, int number2) {
        System.out.println("number1=" + number1 + ", number2=" + number2);
    }
}