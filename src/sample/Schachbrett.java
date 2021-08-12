package sample;

import java.io.IOException;
import java.util.ArrayList;

public class Schachbrett {

    private boolean personenGeneriert = false;
    private final Statistik auswertung = new Statistik();
    private final int wahrscheinlichkeitErkrankt;
    private final int anzahlFiguren;
    public final int groesseX;
    public final int groesseY;
    public ArrayList<Person>[][] schachbrett;
    public boolean ende = false;
    public int runde = 0;

    public Schachbrett(int groesseX, int groesseY, int anzahlFiguren, int wahrscheinlichkeitErkrankt) {
        //schachbrett Erzeugen und variablen festlegen
        this.schachbrett = new ArrayList[groesseY][groesseX];
        this.groesseY = groesseY;
        this.groesseX = groesseX;
        this.anzahlFiguren = anzahlFiguren;
        this.wahrscheinlichkeitErkrankt = wahrscheinlichkeitErkrankt;

        //Schachbrett mit Arraylists befüllen in welchen später die Personen sind
        for (int y = 0; y < schachbrett.length; y++) {
            for (int x = 0; x < schachbrett[0].length; x++) {
                schachbrett[y][x] = new ArrayList<Person>();
            }
        }
    }

    public ArrayList<Person>[][] spielZug() throws IOException {
        //Personen nur bei der 0. Runde generieren und 0. Runde in Statistik schreiben
        if (!personenGeneriert) {
            personenGenerieren();
        } else {
            //runde hochzählen
            runde++;
            personenBewegen();
            //Infektionsrisiko für Personen prüfen und ggf. infizieren werden
            umfeldUeberpruefen();
            //Statistik nächste Runde schreiben
            auswertung.neueZeile(schachbrett, runde, ende);
            //Schachbrett mit bewegeten und infizierten Personen zurückgeben für Anzeige
        }
        return schachbrett;
    }


    private void personenGenerieren() throws IOException {
        // Zufällige Koordinaten für neue Personen festelegen und diese platzieren
        for (int i = 0; i < anzahlFiguren; i++) {
            int randX = (int) (Math.random() * schachbrett[0].length);
            int randY = (int) (Math.random() * schachbrett.length);
            Person p = new Person(wahrscheinlichkeitErkrankt);
            schachbrett[randY][randX].add(p);
        }
        // nullte Runde in die Statistik schreiben
        auswertung.neueZeile(schachbrett, runde, ende);
        personenGeneriert = true;
    }

    private void personenBewegen() {
        //zweites Spielfeld als zwischenspeicher erstellen, da sonst die Liste modifiziert wird die man betrachtet
        ArrayList<Person>[][] schachbrettZwischenspeicher = new ArrayList[schachbrett.length][schachbrett[0].length];

        for (int y = 0; y < schachbrettZwischenspeicher.length; y++) {
            for (int x = 0; x < schachbrettZwischenspeicher[0].length; x++) {
                schachbrettZwischenspeicher[y][x] = new ArrayList<Person>();
            }
        }

        for (int y = 0; y < schachbrett.length; y++) {
            for (int x = 0; x < schachbrett[0].length; x++) {
                for (Person person : schachbrett[y][x]) {
                    //unterscheiden zwischen gesunden/genesenen und kranken Personen -> andere Bewegungseigenschaften
                    if (person.status.equals(Person.Status.GESUND) || person.status.equals(Person.Status.GENESEN)) {
                        //Zufallszahl zwischen -10 - 10 um beide Richtungen in X und Y abzudecken
                        int anzahlFelderHorizontal = (-10) + ((int) (Math.random() * 21));
                        int anzahlFelderVertikaler = (-10) + ((int) (Math.random() * 21));
                        //überprüfen ob in die Richtung gegangen werden kann, wenn man auf eine Wand trifft bis zur Wand laufen
                        anzahlFelderVertikaler = vertikalPruefen(anzahlFelderVertikaler, y);
                        anzahlFelderHorizontal = horizontalPruefen(anzahlFelderHorizontal, x);
                        //neue Person im 2. Schachbrett hinzufügen
                        schachbrettZwischenspeicher[y + anzahlFelderVertikaler][x + anzahlFelderHorizontal].add(person);
                    }
                    if (person.status.equals(Person.Status.ERKRANKT)) {
                        int anzahlFelderHorizontal = (int) ((-10) + ((int) (Math.random() * 21))) / 2;
                        int anzahlFelderVertikaler = (int) ((-10) + ((int) (Math.random() * 21))) / 2;
                        anzahlFelderVertikaler = vertikalPruefen(anzahlFelderVertikaler, y);
                        anzahlFelderHorizontal = horizontalPruefen(anzahlFelderHorizontal, x);
                        schachbrettZwischenspeicher[y + anzahlFelderVertikaler][x + anzahlFelderHorizontal].add(person);
                    }
                }
            }
        }
        //richtiges Schachbrett mit dem Zwischenspeicher überschreiben
        schachbrett = schachbrettZwischenspeicher;
    }

