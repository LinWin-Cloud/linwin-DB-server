package lib;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class buttonUI {
    public static Button buttonUI(String text) {
        Button button = new Button(text);
        button.setPrefWidth(150);
        button.setPrefHeight(35);
        button.setFont(Font.font(19));

        return button;
    }
    public static Button button1(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #e2e2e2");

        Glow glow = new Glow();
        button.addEventFilter(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                button.setEffect(glow);
            }
        });
        button.addEventFilter(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ExecutorService executorService = Executors.newFixedThreadPool(1);
                Future<Integer> future = executorService.submit(new Callable<Integer>() {
                    @Override
                    public Integer call() throws Exception {
                        try{
                            Thread.sleep(100);
                            button.setEffect(null);
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                        return 0;
                    }
                });
                executorService.shutdown();
            }
        });
        return button;
    }
}
