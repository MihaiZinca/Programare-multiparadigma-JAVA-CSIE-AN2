package seminar.seminar2.g1061;

//poate cotine cel putin o metoda abstracta, sablon de descriere partiala a unei realitatii

import javax.swing.text.Element;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public abstract class ElementPatrimonial implements Amortizare, Comparable<ElementPatrimonial>, Serializable {

    protected String denumire; // obiectele String stau in zona de momerie ca niste constat.pour
    protected Long nrInventar; // memorata pe stiva
    protected Double valoare;  //TREBUIA double inloc de Double(e o clasa gen)
    protected Date dataAchizitie;
    protected Locatie locatie;

    public ElementPatrimonial() {
    }

    public ElementPatrimonial(String denumire, Long nrInventar, Double valoare, Date dataAchizitie, Locatie locatie) {
        this.denumire = denumire;
        this.nrInventar = nrInventar;
        this.valoare = valoare;
        this.dataAchizitie = dataAchizitie;
        this.locatie = locatie;
    }

    public ElementPatrimonial(long nrInventar) {
        this.nrInventar = nrInventar;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Long getNrInventar() {
        return nrInventar;
    }

    public void setNrInventar(long nrInventar) {
        this.nrInventar = nrInventar;
    }

    public Double getValoare() {
        return valoare;
    }

    public void setValoare(Double valoare) {
        this.valoare = valoare;
    }

    public Date getDataAchizitie() {
        return dataAchizitie;
    }

    public void setDataAchizitie(Date dataAchizitie) {
        this.dataAchizitie = dataAchizitie;
    }

    public Locatie getLocatie() {
        return locatie;
    }

    public void setLocatie(Locatie locatie) {
        this.locatie = locatie;
    }

    @Override
    public String toString() {
        return "{"+denumire+","+nrInventar+","+valoare+","
                +(dataAchizitie==null?"":Main.fmt.format(dataAchizitie))+
                "\n"+locatie+"}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ElementPatrimonial that = (ElementPatrimonial) o;
        return nrInventar == that.nrInventar;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nrInventar);
    }

    //returneaza (-1,0,1) -1 - <    0-=   1 - >
    @Override
    public int compareTo(ElementPatrimonial elementPatrimonial) {
        return dataAchizitie.compareTo(elementPatrimonial.dataAchizitie);
    }
}


