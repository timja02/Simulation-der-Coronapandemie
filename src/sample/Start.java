package sample;

import javafx.scene.control.TextInputDialog;

import java.util.Optional;


public class Start {

    public Start() {
    }

    public Schachbrett start() {
        //jeweiligen Größen einlesen und dann ein Schachbrett returnen
        TextInputDialog dialogX = new TextInputDialog("Größe in X?");
        Optional<String> ergebnisX = dialogX.showAndWait();
        int groesseX = Integer.parseInt(ergebnisX.get());
        dialogX.close();

        TextInputDialog dialogY = new TextInputDialog("Größe in Y?");
        Optional<String> ergebnisY = dialogY.showAndWait();
        int groesseY = Integer.parseInt(ergebnisY.get());
        dialogY.close();

        TextInputDialog dialogFiguren = new TextInputDialog("Anzahl der Figuren?");
        Optional<String> ergebnisFiguren = dialogFiguren.showAndWait();
        int anzahlFiguren = Integer.parseInt(ergebnisFiguren.get());
        dialogFiguren.close();

        TextInputDialog dialogWahrscheinlichkeit = new TextInputDialog(
                "Wahrscheinlichkeit das eine Person erkrankt ist?");
        Optional<String> ergebnisWahrscheinlichkeit = dialogWahrscheinlichkeit.showAndWait();
        int wahrscheinlichkeit = Integer.parseInt(ergebnisWahrscheinlichkeit.get());
        dialogWahrscheinlichkeit.close();

        return new Schachbrett(groesseX, groesseY, anzahlFiguren, wahrscheinlichkeit);
    }
}
