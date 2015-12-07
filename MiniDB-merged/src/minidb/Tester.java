package minidb;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Random;

import static org.junit.Assert.fail;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
/**
 * This class is the one that called from the command line.
 * 
 * Usage:
 * javac **compile whatever needed**
 * java Tester <filename.json> <destination filename>
 * 
 * 
 * It takes in the file that specifies the test parameters.
 * 
 * filename.json contains a json object with following info:
 *  - column: column_name  //TODO: one column per table, might be unnecessary
 *  - range: choose one from [<, >, <=, >=, <<, <=<, <<=, <=<=]
 *  - fixed_range: None or int (put the difference btw low and high if want it to be constant)
 *  - min_lower_range: None or int
 *  - max_upper_range : None or int
 *  - num_queries: positive int 
 * 
 * destination filename is where all the times are saved
 * 
 * @author danamukusheva
 *
 */
public class Tester {
	public static void main(String[] args) throws InterruptedException {
		
		//Comment in for real command line testing
//		// Check if used correctly
//		// TODO: add a hint
//		if (args.length != 2){
//			System.err.println("Invalid usage!!!");
//			System.exit(0);
//		}
//		//TODO: instantiate and populate DB
//		
//		String filename = args[0];
//		String destFilename = args[1];
		
		//Instantiate DB
		Database db = new Database();
		
		//Populate it, population params specified in the function below
		populateDB(db);
		
		//Test variables, comment out for real command line testing
		String destFilename = "/afs/athena.mit.edu/user/z/u/zulsar/MiniDB-merged/MiniDB-merged/src/minidb/";
	
		//Generate queries
		HashMap<String, ArrayList<RangeScan>> workloads = new HashMap<String, ArrayList<RangeScan>>();
		
		//Run queries, record their times
		for (int i=0; i < 1; i++) {
		    generateWorkload(workloads);
		    for (String key_sort : workloads.keySet()) {
		    	String key = "AVL10000mixed";
		    	System.out.println("testing " + key);
		    	System.out.println(workloads.get(key).size());
		    	ArrayList<Long> rs_times = testWorkload(workloads.get(key));
		    	System.out.println("time " + rs_times);
		    	System.exit(0);
		    	try {
					writeResults(rs_times, destFilename + key + ".txt");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    System.out.println("running times: " + i);
		}
		//Record results in the file
	}
	
	/**
	 * Parse the content of the file containing
	 * info about the test workload and create a corresponding
	 * JSONObject.
	 * 
	 * @param filename String, contains correctly formatted json
	 * @return JSONObject from file content
	 */
	public static JSONObject parseFile(String filename){
		try {
			String content = readFile(filename);
			Object obj=JSONValue.parse(content);
			JSONObject jsonobj=(JSONObject) obj;
			return jsonobj;
		} catch (IOException e) {
			e.printStackTrace();
			System.err.print("Error in Tester parseFile.");
		}
		return null;	
	}
	/**
	 * Generate workload using the parameters specified in
	 * the file
	 * 
	 * @param params JSONObject with parameters for workload (see first lines)
	 * @return array of RangeScan objects
	 */
	public static ArrayList<ArrayList<RangeScan>> generateWorkload(HashMap<String, ArrayList<RangeScan>> workloads){
		
		ArrayList<ArrayList<RangeScan>> queries = new ArrayList<ArrayList<RangeScan>>();
		Integer[] workload_ranges = {10000};
		Integer min_lower = 1;
		Integer max_upper = 100000;
		Random random = new Random();
		String[] single_ranges = {"<", ">", "<=", ">="};
		String[] double_ranges = {"><=", ">=<=", "><", ">=<"};
		String[] mixed_ranges = {"<", ">", "<=", ">=", "><=", ">=<=", "><", ">=<"};
	    ArrayList<String[]> ranges = new ArrayList<String[]>();
	    ranges.add(single_ranges);
	    ranges.add(double_ranges);
	    ranges.add(mixed_ranges);
		Column col;
		String[] names = {"AVL", "HashMap", "Sorted"};
		String[] range_names = {"single", "double", "mixed"};
		try {
			// Looping through each different cracker index type
			for (int i=0; i<3; i++) {
				col = Database.getColumn("testCol" + i);
				RangeScan rs;
				String type_name = names[i];
				for(int index = 0; index < workload_ranges.length; index++){
					try {
						String workload_name = workload_ranges[index].toString();
						String range = null;
						for (int range_i = 0; range_i < 3; range_i++) {
							ArrayList<RangeScan> rangeScans = new ArrayList<RangeScan>();
							String range_name = range_names[range_i];
							String name = type_name + workload_name + range_name;
							String[] given_range = ranges.get(range_i);
					    	range = given_range[random.nextInt(given_range.length)];
					    	System.out.println("generating rs for " + name + " range " + range);
					    	for (int work_ind=0; work_ind < workload_ranges[index]; work_ind++) {
								int low = min_lower-1;
								int high = max_upper; 
								
								while(low < min_lower){
									high = random.nextInt(max_upper-1) + 1;
									low = random.nextInt(high);
								}

					    		rs = new RangeScan(col, low, high, range);
					    		rangeScans.add(rs);
					    	}
					    	workloads.put(name, rangeScans);
					    }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return queries;
	}
	
	/**
	 * Run each query on our database, record the time for each
	 * @param queries Query objects (workload)
	 * @return list of longs (time in ms, unix time)
	 */
	private static ArrayList<Long> testWorkload(ArrayList<RangeScan> rangescans){
		//TODO
		ArrayList<Long> times = new ArrayList<Long>();
		long start;
		for(RangeScan rs : rangescans){
			start = System.currentTimeMillis();
			rs.open();
			Integer tuple;
			try {
				tuple = rs.fetchNext();
				while(tuple != null){
					tuple = rs.fetchNext();
				}
				rs.close();
				times.add(System.currentTimeMillis() - start);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
		rangescans.get(0).getColumn().getCrackerColumn().reset();
		return times;
	}
	
	public static void populateDB(Database db){
		// simple column results versus sorted column result
		for (int i=0; i<3; i++) {
			String colname = "testCol" + i;
			db.createSimpleColumnCracking(colname, i);
			try {
				db.populateColumn(colname, 0, 100000, 1000000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				fail("No such column exists in the DB!");
			}
		}
	}
	
	/*
	 * Helpers:
	 * this one doesn't work now
	 */
	static String readFile(String filepath) throws IOException {

        String line = null;
        
        String content = "";
        try {
            FileReader fileReader = new FileReader(filepath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                content+=line;
            }   

            // Always close files.
            bufferedReader.close();   
            return content;
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filepath + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"  + filepath + "'");                  

        }
        //should not get here
		return null;
	}
	
	public static void writeResults(ArrayList<Long> times, String destination) throws FileNotFoundException, UnsupportedEncodingException{
		//PrintWriter writer = new PrintWriter(new File(destination), "UTF-8");
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(destination));
			for(Long val: times){
				writer.println(val);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
