package tests;

import static org.junit.Assert.*;
import minidb.*;

import org.junit.Test;

public class CrackerIndexAVLTest {

	@Test
	public void testOneValue() {
		int numValues = 10;
		Integer value = 5;
		int position = 0;
		int newPosition = 5;
		
		CrackerIndexAVL ci = new CrackerIndexAVL(numValues);
		ci.addValue(value, position);
		
		//Find value
		int lookup = ci.findIndexValue(value);
		assertTrue(lookup == position);
		
		//Find some other value
		int lookup2 = ci.findIndexValue(value+1);
		assertFalse(lookup2==position);
		
		//Set position for value
		try {
			ci.setPositionForExistingValue(value, newPosition);
			assertTrue(ci.findIndexValue(value) == newPosition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("Couldn't find the value in the tree!");
			
		}
		
		//Successor
		//should not find anything, return the last index of the array
		int nextGreaterInd;
		try {
			nextGreaterInd = ci.findNextGreaterIndex(value);
			assertTrue(nextGreaterInd == (numValues-1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		//Predecessor
		//should not find anything, return the first index of the array, 0
		int nextSmallerInd;
		try {
			nextSmallerInd = ci.findNextSmallerIndex(value);
			assertTrue(nextSmallerInd == 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		
		
		
	}
	
	@Test
	public void testThreeValues() {
		int numValues = 10;
		Integer valueOne = 3;
		Integer valueTwo = 5;
		Integer valueThree = 10;
		int positionOne = 2;
		int positionTwo = 5;
		int positionThree = 0;
		
		CrackerIndexAVL ci = new CrackerIndexAVL(numValues);
		ci.addValue(valueOne, positionOne);
		ci.addValue(valueTwo, positionTwo);
		ci.addValue(valueThree, positionThree);
		
		//Find value 1
		int lookup = ci.findIndexValue(valueOne);
		assertTrue(lookup == positionOne);
		
		//Find sonme value that is not present
		lookup = ci.findIndexValue(100);
		assertTrue(lookup == -1);
		
		//Successor for value 1
		//should find value 2
		int nextGreaterInd;
		try {
			nextGreaterInd = ci.findNextGreaterIndex(valueOne);
			assertTrue(nextGreaterInd == positionTwo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		//Successor for value 2
		//should find value 3
		try {
			nextGreaterInd = ci.findNextGreaterIndex(valueTwo);
			assertTrue(nextGreaterInd == positionThree);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		//Successor for value 3
		//should not find anything, return end of the array index
		try {
			nextGreaterInd = ci.findNextGreaterIndex(valueThree);
			assertTrue(nextGreaterInd == (numValues-1));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		
		//Predecessor for value 3
		//should find value 2
		int nextSmallerInd;
		try {
			nextSmallerInd = ci.findNextSmallerIndex(valueThree);
			assertTrue(nextSmallerInd == positionTwo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		//Predecessor for value 2
		//should find value 1
		try {
			nextSmallerInd = ci.findNextSmallerIndex(valueTwo);
			assertTrue(nextSmallerInd == positionOne);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		//Predecessor for value 1
		//should not find anything, the beginning of the array
		try {
			nextSmallerInd = ci.findNextSmallerIndex(valueOne);
			assertTrue(nextSmallerInd == 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Couldn't find the value in the tree!");
		}
		
		
		
	}

}
