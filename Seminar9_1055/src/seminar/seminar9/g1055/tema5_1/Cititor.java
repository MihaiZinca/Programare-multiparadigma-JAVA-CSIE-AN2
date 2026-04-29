package seminar.seminar9.g1055.tema5_1;

import java.io.BufferedReader;
import java.io.FileReader;

public class Cititor extends Thread{
    private final String numeFisier;
    private final Catalog catalog;

    public Cititor(String numeFisier, Catalog catalog) {
        this.numeFisier = numeFisier;
        this.catalog = catalog;
    }

    public String getNumeFisier() {
        return numeFisier;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    @Override
    public void run() {
        try(BufferedReader in = new BufferedReader(new FileReader(numeFisier))) {
            in.lines().forEach(linie->{
                String[] t = linie.trim().split(",");
                catalog.scrieNota(
                        Integer.parseInt(t[0].trim()),
                        numeFisier.substring(0,numeFisier.indexOf(".csv")),
                        Integer.parseInt(t[1].trim())
                        );
            });
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }
}
