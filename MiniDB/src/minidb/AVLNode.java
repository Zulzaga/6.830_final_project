package minidb;

/**
 * Source: http://blog.blackbam.at/2012/05/04/avl-tree-implementation-in-java/
 * @author danamukusheva
 *
 */
public class AVLNode {
 public AVLNode left; // left child
 public AVLNode right; // right child
 public AVLNode parent; // parent node
 public Integer key; //value
 public int position; //index of the value
 public int balance;


 public AVLNode(Integer k, int position) {
  this.left = null;
  this.parent = null;
  this.right = null;
  this.balance = 0;
  this.key = k;
  this.position = position;
 }
 public String toString() {
  return "" + key;
 }
 
 public int getPosition(){
	 return position;
 }
 
 public void setPosition(int position){
	 this.position = position;
 }
 
 public int getKet(){
	 return this.key;
 }

}
