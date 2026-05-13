package seminar.seminar11.g1061;

import java.io.*;
import java.net.Socket;

public class Client {
    private final int STOP_CLIENT=10;
    private final int STOP_SERVER=20;

    public static void main(String[] args) {
        Client client=new Client();
        client.start();
    }

    private void start() {
        try(BufferedReader cin=new BufferedReader(new InputStreamReader(System.in))) //citire din consola
        {
            int optiune;
            while((optiune=citireOptiune(cin))!=STOP_CLIENT)
            {
                switch (optiune)
                {
                    case 1:
                        cerere1(cin);
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

    private void cerere1(BufferedReader cin)
    {
        try(Socket socket=new Socket("localhost",2222);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("Cerere1");
            System.out.println("Numar inventar:");
            long nrInv = Long.parseLong(cin.readLine().trim());
            out.writeObject(nrInv);
            Object mijlocFix = in.readObject();
            if (mijlocFix == null) {
                System.out.println("Nu exista MF:" + nrInv);
            } else{
                System.out.println(mijlocFix);
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

    private void stop(){
        try(Socket socket=new Socket("localhost",2222);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())){
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            out.writeObject("stop");
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

    private int citireOptiune(BufferedReader cin)
    {
        int optiune=-1;
        System.out.println("1 - Returnarea unui mijloc fix după număr inventar");
        System.out.println(STOP_SERVER+"- Stop Server");
        System.out.println(STOP_CLIENT+"- Stop Client");
        System.out.println("Optiune:");
        try {
            optiune=Integer.parseInt(cin.readLine().trim());
        } catch (Exception e) {
            System.err.println(e);
        }
        return optiune;
    }

}
