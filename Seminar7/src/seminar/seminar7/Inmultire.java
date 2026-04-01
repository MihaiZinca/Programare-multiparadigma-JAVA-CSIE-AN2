package seminar.seminar7;

public class Inmultire<T> extends Matrice<T> implements Runnable{

    private int l1,l2;
    private T[] y,z;

    private Operatiuni<T> o;

    public Inmultire(T[][] x, int l1, int l2, T[] y, T[] z,Operatiuni<T> o) throws Exception {
        super(x);
        if(x[0].length!=y.length || x.length!=z.length)
        {
            throw new Exception("Dimensiuni incorecte pt inmultire!");
        }
        if(l1>l2 || l2>x.length-1) {
            throw  new Exception("Partitie incorecta!");
        }
        this.l1 = l1;
        this.l2 = l2;
        this.y = y;
        this.z = z;
        this.o=o;
    }

    @Override
    public void run() {
        inmultireVector();
    }

    private void inmultireVector()
    {
        for (int i = l1; i <= l2 ; i++) {
            T s=o.init();
            for(int j=0;j<y.length;j++)
            {
                s=o.add(s,o.mul(x[i][j],y[j]));
            }
            z[i]=s;
        }
    }
}
