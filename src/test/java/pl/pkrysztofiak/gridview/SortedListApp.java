package pl.pkrysztofiak.gridview;

import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.stage.Stage;

public class SortedListApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ObservableList<Integer> numbers = FXCollections.observableArrayList();
        SortedList<Integer> sortedNumbers = new SortedList<>(numbers, (number1, number2) -> number1.compareTo(number2));
        JavaFxObservable.emitOnChanged(sortedNumbers).subscribe(System.out::println);
        
        numbers.add(2);
        numbers.add(1);
        numbers.add(5);
        numbers.add(4);
        
        System.out.println("after=" + sortedNumbers);
    }
}