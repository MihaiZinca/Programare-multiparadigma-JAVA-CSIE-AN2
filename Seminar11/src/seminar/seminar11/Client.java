package seminar.seminar11;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Closeable {

    private Socket socket;

    public Client() throws Exception{
        socket=new Socket("localhost",2222);
    }

    static void main(String[] args) {
        try(Client client=new Client())
        {
            client.start();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @Override
    public void close() throws IOException {
        System.out.println("Se opreste conexiunea");
        if(socket!=null)
            if(!socket.isClosed())
                socket.close();
    }

    private void start()throws  Exception{
        stop();
    }

    //metodata care stopeaza serverul trimitem mesaje de inchidere
    private void stop() {
        try(ObjectOutputStream out=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in=new ObjectInputStream(socket.getInputStream()))
        {
            out.writeObject("stop"); //(obiectul o sa l preia serverul) clientul trimite string ul STOP
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
    }

    //peste inputstream creez un object inputstream, respectiv pentru outputstream
}
