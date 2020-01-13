package pl.pkrysztofiak.gridview;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.application.Application;
import javafx.stage.Stage;

public class PublishSubjectApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        PublishSubject<String> receiver = PublishSubject.create();
        PublishSubject<String> publisher1 = PublishSubject.create();
        PublishSubject<String> publisher2 = PublishSubject.create();
        
        
        publisher1.delay(0, TimeUnit.SECONDS, Schedulers.single()).subscribe(receiver::onNext);
        publisher2.delay(0, TimeUnit.SECONDS, Schedulers.single()).subscribe(receiver::onNext);
        
        receiver.buffer(2).subscribe(list -> {
            System.out.println(list);
            if (!list.get(0).equals("one")) {
                throw new RuntimeException();
            }
            if (!list.get(1).equals("two")) {
                throw new RuntimeException();
            }
        });
        
        while (true) {
            publisher1.onNext("one");
            publisher2.onNext("two");
        }
    }
}
