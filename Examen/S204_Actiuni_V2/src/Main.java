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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Actiuni
{
    String simbol;
    String denumire;
    double ziua5;

    public Actiuni() {
    }

    public Actiuni(String simbol, String denumire, double ziua5) {
        this.simbol = simbol;
        this.denumire = denumire;
        this.ziua5 = ziua5;
    }

    @Override
    public String toString() {
        return simbol + " " + denumire + " " + ziua5;
    }
}

class Client
{
    String date;
    String direction;
    String symbol;
    int quantity;
    double pret;

    public Client() {
    }

    public Client(String date, String direction, String symbol, int quantity, double pret) {
        this.date = date;
        this.direction = direction;
        this.symbol = symbol;
        this.quantity = quantity;
        this.pret = pret;
    }

    @Override
    public String toString() {
        return date + " " + direction + " " + symbol + " " + quantity + " " + pret;
    }
}

class Portofoliu
{
    String simbol;
    String denumire;
    int cantitate;
    double valoare;

    public Portofoliu() {
    }

    public Portofoliu(String simbol, String denumire, int cantitate, double valoare) {
        this.simbol = simbol;
        this.denumire = denumire;
        this.cantitate = cantitate;
        this.valoare = valoare;
    }

    @Override
    public String toString() {
        return simbol + " " + denumire + " " + cantitate + " " + valoare;
    }
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Actiuni>actiuni=new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:S204_Actiuni.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("select * from Actiuni");
        while(rs.next())
        {
            String simbol=rs.getString("Simbol");
            String denumire=rs.getString("Denumire");
            double ziua5=rs.getDouble("Ziua5");

            Actiuni a=new Actiuni(simbol, denumire, ziua5);
            actiuni.add(a);
        }
        con.close();

        actiuni.sort((a,b)->a.simbol.compareTo(b.simbol));
        System.out.println("========== CERINTA 1 ==========");
        for(Actiuni a:actiuni)
        {
            System.out.println(a.simbol+", "+a.denumire+"      , "+a.ziua5 +"RON");
        }

        List<Client> clienti=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("S204_Tranzactii.csv"));
        String linie;
        while( (linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");

            String date=p[0].trim();
            String direction=p[1].trim();
            String symbol=p[2].trim();
            int quantity=Integer.parseInt(p[3].trim());
            double pret=Double.parseDouble(p[4].trim());

            Client c=new Client(date,direction,symbol,quantity,pret);
            clienti.add(c);
        }
        br.close();

        double cumparare=0;
        double vanzare=0;
        for(Client c:clienti)
        {
            if(c.direction.equals("Buy"))
            {
                cumparare+=(c.pret*c.quantity);
            }
            if(c.direction.equals("Sell"))
            {
                vanzare+=(c.pret*c.quantity);
            }
        }
        System.out.println("========== CERINTA 2 ==========");
        System.out.println("Total cumparare: "+cumparare+" RON");
        System.out.println("Total vanzare: "+vanzare+" RON");



        Map<String,Integer>mapCantitati=new HashMap<>();
        for(Client c:clienti)
        {
            int val=mapCantitati.getOrDefault(c.symbol,0);
            if(c.direction.equals("Buy"))
                val+=c.quantity;
            else
                val-=c.quantity;

            mapCantitati.put(c.symbol,val);
        }
        List<String>cantitati=new ArrayList<>(mapCantitati.keySet());

        Map<String,Actiuni> mapActiuni=new HashMap<>();
        for(Actiuni a:actiuni)
        {
            mapActiuni.put(a.simbol,a);
        }

        List<Portofoliu> portofoliu=new ArrayList<>();
        for(String simbol:cantitati)
        {
            int cant=mapCantitati.get(simbol);
            Actiuni act=mapActiuni.get(simbol);

            if(act!=null)
            {
                double valoare=cant*act.ziua5;

                Portofoliu p=new Portofoliu(simbol,act.denumire,cant,valoare);
                portofoliu.add(p);
            }
        }

        portofoliu.sort((a,b)->Double.compare(b.valoare,a.valoare));
        System.out.println("========== CERINTA 3 ==========");
        JSONArray arr=new JSONArray();
        for(Portofoliu p:portofoliu)
        {
            JSONObject obj=new JSONObject();
            obj.put("Simbol",p.simbol);
            obj.put("Valoare",p.valoare);
            obj.put("Denumire",p.denumire);
            obj.put("Canitate",p.cantitate);
            obj.put("Valoare",p.valoare);

            arr.put(obj);
        }
        FileWriter fw=new FileWriter("output.json");
        fw.write(arr.toString(2));
        fw.close();






    }
}
