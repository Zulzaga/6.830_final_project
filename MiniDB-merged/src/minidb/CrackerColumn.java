package minidb;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class represents a Cracker Column, a copy of a regular column
 * that will be partitioned with each range query. 
 * @author danamukusheva
 *
 */
public class CrackerColumn{
	public class Pair{
		public int first;
		public int second;
		
		public Pair(int first, int second){
			this.first = first;
			this.second = second;
		}
		
		public int getFirst(){
			return this.first;
		}
		public int getSecond(){
			return this.second;
		}
	}
	
	public ArrayList<Integer> values;
	public ArrayList<Integer> originalValues;
	public CrackerIndex crackerIndex;
	private int minPartitionSize = 10000;
	
	public CrackerColumn(ArrayList<Integer> originalValues, int index){
		this.originalValues = originalValues;
		this.values = this.copyValues(originalValues);
		if (index == 0) {
			this.crackerIndex = new CrackerIndexAVL(originalValues.size());
		} else if (index == 1) {
			this.crackerIndex = new CrackerIndexHashMap(originalValues.size());
		} else {
			this.crackerIndex = new CrackerIndexSortedList(originalValues.size());
		}
	}
	
	/**
	 * Deep copy all the values into a new array
	 * @param values
	 * @return
	 */
	private ArrayList<Integer> copyValues(ArrayList<Integer> values){
		ArrayList<Integer> newValues = new ArrayList<Integer>();
		for (Integer value:values){
			newValues.add(value);
		}
		return newValues;		
	}
	
	public CrackerIndex getCrackerIndex(){
		return this.crackerIndex;
	}
	public void printValues(){
		System.out.println(this.values);
	}
	
	public ArrayList<Integer> getValues(){
		return this.values;
	}
	
	/**
	 * OpenRange (range is < or > or <= or >=)
	 * Cracking involves:
	 *  - check value in the CrackerIndex
	 *      - if exists, return array (to the left, or to the right, depending on range sign)
	 *      - if doesn't, insert with undefined position, find it, and 
	 *      crack array between the parent and the node, setPosition for the node
	 * @throws Exception 
	 *      
	 * 
	 */
	public ArrayList<Integer> crack(Integer value, String range) throws Exception{
		if (range.equals("<=") || range.equals(">")){
			
		}
		else {//range == < or range == >=
			value = value-1; // to be able to use Index (look into CrackerIndex invariant)
		}
		//lookup in the index
		int index = this.crackerIndex.findIndexValue(value);
		//found it
		if (index != -1){
			if (range.equals("<=") || range.equals("<")) { //everything to the left, incl
				return new ArrayList(this.values.subList(0, index+1)); //first arg included, second not according to java specs
			}
			else{
				return new ArrayList(this.values.subList(index+1, this.values.size()));
			} 
		}
		//haven't found it, crack the column
		else{
			this.crackerIndex.addValue(value, -1); //for now position is -1 as we don't know it
			//System.out.println(((CrackerIndexSortedList)this.crackerIndex).values);
			int posL = this.crackerIndex.findNextSmallerIndex(value); //must be valid > 0
			int posH = this.crackerIndex.findNextGreaterIndex(value); //must be valid <size of values array
			// DECIDE IF PARTITION FURTHER, MIN partition size
			//System.out.println("Cracking between positions: " + crackerIndex.getValues());
			int posPivot  = this.crackInTwo(posL, posH, value, true);
		
            this.crackerIndex.setPositionForExistingValue(value, posPivot); //change the position since we know it
            
            if (range.equals("<=") || range.equals("<")){ //everything to the left, incl
                
                return new ArrayList(this.values.subList(0, posPivot+1)); //first arg included, second not according to java specs
            }
            else{
                return new ArrayList(this.values.subList(posPivot+1, this.values.size()));
            }
//			int minPart = findMinPartition(posL, posH, value, true);
//			if (minPart < minPartitionSize || (posH - posL - minPart) < minPartitionSize) {
//			    // if partition size < minPartitionSize don't partition it anymore
//			    crackerIndex.removeValue(value);
//			    return new ArrayList(this.values.subList(posL, posH));
//			} else {
//    			int posPivot  = this.crackInTwo(posL, posH, value, true);
//
//    			this.crackerIndex.setPositionForExistingValue(value, posPivot); //change the position since we know it
//    			if (range.equals("<=") || range.equals("<")){ //everything to the left, incl
//    				
//    			    return new ArrayList(this.values.subList(0, posPivot+1)); //first arg included, second not according to java specs
//    			}
//    			else{
//    				return new ArrayList(this.values.subList(posPivot+1, this.values.size()));
//    			}
//			}
		}
		
	}
	
