package seminar.seminar5;

import seminar.seminar2.g1061.Categorie;
import seminar.seminar2.g1061.Locatie;
import seminar.seminar2.g1061.MijlocFix;

import java.sql.SQLOutput;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

            //Seminar6
            //Cerinta 4
//            List<MijlocFix> cerinta4=lista.stream().sorted(new Comparator<MijlocFix>() {
//                @Override
//                public int compare(MijlocFix o1, MijlocFix o2) {
//                    return Double.compare(o1.calculUzura(),o2.calculUzura());
//                }
//            }).toList();

            List <MijlocFix> cerinta4_=lista.stream().sorted((m1,m2)->Double.compare(m1.calculUzura(),m2.calculUzura())).toList();
            System.out.println("Lista sortata dupa uzura");
            cerinta4_.forEach(mijlocFix -> System.out.println(mijlocFix.getDenumire()+","+mijlocFix.calculUzura()));


            //Cerinta 5
            Set<String> cerinta5=lista.stream().
                    map(mijlocFix -> mijlocFix.getLocatie().getDenumire()).
                    collect(Collectors.toSet());
            System.out.println("\nLista locatiilor:");
            cerinta5.forEach(System.out::println);

            //Cerinta6
            Map<Long, Locatie> cerinta6=lista.stream()
                    .collect(Collectors.toMap(
                            MijlocFix::getNrInventar,
                            MijlocFix::getLocatie
                    ));
            System.out.println("\nColectare locatii pe nr de inventar.");
            cerinta6.forEach((nrinv,locatie)-> System.out.println(nrinv+":"+locatie));

            //Cerinta7
            Map<Categorie,List<MijlocFix>> cerinta7=lista.stream()
                    .collect(Collectors.groupingBy(MijlocFix::getCategorie));
            System.out.println("\nGruparea mijloacelor fixe pe categorii:");
            cerinta7.forEach((categorie, mijloace) -> {
                System.out.println("Categorie:" + categorie);
                mijloace.forEach(System.out::println);
            });

            //Cerinta8
            Map<Categorie,List<String>> cerinta8=lista.stream()
                    .collect(Collectors.groupingBy(
                            MijlocFix::getCategorie,
                            Collectors.mapping(  //transformare MijlocFix → String (denumire) →
                                    MijlocFix::getDenumire,
                                    Collectors.toList()
                                    )
                            ) );
            System.out.println("\nGrupare mijloace fixe pe categorii:");
            cerinta8.forEach( ((categorie, denumire) -> System.out.println(categorie+":"+denumire)) );

            //Cerinta9
            Map<String,Double> cerinta9=lista.stream()
                    .collect(Collectors.groupingBy(
                            mijlocFix->mijlocFix.getLocatie().getDenumire(),
                            Collectors.averagingDouble(MijlocFix::calculUzura)
                    ));
            System.out.println("\nUzura medie pe locatii:");
            cerinta9.forEach( (denumirel,uzura)-> System.out.println(denumirel+":"+uzura) );



            //pana aici se cam cere la examen sub este in "plus"
            //Cerinta10
            Map<Long,?> cerinta10=lista.stream()
                    .collect(Collectors.toMap(
                            MijlocFix::getNrInventar,
                            mijlocFix -> new Object()
                            {
                                private String denumire= mijlocFix.getDenumire();
                                private Date dataA=mijlocFix.getDataAchizitie();

                                @Override
                                public String toString() {
                                    return denumire+"," + (dataA==null?"": seminar.seminar2.g1061.Main.fmt.format(dataA));
                                }
                            }
                    ));
            System.out.println("\nColectare informatii pe mijlocfix(numar inventar):");
            cerinta10.forEach( (nrinv,info)-> System.out.println(nrinv+":"+info) );

            //Cerinta11
            Map<Categorie,List<Long>> cerinta11=lista.stream()
                .collect(new Supplier<Map<Categorie, List<Long>>>() {
                    @Override
                    public Map<Categorie, List<Long>> get() {
                        return new HashMap<>();
                    }
                }, new BiConsumer<Map<Categorie, List<Long>>, MijlocFix>() {
                    @Override
                    public void accept(Map<Categorie, List<Long>>map, MijlocFix mijlocFix) {
                        Categorie categorie=mijlocFix.getCategorie();
                        if(map.containsKey(categorie))
                            map.get(categorie).add(mijlocFix.getNrInventar());
                        else {
                            List<Long> numereInventar=new ArrayList<>();
                            numereInventar.add(mijlocFix.getNrInventar());
                            map.put(categorie,numereInventar);
                        }

                    }
                }, new BiConsumer<Map<Categorie, List<Long>>, Map<Categorie, List<Long>>>() {
                    @Override
                    public void accept(Map<Categorie, List<Long>> map1, Map<Categorie, List<Long>> map2) {
                        map1.putAll(map2);
                    }
                });

            //echivalente

            Map<Categorie,List<Long>> cerinta11_=lista.stream()
                            .collect(
                                    HashMap::new,
                                    (map,mijlocFix)->{
                                        Categorie categorie=mijlocFix.getCategorie();
                                        if(map.containsKey(categorie))
                                            map.get(categorie).add(mijlocFix.getNrInventar());
                                        else {
                                            List<Long> numereInventar=new ArrayList<>();
                                            numereInventar.add(mijlocFix.getNrInventar());
                                            map.put(categorie,numereInventar);
                                        }
                                    },
                                    HashMap::putAll
                            );
            System.out.println("\nCollectare nr inventar pe categorie:");
            cerinta11_.forEach( (categorie,numere)-> System.out.println(categorie+":"+numere));

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
