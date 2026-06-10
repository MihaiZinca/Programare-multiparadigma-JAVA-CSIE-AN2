import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Articol
 {
     int cod;
     String nume;
     String institutie;

     public Articol() {
     }

     public Articol(int cod, String nume, String institutie) {
         this.cod = cod;
         this.nume = nume;
         this.institutie = institutie;
     }
 }

 class Evaluare
 {
     int codEvaluator;
     int codArticol;
     int nivel;
     int notorietate;
     int citari;

     public Evaluare() {
     }

     public Evaluare(int codEvaluator, int codArticol, int nivel, int notorietate, int citari) {
         this.codEvaluator = codEvaluator;
         this.codArticol = codArticol;
         this.nivel = nivel;
         this.notorietate = notorietate;
         this.citari = citari;
     }
 }

 class Raport
 {
     int codA;
     String numeA;
     int punctaj;

     public Raport() {
     }

     public Raport(int codA, String numeA, int punctaj) {
         this.codA = codA;
         this.numeA = numeA;
         this.punctaj = punctaj;
     }
 }


public class Main {
    static void main(String[] args)throws Exception {

        List<Articol> articole=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("articole.txt"));
        String linie;
        while((linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");
            int cod=Integer.parseInt(p[0].trim());
            String nume=p[1].trim();
            String institutie=p[2].trim();

            Articol a=new Articol(cod,nume,institutie);
            articole.add(a);
        }
        br.close();
        System.out.println("Numarul de articole primite spre evaluare: "+articole.size());


        List<Evaluare> evaluari=new  ArrayList<>();
        FileReader fr=new FileReader("evaluari.json");
        JSONArray arry=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arry.length();i++)
        {
            JSONObject obj=arry.getJSONObject(i);
            int codEv=obj.getInt("Cod evaluator");
            int codA=obj.getInt("Cod articol");
            int nivel=obj.getInt("Nivel stiintific");
            int notorietate=obj.getInt("Notorietate");
            int citari=obj.getInt("Citari");

            Evaluare e=new Evaluare(codEv, codA, nivel, notorietate, citari);
            evaluari.add(e);
        }
        fr.close();

        Map<Integer,Integer> map=new HashMap<>();
        for(Evaluare e:evaluari)
        {
            map.put(e.codArticol,map.getOrDefault(e.codArticol,0)+1);
        }

        List<Integer> lista=new ArrayList<>(map.keySet());
        for(Integer ev:lista)
        {
            System.out.println("Cod:"+ev+" "+"NrEvaluari:"+map.get(ev));
        }


        List<Raport> rap=new  ArrayList<>();
        for(Articol a:articole)
        {
            int punctaj=0;
            int medie=0;
            int nr=0;
            for(Evaluare e:evaluari)
            {
                if(a.cod==e.codArticol)
                {
                    nr++;
                    punctaj+=e.nivel+e.notorietate+e.citari;
                }
            }

            if(nr>0)
                medie=punctaj/nr;

            Raport r=new Raport(a.cod,a.nume,medie);
            rap.add(r);
        }

        rap.sort((a,b)->Integer.compare(b.punctaj,a.punctaj));
        BufferedWriter bw=new BufferedWriter(new FileWriter("jurnal.txt"));
        for(Raport r:rap)
        {
            bw.write(r.codA+" "+r.numeA+" "+r.punctaj+"\n");
        }
        bw.close();

    }
}
