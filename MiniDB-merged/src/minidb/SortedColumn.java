package minidb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

public class SortedColumn implements Column{
	
	public String name;
	public ArrayList<Integer> values = new ArrayList<Integer>();
	
	public SortedColumn(String name){
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public ArrayList<Integer> getValues(){
		return this.values;
	}

	public Iterator<Integer> getIterator() {
		return values.iterator();
	}

	public boolean useCracking() {
		return false;
	}
	
	/*
	 * Doesn't preserve order
	 * @see minidb.Column#insertTuple(java.lang.Integer, int)
	 */
	public void insertTuple(Integer value, int index) {
		this.values.add(index, value);
		
	}
	/*
	 * Doesn't preserve order, should be called in bulk populating and then sorted
	 * with a separate method
	 * @see minidb.Column#insertTuple(java.lang.Integer)
	 */
	public void insertTuple(Integer value) {
		this.values.add(value);
		
	}
	
	public void sort(boolean useQuickSort){
		if(useQuickSort){
			try {
				this.quickSort(0, this.values.size()-1);
			} catch (Exception e) {
				System.err.print("SortedColumn sort: error in QuickSort");
				e.printStackTrace();
			}
		}
		else{ //Radix sort
			this.radixSort();
		}
		
	}
	
	/*
	 * SORTING ALGORITHMS
	 */
	/**
	 * Source: http://java2novice.com/java-sorting-algorithms/quick-sort/
	 * @param lowerIndex
	 * @param higherIndex
	 * @throws Exception 
	 */
	private void quickSort(int lowerIndex, int higherIndex) throws Exception {
        
        int i = lowerIndex;
        int j = higherIndex;
        // calculate pivot number, I am taking pivot as middle index number
        int pivot = this.values.get(lowerIndex+(higherIndex-lowerIndex)/2);
        // Divide into two arrays
        while (i <= j) {
            /**
             * In each iteration, we will identify a number from left side which
             * is greater then the pivot value, and also we will identify a number
             * from right side which is less then the pivot value. Once the search
             * is done, then we exchange both numbers.
             */
            while (this.values.get(i) < pivot) {
                i++;
            }
            while (this.values.get(j) > pivot) {
                j--;
            }
            if (i <= j) {
                this.swap(i, j);
                //move index to next position on both sides
                i++;
                j--;
            }
        }
        // call quickSort() method recursively
        if (lowerIndex < j)
            quickSort(lowerIndex, j);
        if (i < higherIndex)
            quickSort(i, higherIndex);
    }
	
	// Sort the numbers beginning with least-significant digit
    public void radixSort(){
    	int maxValPrecision = 1000000000;//TODO:revisit
        // Largest place for a 32-bit int is the 1 billion's place
        for(int place=1; place <= maxValPrecision; place *= 10){
            // Use counting sort at each digit's place
            this.values = this.countingSort(place);
        }

    }

    private ArrayList<Integer> countingSort(int place){
    	int arrayLength = this.values.size();
        int[] out = new int[arrayLength];

        int[] count = new int[10];

        for(int i=0; i < arrayLength; i++){
            int digit = getDigit(this.values.get(i), place);
            count[digit] += 1;
        }

        for(int i=1; i < count.length; i++){
            count[i] += count[i-1];
        }

        for(int i = arrayLength-1; i >= 0; i--){
            int digit = getDigit(this.values.get(i), place);

            out[count[digit]-1] = this.values.get(i);
            count[digit]--;
        }
        ArrayList<Integer> outFormated = new ArrayList<Integer>();
        for(int i = 0; i < arrayLength; i++){
        	outFormated.add(out[i]);
        }
        return outFormated;

    }

    private static int getDigit(int value, int digitPlace){
        return ((value/digitPlace ) % 10);
    }


	/**
	 * Not sure if useful
	 */
	public void deleteTuple(Integer value, int index) throws Exception {
		if (this.values.get(index) == value){
			this.values.remove(index);
		}
		else{
			throw new Exception("SortedColumn deleteTuple: there is different value at the specified index!");
		}

	}

	/**
	 * Not sure if useful
	 */
	public void deleteTuple(int index) {
		this.values.remove(index);

	}

	public void swap(int index1, int index2) throws Exception {
		if (index1<0 || index1>this.values.size() || index2<0 || index2>this.values.size()){
			throw new Exception("SortedColumn swap: specified index is out of bounds!");
		}
		Collections.swap(this.values,index1,index2);
		
	}
	
	/*
	 * THESE METHODS SHOULD NEVER BE CALLED!
	 * @see minidb.Column#isCracked()
	 */
	
	public boolean isCracked() {
		return false;
	}
		
	public void markCracked() {
	}

	public void markUncracked() {
	}

	public CrackerColumn getCrackerColumn() {
		return null;
	}

	public CrackerIndex getCrackerIndex() {
		return null;
	}

	public void initializeCrackerColumn() {
	}

	public void removeCrackerColumn() {
	}

}
