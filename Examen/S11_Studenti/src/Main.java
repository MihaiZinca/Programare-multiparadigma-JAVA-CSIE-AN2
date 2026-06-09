import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import javax.script.ScriptContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.SQLOutput;
import java.util.*;

class Student
{
    int idStudent;
    String nume;
    String prenume;

    public Student() {
    }

    public Student(int idStudent, String nume, String prenume) {
        this.idStudent = idStudent;
        this.nume = nume;
        this.prenume = prenume;
    }

    @Override
    public String toString() {
        return idStudent + " " + nume + " " + prenume;
    }
}

class Note
{
    int idStud;
    String materie;
    double nota;

    public Note() {
    }

    public Note(int idStud, String materie, double nota) {
        this.idStud = idStud;
        this.materie = materie;
        this.nota = nota;
    }

    @Override
    public String toString() {
        return idStud + " " + materie + " " + nota;
    }
}


public class Main {
    static void main(String[] args)throws Exception {

        List<Student> studenti=new ArrayList<Student>();
        FileReader fr=new FileReader("S11_studenti.json");
        JSONArray arry=new JSONArray(new JSONTokener(fr));
        for(int i=0;i<arry.length();i++)
        {
            JSONObject obj=arry.getJSONObject(i);

            int idStudent=obj.getInt("IdStudent");
            String nume=obj.getString("Nume");
            String prenume=obj.getString("Prenume");

            Student stud=new Student();
            stud.idStudent=idStudent;
            stud.nume=nume;
            stud.prenume=prenume;
            studenti.add(stud);
        }
        fr.close();

        System.out.println("Numar total studeti: "+studenti.size());

        List<Note> note=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader("S11_note.txt"));
        String linie;
        while( (linie=br.readLine())!=null)
        {
            String[] p=linie.split(",");

            int idStud=Integer.parseInt(p[0].trim());
            String materie=p[1].trim();
            double nota=Double.parseDouble(p[2].trim());

            Note n=new Note(idStud,materie,nota);
            note.add(n);
        }
        br.close();

        Map<String,Integer> map=new HashMap<>();
        for(Note n:note)
        {
            map.put(n.materie,map.getOrDefault(n.materie,0)+1);
        }

        List<String>lista=new ArrayList<>(map.keySet());
        System.out.println("\n -Lista discipline: ");
        for(String s:lista)
        {
            System.out.println(s+","+map.get(s)+" note");
        }

        //Citire tastatura
        Scanner sc=new Scanner(System.in);
        System.out.println("\nIntorduceti materia cautata:");
        String materieC=sc.nextLine();

        System.out.println("\nCatalog pentru materia " + materieC + ":");
        for(Note n:note)
        {
            if(n.materie.equalsIgnoreCase(materieC))
            {
                for(Student s:studenti)
                {
                    if(s.idStudent==n.idStud) {
                        System.out.println(s.nume + "," + s.prenume + "," + n.nota);
                    }

                }
            }
        }

    }
}
