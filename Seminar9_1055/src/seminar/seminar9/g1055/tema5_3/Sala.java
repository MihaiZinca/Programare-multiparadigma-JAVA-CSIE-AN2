package seminar.seminar9.g1055.tema5_3;

public class Sala {

    private String[][] sala;
    private int locuriLibere=0;
    public Sala(int[]l)
    {
        sala=new String[l.length][];
        for(int i=0;i<sala.length;i++)
        {
            sala[i]=new String[l[i]];
            locuriLibere+=l[i];

        }
    }

    public synchronized void rezervare(String alias, int nrLocuri)
    {
        if (nrLocuri > locuriLibere) {
            System.out.println("Nu sunt suficiente locuri pentru " + alias);
            return;
        }

        for (int i = 0; i < sala.length; i++)
        {
            int contor = 0;
            int start = -1;

            for (int j = 0; j < sala[i].length; j++)
            {
                if (sala[i][j] == null)
                {
                    if (contor == 0) {
                        start = j;
                    }
                    contor++;
                    if (contor == nrLocuri)
                    {
                        for (int k = start; k < start + nrLocuri; k++)
                            sala[i][k] = alias;

                        locuriLibere -= nrLocuri;
                        System.out.println(alias + " a rezervat " + nrLocuri + " locuri pe randul " + i);
                        return;
                    }
                } else
                {
                    contor = 0;
                }
            }
        }
        System.out.println("Locuri ocupate pentru" + alias);
    }
}

//cerinta 2 rez(nu intra la examen)
