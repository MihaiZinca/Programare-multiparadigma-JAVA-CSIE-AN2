package seminar.seminar5;

import seminar.seminar2.g1061.MijlocFix;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args)
    {
        try{
            seminar.seminar2.g1061.Main.citireDate("MijloaceFixe.csv");
            seminar.seminar2.g1061.Main.creareLista();
            List< MijlocFix> lista=seminar.seminar2.g1061.Main.lista;
            System.out.println("Lista mijloacelor fixe");
            for(MijlocFix mijlocFix:lista)
                System.out.println(mijlocFix);

            //Afisare prin clasa anonima(interfata functionala explicita)
            System.out.println("Lista m.f.:");
            lista.forEach(new Consumer<MijlocFix>() {
                @Override
                public void accept(MijlocFix mijlocFix) {
                    System.out.println(mijlocFix);
                }
            });

            //Afisare prin lambda
            lista.forEach(mijlocFix -> System.out.println(mijlocFix));

            //Afisare prin referenta la metoda
            lista.forEach(System.out::println);

            //Cerinta 1
            //Filtrare dupa valoare intre 2 interv

            double vMin=50000, vMax=25000;
//            List<MijlocFix>cerinta1=lista.stream().filter(
//                    new Predicate<MijlocFix>() {
//                        @Override
//                        public boolean test(MijlocFix mijlocFix) {
//                            return mijlocFix.getValoare()>=vMin && mijlocFix.getValoare()<=vMax;
//                        }
//                    }
//            ).toList();
            //filter preia elemente din stream si face tot stream aplicand filtru

            List<MijlocFix> cerinta1=lista.stream().filter(mijlocFix->mijlocFix.getValoare()>=vMin && mijlocFix.getValoare()<=vMax).toList();

            System.out.println("\nMijloace fixe intre ["+vMin+","+vMax+"]:");
            cerinta1.forEach(System.out::println);

            //Cerinta2
            //Selectia de la o anumita locatie

            String denumireLocatie="Punct de lucru Covasna";
            List<MijlocFix> cerinta2=lista.stream().filter(mijlocFix -> mijlocFix.getLocatie().getDenumire().equals(denumireLocatie)).toList();
            System.out.println("\nMijloace fixe din locatia: "+ denumireLocatie);
            cerinta2.forEach(System.out::println);

            //Cerinta3
            //Selecția mijloacelor fixe cu data de achiziție mai recentă decât o dată specificată

            Date dataLimita=seminar.seminar2.g1061.Main.fmt.parse("01.01.2013");
            List<MijlocFix> cerinta3=lista.stream().filter(mijlocFix -> mijlocFix.getDataAchizitie().before(dataLimita)).toList();
            System.out.println("\nMijloace fixe achizitionate inainte de: "+seminar.seminar2.g1061.Main.fmt.format(dataLimita)+":");
            cerinta3.forEach(System.out::println);


        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
