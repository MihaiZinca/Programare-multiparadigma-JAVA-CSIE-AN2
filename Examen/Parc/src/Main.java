import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Formattable;
import java.util.List;

class Activitate
{
    int cod;
    String denumire;
    double tarif;
    int locuri;

    public Activitate(int cod, String denumire, double tarif, int locuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.tarif = tarif;
        this.locuri = locuri;
    }

    public Activitate() {
    }
}

class Rezervare
{
    int id;
    int codA;
    int locuriRezervate;

    public Rezervare(int id, int codA, int locuriRezervate) {
        this.id = id;
        this.codA = codA;
        this.locuriRezervate = locuriRezervate;
    }

    public Rezervare() {
    }
}

class Raport
{
    String denumireAventura;
    int nrLocuriRezervate;
    double valoare;

    public Raport(String denumireAventura, int nrLocuriRezervate, double valoare) {
        this.denumireAventura = denumireAventura;
        this.nrLocuriRezervate = nrLocuriRezervate;
        this.valoare = valoare;
    }

    public Raport() {
    }
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Activitate> activitati=new ArrayList<>();
        FileReader fr=new FileReader("aventuri.json");
        JSONArray arr=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arr.length();i++)
        {
            JSONObject obj=arr.getJSONObject(i);
            Activitate a=new Activitate();
            a.cod=obj.getInt("cod_aventura");
            a.denumire=obj.getString("denumire");
            a.tarif=obj.getDouble("tarif");
            a.locuri=obj.getInt("locuri_disponibile");

            activitati.add(a);
        }
        fr.close();

        System.out.println("\nCerinta 1:\n");
        List<Activitate> lista= activitati.stream().filter(a->a.locuri>=20).toList();
        for(Activitate a:lista)
        {
            System.out.println(a.cod+" "+a.locuri);
        }

        List<Rezervare> rezervari=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("rezervari.txt"));
        String linie;
        while( (linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");
            int idRez=Integer.parseInt(p[0].trim());
            int codAventura=Integer.parseInt(p[1].trim());
            int locuriRezervate=Integer.parseInt(p[2].trim());

            Rezervare r=new Rezervare(idRez,codAventura,locuriRezervate);
            rezervari.add(r);
        }
        br.close();

        System.out.println("\nCerinta 2:\n");
        for(Activitate a:activitati)
        {
            int nrLocuri=a.locuri;
            for(Rezervare r:rezervari) {
                if (r.codA == a.cod)
                {
                    nrLocuri=nrLocuri-r.locuriRezervate;
                }
            }
            if(nrLocuri>=5)
            {
                System.out.println(a.cod+" "+a.denumire+" "+nrLocuri);
            }
        }

        List<Raport> raport=new ArrayList<>();
        for(Activitate a:activitati)
        {
            int nrLocuri=a.locuri;
            for(Rezervare r:rezervari)
            {
                if(r.codA == a.cod)
                {
                    nrLocuri=nrLocuri-r.locuriRezervate;
                }
            }

            int rezervate=a.locuri-nrLocuri;
            double valoare=rezervate*a.tarif;
            Raport r=new Raport(a.denumire,rezervate,valoare);
            raport.add(r);
        }

        raport.sort((a,b)->a.denumireAventura.compareTo(b.denumireAventura));
        System.out.println("\nCerinta 3:\n");
        BufferedWriter bw=new BufferedWriter(new FileWriter("venituri.txt"));
        bw.write("Denumire_aventura, Numar_locuri_rezervate, valoare\n");
        for(Raport r:raport)
        {
            bw.write(r.denumireAventura+" "+r.nrLocuriRezervate+" "+r.valoare+"\n");
        }
        bw.close();



    }
}
