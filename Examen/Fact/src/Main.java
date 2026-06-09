import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.AnnotatedParameterizedType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

class Factura
{
    String denumire;
    String repartizare;
    double valoare;

    public Factura() {
    }

    public Factura(String denumire, String repartizare, double valoare) {
        this.denumire = denumire;
        this.repartizare = repartizare;
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return denumire + " " + repartizare + " " + valoare;
    }
}

class Apartament
{
    int numarApartament;
    int suprafata;
    int numarPersoane;

    public Apartament() {
    }

    public Apartament(int numarApartament, int suprafata, int numarPersoane) {
        this.numarApartament = numarApartament;
        this.suprafata = suprafata;
        this.numarPersoane = numarPersoane;
    }

    @Override
    public String toString() {
        return numarApartament + " " + suprafata + " " + numarPersoane;
    }
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Factura> facturi=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("facturi.txt"));
        String linie;
        while((linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");
            Factura f=new Factura();
            f.denumire=p[0].trim();
            f.repartizare=p[1].trim();
            f.valoare=Double.parseDouble(p[2].trim());

            facturi.add(f);
        }
        br.close();

       Factura max=facturi.stream().max(Comparator.comparingDouble(f->f.valoare)).get();
       System.out.println(max.denumire+" "+max.valoare);

       List<Apartament> apartamente=new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:Apartamente.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM Apartamente WHERE NumarPersoane>=2");
        while(rs.next())
        {
            int numarApartament=rs.getInt("numarApartament");
            int suprafata=rs.getInt("suprafata");
            int numarPersoane=rs.getInt("numarPersoane");

            Apartament a=new Apartament(numarApartament,suprafata,numarPersoane);
            apartamente.add(a);
        }


        apartamente.sort((a,b)->Integer.compare(b.suprafata,a.suprafata));
        System.out.println("Apartamente sortate desc dupa suprafata:");
        for(Apartament a:apartamente)
        {
            System.out.println(a);
        }

        Map<String,Integer> nrMap=new HashMap<>();
        Map<String,Double> sumMap=new HashMap<>();

        for(Factura f:facturi)
        {
            nrMap.put(f.repartizare,nrMap.getOrDefault(f.repartizare,0)+1);
            sumMap.put(f.repartizare,sumMap.getOrDefault(f.repartizare,0.0)+f.valoare);

        }

        List<String> lista=new ArrayList<>(sumMap.keySet());
        lista.sort((a,b)->Double.compare(sumMap.get(b),sumMap.get(a)));

        BufferedWriter bw=new BufferedWriter(new FileWriter("sumare.txt"));
        bw.write("Tip repartizare, Total facturi, Numar Facturi");
        for(String s:lista)
        {
            bw.write(s+","+sumMap.get(s)+","+nrMap.get(s));
            bw.newLine();
        }
        bw.close();









    }
}
