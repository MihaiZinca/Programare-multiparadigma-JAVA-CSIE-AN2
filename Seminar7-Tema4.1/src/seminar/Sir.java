package seminar;

public class Sir<T extends Comparable<T>> {
    protected T[] v;
    public Sir(T[] v) {
        this.v = v;
    }
    public T[] getV() {
        return v;
    }

}
