package minidb;
import java.util.ArrayList;

public class CrackerIndexSortedList implements CrackerIndex {
    
    // Node for holding value and index
    public class Node {
        public int value;
        public int index;
        
        public Node(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }
    
    public ArrayList<Node> values = new ArrayList<Node>();
    public int numValues;
    
    public CrackerIndexSortedList(int value){
        this.numValues = value;
    }
    
    public synchronized int findIndexValue(Integer k) {
        for (int i=0; i < values.size(); i++) {
            if (values.get(i).value == k) {
                return values.get(i).index;
            }
        }
        return -1;
    }

    public synchronized void addValue(Integer k, int index) {
        int ind = 0;
        while (ind < values.size()) {
            if (values.get(ind).value > k) {
                break;
            }
            ind++;
        }
        Node tmp = new Node(k, index);
        while (ind < values.size()) {
            // shift all values by one.
            Node old_value = values.get(ind);
            values.set(ind, tmp);
            tmp = old_value;
            ind++;
        }
        values.add(tmp);
        return;
    }

    public synchronized void setPositionForExistingValue(Integer k, int index) throws Exception {
        for (int i=0; i < values.size(); i++) {
            if (values.get(i).value == k) {
                values.get(i).index = index;
            }
        }
    }

    public synchronized int findNextGreaterIndex(Integer k) throws Exception {
        if (values.get(values.size() - 1).value == k) {
            return this.numValues - 1;
        }
        for (int i=0; i < values.size() - 1; i++) {
            if (values.get(i).value == k) {
                return values.get(i+1).index;
            }
        }

        // should never come here
        throw new Exception();
    }

    public synchronized int findNextSmallerIndex(Integer k) throws Exception {
        if (values.get(0).value == k) {
            return 0;
        }
        
        for (int i=1; i < values.size(); i++) {
            if (values.get(i).value == k) {
                if (values.get(i-1).index == -1) {
                    throw new Exception();
                }
                return values.get(i-1).index;
            }
        }

        // should never come here
        throw new Exception();      
    }

    public synchronized void removeValue(Integer k) {
        for (int i=0; i < values.size(); i++) {
            if (values.get(i).value == k) {
                values.remove(i);
            }
        }
    }
}
