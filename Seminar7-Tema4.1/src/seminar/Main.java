package seminar;

import java.util.Arrays;

public class Main {
    static void main(String[] args) throws Exception {
        try {
            int n = 100;
            Double[] v = new Double[n];
            for (int i = 0; i < n; i++) {
                v[i] = Math.random() * n;
            }

            System.out.println("Sirul nesortat:");
            System.out.println(Arrays.toString(v));

            int m = n / 2;
            SubSir<Double> subsir1 = new SubSir<>(v, 0, m);
            SubSir<Double> subsir2 = new SubSir<>(v, m + 1, n - 1);
            Thread fir1 = new Thread(subsir1);
            Thread fir2 = new Thread(subsir2);
            fir1.start();
            fir2.start();

            fir1.join();
            fir2.join();
            Arrays.sort(v);
            System.out.println("Sirul sortat:\n" + Arrays.toString(v));

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
