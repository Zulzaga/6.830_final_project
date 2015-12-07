package tests;

import static org.junit.Assert.*;
import java.util.Random;

import org.junit.Test;
import minidb.*;
public class SortedColumnTest {

	@Test
	public void testQuickSort() {
		int maxValue = 100;
		SortedColumn sc = new SortedColumn("testColSmall");
		Random randomGen = new Random();
		int colSmallSize = 10;
		
		for(int i = 0; i<colSmallSize; i++){
			Integer val = randomGen.nextInt(maxValue);
			sc.insertTuple(val);
		}
		sc.sort(true);
		for(int i = 0; i<colSmallSize-1; i++){
			Integer val = sc.getValues().get(i);
			Integer valNext = sc.getValues().get(i+1);
			assertTrue(val<=valNext);
		}
		
		SortedColumn scMed = new SortedColumn("testColMedium");
		int colMedSize = 100;
		
		for(int i = 0; i<colMedSize; i++){
			Integer val = randomGen.nextInt(maxValue);
			scMed.insertTuple(val);
		}
		scMed.sort(true);
		for(int i = 0; i<colMedSize-1; i++){
			Integer val = scMed.getValues().get(i);
			Integer valNext = scMed.getValues().get(i+1);
			assertTrue(val<=valNext);
		}
	}
	
	@Test
	public void testRadixSort(){
		int maxValue = 100;
		SortedColumn sc = new SortedColumn("testColSmall");
		Random randomGen = new Random();
		int colSmallSize = 10;
		
		for(int i = 0; i<colSmallSize; i++){
			Integer val = randomGen.nextInt(maxValue);
			sc.insertTuple(val);
		}
		sc.sort(false);
		for(int i = 0; i<colSmallSize-1; i++){
			Integer val = sc.getValues().get(i);
			Integer valNext = sc.getValues().get(i+1);
			assertTrue(val<=valNext);
		}
		
		SortedColumn scMed = new SortedColumn("testColMedium");
		int colMedSize = 100;
		
		for(int i = 0; i<colMedSize; i++){
			Integer val = randomGen.nextInt(maxValue);
			scMed.insertTuple(val);
		}
		scMed.sort(false);
		for(int i = 0; i<colMedSize-1; i++){
			Integer val = scMed.getValues().get(i);
			Integer valNext = scMed.getValues().get(i+1);
			assertTrue(val<=valNext);
		}
	}

}
