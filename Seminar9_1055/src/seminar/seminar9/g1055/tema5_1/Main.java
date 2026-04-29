package seminar.seminar9.g1055.tema5_1;

import java.io.File;
import java.io.FilenameFilter;

public class Main {
    public static void main(String[] args) {
        try{
            File[] fisiereCsv = new File(".").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });
            Cititor[] cititori = new Cititor[fisiereCsv.length];
            Catalog catalog = new Catalog();
            for (int i = 0; i < cititori.length; i++) {
                cititori[i] = new Cititor(fisiereCsv[i].getName(),catalog);
            }
            for (int i = 0; i < cititori.length; i++) {
                cititori[i].start();
            }
            for (int i = 0; i < cititori.length; i++) {
                cititori[i].join();
            }
            System.out.println("Catalog:");
            int k=1;
            catalog.getCatalog().forEach( (idStudent,note)->{
                System.out.println("Note pentru studentul "+idStudent+":");
                note.forEach( (disciplina,nota) -> System.out.println(disciplina+":"+nota) );
            } );
        }
        catch (Exception ex){
            System.err.println(ex);
        }
    }
}
