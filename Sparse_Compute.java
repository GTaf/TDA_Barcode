import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Sparse_Compute {
    private static int[] pivots;
    private static int size;

    static SparseMatrix computeMatrix(Vector<Simplex> F){
        F.sort(new Comparator<Simplex>() {
            @Override
            public int compare(Simplex simplex, Simplex t1) {
                return Float.compare(simplex.val, t1.val);
            }
        });
        HashMap<Integer, LinkedList<Integer>> h = new HashMap<>();//contient tous les ensembles contenant le point key

        for (int i = 0; i < F.size(); i++) {
            F.get(i).id = i;
            //if(F.get(i).dim == 0) continue;
            for (int j = 0; j < F.get(i).vert.size(); j++) {
                if(h.get(F.get(i).vert.get(j)) == null) h.put(F.get(i).vert.get(j), new LinkedList<Integer>());
                h.get(F.get(i).vert.get(j)).add(i);
            }
        }
        HashMap<Integer, Set<Integer>> g = new HashMap<>();
        for (int i = 0; i < F.size(); i++) {//chaque simplex
            Simplex s = F.get(i);
            if(F.get(i).dim == 0) continue;
            for (int j = 0; j < s.vert.size(); j++) {//chaque sommet du simplex
                for (int k = 0; k < h.get(s.vert.get(j)).size(); k++) {//chaque simplex qui a un sommet commun avec le notre
                    if(g.get(i) == null) g.put(i, new HashSet<Integer>());
                    g.get(i).add(h.get(s.vert.get(j)).get(k));
                }
            }
        }
        //System.out.println("Taille de F : "+F.size());
        size = F.size();
        SparseMatrix res = new SparseMatrix();
        for (int i = 0; i < F.size(); i++) {//on parcours les colonnes une à une

            Simplex s = F.get(i);
            if(s.dim > 0){//On ne regarde pas les points
                //System.out.println("Simplex : "+s.val+"      vertices"+s.vert);
                Iterator<Integer> it = g.get(i).iterator();
                while(it.hasNext()) {// on cherhce tous les ommets possibles
                    int j = it.next();//j indice d'un autre simplex
                    if(F.get(i).dim-F.get(j).dim != 1) continue;
                    boolean in = true;
                    for (int k = 0; k < F.get(j).vert.size(); k++) {
                        if(!s.vert.contains(F.get(j).vert.get(k))){
                            in = false;
                        }
                    }
                    //System.out.println(in);
                    if(in) res.add(j, i);
                }
            }
        }
        return res;
    }

    static void reduction(SparseMatrix M){
        pivots = new int[size];
        Arrays.fill(pivots, -1);
        for (int j = 0; j < size; j++) {//pour chaque colonne
            if(!M.containsColumn(j)){
                pivots[j] = -1;
                continue;//passe à la suite de la boucle
            }
            boolean avant;
            pivots[j] = M.maxColumn(j);

            LinkedList<Integer> l = M.getRow(pivots[j]);
            while (l.size() != 1) {//recherche nb avant
                
                int k = l.peekLast();//enleve dernier élèment
                if(k == j){//cherche un autre pivot sur la ligne
                    k = l.pollLast();
                    l.push(k);
                    continue;
                }
                for (int i = 0; i < size; i++) {//reduction
                    M.add(i,k,i,j);
                }
            }

        }
    }

    static void computeBarcode(SparseMatrix M, Vector<Simplex> F) throws IOException {
        FileWriter fw = new FileWriter("out.txt");
        BufferedWriter bw = new BufferedWriter(fw);



        for (int i = 0; i < pivots.length; i++) {
            if(pivots[i] >= 0){ //si il existe un pivot Bij on a l'interval [i,j)
                bw.write(F.get(pivots[i]).dim + " " + F.get(pivots[i]).val + " " + F.get(i).val);
                bw.newLine();
                //System.out.println("dimension : " + F.get(pivots[i]).dim + "  interval [" + F.get(pivots[i]).val + ", " + F.get(i).val + ")");
            }
            if(pivots[i] == -1){ //si pas de pivot alors colonne vide
                boolean iInPivots = false;  //on regarde si il y a un pivot sur la ligne i
                for(int p : pivots){
                    if(p == i){
                        iInPivots = true;
                        break;
                    }
                }
                if(!iInPivots){     //si pas de pivot sur la ligne i alors on a l'intervale [i, inf)
                    bw.write(F.get(i).dim + " " + F.get(i).val + " inf");
                    bw.newLine();
                    //System.out.println("dimension : " + F.get(i).dim + "  interval [" + F.get(i).val + ", inf)");
                }

            }
        }
        bw.close();
        fw.close();
    }

    static void printMatrix(SparseMatrix M){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(M.contains(i,j)) System.out.print("1 ");
                else System.out.print("0 ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){

    }
}