    private int vertikalPruefen(int anzahlFelderVertikal, int y) {
        //entweder 0 in vertikale Richtung oder y-Position +/- die vertikalen Felder kleiner oder gleich der
        // Feldgröße, reichweite der vertikalen Bewegungsrichtung zurückgeben | Else bis zum Rand gehen
        if (anzahlFelderVertikal == 0 || ((y + anzahlFelderVertikal) <= schachbrett.length - 1 && (y + anzahlFelderVertikal) >= 0)) {
            return anzahlFelderVertikal;
        } else {
            int verschiebung = y + anzahlFelderVertikal;
            if (verschiebung > schachbrett.length - 1) {
                anzahlFelderVertikal = anzahlFelderVertikal - (verschiebung - schachbrett.length) - 1;
            }
            if (verschiebung < 0) {
                anzahlFelderVertikal = anzahlFelderVertikal - verschiebung;
            }
        }
        return anzahlFelderVertikal;
    }

    private int horizontalPruefen(int anzahlFelderHorizontal, int x) {
        //entweder 0 in horizontale Richtung oder X-Position +/- die horizontalen Felder kleiner oder gleich der
        // Feldgröße, reichweite der horizontalen Bewegungsrichtung zurückgeben | Else bis zum Rand gehen
        if (anzahlFelderHorizontal == 0 || ((x + anzahlFelderHorizontal) <= schachbrett[0].length - 1 && (x + anzahlFelderHorizontal) >= 0)) {
            return anzahlFelderHorizontal;
        } else {
            int verschiebung = x + anzahlFelderHorizontal;
            if (verschiebung > schachbrett[0].length - 1) {
                anzahlFelderHorizontal = anzahlFelderHorizontal - (verschiebung - schachbrett[0].length) - 1;
            }
            if (verschiebung < 0) {
                anzahlFelderHorizontal = anzahlFelderHorizontal - verschiebung;
            }
        }
        return anzahlFelderHorizontal;
    }

    private void umfeldUeberpruefen() {
        //Array durchgehen und für jede Person die Infektion bestimmen daneben - diagonal
        //Genesene Personen können außer Acht gelassen werden
        //erkrankte Personen zählen, um das Ende bestimmen zu können
        int erkrankte = 0;
        for (int y = 0; y < schachbrett.length; y++) {
            for (int x = 0; x < schachbrett[0].length; x++) {
                for (Person person : schachbrett[y][x]) {
                    if (person.status.equals(Person.Status.ERKRANKT)) {
                        if (person.rundenBisZurGenesung == 0) {
                            person.status = Person.Status.GENESEN;
                        } else {
                            person.rundenBisZurGenesung -= 1;
                            erkrankte += 1;
                        }
                    }
                    if (person.status.equals(Person.Status.GESUND)) {
                        int wahrscheinlichkeit = (int) (Math.random() * 100);
                        if (wahrscheinlichkeit <= wahrscheinlichkeitInfiziertZuWerden(x, y)) {
                            person.status = Person.Status.ERKRANKT;
                            erkrankte += 1;
                        }
                    }
                }
            }
        }
        //bool ende auf true, kein weiterer Durchlauf
        if (erkrankte == 0) {
            ende = true;
        }
    }

    private int wahrscheinlichkeitInfiziertZuWerden(int x, int y) {
        //Selbes Feld +90
        //Nebenstehendes Feld +30 (auch diagonal)
        //Überprüfen ob die Prüfung im Feld bleibt (Array Index out of bounds)

        int wahrscheinlichkeit = 0;

        //gleiches Feld
        for (Person person : schachbrett[y][x]) {
            if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 90;
        }

        //linke Felder
        if (x > 0) {
            //links
            for (Person person : schachbrett[y][x - 1])
                if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            //oben
            if (y > 0) {
                for (Person person : schachbrett[y - 1][x - 1])
                    if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            }
            //unten
            if (y < schachbrett.length - 1) {
                for (Person person : schachbrett[y + 1][x - 1])
                    if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            }
        }

        //rechte Felder
        if (x < schachbrett[0].length - 1) {
            //rechts
            for (Person person : schachbrett[y][x + 1])
                if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            //oben
            if (y > 0) {
                for (Person person : schachbrett[y - 1][x + 1])
                    if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            }
            //unten
            if (y < schachbrett.length - 1) {
                for (Person person : schachbrett[y + 1][x + 1])
                    if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
            }
        }

        //oberes Feld
        if (y > 0) {
            for (Person person : schachbrett[y - 1][x])
                if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
        }

        //unteres Feld
        if (y < schachbrett.length - 1) {
            for (Person person : schachbrett[y + 1][x])
                if (person.status.equals(Person.Status.ERKRANKT)) wahrscheinlichkeit += 30;
        }

        return wahrscheinlichkeit;
    }
}


