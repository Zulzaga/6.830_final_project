package tests;

import static org.junit.Assert.*;
import minidb.CrackerColumn;
import minidb.CrackerColumn.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import org.junit.Test;

public class CrackingAlgorithms {

//	@Test
//	public void testCrackInTwoIncl(){
//		System.out.println("\nTEST 1: testCrackInTwoIncl.");
//		Integer[] numbers = {10,5,4,7,3,2,6};
//		ArrayList<Integer> values = new ArrayList<Integer>(Arrays.asList(numbers));
//		
//		System.out.println("Values "+values);
//		CrackerColumn cIncl = new CrackerColumn(values);
//		
//		Integer pivot = 5;
//		int pivotInd = cIncl.crackInTwo(0, values.size()-1, pivot, true);
//		cIncl.printValues();
//		System.out.println("Pivot index "+pivotInd);
//		int indexFirstGreater = -1;
//		for(int i = 0; i< cIncl.getValues().size(); i++){
//			Integer val = cIncl.getValues().get(i);
//			if(val > pivot){
//				indexFirstGreater = i;
//				break;
//			}
//			
//		}
//		//Test if everything else after first greater is greater than pivot
//		for(int i = indexFirstGreater; i< cIncl.getValues().size(); i++){
//			Integer val = cIncl.getValues().get(i);
//			assert(val > pivot);
//		}
//		
//	}
//	
//	@Test
//	public void testCrackInTwoExcl(){
//		System.out.println("\nTEST 2: testCrackInTwoExcl.");
//		Integer[] numbers = {10,5,4,7,3,2,6};
//		ArrayList<Integer> values = new ArrayList<Integer>(Arrays.asList(numbers));
//		
//		System.out.println("Values "+values);
//		CrackerColumn cExcl = new CrackerColumn(values);
//		
//		Integer pivot = 5;
//		int pivotInd = cExcl.crackInTwo(0, values.size()-1, pivot, false);
//		cExcl.printValues();
//		System.out.println("Pivot index "+pivotInd);
//		
//		int indexFirstGreaterOrEqual = -1;
//		for(int i = 0; i< cExcl.getValues().size(); i++){
//			Integer val = cExcl.getValues().get(i);
//			if(val >= pivot){
//				indexFirstGreaterOrEqual = i;
//				break;
//			}
//			
//		}
//		//Test if everything else after first greater or equal is greater or equalthan pivot
//		for(int i = indexFirstGreaterOrEqual; i< cExcl.getValues().size(); i++){
//			Integer val = cExcl.getValues().get(i);
//			assert(val >= pivot);
//		}
//		
//	}
//	
//	@Test
//	public void testCrackInTwoRandom() {
//		System.out.println("\nTEST 3: testCrackInTwoRandom.");
//		int N = 20; // number of elements in the array
//		int maxInt = 20; // maximum value in the array
//		
//		int indexOfPivot = 3; //index of the value that should be used in partition
//		
//		
//		
//		Random randomGen = new Random();
//		ArrayList<Integer> values = new ArrayList<Integer>();
//		for(int i = 0; i<N; i++){
//			values.add(randomGen.nextInt(maxInt));
//		}
//		System.out.println("Generated array: "+values);
//		
//		
//		CrackerColumn c = new CrackerColumn(values);
//		
//		Integer pivot = values.get(indexOfPivot);
//		System.out.println("Pivot value: "+pivot);
//		
//		
//		int pivotIndex = c.crackInTwo(0, N-1, pivot, true);
//		System.out.println("Pivot index: " + pivotIndex);
//		c.printValues();
//		
//		int indexFirstGreater = -1;
//		for(int i = 0; i< c.getValues().size(); i++){
//			Integer val = c.getValues().get(i);
//			if(val > pivot){
//				indexFirstGreater = i;
//				break;
//			}
//			
//		}
//		//if nothing was found, pivot must the max element of the array
//		if(indexFirstGreater==-1){
//			assert(pivot == Collections.max(c.getValues()));
//		}
//		else{
//			//Test if everything else after first greater is greater than pivot
//			for(int i = indexFirstGreater; i< c.getValues().size(); i++){
//				Integer val = c.getValues().get(i);
//				assert(val > pivot);
//			}
//		}
//		
//		
//	}
	
	
//	@Test
//	public void testCrackInThreeIncl(){
//		/*
//		 * Test range <=x<=
//		 */
//		System.out.println("\nTEST 4: testCrackInThree Inclusive.");
//		int N = 15; // number of elements in the array
//		int maxInt = 30; // maximum value in the array
//		
//		
//		Integer[] numbers = {1,10,23,8,14,25,7,11,27,4,15,20,6,19,19};
//		ArrayList<Integer> values = new ArrayList<Integer>(Arrays.asList(numbers));
//		
//		
//		CrackerColumn c = new CrackerColumn(values);
//		
//		Integer pivotLow = 10;
//		System.out.println("Pivot value low: "+pivotLow);
//
//		Integer pivotHigh = 20;
//		System.out.println("Pivot value high: "+pivotHigh);
//		
//		try {
//			Pair p = c.crackInThree(0, 14, pivotLow, pivotHigh, true, true);
//			System.out.println("Array after cracking: " +c.getValues());
//			System.out.println(p.getFirst());
//			System.out.println(p.getSecond());
//			for (int i = 0; i < c.getValues().size(); i++){
//				Integer val = c.getValue(i);
//				if (i<=p.getFirst()){
//					assertTrue(val<=pivotLow);
//				}
//				if (i<=p.getSecond()){
//					assertTrue(val<=pivotHigh);
//				}
//				else{
//					assertTrue(val>pivotHigh);
//				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	@Test	
	public void testCrackInThreeExcl(){
		
		System.out.println("\nTEST 5: testCrackInThree Exclusive.");
		int N = 15; // number of elements in the array
		int maxInt = 30; // maximum value in the array
		
		
		Integer[] numbers = {1, 10, 23, 8, 14, 25, 7, 11,27, 4, 15, 20, 6,19,19};
		ArrayList<Integer> values = new ArrayList<Integer>(Arrays.asList(numbers));
		
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivotLow = 10;
		System.out.println("Pivot value low: "+pivotLow);

		Integer pivotHigh = 20;
		System.out.println("Pivot value high: "+pivotHigh);
		
		try {
			Pair p = c.crackInThree(0, 14, pivotLow, pivotHigh, false, false);
			System.out.println("Array after cracking: " +c.getValues());
			for (int i = 0; i < c.getValues().size(); i++){
				Integer val = c.getValues().get(i);
				if (i<=p.getFirst()){
					assertTrue(val<pivotLow);
				}
				if (i<=p.getSecond()){
					assertTrue(val<pivotHigh);
				}
				else{
					assertTrue(val>=pivotHigh);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
