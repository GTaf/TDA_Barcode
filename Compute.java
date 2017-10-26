import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class Compute {

    static int[] pivots;

    static int[][] computeMatrix(Vector<Simplex> F){
        F.sort(new Comparator<Simplex>() {
            @Override
            public int compare(Simplex simplex, Simplex t1) {
                return Float.compare(simplex.val, t1.val);
            }
        });

        for (int i = 0; i < F.size(); i++) {
            F.get(i).id = i;
        }

        int[][] res = new int[F.size()][F.size()];
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

                        if(in) res[j][i] = 1;
                    }
                }
            }
        }
        return res;
    }

    static void reduction(int[][] M){
        pivots = new int[M.length];
        Arrays.fill(pivots, -1);
        for (int j = 0; j < M.length; j++) {//pour chaque colonne
            int pivot = -1;
            boolean avant = false;
            for (int i = 0; i < M.length; i++) {
                if(M[i][j] != 0)
                    pivot = i;

            }
            pivots[j] = pivot;
            do{
                avant = false;

                if(pivot != -1){//si on a un pivot
                    for (int k = 0; k < M.length; k++) {//recherche nb avant
                        if(k!= j && M[pivot][k] != 0){//cherche un autre pivot sur la ligne

                            for (int i = 0; i < M.length; i++) {//reduction
                                M[i][k] = (M[i][j] + M[i][k])%2;
                            }
                            avant = true;//on a eu un pivot avant

                        }
                    }

                }
            }
            while ( avant == true);
        }
    }

    static void computeBarcode(int[][] M, Vector<Simplex> F) throws IOException{
        FileWriter fw = new FileWriter("out.txt");
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < pivots.length; i++) {
            if(pivots[i] >= 0){ //si il existe un pivot Bij on a l'interval [i,j)
                bw.write(F.get(pivots[i]).dim + " " + F.get(pivots[i]).val + " " + F.get(i).val);
                bw.newLine();
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
                }
                
            }
        }
        bw.close();
        fw.close();
    }

    static void printMatrix(int[][] M){
        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.print(M[i][j]+" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){

    }
}
