package JavaNEAT.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import JavaNEAT.Genome.Gene;

/*CustomHashSet inspired by Lucex
The HashSet is used for the contains functionality
The ArrayList is used for the Array functionality
*/
public class CustomHashSet<T> {
    HashSet<T> set;
    ArrayList<T> list;

    public CustomHashSet(){
        set = new HashSet<>();
        list = new ArrayList<>();
    }

    public T randomElement(){
        if(set.size() > 0){
            return list.get((int)(Math.random() * size()));
        }
        return null;
    }

    public boolean containts(T item) {
        return set.contains(item);
    }

    public int size(){
        return list.size();
    }

    public void add(T item) {
        if(!set.contains(item)){
            set.add(item);
            list.add(item);
        }
    }

    public void addSorted(Gene object){
        for(int i = 0; i < this.size(); i++){
            int innovation = ((Gene)list.get(i)).getInnovation();
            if(object.getInnovation() < innovation){
                list.add(i,(T)object);
                set.add((T)object);
                return;
            }
        }
        list.add((T)object);
        set.add((T)object);
    }

    public ArrayList<T> getList(){
        return list;
    }
    
    public void clear() {
        set.clear();
        list.clear();
    }

    public T get(int index){
        return (index < 0 || index >= size()) ? null : list.get(index);
    }

    public void remove(int index) {
        if(index < 0 | index >= size()) return;
        set.remove(list.get(index));
        list.remove(index);
    }

    public void remove(T item){
        set.remove(item);
        list.remove(item);
    }
}
