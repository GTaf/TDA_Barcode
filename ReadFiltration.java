import java.io.File;
import java.io.FileNotFoundException;
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
		for (int i=0; i<=dim; i++)
			vert.add(sc.nextInt());
	}

	public String toString(){
		return "{val="+val+"; dim="+dim+"; "+vert+"}\n";
	}

}

public class ReadFiltration {

	static Vector<Simplex> readFiltration (String filename) throws FileNotFoundException {
		Vector<Simplex> F = new Vector<Simplex>();
		Scanner sc = new Scanner(new File(filename));
		sc.useLocale(Locale.US);
		while (sc.hasNext())
			F.add(new Simplex(sc));
		sc.close();
		return F;
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length != 1) {
			System.out.println("Syntax: java ReadFiltration <filename>");
			System.exit(0);
		}

		Vector<Simplex> F = readFiltration(args[0]);
		System.out.println(F);
		int[][] M = Compute.computeMatrix(F);
        Compute.printMatrix(M);
        System.out.println();
        Compute.reduction(M);
        Compute.printMatrix(M);
        Compute.computeBarcode(M,F);
    }

}
