package seminar.seminar8.g1061;

public class PartitieMatrice<T> extends Thread {

    private T[][] x;
    private int k1,k2;
    private T[] s;
    private String tip;
    private Operatiuni<T> operatiuni;

    public PartitieMatrice(T[][] x, int k1, int k2, T[] s,
                           String tip, Operatiuni<T> operatiuni) {
        this.x = x;
        this.k1 = k1;
        this.k2 = k2;
        this.s = s;
        this.tip = tip;
        this.operatiuni = operatiuni;
    }

    public T[][] getX() {
        return x;
    }

    public int getK1() {
        return k1;
    }

    public int getK2() {
        return k2;
    }

    public T[] getS() {
        return s;
    }

    public String getTip() {
        return tip;
    }

    public Operatiuni<T> getOperatiuni() {
        return operatiuni;
    }

    @Override
    public void run() {
        calcul();
    }

    public void calcul(){
        if (tip.equalsIgnoreCase("l")){
            for (int i = k1; i <= k2; i++) {
                s[i] = operatiuni.init();
                for (int j = 0; j < x[i].length; j++) {
                    s[i] = operatiuni.add(s[i],x[i][j]);
                }
            }
        } else {
            for (int j = k1; j <= k2; j++) {
                s[j] = operatiuni.init();
                for (int i = 0; i < x.length; i++) {
                    s[j] = operatiuni.add(s[j],x[i][j]);
                }
            }
        }
    }
}
