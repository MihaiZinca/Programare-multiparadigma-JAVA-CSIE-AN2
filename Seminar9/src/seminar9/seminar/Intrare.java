package seminar9.seminar;

import java.util.ArrayList;
import java.util.List;

public class Intrare<T> extends Thread{
    private Muzeu<T> muzeu;
    private int n;
    private T vizitator;
    private Generator<T> gernerator;

    public Intrare(Muzeu<T> muzeu, int n,Generator<T> generator) {
        this.muzeu = muzeu;
        this.n = n;
        this.gernerator=generator;
        vizitator=generator.init();
    }

    @Override
    public void run() {
        while(!Main.stop)
        {
            int nrVizitatori = (int)(Math.random() * n)+1; //generate intre 1 si n
            List<T> vizitatori=new ArrayList<>();
            for (int i = 0; i < nrVizitatori; i++) {
                vizitatori.add(vizitator);
                vizitator=gernerator.next(vizitator);
            }
            muzeu.intrare(vizitatori);

        }
    }


}