	public int findMinPartition(int posL, int posH, Integer val, boolean inc) {
        int posx1 = posL;
        int posx2 = posH;
        int lessNum = 0;
        
        while (posx1 < posx2){
            if (inc) {
                if (values.get(posx1) <= val) {
                    lessNum++;
                }
            } else {
                if (values.get(posx1) < val) {
                    lessNum++;
                }
            }
            posx1++;
        }
	    return lessNum;
	}
	
	/**
	 * Closed range
	 * @param low lower bound
	 * @param high upper bound
	 * @param range String, one of <<, <=<, <<=, <=<=
	 * @return
	 */
	public ArrayList<Integer> crack(Integer low, Integer high, String range){
		//TODO
		return null;	
	}
	
	/*
	 * Cracking algorithms
	 */
	/**
	 * Source: DB cracking paper
	 * Partitions the values between indices posL and posH (inclusive) 
	 * around pivot value val
	 * @param posL int, index of the beginning of the range
	 * @param posH int, index of the end of the range
	 * @param val pivot value, used to partition the array around it
	 * @param inc boolean IMPORTANT //TODO pay attention to it!
	 * if true, than we have two partitions: everything <= pivot value and everything > pivot value
	 * if false, two partitions: everything < pivot and everything >= pivot
	 * @return pivot position
	 */
	public int crackInTwo(int posL, int posH, Integer val, boolean inc){
	    //System.out.println(posL + " " + posH);
		Integer x1 = this.values.get(posL);
		Integer x2 = this.values.get(posH);
		int posx1 = posL;
		int posx2 = posH;
		
		while (posx1 < posx2){
			//if val is inclusive
			if (inc){
				if (x1 <= val){
					posx1+=1;
					x1 = this.values.get(posx1);
				}
				else{
					while (x2 > val && posx2 > posx1){
						posx2-=1;
						x2 = this.values.get(posx2);
					}
					try {
						this.swap(posx1, posx2);
						posx1++;
						posx2 = (posx2==0)?posx2:(posx2-1);// so we don't run out of bounds
						x1 = this.values.get(posx1);
						x2 = this.values.get(posx2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.err.println("CrackerColumn crackInTwo: indexOutOfBound");
						System.out.println(posx1);
						System.out.println(posx2);
						e.printStackTrace();
					}
				}
				
			}
			else{//val is exclusive
				if (x1 < val){
					posx1++;
					x1 = this.values.get(posx1);
				}
				else{
					while (x2 >= val && posx2 > posx1){
						posx2--;
						x2 = this.values.get(posx2);
					}
					try {
						this.swap(posx1, posx2);
						posx1++;
						posx2--;
						x1 = this.values.get(posx1);
						x2 = this.values.get(posx2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						System.err.println("CrackerColumn crackInTwo: indexOUtOfBound");
					}
				}
			}
		}
		
		return this.findPivotIndex(posL, posH, val, inc);
	
	}
	
	public Pair crackInThree(int posL, int posH, Integer low, Integer high, boolean incL, boolean incH) throws Exception{
		Integer x1 = this.values.get(posL);
		Integer x2 = this.values.get(posH);
		int posx1 = posL;
		int posx2 = posH;

		while(compareTheta1(x2, high, incL, incH) && (posx2>posx1)){
			posx2--;
			x2 = this.values.get(posx2);
		}
		
		
		
		int posx3 = posx2;
		Integer x3 = this.values.get(posx3);
		while(compareTheta2(x3, low, incL, incH) && (posx3>posx1)){

			if (compareTheta1(x3, high, incL, incH)){
				this.swap(posx2, posx3);
				posx2--;
				x2 = this.values.get(posx2);
			}
			posx3--;
			x3 = this.values.get(posx3);
		}
		
//		System.out.println("***");
		while(posx1<=posx3){
			if (compareTheta3(x1, low, incL, incH)){
				posx1++;
				x1 = this.values.get(posx1);
//				System.out.println("**posx1 "+posx1);
//				System.out.println("**x1 "+x1);
			}
			else{
				this.swap(posx1, posx3);
				System.out.println("posx1 "+posx1);
				System.out.println("posx2 "+posx2);
				System.out.println("posx3 "+posx3);
				System.out.println(this.values);
				x3 = this.values.get(posx3);
				x1 = this.values.get(posx1);
				x2 = this.values.get(posx2);
				while(compareTheta2(x3, low, incL, incH) && (posx3>posx1)){
					//System.out.println("x3 "+x3);
					if(compareTheta1(x3, high, incL, incH)){
						this.swap(posx2, posx3);
						posx2--;
						x2 = this.values.get(posx2);
					}
					posx3--;
					x3 = this.values.get(posx3);
//					System.out.println("posx1 "+posx1);
//					System.out.println("posx2 "+posx2);
//					System.out.println("posx3 "+posx3);
					x1 = this.values.get(posx1);
					x2 = this.values.get(posx2);
				}
				
			}
		}
		//System.out.println("***");
		
		return new Pair(this.findPivotIndexLow(posL, posH, low, incL), this.findPivotIndexLow(posL, posH, high, incH)); //TODO
	}
	
	private boolean compareTheta1(Integer val1, Integer val2, boolean incL, boolean incH){
		if (incL && incH){
			return  val1>val2;
		}
		else if (incL && !incH){
			return val1>val2;
		}
		else if (!incL && incH){
			return val1>=val2;
		}
		else if (!incL && !incH){
			return val1>=val2;
			
		}
		else{
			System.err.println("CrackerColumn compareTheta1: invalid incL and incH combination");
			return false;
		}
	}
	
	private boolean compareTheta2(Integer val1, Integer val2, boolean incL, boolean incH){
		if (incL && incH){
			return  val1>=val2;
		}
		else if (incL && !incH){
			return val1>=val2;
		}
		else if (!incL && incH){
			return val1>=val2;
		}
		else if (!incL && !incH){
			return val1>val2;
		}
		else{
			System.err.println("CrackerColumn compareTheta1: invalid incL and incH combination");
			return false;
		}
	}
	
	private boolean compareTheta3(Integer val1, Integer val2, boolean incL, boolean incH){
		if (incL && incH){
			return  val1<=val2;
		}
		else if (incL && !incH){
			return val1<val2;
		}
		else if (!incL && incH){
			return val1<=val2;
		}
		else if (!incL && !incH){
			return val1<val2;
		}
		else{
			System.err.println("CrackerColumn compareTheta1: invalid incL and incH combination");
			return false;
		}
	}
	/*
	 * Helpers
	 */
	private void swap(int index1, int index2) throws Exception {
		if (index1<0 || index1>this.values.size() || index2<0 || index2>this.values.size()){
			throw new Exception("SimpleColumn swap: specified index is out of bounds!");
		}
		Collections.swap(this.values,index1,index2);

	}
	/**
	 * Find index of the element around which we partitioned array:
	 * all elements with positions <= than pivot index are either 
	 * <= value (inc true) or < value (inc false)
	 * 
	 * @param posL index 
	 * @param posH index 
	 * @param value pivot value
	 * @return 
	 */
	private int findPivotIndex(int posL, int posH, Integer value, boolean inc){
		int pivotIndex = -1;
		
		for(int i = posL; i <=posH; i++){
			Integer currentVal = this.values.get(i);
			if (inc){
				if (currentVal>value){//everything before was <= value, inclusive crackInTwo
					pivotIndex = i;
					break;
				}
			}
			else{
				if (currentVal>=value){//everything before was < value, exclusive crackInTwo
					pivotIndex = i;
					break;
				}
			}
		}
		if (pivotIndex == -1){
			return posH;
		}
		return pivotIndex-1;
	}
	
	private int findPivotIndexLow(int posL, int posH, Integer valLow, boolean incL){
		int pivotIndexLow = -1;
		for(int i = posL; i <=posH; i++){
			Integer currentVal = this.values.get(i);
			if (incL){
				if (currentVal>=valLow){//everything before was < value, inclusive 
					pivotIndexLow = i;
					break;
				}
			}
			else{
				if (currentVal>valLow){//everything before was <= value, exclusive crackInTwo
					pivotIndexLow = i;
					break;
				}
			}
		}
		
		//if everything in the array was less than valLow
		if (pivotIndexLow == -1){
			return posH;
		}
		return pivotIndexLow-1;
	}
	
	private int findPivotIndexHigh(int posL, int posH, Integer valHigh, boolean incH){
		int pivotIndexHigh = -1;
		for(int i = posH; i <=posL; i--){
			Integer currentVal = this.values.get(i);
			if (incH){
				if (currentVal<=valHigh){//everything before was < value, inclusive 
					pivotIndexHigh = i;
					break;
				}
			}
			else{
				if (currentVal<valHigh){//everything before was <= value, exclusive crackInTwo
					pivotIndexHigh = i;
					break;
				}
			}
		}
		
		//if everything in the array was greater than valHigh
		if (pivotIndexHigh == -1){
			return 0;
		}
		return pivotIndexHigh-1;
	}
	
	public void reset() {
		this.values = this.copyValues(originalValues);
		this.crackerIndex = new CrackerIndexHashMap(originalValues.size());
	}
}
