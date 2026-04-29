package seminar9.seminar;

public interface Generator<T> {
    T init();
    T next(T a);
}
