package minidb;
/**
 * Nodes of the cracker index have: 
 *  - key: value from the CrackerColumn around which the column is partitioned
 *  - data: position (index) of the key in array of values of CrackerColumn
 *  
 *  ******IMPORTANT*****
 *  INVARIANT:
 *  
 *  For given key k and position p in a single index node, we know that
 *  all values whose indices are less than or equal p, have values LESS THAN OR EQUAL k.
 *  
 *  Therefore, if have node (100, 5) and query arrives asking for values <100, we cannot
 *  simple give subarray [0:5] because this array contains values <=100. 
 *  Solution: since we work with integers, we can change the query to <=99
 *  which is why we value = value-1 in CrackerColumn.crack for open ranges
 *  
 *  
 * @author danamukusheva
 *
 */
public interface CrackerIndex {
	
	/**
	 * Look up the node in the cracker index, get the data(index aka position)
	 * stored in the node; if node not in the index, return -1
	 * @param k value to look up
	 * @return index of the value in the CrackerColumn
	 */
	int findIndexValue(Integer k);
	
	/**
	 * Look up the element that comes right after k, 
	 * return its position in the values array
	 * @param k Integer value (node key)
	 * @return int index
	 * @throws Exception if node with value k wasn't found
	 */
	int findNextGreaterIndex(Integer k) throws Exception;
	
	/**
	 * Look up the element that comes right before k, 
	 * return its position in the values array
	 * @param k Integer value (node key)
	 * @return int index
	 * @throws Exception if node with value k wasn't found
	 */
	int findNextSmallerIndex(Integer k) throws Exception;
	
	
	/**
	 * Add new value to the CrackerIndex as query arrives
	 * if it does not exist in the tree
	 * @param k
	 */
	void addValue(Integer k, int index);
	
	void removeValue(Integer k);
	
	/**
	 * Once the value was added to the tree, the cracker column was cracked, we now
	 * knoe the position of the pivot point, set it.
	 * @param k Integer, pivot value, must be present in the tree
	 * @param index
	 * @throws Exception if node with value k is not present in the tree
	 */
	void setPositionForExistingValue(Integer k, int index) throws Exception;
	
}
