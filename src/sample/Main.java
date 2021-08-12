package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private Pane pane;
    private Schachbrett spielfeld;
    private SchachbrettUI gui;
    private int runde = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //JavaFX set-up, Panes Laden Größe Setzen etc
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        BorderPane mainPane = loader.load();
        pane = (Pane) mainPane.getChildren().get(0);
        pane.setPrefWidth(800);
        pane.setPrefHeight(800);

        //Stage set-up Icon,Titel,Szene
        primaryStage.getIcons().add(new Image("/sample/icon.png"));
        primaryStage.setTitle("Simultaion der Coronapandemie");
        primaryStage.setScene(new Scene(mainPane));

        //Werte einlesen
        spielfeld = new Start().start();
        gui = new SchachbrettUI();
        primaryStage.show();

        Thread thread = new Thread(new Runnable() {
            ArrayList<Person>[][] aktuelleAufstellung;
            @Override
            public void run() {
                Runnable updater = () -> {
                    anzeigen(gui.bildGenerieren(aktuelleAufstellung));
                    runde(runde);
                };

                while (!spielfeld.ende) {
                    try {
                        runde = spielfeld.runde;
                        aktuelleAufstellung = spielfeld.spielZug();
                        Thread.sleep(100);
                    } catch (IOException | InterruptedException ex) {
                        System.out.println(ex);
                    }

                    // UI updaten im Application-Thread
                    Platform.runLater(updater);
                }
                System.out.println("ende");
            }
        });

        // Thread vor JVM shutdown schützen
        thread.setDaemon(true);
        thread.start();
    }

    private void anzeigen(ImageView img) {
        //Elemente aus Pane löschen um Speicher zu sparen, sonst alle Runden übereinander
        pane.getChildren().clear();

        //Bild auf Panegröße skalieren
        img.setFitWidth(pane.getWidth());
        img.setFitHeight(pane.getHeight());
        pane.getChildren().add(img);
    }

    private void runde(int runde) {
        Label label = new Label("Es sind seit Start " + runde + " Tage Vergangen.");
        label.setFont(Font.font("Cambria",28));
        pane.getChildren().add(label);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
