package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import minidb.*;

import org.json.simple.JSONObject;
import org.junit.Test;

public class TesterTest {

//	@Test
//	public void testParseFile() {
//		Tester test = new Tester();
//		String filename = "/MiniDB/src/tests/testWorkload.json";
//		URL url = getClass().getResource("testWorkload.json");
//		//File f = new File(url.getPath());
//		//System.out.println(f.canRead());
//		JSONObject obj = test.parseFile(filename);
//		System.out.print(obj);
//		}
	
	@Test
	public void testGenerateWorkload(){
		/*
		 * {"column" : "testCol", "range" : "<", "fixed_range" : "None", "min_lower_range" : 1, "max_upper_range" : 50, "num_queries" : 10}
		 */
		
		Tester test = new Tester();
		Database db = new Database();
		String colname = "testCol";
		int numQueries = 10;
		Integer min_lower_range = 1;
		Integer max_upper_range = 50;
		
		String range = "<";
		db.createSimpleColumnCracking(colname);
		try {
			db.populateColumn(colname, 0, 100, 100);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("No such column exists in the DB!");
		}
		
		JSONObject obj=new JSONObject();
		
		obj.put("column",colname);
		obj.put("range", range);
		obj.put("fixed_range", "None");
		obj.put("min_lower_range", min_lower_range);
		obj.put("max_upper_range", max_upper_range);
		obj.put("num_queries", numQueries);
		
		ArrayList<RangeScan> rs = test.generateWorkload(obj);
		assertEquals(rs.size(), numQueries);
		
	}

}
