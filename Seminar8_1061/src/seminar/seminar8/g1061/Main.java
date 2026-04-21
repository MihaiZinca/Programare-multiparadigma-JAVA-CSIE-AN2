package seminar.seminar8.g1061;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try{
            int n=10,m=10;
            Double[][] x = new Double[n][m];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    x[i][j] = (double)(i*m+j);
                }
            }
            Double[] sl = new Double[n],sc = new Double[m];
            print(x);
            Operatiuni<Double> operatiuni = new Operatiuni<Double>() {
                @Override
                public Double init() {
                    return 0.0;
                }

                @Override
                public Double add(Double a, Double b) {
                    return a+b;
                }
            };

            String tip = "c";

            int nrFire = 4;
            PartitieMatrice[] fire = new PartitieMatrice[nrFire];

            if (tip.equalsIgnoreCase("l")) {
                int pas = n/nrFire;
                for (int i = 0; i < nrFire; i++) {
                    if (i == nrFire - 1) {
                        fire[i] = new PartitieMatrice<>(x, i * pas, n - 1, sl, tip, operatiuni);
                    } else {
                        fire[i] = new PartitieMatrice<>(x, i * pas, (i + 1) * pas - 1, sl, tip, operatiuni);
                    }
                }
            } else {
                int pas = m/nrFire;
                for (int i = 0; i < nrFire; i++) {
                    if (i == nrFire - 1) {
                        fire[i] = new PartitieMatrice<>(x, i * pas, m - 1, sc, tip, operatiuni);
                    } else {
                        fire[i] = new PartitieMatrice<>(x, i * pas, (i + 1) * pas - 1, sc, tip, operatiuni);
                    }
                }
            }
            for (int i = 0; i < nrFire; i++) {
                fire[i].start();
            }
            for (int i = 0; i < nrFire; i++) {
                fire[i].join();
            }
            if (tip.equalsIgnoreCase("l")) {
                System.out.println(Arrays.toString(sl));
            } else {
                System.out.println(Arrays.toString(sc));
            }
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }

    private static <T> void print(T[][] x){
        for (T[] linie:x){
            System.out.println(Arrays.toString(linie));
        }
    }
}
