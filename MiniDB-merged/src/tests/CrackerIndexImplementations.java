package tests;

import static org.junit.Assert.*;
import minidb.*;

import org.junit.Test;

public class CrackerIndexImplementations {

	@Test
	public void testImplementation() throws Exception {
		Database db = new Database();
		String colname = "TestColumn";
		db.createSimpleColumnCracking(colname, 1);
		db.populateColumn(colname, 0, 100, 20);
		SimpleColumn col = (SimpleColumn) db.getColumn(colname);
		System.out.println("Generated array: " + col.getValues());
		Integer low = 50;
		RangeScan rs = new RangeScan(col, low, 50, "<");
		rs.open();
		Integer result = rs.fetchNext();
		while(result != null){
			assert(result<=low);
			System.out.println(result);
			result = rs.fetchNext();
		}
		
	}

}
