package seminar.seminar2.g1061;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static SimpleDateFormat fmt=new SimpleDateFormat("dd.MM.yyyy");
    private static MijlocFix[] mijloaceFixe=new MijlocFix[0];

    public static List<MijlocFix> lista=new ArrayList<>(); //folosesc referinta de tip list

    public static void main(String[] args)
    {
        System.out.println("Tema 2.2");
        try {
            //Seminar 3
//            Adresa a1 = new Adresa("Brasov", "Brasov", "Bronzului", "39");
//            //  System.out.println(a1);
//
//            MijlocFix m1 = new MijlocFix("Cladire sediu central", 111L,
//                    500000.0, fmt.parse("10.10.2020"),
//                    new Locatie("Sediu central", a1),
//                    Categorie.CONSTRUCTII, 100); //am pus 50000.0 ca am pus Double inloc de double
//
//            System.out.println(m1);
//            System.out.println("Amortizare:"+m1.amortizare());
//
//            MijlocFix m2=new MijlocFix(111L);
//            System.out.println(m1.equals(m2));
//
//            MijlocFix clona=(MijlocFix)m1.clone(); //nu modifc clona deoarece am creat inainte si dupa am modificat
//            m1.getLocatie().getAdresa().setJudet("Maramures");
//            System.out.println("Clona:");
//            System.out.println(clona);
//            System.out.println("MijlocFix:");
//            System.out.println(m1);

            citireDate("MijloaceFixe.csv");
            for(MijlocFix m: mijloaceFixe) {
                System.out.println(m);
            }

            creareLista();
            System.out.println("Lista Mijloacelor fixe");
            for(MijlocFix mijlocFix:lista)
            {
                System.out.println(mijlocFix);
            }


            //Sortare dupa data achizitie
            Collections.sort(lista);
            System.out.println("Lista sortata dupa data achizitie:");
            for(MijlocFix mijlocFix:lista)
            {
                System.out.println(mijlocFix);
            }

            //clase anonime-clase care  new numeInterfata() { implement interfata} sau pot cu NewObject()
            //Sortare dupa nr inventar
            //metoda statica sort face sortare dupa list, dupa un crietriu fata de altul implementat prin clasa
            //vrea un comparator gen
            System.out.println("Lista sortata dupa nrInventar:");
            Collections.sort(lista, new Comparator<MijlocFix>() {
                @Override
                public int compare(MijlocFix m1, MijlocFix m2) {
                    return Long.compare(m1.nrInventar,m2.nrInventar);
                }
            });
            for(MijlocFix mijlocFix:lista)
            {
                System.out.println(mijlocFix);
            }

           //am fct asa pt ca mai avem nevoie de instantiere, nu mai il scriam cand il folosesc
            Comparator<MijlocFix> comparatorValoare=new Comparator<MijlocFix>() {
                @Override
                public int compare(MijlocFix m1, MijlocFix m2) {
                    return Double.compare(m1.valoare,m2.valoare);
                }
            };

            //Sortare dupa valoarea
            Collections.sort(lista,comparatorValoare);
            System.out.println("\nLista sortat dupa valoare:");
            for(MijlocFix mijlocFix:lista)
            {
                System.out.println(mijlocFix);
            }


            //Selectie dupa nrInventar folosim equals
            long nrInventar=3L;
            int k=lista.indexOf(new MijlocFix(nrInventar)); //foloseste equals din ElPatrimonial si se compara
            //intoarce indexul daca il gaseste
            if(k==-1) {
                System.out.println("Nu exista mijloc fix cu nr inv:"+nrInventar);
            }
            else {
                System.out.println("Mijlocul fix cautat:\n"+lista.get(k));
            }

           //Selectie dupa valoare

            MijlocFix mijlocFix=new MijlocFix();
            mijlocFix.setValoare(50000D);
            Collections.sort(lista,comparatorValoare);
            k=Collections.binarySearch(lista,mijlocFix,comparatorValoare);
            if(k>=0){
                System.out.println("\nMijloc fix cautat:\n"+lista.get(k));
            }
            else {
                //k=-i-1 unde i este pozitia de inserare(unde ar trebui sa se afle)
                //i=-k-1 pozitia unde ar trb sa se afle
                System.out.println("Nu exista mijloc fix cu valoare:"+mijlocFix.getValoare());
                System.out.println("Pozitie de inserare:"+(-k-1));
            }


            //Printare uzura
            printare("UzuraMF.csv");

            //Salvare lista -Serializare
            // am pus implements Serializable
            salvare("mf.dat");
            System.out.println("Date salvate...");

            //Restaurare
            restaurare("mf.dat");
            System.out.println("List restaurata:");
            for(MijlocFix m:lista)
            {
                System.out.println(m);
            }
        }
        catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static void citireDate(String numeFisier)
    {
        try(Scanner scanner=new Scanner(new File(numeFisier))){
            while(scanner.hasNextLine()) //citesc linie cu linie
            {
                String[] t=scanner.nextLine().trim().split(","); //trim elimina spatiile
                MijlocFix mijlocFix=new MijlocFix();
                mijlocFix.setDenumire(t[0].trim());
                mijlocFix.setNrInventar(Long.parseLong(t[1].trim()));
                mijlocFix.setValoare(Double.parseDouble(t[2].trim()));
                mijlocFix.setDataAchizitie(fmt.parse(t[3].trim())); //citeste data conform formatului
                mijlocFix.setCategorie(Categorie.valueOf(t[4].trim().toUpperCase())); //value0f intoarce categoria
                mijlocFix.setDurataNormata(Integer.parseInt(t[5].trim()));
                t=scanner.nextLine().trim().split(",");
                Locatie locatie=new Locatie();
                locatie.setDenumire(t[0].trim());
                Adresa adresa=new Adresa(t[1].trim(),t[2].trim(),t[3].trim(),t[4].trim());
                locatie.setAdresa(adresa);
                mijlocFix.setLocatie(locatie);

                mijloaceFixe= Arrays.copyOf(mijloaceFixe,mijloaceFixe.length+1);//copiaza + inca ultima poz null
                mijloaceFixe[mijloaceFixe.length-1]=mijlocFix;
            }

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    public static void creareLista()
    {
        lista.addAll(Arrays.asList(mijloaceFixe));//creaza o lista din elemente unui vector am copiat referientele
    }



    //fluxri binare in care unitatea octetul seralizare
    //si fluxuri de tip caracter caracterul unicode
    //flux de timp caracter -print writter

    //FISIER TEXT
    private static void printare(String numeFisier)
    {
        try(PrintWriter fout=new PrintWriter(numeFisier)) { //instantiere flux
            for(MijlocFix mijlocFix:lista) {
                fout.println(mijlocFix.nrInventar+","+mijlocFix.denumire+','+mijlocFix.calculUzura());
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }


    //fisier binar serializare-transformare intr un sir de octeti
    //FISIER BINAR
    private static void salvare(String numeFisier)
    {
        try(ObjectOutputStream fout=new ObjectOutputStream(new FileOutputStream(numeFisier))) {
            //scriem pt obiecte deci facem serializare flux de tip ObjectOutputStream flux de nivel SUPERIOR
            for(MijlocFix mijlocFix:lista)
            {
                fout.writeObject(mijlocFix); //nu trimite doar octeti atasati continutului,cat si informatii despre clasa
            }
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private static void restaurare(String numeFisier)
    {
        try(FileInputStream in=new FileInputStream(numeFisier);ObjectInputStream fin=new ObjectInputStream(in)) {
            //metoda mostenita din inputStream, evaluable estimare a nr de octeti care pot fi cititi pe flux
            lista.clear(); //eliberaza lista - facem pt noi
            while(in.available()!=0)
            {
                //fin face deserializare
                lista.add((MijlocFix) fin.readObject()); //intoarce referinta de tip Object deci facem cast
            }

        } catch (Exception ex) {
            System.err.println(ex);
        }
    }
}
