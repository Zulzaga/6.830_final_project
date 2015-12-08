package minidb;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents range scan on a specified Column object.
 * 
 * To create a RangeScan, we need to pass in a Column object, low value, high
 * value, and type of range: <, <=, >, >=, <<, <=<, <<=, <=<=
 * 
 * @author danamukusheva
 *
 */
public class RangeScan {

    private Column column;
    private Iterator<Integer> childIterator;

    public Integer low; // Cannot be null, if openRange, must be the only non
                         // null end
    public Integer high; // Can be null
    private String range; // One of <, <=, >, >=, <<, <=<, <<=, <=<=
    private boolean openRange; // True if range is <, >, <= or >=

    private boolean open;

    public RangeScan(Column col, Integer low, Integer high, String range) throws Exception {
        this.range = range;
        this.low = low;
        this.high = high;

        this.checkRangeEnds();
        this.column = col;

    }

    /**
     * Check if given tuple lies in the range
     * 
     * @param tuple
     *            Integer, value from the column
     * @return True if tuple passes the check
     * @throws Exception
     */
    private boolean filter(Integer tuple) throws Exception {
        if (this.openRange) {
            if (range.equals("<")) {
                return tuple < this.low;
            } else if (range.equals("<=")) {
                return tuple <= this.low;
            } else if (range.equals(">")) {
                return tuple > this.low;
            } else if (range.equals(">=")) {
                return tuple >= this.low;
            } else {
                throw new Exception("RangeScan filter: expected open range, got " + range);
            }
        } else {
            if (range.equals("><")) {
                return tuple > this.low && tuple < this.high;
            } else if (range.equals(">=<")) {
                return tuple >= this.low && tuple < this.high;
            } else if (range.equals("><=")) {
                return tuple > this.low && tuple <= this.high;
            } else if (range.equals(">=<=")) {
                return tuple >= this.low && tuple <= this.high;
            } else {
                throw new Exception("RangeScan filter: expected closed range, got " + range);
            }
        }
    }

    /**
     * Check if low and high value of the range can be used with a specified
     * range.
     * 
     * @throws Exception
     *             if cannot instantiate RangeScan
     */
    private void checkRangeEnds() throws Exception {
        if (low == null && high == null) {
            throw new Exception("RangeScan cannot be instantiated: low and high values are both null!");
        }

        if (this.range.equals("<") || this.range.equals("<=") || this.range.equals(">") || this.range.equals(">=")) {
            this.openRange = true;
        } else {
            this.openRange = false;
        }

        if (this.range.length() == 3 && this.openRange) {
            throw new Exception("RangeScan cannot be instantiated:low and high should be both non-null!");
        }
        // if (this.range.length()==1 && !this.openRange){
        // throw new Exception("RangeScan cannot be instantiated: either low or
        // high should null!");
        // }
    }

    /**
     * Start the iterator DO THE CRACKING HERE!!! IF the column wants to be
     * cracked (check Column.useCracking boolean), then all the work is done
     * here, and fetchNext will only return answers from the already identified
     * range (no need to apply filter)
     * 
     * else: simply go over each tuple and check if it passes the filter
     * 
     * 
     * 
     * More details: if col.useCracking { - check if was already cracked: - if
     * yes, operate on col.getCrackerColumn - if no, call
     * col.initializeCrackerColumn() and then get it col.getCrackerColumn and
     * operate on it
     * 
     * Operate = call crackercol.crack(); }
     */
    public void open() {
        this.open = true;
        if (this.column.useCracking()) {
            CrackerColumn cc;
            if (!this.column.isCracked()) {
                this.column.initializeCrackerColumn();
                this.column.markCracked();
            }
            cc = this.column.getCrackerColumn();
            if (this.openRange) {
                try {
                    ArrayList<Integer> results = cc.crack(this.low, this.range);
                    this.childIterator = results.iterator();
                    this.open = true;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.err.println("Error while trying to crack the column!");
                    e.printStackTrace();
                }
            } else {
                try {
                    int index = this.range.indexOf("<");
                    ArrayList<Integer> results_low = cc.crack(this.low, this.range.substring(0, index));
                    ArrayList<Integer> results_high = cc.crack(this.high, this.range.substring(index));
                    ArrayList<Integer> results = new ArrayList<Integer>();
                    for (int result : results_low) {
                    	if (result !=results_high.get(results_high.size()-1)) {
                    		results.add(result);
                    	} else {
                    		break;
                    	}
                    }
                    this.childIterator = results.iterator();
                    this.open = true;
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    System.err.println("Error while trying to crack the column!");
                    e.printStackTrace();
                }
                // TODO double ranges
            }
        } else {
            this.childIterator = column.getIterator();
        }
    }

    /**
     * Close the iterator
     */
    public void close() {
        this.open = false;
        this.childIterator = null;

    }

    /**
     * Rewind the iterator
     */
    public void rewind() {
        if (this.open) {
            if (this.column.useCracking()) {
                CrackerColumn cc = this.column.getCrackerColumn();
                if (this.openRange) {
                    try {
                        // since second opening, already saved this query, no
                        // additional work
                        ArrayList<Integer> results = cc.crack(this.low, this.range);
                        this.childIterator = results.iterator();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        System.err.println("Error while trying to crack the column!");
                        e.printStackTrace();
                    }
                } else {
                    // TODO double ranges
                }
            } else {
                this.childIterator = column.getIterator();
            }
        }
    }

    /**
     * Check if tuple is in the range, return it if yes
     * 
     * @return tuple (int) that passes the filter
     */
    public Integer fetchNext() throws Exception {
        if (!this.open) {
            throw new Exception("Error in RangeScan fetchNext: scan is not open.");
        }
        if (this.column.useCracking()) { // everything is valid, no need to
                                         // check
            Integer tuple;
            if (this.childIterator.hasNext()) {
                tuple = this.childIterator.next();
                return tuple;
            } else {
                return null;
            }
        } else { // no cracking, scan every tuple and check if within the range
            while (this.childIterator.hasNext()) {
                Integer tuple = this.childIterator.next();
                if (this.filter(tuple)) {
                    return tuple;
                }
            }
            return null;// out of tuples
        }
    }
    
    public Column getColumn() {
    	return this.column;
    }
}
