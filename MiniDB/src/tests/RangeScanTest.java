package tests;

import static org.junit.Assert.*;
import minidb.*;

import org.junit.Test;

public class RangeScanTest {

	@Test
	public void testSimpleRange() {
		//Create and populate column
		Integer[] numbers = {2, 3, 5, 1, 19, 14, 8, 12, 7, 18};
		SimpleColumn col = new SimpleColumn("sc1", true);
		for (Integer i:numbers){
			col.insertTuple(i);
		}
		Integer low = 12;
		Integer high = null;
		String range = "<";
		int numberOfReturnedTuples = 6;
		try {
			RangeScan rs = new RangeScan(col, low, high, range);
			rs.open();
			Integer tuple = rs.fetchNext();
			int count = 0;
			while(tuple!=null){
				assertTrue(tuple < low);
				count++;
				tuple = rs.fetchNext();
			}
			assertEquals(numberOfReturnedTuples, count);
			rs.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		low = 1;
		range = ">=";
		numberOfReturnedTuples = 10;
		
		try {
			RangeScan rs = new RangeScan(col, low, high, range);
			rs.open();
			Integer tuple = rs.fetchNext();
			int count = 0;
			while(tuple!=null){
				assertTrue(tuple >= low);
				count++;
				tuple = rs.fetchNext();
			}
			assertEquals(numberOfReturnedTuples, count);
			rs.rewind();
			tuple = rs.fetchNext();
			count = 0;
			while(tuple!=null){
				assertTrue(tuple >= low);
				count++;
				tuple = rs.fetchNext();
			}
			rs.close();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
