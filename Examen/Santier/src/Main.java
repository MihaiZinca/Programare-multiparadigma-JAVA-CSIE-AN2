import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Santier
{
    int codSantier;
    String localitate;
    String strada;
    String obiectiv;
    double valoare;

    public Santier(int codSantier, String localitate, String strada, String obiectiv, double valoare) {
        this.codSantier = codSantier;
        this.localitate = localitate;
        this.strada = strada;
        this.obiectiv = obiectiv;
        this.valoare = valoare;
    }

    public Santier() {
    }
}

class Cheltuiala
{
    int codCapitol;
    int codSantier;
    String denumire;
    String unitate;
    double cantitate;
    double pretUnitar;

    public Cheltuiala() {
    }

    public Cheltuiala(int codCapitol, int codSantier, String denumire, String unitate, double cantitate, double pretUnitar) {
        this.codCapitol = codCapitol;
        this.codSantier = codSantier;
        this.denumire = denumire;
        this.unitate = unitate;
        this.cantitate = cantitate;
        this.pretUnitar = pretUnitar;
    }
}

class Raport
{
    int codCapitol;
    int codSantier;
    double valoare;
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Santier> santiere = new ArrayList<>();
        FileReader fr = new FileReader("santiere.json");
        JSONArray arr = new JSONArray(new JSONTokener(fr));
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Santier s = new Santier();
            s.codSantier = obj.getInt("Cod Santier");
            s.localitate = obj.getString("Localitate");
            s.strada = obj.getString("Strada");
            s.obiectiv = obj.getString("Obiectiv");
            s.valoare = obj.getDouble("Valoare");

            santiere.add(s);
        }
        fr.close();

        System.out.println("Cerinta 1:\n");
        double sum = 0;
        for (Santier s : santiere) {
            System.out.println(s.codSantier + " " + s.codSantier + " " + s.strada + " " + s.obiectiv + " " + s.valoare);
            sum = sum + s.valoare;
        }
        System.out.println("Valoare medie a obiectivelor: " + sum / santiere.size());

        List<Cheltuiala> cheltuieli = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader("cheltuieli.txt"));
        String linie;
        while ((linie = br.readLine()) != null) {
            String[] p = linie.split(",");
            int codCapitol = Integer.parseInt(p[0]);
            int codSantier = Integer.parseInt(p[1]);
            String denumire = p[2];
            String unitate = p[3];
            double cantitate = Double.parseDouble(p[4]);
            double pretUnitar = Double.parseDouble(p[5]);

            Cheltuiala c = new Cheltuiala(codCapitol, codSantier, denumire, unitate, cantitate, pretUnitar);
            cheltuieli.add(c);
        }
        br.close();

        Map<Integer, Double> map = new HashMap<>();
        for (Cheltuiala c : cheltuieli) {
            map.put(c.codCapitol, map.getOrDefault(c.codCapitol, 0.0) + c.cantitate);
        }

        List<Integer> cantitati = new ArrayList<>(map.keySet());
        System.out.println("Cerinta 2\n");
        System.out.println("Cod capitol,cantitate");
        for (Integer cod : cantitati) {
            System.out.println(cod + " " + map.get(cod));
        }




    }
}
