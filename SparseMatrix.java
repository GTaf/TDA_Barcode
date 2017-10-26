import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

public class SparseMatrix {
    private HashMap< Integer, LinkedList<Integer>> rowColumn;
    private HashMap< Integer, LinkedList<Integer> > columnRaw;


    public SparseMatrix(){
        rowColumn = new HashMap< Integer, LinkedList<Integer> >();
        columnRaw = new HashMap< Integer, LinkedList<Integer> >();
    }

    public void add(int i, int j){
        if(rowColumn.get(i) == null) rowColumn.put(i, new LinkedList<Integer>());
        rowColumn.get(i).add(j);

        if(columnRaw.get(j) == null) columnRaw.put(j, new LinkedList<Integer>());
        columnRaw.get(j).add(i);
    }

    public  void remove(int i, int j){
        rowColumn.get(i).remove((Object) j);
        columnRaw.get(j).remove((Object) i);

        if(rowColumn.get(i).isEmpty()) rowColumn.remove(rowColumn.get(i));
        if(columnRaw.get(j).isEmpty()) columnRaw.remove(rowColumn.get(j));
    }

    public Boolean contains(int i, int j){
        return (rowColumn.get(i)!=null && rowColumn.get(i).contains(j));
    }
    public Boolean containsColumn(int j) {
        if(columnRaw.get(j) == null) return false;
        if(columnRaw.get(j).size() == 0) return false;
        return true;}
    public int maxColumn(int j){
        return Collections.max(columnRaw.get(j));
    }
    public int minLine(int i){return Collections.min(rowColumn.get(i));}
    public LinkedList<Integer> getRow(int i){return rowColumn.get(i);}


    public void add(int i, int j, int ip, int jp){//ajoute [ip,jp] a [i,j]
        if (this.contains(ip,jp)){
            if(this.contains(i,j)) this.remove(i,j);
            else this.add(i,j);
        }
    }


}
