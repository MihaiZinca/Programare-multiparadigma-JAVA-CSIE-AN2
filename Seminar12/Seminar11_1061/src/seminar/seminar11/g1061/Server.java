package seminar.seminar11.g1061;

import seminar.seminar2.g1061.Categorie;
import seminar.seminar2.g1061.Locatie;
import seminar.seminar2.g1061.Main;
import seminar.seminar2.g1061.MijlocFix;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server implements Closeable {
    private ServerSocket serverSocket;
    private boolean stop;
    private List<MijlocFix> lista;

    public Server() throws Exception{
        serverSocket = new ServerSocket(2222);
        serverSocket.setSoTimeout(5000);
    }

    public static void main(String[] args) {
        try(Server server=new Server()){
            server.start();
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("Stop server.");
        if (serverSocket!=null){
            if (!serverSocket.isClosed()){
                serverSocket.close();
            }
        }
    }

    private void start() throws Exception{
        System.out.println("Start server.");
        citireDate();
        while (!stop){
            try{
                Socket socket = serverSocket.accept();
                Thread firPrelucrare = new Thread( ()->tratareCerere(socket) );
                firPrelucrare.start();
            }
            catch (Exception ex){}
        }
    }

    private void tratareCerere(Socket socket){
        System.out.println("Tratare cerere.");
        try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
            String mesaj = in.readObject().toString();
            switch (mesaj){
                case "stop":
                    stop=true;
                    break;
                case "Cerere1":
                    long nrInv=(Long)in.readObject();
                    //cand caut un singur elemenet
                    Optional<MijlocFix>raspuns=lista.stream().
                            filter(mijlocFix -> mijlocFix.getNrInventar()==nrInv).
                            findFirst();
                    if(raspuns.isPresent()){
                        //daca a gasit obiect
                        out.writeObject(raspuns.get());
                    }
                    else
                    {
                        out.writeObject(null);
                    }
                    break;
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
        finally {
            try {
                socket.close();
            }catch (Exception e)
            {

            }
        }
    }

    private void citireDate() throws Exception{
        try(Connection c = DriverManager.getConnection("jdbc:sqlite:g1061.db")){
            System.out.println("Conexiune creata.");
            DatabaseMetaData dbm = c.getMetaData();
            try(ResultSet r = dbm.getTables(null,null,"MIJLOACE_FIXE",new String[]{"TABLE"})){
                if (r.next()){
                    System.out.println("Citire tabela MIJLOACE_FIXE");
                    try(Statement s=c.createStatement();
                    ResultSet rs=s.executeQuery("select * from MIJLOACE_FIXE"))
                    {
                        lista=new ArrayList<>();
                        while (rs.next()) //exista inregistrare
                        {
                            //aici am populat obj mijlocFix
                            MijlocFix mijlocFix=new MijlocFix();
                            mijlocFix.setDenumire(rs.getString(1));
                            mijlocFix.setNrInventar(rs.getLong(2));
                            mijlocFix.setValoare(rs.getDouble(3));
                            mijlocFix.setDataAchizitie(Main.fmt.parse(rs.getString(4)));
                            mijlocFix.setCategorie(Categorie.valueOf(rs.getString(5)));
                            mijlocFix.setDurataNormata(rs.getInt(6));
                            mijlocFix.setLocatie(new Locatie(rs.getString(7)));

                            lista.add(mijlocFix);
                        }
                        System.out.println("Lista inregistrata din BD:");
                        lista.forEach(System.out::println);
                    }
                } else {
                    System.out.println("Nu exista tabela MIJLOACE_FIXE");
                    Main.citireDate("MijloaceFixe.csv");
                    Main.creareLista();
                    lista = Main.lista;
                    System.out.println("Lista mijloacelor fixe:");
                    lista.forEach(System.out::println);
//                    Crearea tabelei MIJLOACE_FIXE
                    try(Statement s=c.createStatement()) //pt a nu bloca conexiunea si sa fie inchisa
                    {
                        String comandaCreare="create table MIJLOACE_FIXE("+
                                "denumire varchar(30), nr_inventar bigint primary key," +
                                "valoare double, data_achizitie  varchar(10),categorie  varchar(30)," +
                                "durata_normata integer,locatie varchar(50)" +
                                ")";
                        System.out.println(comandaCreare);
                        s.executeUpdate(comandaCreare);
                        System.out.println("Tabela creata.");


                    }
                    //pt a vedea baza de date DB Browser(SQ LITE)!
                    String comandaInserare="insert into MIJLOACE_FIXE values(?,?,?,?,?,?,?)";
                    try(PreparedStatement pst=c.prepareStatement(comandaInserare)){
                        for(MijlocFix mijlocFix:lista) {
                            pst.setString(1, mijlocFix.getDenumire());
                            pst.setLong(2,mijlocFix.getNrInventar());
                            pst.setDouble(3,mijlocFix.getValoare());
                            pst.setString(4,Main.fmt.format(mijlocFix.getDataAchizitie()));
                            //conversie de la data la string dupa format(dina jar..)
                            pst.setString(5,mijlocFix.getCategorie().name());
                            pst.setInt(6,mijlocFix.getDurataNormata());
                            pst.setString(7,mijlocFix.getLocatie().getDenumire());
                            pst.executeUpdate();
                            System.out.println("Inregistrare adaugata!");
                        }
                    }


                }
            }
        }
    }


}

//examen
//citire crect din fisiere(json/text
//citire din bd