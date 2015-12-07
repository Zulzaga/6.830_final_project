package minidb;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represents a database, a static instance.
 * The database contains only columns, since we chose to work with
 * tables consisting of single columns, therefore table = column. 
 * Column names have to be unique.
 * 
 * Columns contain integers only.
 * 
 * Class attributes:
 *  - catalog: HashMap of column names and Column objects. Column name has to be unique
 *  - something identifying which implementation for CrackerIndex we use //TODO
 *  
 * Class methods:
 *  - getCatalog
 *  - createColumn
 *  - listColumnNames
 *  - populateColumn
 *  - removeColumn
 *  
 * @author danamukusheva
 *
 */
public class Database {
	private static AtomicReference<Database> _instance = new AtomicReference<Database>(new Database());
	private final HashMap<String, Column> _catalog;
	
	/**
	 * Constructor
	 */
	public Database(){
		_catalog = new HashMap<String, Column>();
		
	}
	
	/**
	 * Return a static "column name-Column object" mapping
	 * @return
	 */
	public static HashMap<String, Column> getCatalog() {
        return _instance.get()._catalog;
    }
	
	/**
	 * Return all column (aka table) names in the database
	 * @return
	 */
	public Set<String> listColumnNames(){
		return Database.getCatalog().keySet();
	}
	/**
	 * Get column by its name
	 * @return
	 * @throws Exception if can't find the column with such name
	 */
	public static Column getColumn(String colname) throws Exception{
		Column col =  Database.getCatalog().get(colname);
		if (col == null){
			throw new Exception("DB doesn't have column with name "+colname);
		}
		return col;
	}
	
	/**
	 * Create and add to the catalog a new Column instance
	 */
	public void createSimpleColumnCracking(String colname){
		SimpleColumn col = new SimpleColumn(colname, true);
		Database.getCatalog().put(colname, col);
		
	}
	
	
	/**
	 * Parses a file with an integer on each line, adds all
	 * values to the column.
	 * 
	 * @param colname column name
	 * @param filename file name of TSV formated file
	 */
	public void populateColumn(String colname, String filename){
		//TODO
	}
	/**
	 * Creates random integers to populate the specified column
	 * @param colname column name
	 * @param min_value minimum value in the column
	 * @param max_value maximum value in the column
	 * @param num_entries column size
	 * @throws Exception  if column is not present
	 */
	public void populateColumn(String colname, int min_value, int max_value, int num_entries) throws Exception{
		Column col = Database.getCatalog().get(colname);
		if(col == null){
			throw new Exception("DB doesn't have column with name "+colname);
		}
		Random r = new Random();
		int count = 0;
		while(count<num_entries){
			Integer val = r.nextInt(max_value);
			if (val > min_value){
				col.insertTuple(val);
				count++;
				
			}
		}
		
	}
	
	/**
	 * Delete specified column from the DataBase
	 * @param colname
	 */
	public static void removeColumn(String colname){
		Database.getCatalog().remove(colname);
	}

}
