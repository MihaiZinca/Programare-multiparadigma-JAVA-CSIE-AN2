package seminar9.seminar;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

//probl A
public class Muzeu<T>{
    private Deque<T> muzeu=new ArrayDeque<>();
    private int v;

    public Muzeu(int v) {
        this.v = v;
    }

    public synchronized void intrare(List<T> vizitatori)
    {
        while(muzeu.size()+vizitatori.size()>v)
            try{
                wait();
            } catch (InterruptedException ex)  {}
        System.out.println("Vizitatori intrati:"+ vizitatori);
        muzeu.addAll(vizitatori);
        System.out.println("Vizitatori in muzeu:");
        System.out.println(muzeu);
        notifyAll();
    }

    //metoda de iesire
    public synchronized void iesire(int m)
    {
        while(muzeu.isEmpty())
        {
            try{
                wait();
            } catch (InterruptedException ex)  {}
        }
        int nrIesiti=Math.min(m, muzeu.size());
        System.out.println("Numar vizitatori iesiti:"+nrIesiti);
        for (int i = 0; i <nrIesiti ; i++) {
            muzeu.pop();
        }
        System.out.println("Vizitatori in muzeu:");
        System.out.println(muzeu);
    }



    //prima metoda primeste o lista de vizitatori,vedem daca incap in muzeu,daca nu firul trece in asteptare
    //met synchronized doar un singur fir intra si se executa
}
