package minidb;

import java.util.ArrayList;

/**
 * Source: http://blog.blackbam.at/2012/05/04/avl-tree-implementation-in-java/
 * This class is the complete and tested implementation of an AVL-tree.
 */
public class AVLTree {
 
 protected AVLNode root; // the root node
 
/***************************** Core Functions ************************************/

 /**
  * Add a new element with key "k" into the tree.
  * 
  * @param k
  *            The key of the new node.
  */
 public void insert(int k, int position) {
  // create new node
  AVLNode n = new AVLNode(k, position);
  // start recursive procedure for inserting the node
  insertAVL(this.root,n);
 }
 
 /**
  * Recursive method to insert a node into a tree.
  * 
  * @param p The node currently compared, usually you start with the root.
  * @param q The node to be inserted.
  */
 public void insertAVL(AVLNode p, AVLNode q) {
  // If  node to compare is null, the node is inserted. If the root is null, it is the root of the tree.
  if(p==null) {
   this.root=q;
  } else {
   
   // If compare node is smaller, continue with the left node
   if(q.key<p.key) {
    if(p.left==null) {
     p.left = q;
     q.parent = p;
     
     // Node is inserted now, continue checking the balance
     recursiveBalance(p);
    } else {
     insertAVL(p.left,q);
    }
    
   } else if(q.key>p.key) {
    if(p.right==null) {
     p.right = q;
     q.parent = p;
     
     // Node is inserted now, continue checking the balance
     recursiveBalance(p);
    } else {
     insertAVL(p.right,q);
    }
   } else {
    // do nothing: This node already exists
   }
  }
 }
 
 /**
  * Check the balance for each node recursively and call required methods for balancing the tree until the root is reached.
  * 
  * @param cur : The node to check the balance for, usually you start with the parent of a leaf.
  */
 public void recursiveBalance(AVLNode cur) {
  
  // we do not use the balance in this class, but the store it anyway
  setBalance(cur);
  int balance = cur.balance;
  
  // check the balance
  if(balance==-2) {
   
   if(height(cur.left.left)>=height(cur.left.right)) {
    cur = rotateRight(cur);
   } else {
    cur = doubleRotateLeftRight(cur);
   }
  } else if(balance==2) {
   if(height(cur.right.right)>=height(cur.right.left)) {
    cur = rotateLeft(cur);
   } else {
    cur = doubleRotateRightLeft(cur);
   }
  }
  
  // we did not reach the root yet
  if(cur.parent!=null) {
   recursiveBalance(cur.parent);
  } else {
   this.root = cur;
   System.out.println("------------ Balancing finished ----------------");
  }
 }

 /**
  * @danamukusheva
  * Find a node with key k in the tree, if it exists
  * @param k int key
  * @return AVLNode or null
  */
 public AVLNode find(int k){
	 return findAVL(this.root, k);
 }
 /**
  * Recursively find the node with key l
  * @param p start search node
  * @param k int key
  * @return AVLNode
  */
 public AVLNode findAVL(AVLNode p, int k){
	 
	 if (p==null){
		 return null;
	 }
	 else{
		 if (p.key == k){ //found the node
			 return p;
		 }
		 else if (p.key>k){
			 return findAVL(p.left, k);
		 }
		 else{ // if (p.key<k)
			 return findAVL(p.right, k);
		 }
	 }
 }
 
 
 
 /**
  * Removes a node from the tree, if it is existent.
  */
 public void remove(int k) {
  // First we must find the node, after this we can delete it.
  removeAVL(this.root,k);
 }
 
 /**
  * Finds a node and calls a method to remove the node.
  * 
  * @param p The node to start the search.
  * @param q The KEY of node to remove.
  */
 public void removeAVL(AVLNode p,int q) {
  if(p==null) {
   return;
  } else {
   if(p.key>q)  {
    removeAVL(p.left,q);
   } else if(p.key<q) {
    removeAVL(p.right,q);
   } else if(p.key==q) {
    // we found the node in the tree.. now lets go on!
    removeFoundNode(p);
   }
  }
 }
 
 /**
  * Removes a node from a AVL-Tree, while balancing will be done if necessary.
  * 
  * @param q The node to be removed.
  */
 public void removeFoundNode(AVLNode q) {
  AVLNode r;
  // at least one child of q, q will be removed directly
  if(q.left==null || q.right==null) {
   // the root is deleted
   if(q.parent==null) {
    this.root=null;
    q=null;
    return;
   }
   r = q;
  } else {
   // q has two children --> will be replaced by successor
   r = successor(q);
   q.key = r.key;
  }
  
  AVLNode p;
  if(r.left!=null) {
   p = r.left;
  } else {
   p = r.right;
  }
  
  if(p!=null) {
   p.parent = r.parent;
  }
  
  if(r.parent==null) {
   this.root = p;
  } else {
   if(r==r.parent.left) {
    r.parent.left=p;
   } else {
    r.parent.right = p;
   }
   // balancing must be done until the root is reached.
   recursiveBalance(r.parent);
  }
  r = null;
 }
 
 /**
  * Left rotation using the given node.
  * 
  * 
  * @param n
  *            The node for the rotation.
  * 
  * @return The root of the rotated tree.
  */
 public AVLNode rotateLeft(AVLNode n) {
  
  AVLNode v = n.right;
  v.parent = n.parent;
  
  n.right = v.left;
  
  if(n.right!=null) {
   n.right.parent=n;
  }
  
  v.left = n;
  n.parent = v;
  
  if(v.parent!=null) {
   if(v.parent.right==n) {
    v.parent.right = v;
   } else if(v.parent.left==n) {
    v.parent.left = v;
   }
  }
  
  setBalance(n);
  setBalance(v);
  
  return v;
 }
 
