package minidb;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/**
 * This class represents a column that simply stored an ArrayList of integers.
 * @author danamukusheva
 *
 */
public class SimpleColumn implements Column {
	
	public String name;
	public boolean useCracking; //is the column to be cracked or scanned as it is
	
	private ArrayList<Integer> values = new ArrayList<Integer>();
	
	private boolean isCracked = false;
	private CrackerColumn crackerColumn = null;
	public int index;

	public SimpleColumn(String name, boolean useCracking, int index){
		this.name = name;
		this.useCracking = useCracking;
		this.index = index;
	}
	
	public String getName() {
		return this.name;
	}
	
	/**
	 * To be used if column does not use cracking and simple scanned
	 * from beginning to end
	 */
	public Iterator<Integer> getIterator() {
		return this.values.iterator();
	}
	
	/**
	 * Values with positions >= than index are shifted by one
	 * to the right.
	 * 
	 * Not sure if useful
	 */
	public void insertTuple(Integer value, int index) {
		this.values.add(index, value);

	}
	/**
	 * Value appended to the end of the list 
	 * Useful while populating the columns
	 */
	public void insertTuple(Integer value) {
		this.values.add(value);
	}
	
	/**
	 * Not sure if useful
	 */
	public void deleteTuple(Integer value, int index) throws Exception {
		if (this.values.get(index) == value){
			this.values.remove(index);
		}
		else{
			throw new Exception("SimpleColumn deleteTuple: there is different value at the specified index!");
		}

	}

	/**
	 * Not sure if useful
	 */
	public void deleteTuple(int index) {
		this.values.remove(index);

	}
	/**
	 * Not sure if useful
	 */
	public void swap(int index1, int index2) throws Exception {
		if (index1<0 || index1>this.values.size() || index2<0 || index2>this.values.size()){
			throw new Exception("SimpleColumn swap: specified index is out of bounds!");
		}
		Collections.swap(this.values,index1,index2);

	}

	public boolean isCracked() {
		return this.isCracked;
	}

	public void markCracked() {
		this.isCracked = true;
		
	}

	public void markUncracked() {
		this.isCracked = false;
		
	}

	public CrackerColumn getCrackerColumn() {
		// TODO Auto-generated method stub
		return this.crackerColumn;
	}

	public CrackerIndex getCrackerIndex() {
		// TODO Auto-generated method stub
		return this.crackerColumn.getCrackerIndex();
	}
	
	/**
	 * Create a new instance of cracker column
	 */
	public void initializeCrackerColumn() {
		this.crackerColumn = new CrackerColumn(this.values, index);	
	}

	public void removeCrackerColumn() {
		this.crackerColumn = null;
		
	}
	public boolean useCracking(){
		return this.useCracking;
	}

	public void reset() {
		if (crackerColumn != null) {
			crackerColumn.reset();
		}
	}

	public boolean isSorted() {
		// TODO Auto-generated method stub
		return false;
	}
}
