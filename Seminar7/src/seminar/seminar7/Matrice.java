package seminar.seminar7;

public class Matrice<T>{
    protected T[][] x;

    public Matrice(T[][] x) {
        this.x = x;
    }

    public T[][] getX() {
        return x;
    }

}
