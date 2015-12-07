package minidb;

import java.util.Iterator;

/**
 * Interface to be implemented by various column types:
 * SimpleColumn, SortedColumn, CrackedColumn
 * 
 * @author danamukusheva
 *
 */
public interface Column {
	
	/**
	 * Get column name
	 * @return String
	 */
	String getName();
	
	/**
	 * Get iterator over tuples (Integer objects)
	 * @return
	 */
	Iterator<Integer> getIterator();
	
	/**
	 * Tell if this column object uses cracking
	 * @return boolean
	 */
	boolean useCracking();
	/**
	 * Check if the column has been cracked (should have a cracker column and index then)
	 * Not really applicable for CrackedColumn, only for SimpleColumn and potentially, SortedColumn.
	 * @return True if has been cracked
	 */
	boolean isCracked();
	/**
	 * Return 
	 * @return
	 */
	
	/**
	 * Mark as cracked. Should be followed by creation of a CrackedColumn and CrackedIndex instances
	 * Not applicable for CrackedColumn.
	 */
	void markCracked();
	
	/** 
	 * Mark as uncracked. Should be followed by setting CrackedColumn and CrackedIndex instances to null.
	 * Not applicable for CrackedColumn.
	 */
	void markUncracked();
	
	/**
	 * If the column has been cracked (need to check first), return its CrackerColumn
	 * @return CrackerColumn
	 */
	CrackerColumn getCrackerColumn();
	
	/**
	 * If the column has been cracked (need to check first), return its CrackerIndex
	 * @return CrackerIndex
	 */
	
	CrackerIndex getCrackerIndex();
	/**
	 * Initialize CrackerColumn instance for this Column obj
	 * pass in values, they will be copied
	 */
	void initializeCrackerColumn();
	
	/**
	 * Forget about all the cracking, restore column to uncracked state
	 */
	
	void removeCrackerColumn();

	/**
	 * Add a tuple at the specified index
	 * @param value Integer object
	 * @param index int, MUST BE a valid location (less then the column size)
	 */
	void insertTuple(Integer value, int index);
	
	/**
	 * Add a tuple at any location
	 * @param value Integer object
	 */
	void insertTuple(Integer value);
	
	/**
	 * Delete a specified value from a specified location
	 * @param value
	 * @param index
	 * @throws Exception if the value at location is not the one specified
	 */
	void deleteTuple(Integer value, int index) throws Exception;
	
	/**
	 * Delete a tuple from the specified location, value doesn't matter
	 * @param index
	 */
	void deleteTuple(int index);
	
	/**
	 * Swap values at two specified locations.
	 * @param index1
	 * @param index2
	 * @throws Exception 
	 */
	void swap(int index1, int index2) throws Exception;
	
	/**
	 * Resets the column values and index back to original, forgets all prior knowledge.
	 */
	void reset();
}
