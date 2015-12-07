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
import java.util.Scanner;
import java.util.Random;

import static java.lang.Math.toIntExact;
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
		String filename = "/Users/zulsarbatmunkh/Desktop/final_project/code/MiniDB/src/minidb/testWorkload.json";
		String destFilename = "/Users/zulsarbatmunkh/Desktop/final_project/code/MiniDB/src/minidb/Results.txt";
		
		//Parse file and get parameters
		JSONObject params = parseFile(filename);
		System.out.println(params);
		//Generate queries
		ArrayList<ArrayList<Long>> times = new ArrayList<ArrayList<Long>>();
		
		//Run queries, record their times
		for (int i=0; i < 10; i++) {
		    ArrayList<RangeScan> rscans = generateWorkload(params);
		    times.add(testWorkload(rscans));
		    System.out.println(times);
		}
		
		//Record results in the file
		try {
			writeResults(times, destFilename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public static ArrayList<RangeScan> generateWorkload(JSONObject params){
		
		//names of json keys, in case they change
		String num_queries = "num_queries";
		String fixed_range = "fixed_range";
		String min_lower_range = "min_lower_range";
		String max_upper_range = "max_upper_range";
		String column = "column";
		
		//System.out.print(params.get);
		ArrayList<RangeScan> queries = new ArrayList<RangeScan>();
		Integer workloadSize = Integer.parseInt(params.get(num_queries).toString());
		Integer min_lower = Integer.parseInt(params.get(min_lower_range).toString());
		Integer max_upper = Integer.parseInt(params.get(max_upper_range).toString());
		Random random = new Random();
		//String[] ranges = {"<", ">", "<=", ">=", "<<=", "<=<=", "<<", "<=<"};
		//String range = (String) params.get(rangeName);
		//String range = ranges[random.nextInt(8)];
		String fixedRange = (String) params.get(fixed_range);

		String columnName = (String) params.get(column);
		Column col;
		boolean openRange;
//		if (range == "<" || range == "<=" || range == ">" || range == ">="){
//			openRange = true;
//		}
//		else{
//			openRange = false;
//		}
		try {
			col = Database.getColumn(columnName);
			RangeScan rs;
			for(int i = 0; i < workloadSize; i++){

				try {
					int low = min_lower-1;
					int high = max_upper; 
					
					while(low < min_lower){
						high = random.nextInt(max_upper-1) + 1;
						if (!fixedRange.equals("None")){ //high-low is fixed
							
							int difference = Integer.parseInt(fixedRange);
							low = high-difference;
						}
						else{
							low = random.nextInt(high);
						}
					}
					//high = random.nextInt(max_upper - low + 1) + low;
					//low = random.nextInt(high - 1 - min_lower) + min_lower - 1;
					// Choose a random range
					String[] ranges = {"<", ">", "<=", ">=", "><=", ">=<=", "><", ">=<"};
			        //String range = (String) params.get(rangeName);
			        String range = ranges[random.nextInt(8)];
					rs = new RangeScan(col, low, high, range);
					queries.add(rs);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		return times;
	}
	
	public static void populateDB(Database db){
		String colname = "testCol";

		db.createSimpleColumnCracking(colname);
		try {
			db.populateColumn(colname, 0, 1000, 100000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("No such column exists in the DB!");
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
	
	public static void writeResults(ArrayList<ArrayList<Long>> times, String destination) throws FileNotFoundException, UnsupportedEncodingException{
		//PrintWriter writer = new PrintWriter(new File(destination), "UTF-8");
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(destination));
			for(ArrayList<Long> val: times){
				writer.println(val);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
