import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.AccessFlag;
import java.security.spec.RSAOtherPrimeInfo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Persoana
{
    int cod;
    String cnp;
    String nume;

    public Persoana() {
    }

    public Persoana(int cod, String cnp, String nume) {
        this.cod = cod;
        this.cnp = cnp;
        this.nume = nume;
    }

}

class Client
{
    int codPersoana;
    String simbol;
    String tip;
    int cantitate;
    double pret;

    public Client(int codPersoana, String simbol, String tip, int cantitate, double pret) {
        this.codPersoana = codPersoana;
        this.simbol = simbol;
        this.tip = tip;
        this.cantitate = cantitate;
        this.pret = pret;
    }

    public Client() {
    }
}

class Portofoliu
{
    String numeP;
    String simbol;
    int nrActiuni;

    public Portofoliu() {
    }

    public Portofoliu(String numeP, String simbol, int nrActiuni) {
        this.numeP = numeP;
        this.simbol = simbol;
        this.nrActiuni = nrActiuni;
    }
}




public class Main {
    static void main(String[] args) throws Exception {

        List<Persoana> persoane = new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:bursa.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM Persoane");
        while (rs.next())
        {
            int cod=rs.getInt("cod");
            String cnp=rs.getString("cnp");
            String nume=rs.getString("nume");

            Persoana p=new Persoana(cod,cnp,nume);
            persoane.add(p);
        }
        con.close();

        int nr=0;
        for(Persoana p:persoane)
        {
            if(p.cnp.charAt(0)=='8'|| p.cnp.charAt(0)=='9')
                nr++;
        }
        System.out.println("Numar nerezidenti: "+nr);


        List<Client> clienti = new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("date\\bursa_tranzactii.txt"));
        String linie;
        while((linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");
            int cod=Integer.parseInt(p[0].trim());
            String simbol=p[1].trim();
            String tip=p[2].trim();
            int cantitate=Integer.parseInt(p[3].trim());
            double pret=Double.parseDouble(p[4].trim());

            Client c=new Client(cod,simbol,tip,cantitate,pret);
            clienti.add(c);
        }
        br.close();

        Map<String,Integer> map=new HashMap<>();
        for(Client c:clienti)
        {
            map.put(c.simbol,map.getOrDefault(c.simbol,0)+1);
        }

        List<String> lista=new ArrayList<>(map.keySet());
        lista.sort((a,b)->Integer.compare(map.get(a),map.get(b)));

        for(String s:lista)
        {
            System.out.println(s+" -> "+ map.get(s)+" tranzactii");
        }

        BufferedWriter bw=new BufferedWriter(new FileWriter("date\\simboluri.txt"));
        for(String s:lista)
        {
            bw.write(s);
            bw.newLine();
        }
        bw.close();


        Map<String,Integer> mapCantitati=new HashMap<>();
        for(Client c:clienti)
        {
            String cheie=c.codPersoana+"-"+c.simbol;
            int val=mapCantitati.getOrDefault(cheie,0);
            if(c.tip.equals("cumparare"))
                val+=c.cantitate;
            else
                val-=c.cantitate;

            mapCantitati.put(cheie,val);
        }
        List<String> port=new ArrayList<>(mapCantitati.keySet());

        for(Persoana p:persoane)
        {
            System.out.println(p.nume);
            for(String s:port)
            {
                String[] pa=s.split("-");
                int cod=Integer.parseInt(pa[0].trim());
                String simbol=pa[1].trim();

                if(cod==p.cod)
                {
                    int total=mapCantitati.get(s);
                    System.out.println(simbol+"-"+total);
                }
            }
        }





    }
}
