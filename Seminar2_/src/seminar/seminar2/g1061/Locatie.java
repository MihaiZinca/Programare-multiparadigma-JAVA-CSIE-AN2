package seminar.seminar2.g1061;

import java.io.Serializable;

public class Locatie implements Cloneable, Serializable {

    private String denumire;
    private Adresa adresa;

    public Locatie() {
    }

    public Locatie(String denumire, Adresa adresa) {
        this.denumire = denumire;
        this.adresa = adresa;
    }

    public Locatie(String denumire) {
        this.denumire = denumire;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Adresa getAdresa() {
        return adresa;
    }

    public void setAdresa(Adresa adresa) {
        this.adresa = adresa;
    }

    @Override
    public String toString() {
        return "{"+denumire+", "+adresa+"}";
    }

    @Override
    public Object clone() throws CloneNotSupportedException { //aici facem deepcopy pt ca avem Adresa - adica clona profunda
        Locatie clona=(Locatie)super.clone();
        if(adresa!=null)
            clona.setAdresa((Adresa) adresa.clone());
        return clona;
    }
}
