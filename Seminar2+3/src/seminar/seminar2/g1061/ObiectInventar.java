package seminar.seminar2.g1061;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ObiectInventar extends ElementPatrimonial{
    private String gestionar;

    public ObiectInventar() {
    }

    public ObiectInventar(String denumire, Long nrInventar, Double valoare, Date dataAchizitie, Locatie locatie, String gestionar) {
        super(denumire, nrInventar, valoare, dataAchizitie, locatie);
        this.gestionar = gestionar;
    }

    public ObiectInventar(Long nrInventar, String gestionar) {
        super(nrInventar);
        this.gestionar = gestionar;
    }

    public String getGestionar() {
        return gestionar;
    }

    public void setGestionar(String gestionar) {
        this.gestionar = gestionar;
    }

    @Override
    public String toString() {
        return  super.toString()+","+ gestionar ; //super cand o metoda este acoperita din superclasa
    }

    @Override
    public double calculUzura() {
        Date dataCurenta=new Date();
        long numarZileFunctionare= TimeUnit.MICROSECONDS.toDays(dataCurenta.getTime() - dataAchizitie.getTime());
        return numarZileFunctionare>365?1.0:0.0;
    }

    @Override
    public double amortizare() {
        return calculUzura() * valoare; //pt ca e protected
    }
}
