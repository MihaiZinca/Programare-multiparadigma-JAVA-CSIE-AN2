import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.sql.*;
import java.util.*;


class Produs implements Serializable
{

   int cod;
   String denumire;
   double pret;

    public Produs(int cod, String denumire, double pret) {
        this.cod = cod;
        this.denumire = denumire;
        this.pret = pret;
    }

    public Produs() { }

    @Override
    public String toString() {
        return cod+" "+denumire+" "+pret;
    }
}

class Tranzactie implements Serializable
{
   int codProdus;
   int cantitate;
   String tip;

    public Tranzactie() {
    }

    public Tranzactie(int codProdus, int cantitate, String tip) {
        this.codProdus = codProdus;
        this.cantitate = cantitate;
        this.tip = tip;
    }


    @Override
    public String toString() {
        return codProdus+" "+cantitate+" "+tip;
    }
}
public class Main {

    public static void main(String[] args) throws Exception {

        List<Produs> produse=new ArrayList<>();
        List<Tranzactie> tranzactii=new ArrayList<>();

        //Citire txt

        BufferedReader br=new BufferedReader(new FileReader("produse.txt"));
        String linie;

        while( (linie=br.readLine()) !=null)
        {
            String[] p=linie.split(",");

            Produs pr=new Produs();
            pr.cod=Integer.parseInt(p[0].trim());
            pr.denumire=p[1].trim();
            pr.pret=Double.parseDouble(p[2].trim());

            produse.add(pr);
        }
        br.close();

        System.out.println("Numar produse: " + produse.size());
        produse.sort((a,b)->a.denumire.compareTo(b.denumire));

        System.out.println("Produse sortate");
        for(Produs p:produse)
        {
            System.out.println(p.toString());
        }


        //Citire json
        FileReader fr=new FileReader("tranzactii.json");
        JSONArray arr=new JSONArray(new JSONTokener(fr));

        for(int i=0;i<arr.length();i++)
        {
            JSONObject obj=arr.getJSONObject(i);
            Tranzactie tr=new Tranzactie();

            tr.codProdus=obj.getInt("codProdus");
            tr.cantitate=obj.getInt("cantitate");
            tr.tip=obj.getString("tip");

            List<Integer> listaVag=new ArrayList<>();
            JSONArray lv=obj.getJSONArray("Vagoane");
            for(int j=0;i<lv.length();j++)
            {
                listaVag.add(lv.getInt(j));
            }

            tranzactii.add(tr);
        }
        fr.close();

        //Scriere json
        JSONArray arr2=new JSONArray();
        for(Tranzactie tr:tranzactii)
        {
            JSONObject obj=new JSONObject();
            obj.put("codProdus",tr.codProdus);
            obj.put("cantitate",tr.cantitate);
            obj.put("tip",tr.tip);

            arr2.put(obj);
        }
        FileWriter fw=new FileWriter("output.json");
        fw.write(arr2.toString(2));
        fw.close();


        //Scriere raport in txt
        Map<Integer,Integer> map=new HashMap<>();

        for(Tranzactie t:tranzactii)
            map.put(t.codProdus,map.getOrDefault(t.codProdus,0)+1);

        produse.sort((a,b)->map.getOrDefault(b.cod,0)-map.getOrDefault(a.cod,0));

        //scriere txt
        BufferedWriter bw=new BufferedWriter(new FileWriter("tranzactii.txt"));
        bw.write("Denumire Produs,Numar tranzactii\n");
        for(Produs p:produse)
        {
            bw.write(p.denumire+","+map.getOrDefault(p.cod,0)+"\n");

        }
        bw.close();




        //Scriere XML
        DocumentBuilderFactory dbf1=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder1=dbf1.newDocumentBuilder();

        Document docu=builder1.newDocument();
        Element radacina= docu.createElement("produse");
        docu.appendChild(radacina);

        for(Produs p:produse)
        {
            Element produs=docu.createElement("produs");

            Element cod=docu.createElement("cod");
            cod.appendChild(docu.createTextNode(String.valueOf(p.cod)));

            Element denumire= docu.createElement("denumire");
            denumire.appendChild(docu.createTextNode(p.denumire));

            Element pret=docu.createElement("pret");
            pret.appendChild(docu.createTextNode(String.valueOf(p.pret)));

            produs.appendChild(cod);
            produs.appendChild(denumire);
            produs.appendChild(pret);

            radacina.appendChild(produs);
        }

        TransformerFactory tf1=TransformerFactory.newInstance();
        Transformer trans=tf1.newTransformer();
        trans.setOutputProperty(OutputKeys.INDENT,"yes");


        DOMSource source=new DOMSource(docu);
        StreamResult sr=new StreamResult(new File("output.xml"));
        trans.transform(source,sr);


        //Citire XML

        List<Produs> prod=new ArrayList<>();
        DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder=dbf.newDocumentBuilder();

        Document doc=builder.parse((new File("produse.xml")));
        doc.getDocumentElement().normalize();

        NodeList listsa=doc.getElementsByTagName("produs");
        for(int i=0;i<listsa.getLength();i++)
        {
            Node nod=listsa.item(i);
            if(nod.getNodeType()==Node.ELEMENT_NODE)
            {
                Element elem=(Element)nod;
                Produs p=new Produs();
                p.cod=Integer.parseInt(elem.getElementsByTagName("cod").
                        item(0).getTextContent());

                p.denumire=elem.getElementsByTagName("denumire").
                        item(0).getTextContent();

                p.pret=Double.parseDouble(elem.getElementsByTagName("pret").
                        item(0).getTextContent());

                prod.add(p);
            }
        }

        //Citire din DB
        Connection con= DriverManager.getConnection("jdbc:sqlite:produse.db");

        Statement stmt=con.createStatement();
        ResultSet rs=stmt.executeQuery("select * from produse");
        while(rs.next())
        {
            int cod=rs.getInt("codProdus");
            String denumire=rs.getString("denumire");
            double pret=rs.getDouble("pret");

            System.out.println(cod + " " + denumire + " " + pret);
        }


        //Scriere in DB
        PreparedStatement ps=con.prepareStatement("INSERT INTO produse(codProdus, denumire, pret) VALUES(?,?,?)");
        for(Produs p:produse)
        {
            ps.setInt(1, p.cod);
            ps.setString(2, p.denumire);
            ps.setDouble(3, p.pret);
            ps.executeUpdate();
        }

        //Citire tastatura
        Scanner sc=new Scanner(System.in);
        String materieC=sc.nextLine();


    }
}
