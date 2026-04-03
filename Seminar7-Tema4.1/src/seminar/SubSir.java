package seminar;

import java.util.Arrays;

public class SubSir <T extends Comparable<T>> extends Sir<T> implements Runnable {
    private int p,u;

    public SubSir(T[] v, int p, int u) throws Exception {
        super(v);
        if(p>u)
        {
            throw new Exception("Indici invalizi!");
        }
        this.p = p;
        this.u = u;
    }

    @Override
    public void run() {
        sortare();
    }
    public void sortare()
    {
        Arrays.sort(v,p,u+1);
    }
}
