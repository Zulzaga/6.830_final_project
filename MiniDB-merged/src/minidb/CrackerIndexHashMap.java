package minidb;

import java.util.HashMap;

public class CrackerIndexHashMap implements CrackerIndex {
    
    public HashMap<Integer, Integer> values = new HashMap<Integer, Integer>();
    public int numValues;
    
    public CrackerIndexHashMap(int numValues){
        this.numValues = numValues;
    }
    
    public int findIndexValue(Integer k) {
        if (!values.containsKey(k)) {
            return -1;
        }
        return values.get(k);
    }

    public void addValue(Integer k, int index) {
        values.put(k, index);
    }

    public void setPositionForExistingValue(Integer k, int index) throws Exception {
        values.put(k, index);
    }

    public int findNextGreaterIndex(Integer k) throws Exception {
        int nextLargest = k;
        int maxIndex = values.get(k);
        for (int i : values.keySet()) {
            if (i > k && (nextLargest == k || i < nextLargest)) {
                nextLargest = i;
                maxIndex = values.get(i);
            }
        }
        if (maxIndex == values.get(k)) {
            return this.numValues - 1;
        }
        return maxIndex;        
    }

    public int findNextSmallerIndex(Integer k) throws Exception {
        int nextSmallest = k;
        int minIndex = values.get(k);
        for (int i : values.keySet()) {
            if (i < k && (i > nextSmallest || nextSmallest == k)) {
                nextSmallest = i;
                minIndex = values.get(i);
            }
        }
        if (minIndex == values.get(k)) {
            return 0;
        }
        return minIndex;
    }

    public void removeValue(Integer k) {
        values.remove(k);    
    }

	public int getValues() {
		// TODO Auto-generated method stub
		return values.size();
	}
}
