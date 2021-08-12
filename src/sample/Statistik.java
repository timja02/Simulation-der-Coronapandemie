package sample;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Statistik {

    public ArrayList<Person>[][] schachbrett;
    int anzahlInfizierter;
    int anzahlGenesener;
    int anzahlGesunder;
    private FileWriter csvwirter;
    private final StringBuilder sb = new StringBuilder();

    public Statistik() {
        //die erste Zeile also Namen schreiben
        sb.append("Runde");
        sb.append(",");
        sb.append("Anzahl gesunder Personen");
        sb.append(",");
        sb.append("Anzahl infizierter Personen");
        sb.append(",");
        sb.append("Anzahl genesener Personen");
        sb.append("\n");
    }

    private void auswertenDerPeronen() {
        //zurücksetzen der Werte aufgrund neuer Runde
        anzahlInfizierter = 0;
        anzahlGenesener = 0;
        anzahlGesunder = 0;
        //Personen aus dem schachbrett in alle personen übertragen
        ArrayList<Person> allePersonen = new ArrayList<>();
        for (int y = 0; y < schachbrett.length; y++) {
            for (int x = 0; x < schachbrett[0].length; x++) {
                allePersonen.addAll(schachbrett[y][x]);
            }
        }


        //Personen mit jeweiligem Status zählen
        for (Person person : allePersonen) {
            if (person.status.equals(Person.Status.GESUND)) {
                anzahlGesunder += 1;
            }
            if (person.status.equals(Person.Status.ERKRANKT)) {
                anzahlInfizierter += 1;
            }
            if (person.status.equals(Person.Status.GENESEN)) {
                anzahlGenesener += 1;
            }
        }
    }

    public void neueZeile(ArrayList<Person>[][] schachbrett, int runde, boolean ende) throws IOException {
        //Übergebenes Schachbrett auswerten
        //Wenn ende==true dann den mit Filewriter alles in die Datei schreiben
        this.schachbrett = schachbrett;
        auswertenDerPeronen();
        sb.append(runde);
        sb.append(",");
        sb.append(anzahlGesunder);
        sb.append(",");
        sb.append(anzahlInfizierter);
        sb.append(",");
        sb.append(anzahlGenesener);
        sb.append("\n");

        if (ende) {
            inDateiSchreiben();
        }
    }

    public void inDateiSchreiben() {
        try {
            //Filewriter Datei benennen
            csvwirter = new FileWriter("Statistik.csv");
            //den gebauten string aus dem StringBuilder anhängen
            csvwirter.append(sb.toString());
            //schließen
            csvwirter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
