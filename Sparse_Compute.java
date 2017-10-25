import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;

public class Sparse_Compute {
    static int[] pivots;
    static int size;

    static SparseMatrix computeMatrix(Vector<Simplex> F){
        F.sort(new Comparator<Simplex>() {
            @Override
            public int compare(Simplex simplex, Simplex t1) {
                return Float.compare(simplex.val, t1.val);
            }
        });

        for (int i = 0; i < F.size(); i++) {
            F.get(i).id = i;
        }
        System.out.println("coucou");
        size = F.size();
        SparseMatrix res = new SparseMatrix();
        for (int i = 0; i < F.size(); i++) {//on parcours les colonnes une à une
            Simplex s = F.get(i);
            if(s.dim > 0){//On ne regarde pas les points
                for (int j = 0; j < F.size(); j++) {// on cherhce tous les ommets possibles
                    if(F.get(j).dim == s.dim-1){// si bonne taille
                        boolean in = true;
                        for (int k = 0; k < F.get(j).vert.size(); k++) {
                            if(!s.vert.contains(F.get(j).vert.get(k))){
                                in = false;
                            }
                        }

                        if(in) res.add(F.get(j).id, i);
                    }
                }
            }
        }
        return res;
    }

    static void reduction(SparseMatrix M){
        pivots = new int[size];
        Arrays.fill(pivots, -1);
        for (int j = 0; j < size; j++) {//pour chaque colonne
            int pivot = -1;
            boolean avant = false;
            for (int i = 0; i < size; i++) {
                if(M.contains(i,j))
                    pivot = i;
            }
            pivots[j] = pivot;
            do{
                avant = false;

                //System.out.println("pivot : " + pivot);
                if(pivot != -1){//si on a un pivot
                    for (int k = 0; k < size; k++) {//recherche nb avant
                        if(k!= j && M.contains(pivot,k)){//cherche un autre pivot sur la ligne
                            for (int i = 0; i < size; i++) {//reduction
                                M.add(i,k,i,j);
                            }
                            avant = true;//on a eu un pivot avant
                        }
                    }
                }
            }
            while ( avant == true);
        }
    }

    static void computeBarcode(SparseMatrix M, Vector<Simplex> F) throws IOException {
        FileWriter fw = new FileWriter("out.txt");
        BufferedWriter bw = new BufferedWriter(fw);



        for (int i = 0; i < pivots.length; i++) {
            if(pivots[i] >= 0){ //si il existe un pivot Bij on a l'interval [i,j)
                bw.write(F.get(pivots[i]).dim + " " + F.get(pivots[i]).val + " " + F.get(i).val);
                bw.newLine();
                System.out.println("dimension : " + F.get(pivots[i]).dim + "  interval [" + F.get(pivots[i]).val + ", " + F.get(i).val + ")");
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
                    System.out.println("dimension : " + F.get(i).dim + "  interval [" + F.get(i).val + ", inf)");
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
