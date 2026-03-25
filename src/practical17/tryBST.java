public class tryBST {

  
    //tNode: represents a single node in the binary search tree.
    //Each node stores an integer key, and has a left and right child.
  
    static class tNode {
        int key; //the integer value stored in this node
        tNode left; //left child (values smaller than key)
        tNode right; //right child (values larger than key)

        //Constructor: create a new node with a given key, no children yet
        tNode(int key) {
            this.key = key;
            this.left = null;
            this.right = null;
        }
    }

    
    //BST class: an object-oriented binary search tree.
    //The tree is accessed through its root node.
    
    static class BST {
        tNode root; //the top of the tree (null if tree is empty)

        //Constructor: start with an empty tree
        BST() {
            root = null;
        }

       
        //insert: adds a new integer key into the BST.
        //Follows the BST rule: smaller keys go left, larger go right.
        //Duplicate keys are ignored.
        
        void insert(int key) {
            root = insertRec(root, key);
        }

        //Recursive helper for insert.
        //Returns the (possibly new) root of the subtree.
        private tNode insertRec(tNode node, int key) {
            //Base case: we've reached an empty spot 
            if (node == null) return new tNode(key);

            if (key < node.key)
                //Key is smallermgo left
                node.left = insertRec(node.left, key);
            else if (key > node.key)
                // Key is larger go right
                node.right = insertRec(node.right, key);
            //If key == node.key, it's a duplicate, do nothing

            return node;
        }

        
        //delete: removes a node with the given key from the BST.
        //Handles three cases:
        //1. Node has no children just remove it
        //2. Node has one child replace node with that child
        //3. Node has two children replace with in-order successor
        //(smallest node in right subtree), then delete that successor
       
        void delete(int key) {
            root = deleteRec(root, key);
        }

        //Recursive helper for delete.
        private tNode deleteRec(tNode node, int key) {
            //Base case: key not found in tree
            if (node == null) return null;

            if (key < node.key) {
                //Key is in the left subtree
                node.left = deleteRec(node.left, key);
            } else if (key > node.key) {
                //Key is in the right subtree
                node.right = deleteRec(node.right, key);
            } else {
                //Found the node to delete!

                //Case 1 & 2: zero or one child
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;

                //Case 3: two children
                //Find in-order successor: smallest node in right subtree
                tNode successor = findMin(node.right);
                //Replace current node's key with successor's key
                node.key = successor.key;
                //Delete the successor from the right subtree
                node.right = deleteRec(node.right, successor.key);
            }
            return node;
        }

        //findMin: returns the node with the smallest key in a subtree.
        //Used by delete to find the in order successor.
        private tNode findMin(tNode node) {
            //Keep going left until there is no left child
            while (node.left != null) node = node.left;
            return node;
        }

      
      
        //deleteAllEvens: removes every node whose key is even.
        //We use a postorder traversal so children are handled before
        //their parents this avoids missing nodes during deletion.
       
        void deleteAllEvens() {
            root = deleteEvensRec(root);
        }

        //Recursive helper: visits left subtree, right subtree, then
        //checks the current node. Returns the cleaned subtree root.
        private tNode deleteEvensRec(tNode node) {
            if (node == null) return null;

            //First clean the children
            node.left = deleteEvensRec(node.left);
            node.right = deleteEvensRec(node.right);

            //Then check this node
            if (node.key % 2 == 0) {
                //This node is even delete it using the same BST delete logic
                //(reuse deleteRec so we handle all three cases correctly)
                return deleteRec(node, node.key);
            }
            return node;
        }

