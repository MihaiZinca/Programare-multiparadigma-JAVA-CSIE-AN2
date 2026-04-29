package seminar9.seminar;

public class Iesire<T> extends Thread{
    private  Muzeu<T> muzeu;
    private int m;

    public Iesire(Muzeu<T> muzeu, int m) {
        this.muzeu = muzeu;
        this.m = m;
    }

    @Override
    public void run() {
        while(!Main.stop)
        {
            int nrIesiti=(int)(Math.random()*m)+1;
            muzeu.iesire(nrIesiti);
        }
    }
}
