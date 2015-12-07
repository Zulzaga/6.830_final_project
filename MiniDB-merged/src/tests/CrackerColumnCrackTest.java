package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import minidb.*;

import org.junit.Test;

public class CrackerColumnCrackTest {

	@Test
	public void testCrackRangeLessThanOrEqual() {
		System.out.println("\n\nTEST 1: testCrackRangeLessThanOrEqual: '<=' ");
		int N = 10; // number of elements in the array
		int maxInt = 20; // maximum value in the array
		
		int indexOfPivot = 3; //index of the value that should be used in partition
		String range = "<=";
		
		
		Random randomGen = new Random();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i<N; i++){
			values.add(randomGen.nextInt(maxInt));
		}
		System.out.println("Generated array: "+values);
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivot = values.get(indexOfPivot);
		System.out.println("Pivot value: "+pivot);
		
		try {
			ArrayList<Integer> result = c.crack(pivot, range);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val <= pivot);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCrackRangeGreaterThan() {
		System.out.println("\n\nTEST 2: testCrackRangeGreaterThan: '>' ");
		int N = 10; // number of elements in the array
		int maxInt = 20; // maximum value in the array
		
		int indexOfPivot = 3; //index of the value that should be used in partition
		String range = ">";
		
		
		Random randomGen = new Random();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i<N; i++){
			values.add(randomGen.nextInt(maxInt));
		}
		System.out.println("Generated array: "+values);
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivot = values.get(indexOfPivot);
		System.out.println("Pivot value: "+pivot);
		
		try {
			ArrayList<Integer> result = c.crack(pivot, range);
			System.out.println("Result: "+result);
			System.out.print("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val > pivot);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCrackRangeLessThan() {
		System.out.println("\n\nTEST 3: testCrackRangeLessThan: '<' ");
		int N = 10; // number of elements in the array
		int maxInt = 20; // maximum value in the array
		
		int indexOfPivot = 3; //index of the value that should be used in partition
		String range = "<";
		
		
		Random randomGen = new Random();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i<N; i++){
			values.add(randomGen.nextInt(maxInt));
		}
		System.out.println("Generated array: "+values);
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivot = values.get(indexOfPivot);
		System.out.println("Pivot value: "+pivot);
		
		try {
			ArrayList<Integer> result = c.crack(pivot, range);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val < pivot);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCrackRangeGreaterThanOrEqual() {
		System.out.println("\n\nTEST 4: testCrackRangeGreaterThanOrEqual: '>=' ");
		int N = 10; // number of elements in the array
		int maxInt = 20; // maximum value in the array
		
		int indexOfPivot = 3; //index of the value that should be used in partition
		String range = ">=";
		
		
		Random randomGen = new Random();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i<N; i++){
			values.add(randomGen.nextInt(maxInt));
		}
		System.out.println("Generated array: "+values);
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivot = values.get(indexOfPivot);
		System.out.println("Pivot value: "+pivot);
		
		try {
			ArrayList<Integer> result = c.crack(pivot, range);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val >= pivot);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test //Fails now :(
	public void testCrackFourTimes() {
		System.out.println("\n\nTEST 5: testCrackFourTimes: '<', '>', '<=', '>='");
		int N = 15; // number of elements in the array
		int maxInt = 20; // maximum value in the array
		
		int indexOfPivot1 = 3; //index of the value that should be used in partition
		String range1 = "<";
		
		int indexOfPivot2 = 10; //index of the value that should be used in partition
		String range2 = ">";
		
		
		Random randomGen = new Random();
		ArrayList<Integer> values = new ArrayList<Integer>();
		for(int i = 0; i<N; i++){
			values.add(randomGen.nextInt(maxInt));
		}
		System.out.println("Generated array: "+values);
		
		CrackerColumn c = new CrackerColumn(values);
		
		Integer pivot1 = values.get(indexOfPivot1);
		System.out.println("Pivot value1: "+pivot1);
		Integer pivot2 = values.get(indexOfPivot2);
		System.out.println("Pivot value2: "+pivot2);
		
		try {
			ArrayList<Integer> result = c.crack(pivot1, range1);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val < pivot1);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ArrayList<Integer> result = c.crack(pivot2, range2);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val > pivot2);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		int indexOfPivot3 = 6; //index of the value that should be used in partition
		String range3 = "<=";
		
		int indexOfPivot4 = 12; //index of the value that should be used in partition
		String range4 = ">=";
		
		Integer pivot3 = values.get(indexOfPivot3);
		System.out.println("Pivot value3: "+pivot3);
		Integer pivot4 = values.get(indexOfPivot4);
		System.out.println("Pivot value4: "+pivot4);
		
		try {
			ArrayList<Integer> result = c.crack(pivot3, range3);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val <= pivot3);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			ArrayList<Integer> result = c.crack(pivot4, range4);
			System.out.println("Result: "+result);
			System.out.println("Array after cracking: "+c.getValues());
			for(Integer val : result){
				assertTrue(val >= pivot4);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	

}
