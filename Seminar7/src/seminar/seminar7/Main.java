package seminar.seminar7;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.lang.IO.print;

public class Main {
    static void main(String[] args) {
        try{
            int n=10,m=10;
            Double[][]x=new Double[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    x[i][j]= (double) (n*i+j);
                }
            }
            Double[] y=new Double[m], z=new Double[n];
            Arrays.fill(y,1.0);
           // print(x);

            //Inmultire pe un singur fir
            Date start =new Date();
            Operatiuni<Double> o=new Operatiuni<Double>() {
                @Override
                public Double init() {
                    return 0.0;
                }

                @Override
                public Double add(Double a, Double b) {
                    return a+b;
                }

                @Override
                public Double mul(Double a, Double b) {
                    return a*b;
                }
            };
            Inmultire<Double> inmultire=new Inmultire<Double>(x,0,n-1,y,z,o);

            //acum creez fir de executie
            Thread firInmultire=new Thread(inmultire);
            firInmultire.start(); //nu se invoca involuntar run!!
            //trb sa opresc firul curent, pt a astepa
            firInmultire.join();
            Date stop=new Date();
            System.out.println("Timp de exectuie pe un fir:"+(stop.getTime()-start.getTime()));
            //System.out.println(Arrays.toString(z));

            //sistem de exectuie pe mai multe fire
            start=new Date();
            List<Thread> fire=new ArrayList<>();
            int nrMaxFire=4,l1=0,l2;
            int pas=n/nrMaxFire+1;
            while (l1<n) {
                if(l1+pas<n)
                {
                    l2=l1+pas;
                }
                else
                {
                    l2=n-1;
                }
                fire.add(new Thread(new Inmultire<>(x,l1,l2,y,z,o) ) );
                l1=l2+1;
            }

            for(Thread fir:fire)
            {
                fir.start();
            }
            for(Thread fir:fire)
            {
                fir.join();
            }
            stop=new Date();
            //System.out.println(Arrays.toString(z));
            System.out.println("Timp de exectuie pe "+fire.size()+" fire:"+(stop.getTime()-start.getTime()));


        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void print(Double[][] x)
    {
        for(Double[] linie:x)
            System.out.println(Arrays.toString(linie));
    }
}