 /**
  * Right rotation using the given node.
  * 
  * @param n
  *            The node for the rotation
  * 
  * @return The root of the new rotated tree.
  */
 public AVLNode rotateRight(AVLNode n) {
  
  AVLNode v = n.left;
  v.parent = n.parent;
  
  n.left = v.right;
  
  if(n.left!=null) {
   n.left.parent=n;
  }
  
  v.right = n;
  n.parent = v;
  
  
  if(v.parent!=null) {
   if(v.parent.right==n) {
    v.parent.right = v;
   } else if(v.parent.left==n) {
    v.parent.left = v;
   }
  }
  
  setBalance(n);
  setBalance(v);
  
  return v;
 }
 /**
  * 
  * @param u The node for the rotation.
  * @return The root after the double rotation.
  */
 public AVLNode doubleRotateLeftRight(AVLNode u) {
  u.left = rotateLeft(u.left);
  return rotateRight(u);
 }
 
 /**
  * 
  * @param u The node for the rotation.
  * @return The root after the double rotation.
  */
 public AVLNode doubleRotateRightLeft(AVLNode u) {
  u.right = rotateRight(u.right);
  return rotateLeft(u);
 }
 
/***************************** Helper Functions ************************************/
 
 /**
  * Returns the successor of a given node in the tree (search recursivly).
  * 
  * @param q The predecessor.
  * @return The successor of node q.
  */
 public AVLNode successor(AVLNode q) {
  if(q.right!=null) {
   AVLNode r = q.right;
   while(r.left!=null) {
    r = r.left;
   }
   return r;
  } else {
   AVLNode p = q.parent;
   while(p!=null && q==p.right) {
    q = p;
    p = q.parent;
   }
   return p;
  }
 }
 
 public int successorStoredPosition(Integer k){
	 AVLNode node = this.find(k);
	 AVLNode suc =  successor(node);
	 if (suc == null){//single node, no children, or left child only; or the largest node itself, no successors
		 return -1;
	 }
	 else{
		 return suc.getPosition();
	 }
 }
 /**
  * Returns the predecessor of a given node in the tree (search recursivly).
  * 
  * @param q The predecessor.
  * @return The successor of node q.
  */
 public AVLNode predecessor(AVLNode q) {
  if(q.left!=null) {
   AVLNode r = q.left;
   while(r.right!=null) {
    r = r.right;
   }
   return r;
  } else {
   AVLNode p = q.parent;
   while(p!=null && q==p.left) {
    q = p;
    p = q.parent;
   }
   return p;
  }
 }
 
 public int predecessorStoredPosition(Integer k){
	 AVLNode node = this.find(k);
	 if(node ==null){
		 return -2;// not -1 because if no successors, then the node itself will be returned and will have-1 as index
		 //since we initially inserted it like that
	 }
	 else{
		 AVLNode pred =  predecessor(node);
		 if (pred == null){//single node, no children, or left child only; or the largest node itself, no successors
			 return -1;
		 }
		 else{
			 return pred.getPosition();
		 }
	 }
 }
 
 /**
  * Calculating the "height" of a node.
  * 
  * @param cur
  * @return The height of a node (-1, if node is not existent eg. NULL).
  */
 private int height(AVLNode cur) {
  if(cur==null) {
   return -1;
  }
  if(cur.left==null && cur.right==null) {
   return 0;
  } else if(cur.left==null) {
   return 1+height(cur.right);
  } else if(cur.right==null) {
   return 1+height(cur.left);
  } else {
   return 1+maximum(height(cur.left),height(cur.right));
  }
 }
 
 /**
  * Return the maximum of two integers.
  */
 private int maximum(int a, int b) {
  if(a>=b) {
   return a;
  } else {
   return b;
  }
 }

 /** 
  * Only for debugging purposes. Gives all information about a node.
  
  * @param n The node to write information about.
  */
 public void debug(AVLNode n) {
  int l = 0;
  int r = 0;
  int p = 0;
  if(n.left!=null) {
   l = n.left.key;
  }
  if(n.right!=null) {
   r = n.right.key;
  }
  if(n.parent!=null) {
   p = n.parent.key;
  }
  
  System.out.println("Left: "+l+" Key: "+n+" Right: "+r+" Parent: "+p+" Balance: "+n.balance);
  
  if(n.left!=null) {
   debug(n.left);
  }
  if(n.right!=null) {
   debug(n.right);
  }
 }
 
 private void setBalance(AVLNode cur) {
  cur.balance = height(cur.right)-height(cur.left);
 }
 
 /**
  * Calculates the Inorder traversal of this tree.
  * 
  * @return A Array-List of the tree in inorder traversal.
  */
 final protected ArrayList<AVLNode> inorder() {
  ArrayList<AVLNode> ret = new ArrayList<AVLNode>();
  inorder(root, ret);
  return ret;
 }
 
 /**
  * Function to calculate inorder recursivly.
  * 
  * @param n
  *            The current node.
  * @param io
  *            The list to save the inorder traversal.
  */
 final protected void inorder(AVLNode n, ArrayList<AVLNode> io) {
  if (n == null) {
   return;
  }
  inorder(n.left, io);
  io.add(n);
  inorder(n.right, io);
 }
}
