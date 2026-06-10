import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class Specializare
{
    int cod;
    String denumire;
    int locuri;

    public Specializare(int cod, String denumire, int locuri) {
        this.cod = cod;
        this.denumire = denumire;
        this.locuri = locuri;
    }

    public Specializare() {
    }
}

class Candidat
{
    Long cnp;
    String nume;
    double notaBac;
    int codSpecializare;

    public Candidat(Long cnp, String nume, double notaBac, int codSpecializare) {
        this.cnp = cnp;
        this.nume = nume;
        this.notaBac = notaBac;
        this.codSpecializare = codSpecializare;
    }

    public Candidat() {
    }
}


class Raport
{
    int codSpecializar;
    String denumire;
    int nrInscrieri;
    double medie;

    public Raport() {
    }

    public Raport(int codSpecializar, String denumire, int nrInscrieri, double medie) {
        this.codSpecializar = codSpecializar;
        this.denumire = denumire;
        this.nrInscrieri = nrInscrieri;
        this.medie = medie;
    }

    @Override
    public String toString() {
        return codSpecializar+" "+denumire+" "+nrInscrieri+" "+medie;
    }
}
public class Main {
    static void main(String[] args)throws Exception {

        List<Specializare> specializari = new ArrayList<>();

        Connection con= DriverManager.getConnection("jdbc:sqlite:facultate.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select * from specializari");
        while(rs.next())
        {
            int cod=rs.getInt("cod");
            String denumire=rs.getString("denumire");
            int locuri=rs.getInt("locuri");

            Specializare s=new Specializare(cod,denumire,locuri);
            specializari.add(s);
        }
        con.close();

        int total=0;
        for(Specializare s:specializari)
        {
            total+=s.locuri;
        }
        System.out.println("Numarul total de locuri: "+total);

        List<Candidat> candidati=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("inscrieri.txt"));
        String linie;
        while((linie=br.readLine())!=null)
        {
            String[] p= linie.split(",");

            long cnp=Long.parseLong(p[0].trim());
            String nume=p[1].trim();
            double notaBac=Double.parseDouble(p[2].trim());
            int codSpecializare=Integer.parseInt(p[3].trim());

            Candidat c=new Candidat(cnp,nume,notaBac,codSpecializare);
            candidati.add(c);
        }
        br.close();

        System.out.println("\nCerinta 2:\n");
        for(Specializare s:specializari)
        {
            int nrLocuri=s.locuri;
            for(Candidat c:candidati)
            {
                if(c.codSpecializare==s.cod)
                {
                    nrLocuri=nrLocuri-1;
                }
            }

            if(nrLocuri>=100)
            {
                System.out.println(s.cod+" "+s.denumire+" "+nrLocuri+" disponibile");
            }
        }

        List<Raport> raport=new ArrayList<>();
        for(Specializare s:specializari)
        {
            int nrInscrieri=0;
            double suma=0;
            double medie=0;
            for(Candidat c:candidati)
            {
                if(c.codSpecializare==s.cod)
                {
                    nrInscrieri++;
                    suma+=c.notaBac;
                }
            }

            if(nrInscrieri>0)
                medie=suma/nrInscrieri;

           Raport r=new Raport(s.cod,s.denumire,nrInscrieri,medie);
           raport.add(r);
        }

        JSONArray arry=new JSONArray();
        for(Raport r:raport)
        {
            JSONObject obj=new JSONObject();
            obj.put("cod_specializare",r.codSpecializar);
            obj.put("denumire",r.denumire);
            obj.put("numar_inscrieri",r.nrInscrieri);
            obj.put("medie",r.medie);

            arry.put(obj);
        }
        FileWriter fw=new FileWriter("inscreri_specializari.json");
        fw.write(arry.toString(2));
        fw.close();


    }
}
