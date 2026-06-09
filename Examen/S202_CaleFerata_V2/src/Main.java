import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class Vagon
{
    int cod;
    String text;
    int capacitate;

    public Vagon() {
    }

    public Vagon(int cod, String text, int capacitate) {
        this.cod = cod;
        this.text = text;
        this.capacitate = capacitate;
    }

    @Override
    public String toString() {
        return cod+" "+text+" "+capacitate;
    }
}

class Tren
{
    int codTren;
    String tip;
    List<Integer> vagoane;

    public Tren() {
    }

    public Tren(int codTren, String tip, List<Integer> vagoane) {
        this.codTren = codTren;
        this.tip = tip;
        this.vagoane = vagoane;
    }
}

public class Main {
    static void main(String[] args)throws Exception {

        List<Vagon> vagoane = new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("S202_Vagoane.csv"));
        String linie;
        while( (linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");

            int cod=Integer.parseInt(p[0].trim());
            String text=p[1].trim();
            int capacitate=Integer.parseInt(p[2].trim());

             Vagon vag=new Vagon(cod,text,capacitate);
             vagoane.add(vag);
        }
        br.close();

        Map<String,Integer> map=new HashMap<>();
        for(Vagon vag:vagoane)
        {
            map.put(vag.text,map.getOrDefault(vag.text,0)+vag.capacitate);
        }

        System.out.println("========== CERINTA 1 ==========");
        List<String> lista=new ArrayList<>(map.keySet());
        for(String s:lista)
        {
            System.out.println(s+"-"+map.get(s));
        }

        List<Tren> trenuri=new ArrayList<>();
        FileReader fr=new FileReader("S202_Trenuri.json");
        JSONArray arry=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arry.length();i++)
        {
            JSONObject obj=arry.getJSONObject(i);
            int codT=obj.getInt("CodTren");
            String tip=obj.getString("TipLocomotiva");

            List<Integer>listaVag=new ArrayList<>();
            JSONArray lv=obj.getJSONArray("Vagoane");
            for(int j=0;j<lv.length();j++)
            {
                listaVag.add(lv.getInt(j));
            }
            Tren t=new Tren(codT,tip,listaVag);
            trenuri.add(t);
        }
        fr.close();

        trenuri.sort((a,b)->Integer.compare(a.codTren,b.codTren));
        System.out.println("========== CERINTA 2 ==========");
        for(Tren tr:trenuri)
        {
            System.out.println(tr.codTren+" "+tr.tip+" "+tr.vagoane.size());
        }


        Scanner sc=new Scanner(System.in);
        System.out.println("Introduceti tipul vagonului:");
        String tipC=sc.nextLine();

        System.out.println("========== CERINTA 3 ==========");
        System.out.println("Tipp: "+tipC);

        for(Tren tr:trenuri)
        {
            int suma=0;
            for(int codV:tr.vagoane)
            {
                for(Vagon v:vagoane)
                {
                    if(v.cod==codV && v.text.equals(tipC.trim()))
                    {
                        suma+=v.capacitate;
                        
                    }
                }
            }

            if(suma>0)
            {
                System.out.println(tr.codTren+" "+tr.tip+" "+suma+" tone - "+tipC);
            }
        }

    }
}
