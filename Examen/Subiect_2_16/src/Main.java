import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.SocketTimeoutException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Sectie
{
    int codSectie;
    String denumire;
    int nrLocuri;

    public Sectie() {
    }

    public Sectie(int codSectie, String denumire, int nrLocuri) {
        this.codSectie = codSectie;
        this.denumire = denumire;
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return codSectie+" "+denumire+" "+nrLocuri;
    }
}

class Pacient
{
    long cnp;
    String nume;
    int varsta;
    int codSec;

    public Pacient() {
    }

    public Pacient(long cnp, String nume, int varsta, int codSec) {
        this.cnp = cnp;
        this.nume = nume;
        this.varsta = varsta;
        this.codSec = codSec;
    }

    @Override
    public String toString() {
        return cnp+" "+nume+" "+varsta+" "+codSec;
    }
}

class Raport
{
    int codSectie;
    String denumire;
    int nrLocuri;
    double varstaMedie;

    public Raport(int codSectie, String denumire, int nrLocuri, double varstaMedie) {
        this.codSectie = codSectie;
        this.denumire = denumire;
        this.nrLocuri = nrLocuri;
        this.varstaMedie = varstaMedie;
    }

    public Raport() {
    }
}

public class Main {
    static void main(String[] args)throws Exception{

        List<Sectie> sectii=new ArrayList<>();
        FileReader fr=new FileReader("sectii.json");
        JSONArray array=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<array.length();i++)
        {
            JSONObject obj=array.getJSONObject(i);
            Sectie s=new Sectie();
            s.codSectie= obj.getInt("cod_sectie");
            s.denumire=obj.getString("denumire");
            s.nrLocuri=obj.getInt("numar_locuri");
            sectii.add(s);
        }
        fr.close();

        for(Sectie s:sectii)
        {
            if(s.nrLocuri>5)
                System.out.println(s.denumire+"-"+s.nrLocuri);
        }

        List<Pacient> pacienti=new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:spital.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM Pacienti");
        while(rs.next())
        {
            Pacient pa=new Pacient();
            pa.cnp=rs.getLong("CNP");
            pa.nume=rs.getString("NumePacient");
            pa.varsta=rs.getInt("VarstaPacient");
            pa.codSec=rs.getInt("CodSectie");

            pacienti.add(pa);
        }
        con.close();

        for(Sectie s:sectii)
        {
            String denumire="";
            int nr=0;
            for(Pacient p:pacienti)
            {
                if(p.codSec==s.codSectie)
                {
                    nr++;
                    denumire=s.denumire;
                }
            }
            System.out.println(s.codSectie+" "+denumire+" "+nr);
        }

        List<Raport> raport=new ArrayList<>();
        for(Sectie s:sectii)
        {
            int sum=0;
            int nr=0;
            for(Pacient p:pacienti)
            {
                if(p.codSec==s.codSectie)
                {
                    sum+=p.varsta;
                    nr++;
                }
            }
            double medie=0;
            if(nr>0)
                medie=(double)sum/nr;

            Raport r=new Raport(s.codSectie,s.denumire,s.nrLocuri,medie);
            raport.add(r);
        }
        raport.sort((a,b)->Double.compare(b.varstaMedie,a.varstaMedie));

        BufferedWriter bw=new BufferedWriter(new FileWriter("sit.txt"));
        bw.write("Cod sectie, Denumire sectie, Numar locuri, Varsta medie");
        bw.newLine();
        for(Raport ra:raport)
        {
            bw.write(ra.codSectie+" "+ra.denumire+" "+ra.nrLocuri+" "+ra.varstaMedie);
            bw.newLine();

        }
        bw.close();





    }
}
