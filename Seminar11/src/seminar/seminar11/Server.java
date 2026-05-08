package seminar.seminar11;

import seminar.seminar2.g1061.Main;
import seminar.seminar2.g1061.MijlocFix;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.List;

public class Server implements Closeable {

    private ServerSocket serverSocket;
    //il instantez cu constructor
    private boolean stop;
    private List<MijlocFix>lista;

    public Server() throws  Exception {
        serverSocket=new ServerSocket(2222);
        //trebuie sa se termine aplicatia, un client poate trimite un mesaj de stop
        serverSocket.setSoTimeout(5000);

    }

    static void main(String[] args) {
        try(Server server =new Server()){
            server.start();

        } catch (Exception e)
        {
            System.err.println(e);
        }
    }
    @Override
    public void close() throws IOException {
        System.out.println("Stop server");
        //se executa la finalizare - aici efectiv inchid socket ul
        if(serverSocket!=null)
        {
            if(!serverSocket.isClosed())
            {
                serverSocket.close();
            }
        }
    }

    private void start()throws Exception
    {
        System.out.println("Start server");
        citireDate();
        while(!stop)
        {
            try {
                Socket socket=serverSocket.accept(); //metoda e blocata in mom in care clientul trimite o solicitare si o deblocheaza
                Thread firPrelucrare=new Thread(()->tratareCerere(socket));
                firPrelucrare.start();
            } catch (Exception e) {}
        }
    }

    //trimitem fluxul de prelucrare pe o metoda  -- aici practic obiectul o sa l preia
    public void tratareCerere(Socket socket)
    {
        System.out.println("Tratare cerere");
        try(ObjectInputStream in=new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream()))
        {
            String mesaj=in.readObject().toString();
            switch (mesaj)
            {
                case "stop":
                    stop=true;
                    break;
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }


    //citirea informatiilor
    //daca nu merge citirea datelor opresc si serverul
    public void citireDate() throws Exception{
        try(Connection c= DriverManager.getConnection("jdbc:sqlite:g1061.db"))//daca nu exista db o creeaza
        {
            System.out.println("Conexiune creata");
            DatabaseMetaData dbm=c.getMetaData();
            try(ResultSet r=dbm.getTables(null,null,"MIJLOACE_FIXE",new String[]{"TABLE"}))
            {
                //daca nu exista tabela nu am inregistrari in resultset
                //metodele result set, next-avanseaza(intoarce true,daca nu false)

                if(r.next())
                    System.out.println("Citire tabela MIJLOACE_FIXE");
                else {
                    System.out.println("Nu exista tabela MIJLOACE_FIXE");
                    Main.citireDate("MijloaceFixe.csv");
                    Main.creareLista();
                    lista=Main.lista;
                    System.out.println("Lista mijloacelor fixe:");
                    lista.forEach(System.out::println);

                    //Crearea tabelei MIJLOACE_FIXE
                }
            }
        }

    }
}
