package minidb;

public class CrackerIndexAVL implements CrackerIndex {
	
	public AVLTree avltree = new AVLTree();
	public int numValues;
	public int size;
	
	public CrackerIndexAVL(int numValues){
		this.numValues = numValues;
		size = 0;
	}
	

	public int findIndexValue(Integer k) {
		AVLNode node = avltree.find(k);
		if (node == null){
			return -1; //no such node in the tree
		}
		else{
			return node.getPosition();
		}
	}

	public void addValue(Integer k, int index) {
		avltree.insert(k, index);
		size++;

	}

	public void setPositionForExistingValue(Integer k, int index) throws Exception {
		AVLNode node = avltree.find(k);
		if(node==null){
			throw new Exception("CrackerIndexAVL setPositionForExistingValue: node with k="+k+" is not found in index!");
		}
		node.setPosition(index);
		
		
	}

	public int findNextGreaterIndex(Integer k) throws Exception {
		int index = avltree.successorStoredPosition(k);
		//must never happen! we first insert and then inquire this
		if (index == -2){
			throw new Exception("CrackerIndexAVL findNextGreaterIndex: node with k="+k+" is not found in index!");
		}
		//means that it is itself the greatest node.
		else if (index == -1){
			return this.numValues-1;
		}
		else{
			return index;
		}
	}

	public int findNextSmallerIndex(Integer k) throws Exception {

		int index = avltree.predecessorStoredPosition(k);
		//must never happen! we first insert and then inquire this
		if (index == -2){
			throw new Exception("CrackerIndexAVL findNextSmallerIndex: node with k="+k+" is not found in index!");
		}
		//means that it is itself is a smallest node.
		else if (index == -1){
			return 0;
		}
		else{
			return index;
		}
	}


    public void removeValue(Integer k) {
        // TODO Auto-generated method stub
        
    }

    public int getValues() {
		return size;
    }
}
