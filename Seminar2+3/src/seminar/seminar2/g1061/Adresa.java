package seminar.seminar2.g1061;

public class Adresa implements Cloneable{
    private String judet, localitate, strada, numar;

    public Adresa() {
    }

    public Adresa(String judet, String localitate, String strada, String numar) {
        this.judet = judet;
        this.localitate = localitate;
        this.strada = strada;
        this.numar = numar;
    }

    public String getJudet() {
        return judet;
    }

    public void setJudet(String judet) {
        this.judet = judet;
    }

    public String getLocalitate() {
        return localitate;
    }

    public void setLocalitate(String localitate) {
        this.localitate = localitate;
    }

    public String getStrada() {
        return strada;
    }

    public void setStrada(String strada) {
        this.strada = strada;
    }

    public String getNumar() {
        return numar;
    }

    public void setNumar(String numar) {
        this.numar = numar;
    }

    @Override
    public String toString() {
        return "{"+judet+","+localitate+","+strada+","+numar+"}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //este suficienta clona superficiala deoarece nu am referinte memorate in alta parte
    }
}
