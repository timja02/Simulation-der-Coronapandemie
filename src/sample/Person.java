package sample;

public class Person {

    public enum Status {GESUND, GENESEN, ERKRANKT}
    public Status status;
    public int rundenBisZurGenesung = 14;


    public Person(int wahrscheinlichkeitKrank) {
        status = Status.GESUND;
        int wahrscheinlichkeit = (int) (Math.random() * 101);
        if (wahrscheinlichkeit <= wahrscheinlichkeitKrank) {
            status = Status.ERKRANKT;
        }
    }
}
