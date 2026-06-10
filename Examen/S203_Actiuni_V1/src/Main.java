import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Actiune
{
    String simbol;
    String denumire;
    double ziua1;
    double ziua5;

    public Actiune() {
    }

    public Actiune(String simbol, String denumire, double ziua1, double ziua5) {
        this.simbol = simbol;
        this.denumire = denumire;
        this.ziua1 = ziua1;
        this.ziua5 = ziua5;
    }
}

class Tranzactie
{
    String data;
    String direction;
    String simbol;
    int quantity;
    double price;

    public Tranzactie() {
    }

    public Tranzactie(String data, String direction, String simbol, int quantity, double price) {
        this.data = data;
        this.direction = direction;
        this.simbol = simbol;
        this.quantity = quantity;
        this.price = price;
    }
}

class Raport
{
    String simbol;
    String denumire;
    int vandute;
    int cumparate;


    public Raport() {
    }

    public Raport(String simbol, String denumire, int vandute, int cumparate) {
        this.cumparate = cumparate;
        this.vandute = vandute;
        this.denumire = denumire;
        this.simbol = simbol;
    }
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Actiune> actiuni=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("S203_Actiuni.csv"));
        String linie;
        while((linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");

            String simbol=p[0].trim();
            String denumire=p[1].trim();
            double ziua1=Double.parseDouble(p[2].trim());
            double ziua5=Double.parseDouble(p[6].trim());

            Actiune a=new Actiune(simbol, denumire, ziua1, ziua5);
            actiuni.add(a);
        }
        br.close();

        System.out.println("========== CERINTA 1 ==========");
        Actiune max=actiuni.stream().max((a,b)->Double.compare(((a.ziua5-a.ziua1)*100)/a.ziua1,((b.ziua5-b.ziua1)*100)/b.ziua1)).get();
        double crestere = (max.ziua5 - max.ziua1) * 100 / max.ziua1;
        System.out.println(max.simbol+" - crestere "+crestere +"%");


        List<Tranzactie> tranzactii=new ArrayList<>();
        FileReader fr=new FileReader("S203_Tranzactii.json");
        JSONArray arr=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arr.length();i++)
        {
            JSONObject obj=arr.getJSONObject(i);

            String data=obj.getString("Date");
            String direction=obj.getString("Direction");
            String simbol=obj.getString("Symbol");
            int quantity=obj.getInt("Quantity");
            double price=obj.getDouble("Price");

            Tranzactie t=new Tranzactie(data,direction,simbol,quantity,price);
            tranzactii.add(t);
        }
        fr.close();

        tranzactii.sort((a,b)->Double.compare((b.price*b.quantity),(a.price*a.quantity)));
        System.out.println("========== CERINTA 2 ==========");
        int nr=0;
        for(Tranzactie t:tranzactii)
        {
            if(t.direction.equals("Buy") && nr<5)
            {
                nr++;
                double total=t.price*t.quantity;
                System.out.println(t.data+" "+t.direction+" "+t.simbol+" "+t.quantity+" X  "+t.price+" RON = "+total);
            }
        }


        System.out.println("========== CERINTA 3 ==========");
        actiuni.sort((a,b)->a.simbol.compareTo(b.simbol));
        List<Raport> rap=new ArrayList<>();
        for(Actiune a:actiuni)
        {
            int cumparate=0;
            int vandute=0;

            for(Tranzactie t:tranzactii)
            {
                if(t.simbol.equals(a.simbol))
                {
                    if(t.direction.equals("Buy"))
                        cumparate+=t.quantity;
                    else
                        vandute+=t.quantity;
                }
            }

            Raport r=new Raport(a.simbol,a.denumire,vandute,cumparate);
            rap.add(r);
        }

        BufferedWriter bw=new BufferedWriter(new FileWriter("raport.txt"));
        bw.write("Simbol, Denumire, Total Actiuni Vândute, Total Actiuni Cumpărate");
        bw.newLine();
        for(Raport r:rap)
        {
            bw.write(r.simbol+","+r.denumire+","+r.vandute+","+r.cumparate);
            bw.newLine();
        }
        bw.close();



    }
}
