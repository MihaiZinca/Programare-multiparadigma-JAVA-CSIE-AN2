import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class MateriePrima
{
    int cod;
    String denumire;
    Double cantitate;
    double pret_unitar;
    String unitate_masura;

    public MateriePrima() {
    }

    public MateriePrima(Double cantitate, int cod, String denumire, double pret_unitar, String unitate_masura) {
        this.cantitate = cantitate;
        this.cod = cod;
        this.denumire = denumire;
        this.pret_unitar = pret_unitar;
        this.unitate_masura = unitate_masura;
    }
}

class Consum
{
    int codMaterie;
    Double cantitate;

    public Consum() {
    }

    public Consum(Double cantitate, int codMaterie) {
        this.cantitate = cantitate;
        this.codMaterie = codMaterie;
    }
}

class Produs
{
    int codProdus;
    String numeProdus;
    List<Consum> consumuri;
    double cantitate;
    String unitateM;

    public Produs() {
    }

    public Produs(int codProdus,String numeProdus,List<Consum>consumuri,double cantitate,String unitateM) {
        this.cantitate = cantitate;
        this.codProdus = codProdus;
        this.consumuri = consumuri;
        this.numeProdus = numeProdus;
        this.unitateM = unitateM;
    }
}


public class Main {
    static void main(String[] args)throws Exception {

        List<MateriePrima> materii=new ArrayList<>();
        Connection con= DriverManager.getConnection("jdbc:sqlite:examen.db");
        Statement st=con.createStatement();
        ResultSet rs=st.executeQuery("SELECT * FROM MateriiPrime");
        while (rs.next())
        {
            int cod=rs.getInt("Cod");
            String denumore=rs.getString("Denumire");
            double cantitate=rs.getDouble("Cantitate");
            double pretUnitar=rs.getDouble("Pret_unitar");
            String unitateM=rs.getString("Unitate_masura");

            MateriePrima mp=new MateriePrima(cantitate,cod,denumore,pretUnitar,unitateM);
            materii.add(mp);
        }
        con.close();

        double val=0;
        for(MateriePrima mp:materii)
        {
            val+=(mp.pret_unitar*mp.cantitate);
        }
        System.out.println("Valoarea totala: "+val);

        List<Produs> produse=new ArrayList<>();
        FileReader fr=new FileReader("produse.json");
        JSONArray arr=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arr.length();i++)
        {
            JSONObject obj=arr.getJSONObject(i);

            int codProdus=obj.getInt("Cod produs");
            String nume=obj.getString("Denumire produs");

            List<Consum> cns=new ArrayList<>();
            JSONArray acn=obj.getJSONArray("Consumuri");
            for(int j=0;j<acn.length();j++)
            {
                JSONObject ob=acn.getJSONObject(j);

                int codM=ob.getInt("Cod materie prima");
                double cantitate=ob.getDouble("Cantitate");

                Consum conu=new Consum(cantitate,codM);
                cns.add(conu);
            }
            double cantitate=obj.getDouble("Cantitate");
            String unitateM=obj.getString("Unitate masura");

            Produs pr=new Produs(codProdus,nume,cns,cantitate,unitateM);
            produse.add(pr);
        }
        fr.close();

        produse.sort((a,b)->Integer.compare(b.consumuri.size(),a.consumuri.size()));
        for(Produs p:produse)
        {
            System.out.println(p.codProdus+" "+p.numeProdus+" "+p.consumuri.size());
        }

        for(Produs p:produse)
        {
            for(Consum c:p.consumuri)
            {
                for(MateriePrima mp:materii)
                {
                    if(mp.cod==c.codMaterie)
                        mp.cantitate-=c.cantitate;
                }
            }
        }

        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=dbf.newDocumentBuilder();

        Document doc=builder.newDocument();
        Element radacina=doc.createElement("materii_prime");
        doc.appendChild(radacina);
        for(MateriePrima mp:materii)
        {
            Element materiePrima=doc.createElement("materie_prima");

            Element cod=doc.createElement("Cod");
            cod.appendChild(doc.createTextNode(String.valueOf(mp.cod)));

            Element denumire=doc.createElement("denumire");
            denumire.appendChild(doc.createTextNode(mp.denumire));

            Element valoare=doc.createElement("valoare");
            valoare.appendChild(doc.createTextNode(String.valueOf(mp.cantitate*mp.pret_unitar)));

            materiePrima.appendChild(cod);
            materiePrima.appendChild(denumire);
            materiePrima.appendChild(valoare);
            radacina.appendChild(materiePrima);
        }
        TransformerFactory tf=TransformerFactory.newInstance();
        Transformer trans=tf.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT,"yes");

        DOMSource source=new DOMSource(doc);
        StreamResult sr=new StreamResult(new File("stoc.xml"));
        trans.transform(source,sr);

    }
}
