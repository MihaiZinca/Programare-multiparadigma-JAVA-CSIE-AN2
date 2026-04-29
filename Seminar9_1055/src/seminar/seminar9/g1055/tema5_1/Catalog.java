package seminar.seminar9.g1055.tema5_1;

import java.util.HashMap;
import java.util.Map;

public class Catalog {
    private Map<Integer,Map<String,Integer>> catalog = new HashMap<>();

    public synchronized void scrieNota(int idStudent,String disciplina,int nota){
        if (catalog.containsKey(idStudent)){
            catalog.get(idStudent).put(disciplina,nota);
        } else {
            Map<String,Integer> notaDisciplina = new HashMap<>();
            notaDisciplina.put(disciplina,nota);
            catalog.put(idStudent,notaDisciplina);
        }
    }

    public Map<Integer, Map<String, Integer>> getCatalog() {
        return catalog;
    }
}
