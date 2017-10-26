import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.Vector;



class Simplex {
	float val;
	int dim;
	Vector<Integer> vert;
	public int id = 0;

	Simplex(Scanner sc){
		val = sc.nextFloat();
		dim = sc.nextInt();
		vert = new Vector<Integer>();

		for (int i=0; i<=dim; i++){
            vert.add(sc.nextInt());
        }

	}

	public String toString(){
		return "{val="+val+"; dim="+dim+"; "+vert+"}\n";
	}

}

public class ReadFiltration {

	static Vector<Simplex> readFiltration (String filename) throws FileNotFoundException{
		Vector<Simplex> F = new Vector<Simplex>();
		Scanner sc = new Scanner(new File(filename));
		sc.useLocale(Locale.US);
		while (sc.hasNext())
			F.add(new Simplex(sc));
		sc.close();
		return F;
	}

    public static void main(String[] args) throws FileNotFoundException, IOException {
        if (args.length != 1) {
            System.out.println("Syntax: java ReadFiltration <filename>");
            System.exit(0);
        }

        Vector<Simplex> F = readFiltration(args[0]);

        System.out.println("Filtration : ");
        //System.out.println(F);
        SparseMatrix M = Sparse_Compute.computeMatrix(F);
        //int[][] Mp = Compute.computeMatrix(F);

        System.out.println("Matrice : ");
        //Sparse_Compute.printMatrix(M);
        //System.out.println();
        //Compute.printMatrix(Mp);

        System.out.println("Reduction :");
        //Compute.reduction(Mp);
        //Compute.printMatrix(Mp);
        //System.out.println();
        Sparse_Compute.reduction(M);
        Sparse_Compute.printMatrix(M);

        System.out.println("Barcode : ");
        Sparse_Compute.computeBarcode(M,F);
			
    }

}
