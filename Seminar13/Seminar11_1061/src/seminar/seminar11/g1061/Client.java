package seminar.seminar11.g1061;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import seminar.seminar2.g1061.Categorie;
import seminar.seminar2.g1061.Locatie;
import seminar.seminar2.g1061.Main;
import seminar.seminar2.g1061.MijlocFix;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
    private final int STOP_CLIENT=10;
    private final int STOP_SERVER=20;

    public static void main(String[] args) {
        Client client = new Client();
        client.start();
    }

    private void start() {
        try(BufferedReader cin = new BufferedReader(new InputStreamReader(System.in))){
            int optiune;
            while ( (optiune=citireOptiune(cin))!=STOP_CLIENT ){
                switch (optiune){
                    case 1:
                        cerere1(cin);
                        break;
                    case 2:
                        cerere2(cin);
                        break;
                    case 4:
                        cerere4();
                        break;
                    case STOP_SERVER:
                        stop();
                        break;
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void cerere4(){
        try (Socket socket = new Socket("localhost", 2222);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("Cerere4");
            List<MijlocFix> lista = citireJson("Input.json");
            out.writeObject(lista.size());
            for (MijlocFix mijlocFix:lista){
                out.writeObject(mijlocFix);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    private List<MijlocFix> citireJson(String numeFisier){
        List<MijlocFix> lista = new ArrayList<>();
        try(FileReader in = new FileReader(numeFisier)) {
            JSONTokener tok = new JSONTokener(in);
            JSONArray jsa = new JSONArray(tok);
            for (int i = 0; i < jsa.length(); i++) {
                JSONObject jso = jsa.getJSONObject(i);
                MijlocFix mf = new MijlocFix();
                mf.setDenumire(jso.getString("Denumire"));
                mf.setCategorie(Categorie.valueOf(jso.getString("Categorie")));
                mf.setValoare(jso.getDouble("ValInv"));
                mf.setDurataNormata(jso.getInt("DN"));
                mf.setDataAchizitie( Main.fmt.parse(jso.getString("Data")) );
                mf.setNrInventar(jso.getLong("NrInv"));
                mf.setLocatie(new Locatie(jso.getString("Locatie")));
                lista.add(mf);
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }

        return lista;
    }

    private void cerere1(BufferedReader cin){
        try (Socket socket = new Socket("localhost", 2222);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("Cerere1");
            System.out.println("Numar inventar:");
            long nrInv = Long.parseLong(cin.readLine().trim());
            out.writeObject(nrInv);
            Object mijlocFix = in.readObject();
            if (mijlocFix==null){
                System.out.println("Nu exista MF "+nrInv);
            } else {
                System.out.println(mijlocFix);
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    private void cerere2(BufferedReader cin){
        try (Socket socket = new Socket("localhost", 2222);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("Cerere2");

            System.out.println("Locatie:");
            String denumireLocatie = cin.readLine().trim();
            out.writeObject(denumireLocatie);
            int nrMijloace = (Integer) in.readObject();
            List<MijlocFix> lista = new ArrayList<>();
            for (int i = 0; i < nrMijloace; i++) {
                lista.add((MijlocFix) in.readObject());
            }
            System.out.println("Mijloace fixe returnate:");
            lista.forEach(System.out::println);
            salvareJson(lista,denumireLocatie);
        } catch (Exception ex) {
            System.err.println(ex);
        }

    }

    private void stop() {
        try (Socket socket = new Socket("localhost", 2222);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
            out.writeObject("stop");
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private int citireOptiune(BufferedReader cin){
        int optiune=-1;
        System.out.println("1 - Returnarea unui mijloc fix după număr inventar");
        System.out.println("2 - Solicitarea mijloacelor fixe aflate într-o anumită locație și salvarea lor în fișierul JSON locatie.json");
        System.out.println("4 - Adăugarea de noi mijloace fixe din fișierul Input.json");
        System.out.println(STOP_SERVER+" - Stop server");
        System.out.println(STOP_CLIENT+" - Stop client");
        System.out.println("Optiune:");
        try{
            optiune = Integer.parseInt(cin.readLine().trim());
        } catch (Exception e) {
            System.err.println(e);
        }
        return optiune;
    }

    private void salvareJson(List<MijlocFix> lista,String locatie){
        try(FileWriter out = new FileWriter(locatie+".json")){
            JSONArray jsa = new JSONArray();
            for (int i = 0; i < lista.size(); i++) {
                JSONObject jso = new JSONObject();
                MijlocFix mf = lista.get(i);
                jso.put("Denumire",mf.getDenumire());
                jso.put("NrInv",mf.getNrInventar());
                jso.put("ValInv",mf.getValoare());
                jso.put("Categorie",mf.getCategorie());
                jso.put("DN",mf.getDurataNormata());
                jso.put("Data", Main.fmt.format(mf.getDataAchizitie()));
                jso.put("Locatie",mf.getLocatie().getDenumire());
                jsa.put(i,jso);
            }
            jsa.write(out,8,0);
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

}
