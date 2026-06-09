import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

class Preturi
{
    String simbol;
    double deschidere;
    double max;
    double min;
    double inchidere;
    long volum;

    public Preturi() {
    }

    public Preturi(String simbol, double deschidere, double max, double min, double inchidere, long volum) {
        this.simbol = simbol;
        this.deschidere = deschidere;
        this.max = max;
        this.min = min;
        this.inchidere = inchidere;
        this.volum = volum;
    }

    @Override
    public String toString() {
        return simbol+" "+deschidere+" "+max+" "+min+" "+inchidere+" "+volum;
    }
}

class Titluri
{
    String simbol;
    String denumire;

    public Titluri() {
    }

    public Titluri(String simbol, String denumire) {
        this.simbol = simbol;
        this.denumire = denumire;
    }

    @Override
    public String toString() {
        return simbol+" "+denumire;
    }
}

public class Main {
    static void main(String[] args) throws Exception {

        List<Preturi> preturi = new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("PretVolum.txt"));
        String linie;
        br.readLine();
        while((linie=br.readLine())!=null)
        {
            String[] p= linie.split(",");

            String simbol=p[0].trim();
            double deschidere=Double.parseDouble(p[1].trim());
            double max=Double.parseDouble(p[2].trim());
            double min=Double.parseDouble(p[3].trim());
            double inchidere=Double.parseDouble(p[4].trim());
            long volum=Long.parseLong(p[5].trim());

            Preturi pr=new Preturi(simbol, deschidere, max, min, inchidere, volum);
            preturi.add(pr);
        }
        br.close();

        Preturi min=preturi.get(0);
        Preturi max=preturi.get(0);
        for(Preturi p:preturi)
        {
            double val=p.inchidere*p.deschidere;
            if(val<min.inchidere*min.deschidere)
                min=p;
            if(val>max.inchidere*max.deschidere)
                max=p;
        }

        System.out.println(min.simbol+" "+(min.inchidere*min.deschidere));
        System.out.println(max.simbol+" "+(max.inchidere*max.deschidere));

        List<Titluri> titluri = new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:Titluri.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select * from Titluri");
        while(rs.next())
        {
            String simbol=rs.getString("Simbol");
            String denumire=rs.getString("Denumire");

            Titluri t=new Titluri(simbol, denumire);
            titluri.add(t);
        }
        con.close();

        System.out.println("Cerinta 2:");
       preturi.sort((a,b)->Long.compare(b.volum,a.volum));
       for(Preturi p:preturi)
       {
           String denumire="";
           for(Titluri t:titluri)
           {
               if(t.simbol.equals(p.simbol))
               {
                   denumire=t.denumire;
                   break;
               }
           }
           System.out.println(p.simbol + " " +denumire + " " + p.volum);
       }

       List<Preturi> lista=new ArrayList<>();
       for(Preturi p:preturi) {
           double diff = p.max - p.min;
           if (diff > 0.01)
               lista.add(p);
       }


        System.out.println("Cerinta 3:");
       lista.sort((a,b)->Double.compare((b.max-b.min),(a.max-a.min)));
        for(Preturi p:lista)
        {
            String denumire="";
            for(Titluri t:titluri)
            {
                if(t.simbol.equals(p.simbol))
                {
                    denumire=t.denumire;
                    break;
                }
            }
            System.out.println(p.simbol + " " +denumire + " " + (p.max-p.min));
        }




    }
}
