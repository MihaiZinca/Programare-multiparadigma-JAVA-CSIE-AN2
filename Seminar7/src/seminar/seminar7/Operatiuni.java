package seminar.seminar7;

public interface Operatiuni<T> {
    T init();
    T add(T a,T b); //adunarea
    T mul(T a, T b); //inmultire
}
