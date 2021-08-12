package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SchachbrettUI {

    private ArrayList<Person>[][] schachbrett;
    private BufferedImage bi;
    private Graphics2D g2d;

    public SchachbrettUI() {
    }

    public ImageView bildGenerieren(ArrayList<Person>[][] schachbrett) {
        this.schachbrett = schachbrett;
        bi = new BufferedImage(schachbrett.length, schachbrett[0].length, BufferedImage.TYPE_INT_RGB);
        g2d = (Graphics2D) bi.getGraphics();
        datenUmwandeln();
        ImageView img = new ImageView(SwingFXUtils.toFXImage(bi, null));
        return img;
    }

    private void datenUmwandeln() {
        //schachbrett in Daten 0=leer 1=nur gesunde 2=mindestens ein Kranker
        for (int y = 0; y < schachbrett.length; y++) {
            for (int x = 0; x < schachbrett[0].length; x++) {
                int erkrankte = 0;
                int personenAufFeld = 0;
                for (Person person : schachbrett[y][x]) {
                    personenAufFeld++;
                    if (person.status.equals(Person.Status.ERKRANKT)) {
                        erkrankte++;
                        break;
                    }
                }
                if (personenAufFeld == 0) {
                    g2d.setColor(Color.WHITE);
                } else {
                    if (erkrankte == 0)
                        g2d.setColor(Color.GREEN);
                    if (erkrankte > 0)
                        g2d.setColor(Color.RED);
                }
                g2d.fillRect(y, x, 1, 1);
            }
        }
        g2d.drawImage(bi, 0, 0, null);
    }
}
