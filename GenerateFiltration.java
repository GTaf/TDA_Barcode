import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class GenerateFiltration {

    public static void main(String[] args){
        if (args.length != 2) {
            System.out.println("Syntax: java GenerateFiltration dimension empty");
            System.out.println("dimension is a positive integer, empty value is either 1 or 0, for sphere or ball");
            System.exit(0);
        }

        int dim = Integer.parseInt(args[0]);
        File f = new File("input"+dim+".txt");

        int id = 0;

        Vector<Simplex> V = simplex(dim);
        System.out.println(V.toString());
        try {
            //fw = new FileWriter(f);
            FileWriter fw = new FileWriter (f);
            for(Simplex s : V){

                fw.write(s.val+" "+s.dim+" ");
                for (Integer i: s.vert) {
                    fw.write(i+" ");
                }
                fw.write("\n");
            }

            fw.close();
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    static void combinations2(String[] arr, int len, int startPosition, String[] result){
        if (len == 0){
            System.out.println(Arrays.toString(result));
            return;
        }
        for (int i = startPosition; i <= arr.length-len; i++){
            System.out.println(Arrays.toString(result));
            result[result.length - len] = arr[i];
            combinations2(arr, len-1, i+1, result);
        }
    }

    static Vector<Simplex> simplex(int n){
        if(n == 0){
            Vector<Simplex> F = new Vector<>();
            F.add(new Simplex((float)1.0,0,0, new Vector<>()));
            F.get(0).vert.add(0);
            F.add(new Simplex((float)1.0,0,1,new Vector<>()));
            F.get(1).vert.add(1);
            F.add(new Simplex((float)1.1,1,2,new Vector<>()));
            F.get(2).vert.add(1);
            F.get(2).vert.add(0);

            return F;
        }
        Vector<Simplex> F = simplex(n-1);
        Vector<Simplex> G = new Vector<Simplex>();
        int id = F.get(F.size()-1).id+1;
        int idref = id;
        G.add(new Simplex((float)((int)F.get(F.size()-1).val+1),0,id++, new Vector<>()));
        G.get(0).vert.add(idref);
        for (Simplex s : F){
            G.add(s);
            Vector<Integer> v =  new Vector<>(s.vert);
            v.add(idref);
            System.out.println(v.toString());
            G.add(new Simplex(s.val+1,s.dim+1,id++,v));
            G.lastElement().vert= v;
        }

        return G;
    }



    private static long binomial(int n, int k)
    {
        if (k>n-k)
            k=n-k;

        long b=1;
        for (int i=1, m=n; i<=k; i++, m--)
            b=b*m/i;
        return b;
    }
}


