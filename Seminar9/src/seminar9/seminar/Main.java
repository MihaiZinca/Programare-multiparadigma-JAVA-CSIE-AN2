package seminar9.seminar;

public class Main {
    public static boolean stop; //val neutra=false

    static void main(String[] args) {
        try{
            Muzeu<Integer> muzeu=new Muzeu<>(30);

            //generam fire
            Generator<Integer> generator=new Generator<Integer>() {
                @Override
                public Integer init() {
                    return 1;
                }

                @Override
                public Integer next(Integer a) {
                    return a+1;
                }
            };
            //fir intrare
            Intrare<Integer>intrare =new Intrare<>(muzeu,5,generator);
            Iesire<Integer> iesire=new Iesire<>(muzeu,3);
            iesire.start();
            intrare.start();

            //scenariu delurare
            Thread.sleep(10);
            stop=true;

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}

//test fara fire exectuie, intra doar pana la connect
